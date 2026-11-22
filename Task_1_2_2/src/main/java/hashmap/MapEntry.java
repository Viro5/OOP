package hashmap;

import java.util.Map;
import java.util.Objects;

/**
 * Класс, представляющий пару ключ-значение для использования в хеш-таблице.
 *
 * @param <K> тип ключей, поддерживаемых этой записью
 * @param <V> тип значений, поддерживаемых этой записью
 */
public class MapEntry<K, V> implements Map.Entry<K, V> {
    private final K key;
    private V value;

    /**
     * Создает новую пару ключ-значение.
     *
     * @param key   ключ
     * @param value значение
     */
    public MapEntry(K key, V value) {
        this.key = key;
        this.value = value;
    }

    /**
     * Возвращает ключ, связанный с этой записью.
     *
     * @return ключ
     */
    @Override
    public K getKey() {
        return key;
    }

    /**
     * Возвращает значение, связанное с этой записью.
     *
     * @return значение
     */
    @Override
    public V getValue() {
        return value;
    }

    /**
     * Заменяет значение, связанное с этой записью, на указанное значение.
     *
     * @param value новое значение
     * @return старое значение
     */
    @Override
    public V setValue(V value) {
        V oldValue = this.value;
        this.value = value;
        return oldValue;
    }

    /**
     * Сравнивает указанный объект с этой записью на равенство.
     *
     * @param o объект для сравнения
     * @return true, если объекты равны
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Map.Entry)) {
            return false;
        }
        Map.Entry<?, ?> entry = (Map.Entry<?, ?>) o;
        return Objects.equals(key, entry.getKey())
                && Objects.equals(value, entry.getValue());
    }

    /**
     * Возвращает хеш-код для этой записи.
     *
     * @return хеш-код
     */
    @Override
    public int hashCode() {
        return Objects.hashCode(key) ^ Objects.hashCode(value);
    }

    /**
     * Возвращает строковое представление записи в формате "ключ=значение".
     *
     * @return строковое представление
     */
    @Override
    public String toString() {
        return key + "=" + value;
    }
}