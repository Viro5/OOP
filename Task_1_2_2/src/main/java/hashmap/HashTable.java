package hashmap;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.ConcurrentModificationException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Set;

/**
 * Параметризованный контейнер, реализующий структуру данных хеш-таблица.
 * Поддерживает добавление, поиск и удаление объектов по ключу за константное время.
 *
 * @param <K> тип ключей
 * @param <V> тип значений
 */
public class HashTable<K, V> implements Map<K, V>, Iterable<MapEntry<K, V>> {

    private static final int DEFAULT_CAPACITY = 16;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;

    private LinkedList<MapEntry<K, V>>[] table;
    private int size;
    private int capacity;
    private float loadFactor;
    private int modCount = 0;

    /**
     * Создает пустую хеш-таблицу с заданной начальной емкостью и коэффициентом загрузки.
     *
     * @param capacity   начальная емкость
     * @param loadFactor коэффициент загрузки
     * @throws IllegalArgumentException если емкость или коэффициент загрузки некорректны
     */
    @SuppressWarnings("unchecked")
    public HashTable(int capacity, float loadFactor) {
        if (capacity <= 0) {
            throw new IllegalArgumentException("Capacity must be greater than zero");
        }
        if (loadFactor <= 0 || Float.isNaN(loadFactor)) {
            throw new IllegalArgumentException("Illegal load factor");
        }
        this.capacity = capacity;
        this.loadFactor = loadFactor;
        this.table = new LinkedList[capacity];
    }

    /**
     * Создает пустую хеш-таблицу с емкостью по умолчанию (16) и коэффициентом загрузки (0.75).
     */
    public HashTable() {
        this(DEFAULT_CAPACITY, DEFAULT_LOAD_FACTOR);
    }

    /**
     * Возвращает количество элементов в таблице.
     *
     * @return количество элементов
     */
    @Override
    public int size() {
        return size;
    }

    /**
     * Проверяет, пуста ли таблица.
     *
     * @return true, если таблица пуста
     */
    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    /**
     * Проверяет наличие ключа в таблице.
     *
     * @param key искомый ключ
     * @return true, если ключ присутствует
     */
    @Override
    public boolean containsKey(Object key) {
        int index = getBucketIndex(key);
        if (table[index] == null) {
            return false;
        }
        for (MapEntry<K, V> entry : table[index]) {
            if (Objects.equals(entry.getKey(), key)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Проверяет наличие значения в таблице.
     *
     * @param value искомое значение
     * @return true, если значение присутствует
     */
    @Override
    public boolean containsValue(Object value) {
        for (LinkedList<MapEntry<K, V>> bucket : table) {
            if (bucket != null) {
                for (MapEntry<K, V> entry : bucket) {
                    if (Objects.equals(entry.getValue(), value)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * Возвращает значение по указанному ключу.
     *
     * @param key ключ
     * @return значение или null, если ключ не найден
     */
    @Override
    public V get(Object key) {
        int index = getBucketIndex(key);
        if (table[index] == null) {
            return null;
        }
        for (MapEntry<K, V> entry : table[index]) {
            if (Objects.equals(entry.getKey(), key)) {
                return entry.getValue();
            }
        }
        return null;
    }

    /**
     * Добавляет пару ключ-значение в таблицу. Если ключ уже существует, обновляет значение.
     *
     * @param key   ключ
     * @param value значение
     * @return предыдущее значение, связанное с ключом, или null, если ключа не было
     */
    @Override
    public V put(K key, V value) {
        int index = getBucketIndex(key);
        if (table[index] == null) {
            table[index] = new LinkedList<>();
        }

        for (MapEntry<K, V> entry : table[index]) {
            if (Objects.equals(entry.getKey(), key)) {
                V oldValue = entry.getValue();
                entry.setValue(value);
                modCount++;
                return oldValue;
            }
        }

        table[index].add(new MapEntry<>(key, value));
        size++;
        modCount++;

        if ((float) size / capacity > loadFactor) {
            resize();
        }

        return null;
    }

    /**
     * Обновляет значение по существующему ключу.
     *
     * @param key   ключ
     * @param value новое значение
     * @return старое значение
     * @throws NoSuchElementException если ключ не найден
     */
    public V update(K key, V value) {
        if (!containsKey(key)) {
            throw new NoSuchElementException("Key not found: " + key);
        }
        return put(key, value);
    }

    /**
     * Удаляет пару ключ-значение по указанному ключу.
     *
     * @param key ключ
     * @return удаленное значение или null, если ключ не найден
     */
    @Override
    public V remove(Object key) {
        int index = getBucketIndex(key);
        if (table[index] == null) {
            return null;
        }

        Iterator<MapEntry<K, V>> iterator = table[index].iterator();
        while (iterator.hasNext()) {
            MapEntry<K, V> entry = iterator.next();
            if (Objects.equals(entry.getKey(), key)) {
                V value = entry.getValue();
                iterator.remove();
                size--;
                modCount++;
                return value;
            }
        }
        return null;
    }

    /**
     * Копирует все элементы из указанной Map в текущую таблицу.
     *
     * @param m Map с элементами для добавления
     */
    @Override
    public void putAll(Map<? extends K, ? extends V> m) {
        for (Map.Entry<? extends K, ? extends V> entry : m.entrySet()) {
            put(entry.getKey(), entry.getValue());
        }
    }

    /**
     * Очищает таблицу, удаляя все элементы.
     */
    @Override
    public void clear() {
        Arrays.fill(table, null);
        size = 0;
        modCount++;
    }

    /**
     * Возвращает множество ключей, содержащихся в таблице.
     *
     * @return множество ключей
     */
    @Override
    public Set<K> keySet() {
        Set<K> keys = new HashSet<>();
        for (Map.Entry<K, V> entry : entrySet()) {
            keys.add(entry.getKey());
        }
        return keys;
    }

    /**
     * Возвращает коллекцию значений, содержащихся в таблице.
     *
     * @return коллекция значений
     */
    @Override
    public Collection<V> values() {
        List<V> values = new ArrayList<>();
        for (Map.Entry<K, V> entry : entrySet()) {
            values.add(entry.getValue());
        }
        return values;
    }

    /**
     * Возвращает множество пар ключ-значение, содержащихся в таблице.
     *
     * @return множество записей
     */
    @Override
    public Set<Entry<K, V>> entrySet() {
        Set<Entry<K, V>> entries = new HashSet<>();
        for (LinkedList<MapEntry<K, V>> bucket : table) {
            if (bucket != null) {
                entries.addAll(bucket);
            }
        }
        return entries;
    }

    /**
     * Возвращает итератор для обхода элементов таблицы (пар ключ-значение).
     * Итератор поддерживает обработку исключительной ситуации ConcurrentModificationException.
     *
     * @return итератор по элементам
     */
    @Override
    public Iterator<MapEntry<K, V>> iterator() {
        return new HashTableIterator();
    }

    /**
     * Внутренний класс итератора для обхода хеш-таблицы.
     */
    private class HashTableIterator implements Iterator<MapEntry<K, V>> {
        private int expectedModCount;
        private int currentBucketIndex = 0;
        private Iterator<MapEntry<K, V>> currentBucketIterator = null;
        private MapEntry<K, V> lastReturned = null;
        private MapEntry<K, V> nextElement = null;

        HashTableIterator() {
            this.expectedModCount = modCount;
            findNext();
        }

        private void findNext() {
            nextElement = null;
            while (nextElement == null) {
                if (currentBucketIterator != null && currentBucketIterator.hasNext()) {
                    nextElement = currentBucketIterator.next();
                    return;
                }
                if (currentBucketIndex < table.length) {
                    LinkedList<MapEntry<K, V>> bucket = table[currentBucketIndex++];
                    if (bucket != null) {
                        currentBucketIterator = bucket.iterator();
                    } else {
                        currentBucketIterator = null;
                    }
                } else {
                    return;
                }
            }
        }

        @Override
        public boolean hasNext() {
            checkForComodification();
            return nextElement != null;
        }

        @Override
        public MapEntry<K, V> next() {
            checkForComodification();
            if (nextElement == null) {
                throw new NoSuchElementException();
            }
            lastReturned = nextElement;
            findNext();
            return lastReturned;
        }

        @Override
        public void remove() {
            if (lastReturned == null) {
                throw new IllegalStateException("next() has not been called yet");
            }
            checkForComodification();
            HashTable.this.remove(lastReturned.getKey());
            lastReturned = null;
            expectedModCount = modCount;
        }

        private void checkForComodification() {
            if (modCount != expectedModCount) {
                throw new ConcurrentModificationException();
            }
        }
    }

    /**
     * Увеличивает емкость таблицы в два раза и пересчитывает хеши для всех элементов.
     */
    @SuppressWarnings("unchecked")
    private void resize() {
        int newCapacity = capacity * 2;
        LinkedList<MapEntry<K, V>>[] oldTable = table;
        table = new LinkedList[newCapacity];
        capacity = newCapacity;
        size = 0;

        for (LinkedList<MapEntry<K, V>> bucket : oldTable) {
            if (bucket != null) {
                for (MapEntry<K, V> entry : bucket) {
                    put(entry.getKey(), entry.getValue());
                }
            }
        }
    }

    /**
     * Вычисляет индекс корзины для указанного ключа.
     *
     * @param key ключ
     * @return индекс корзины
     */
    private int getBucketIndex(Object key) {
        if (key == null) {
            return 0;
        }
        return Math.abs(key.hashCode()) % capacity;
    }

    /**
     * Сравнивает текущую таблицу с другим объектом на равенство.
     *
     * @param o объект для сравнения
     * @return true, если объекты равны
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Map)) {
            return false;
        }
        Map<?, ?> other = (Map<?, ?>) o;
        if (other.size() != size()) {
            return false;
        }

        try {
            for (Map.Entry<K, V> entry : entrySet()) {
                K key = entry.getKey();
                V value = entry.getValue();
                if (value == null) {
                    if (!(other.get(key) == null && other.containsKey(key))) {
                        return false;
                    }
                } else {
                    if (!value.equals(other.get(key))) {
                        return false;
                    }
                }
            }
        } catch (ClassCastException | NullPointerException unused) {
            return false;
        }
        return true;
    }

    /**
     * Возвращает хеш-код таблицы.
     *
     * @return хеш-код
     */
    @Override
    public int hashCode() {
        int hash = 0;
        for (Map.Entry<K, V> entry : entrySet()) {
            hash += entry.hashCode();
        }
        return hash;
    }

    /**
     * Возвращает строковое представление таблицы.
     *
     * @return строковое представление
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        boolean first = true;
        for (Map.Entry<K, V> entry : entrySet()) {
            if (!first) {
                sb.append(", ");
            }
            sb.append(entry.getKey()).append("=").append(entry.getValue());
            first = false;
        }
        sb.append("}");
        return sb.toString();
    }
}