package hashmap;

import java.util.ConcurrentModificationException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Юнит-тесты для проверки класса HashTable.
 */
public class HashTableTest {

    private HashTable<String, Number> hashTable;

    @BeforeEach
    public void setUp() {
        hashTable = new HashTable<>();
    }

    @Test
    public void testPutAndGet() {
        assertTrue(hashTable.isEmpty());
        hashTable.put("one", 1);
        hashTable.put("two", 2.0);

        assertEquals(2, hashTable.size());
        assertEquals(1, hashTable.get("one"));
        assertEquals(2.0, hashTable.get("two"));
        assertNull(hashTable.get("nonexistent"));
    }

    @Test
    public void testUpdate() {
        hashTable.put("one", 1);
        hashTable.update("one", 1.0);
        assertEquals(1.0, hashTable.get("one"));

        // Проверка выброса исключения при обновлении несуществующего ключа
        assertThrows(NoSuchElementException.class, () -> {
            hashTable.update("missing", 42);
        });
    }

    @Test
    public void testContainsKeyAndValue() {
        hashTable.put("test", 100);
        hashTable.put("another", 200);

        assertTrue(hashTable.containsKey("test"));
        assertFalse(hashTable.containsKey("other"));

        assertTrue(hashTable.containsValue(100));
        assertFalse(hashTable.containsValue(999));
    }

    @Test
    public void testRemove() {
        hashTable.put("a", 10);
        hashTable.put("b", 20);
        assertEquals(2, hashTable.size());

        Integer removed = (Integer) hashTable.remove("a");
        assertEquals(10, removed);
        assertEquals(1, hashTable.size());
        assertFalse(hashTable.containsKey("a"));
        assertNull(hashTable.remove("nonexistent"));
    }

    @Test
    public void testNullKey() {
        hashTable.put(null, 999);
        assertTrue(hashTable.containsKey(null));
        assertEquals(999, hashTable.get(null));

        hashTable.remove(null);
        assertFalse(hashTable.containsKey(null));
        assertNull(hashTable.get(null));
    }

    @Test
    public void testResizeAndCollisions() {
        // Добавляем много элементов для вызова re-hashing и создания коллизий
        for (int i = 0; i < 100; i++) {
            hashTable.put("key" + i, i);
        }

        assertEquals(100, hashTable.size());
        for (int i = 0; i < 100; i++) {
            assertEquals(i, hashTable.get("key" + i));
        }
    }

    @Test
    public void testIterator() {
        hashTable.put("A", 1);
        hashTable.put("B", 2);

        int count = 0;
        for (MapEntry<String, Number> entry : hashTable) {
            assertNotNull(entry.getKey());
            assertNotNull(entry.getValue());
            count++;
        }
        assertEquals(2, count);
    }

    @Test
    public void testIteratorRemove() {
        hashTable.put("A", 1);
        hashTable.put("B", 2);

        Iterator<MapEntry<String, Number>> iterator = hashTable.iterator();
        while (iterator.hasNext()) {
            MapEntry<String, Number> entry = iterator.next();
            if (entry.getKey().equals("A")) {
                iterator.remove();
            }
        }
        assertEquals(1, hashTable.size());
        assertFalse(hashTable.containsKey("A"));
        assertTrue(hashTable.containsKey("B"));
    }

    @Test
    public void testConcurrentModificationException() {
        hashTable.put("A", 1);
        hashTable.put("B", 2);

        Iterator<MapEntry<String, Number>> iterator = hashTable.iterator();
        assertTrue(iterator.hasNext());

        // Модифицируем таблицу во время итерации
        hashTable.put("C", 3);

        assertThrows(ConcurrentModificationException.class, () -> {
            iterator.next();
        });
    }

    @Test
    public void testEqualsAndHashCode() {
        HashTable<String, Number> t1 = new HashTable<>();
        HashTable<String, Number> t2 = new HashTable<>();

        t1.put("x", 10);
        t2.put("x", 10);

        assertEquals(t1, t2);
        assertEquals(t1.hashCode(), t2.hashCode());

        t2.put("y", 20);
        assertNotEquals(t1, t2);

        assertEquals(t1, t1);
        assertNotEquals(t1, null);
        assertNotEquals(t1, new Object());
        assertNotEquals(t1, new HashMap<>());
    }

    @Test
    public void testToString() {
        hashTable.put("key", 123);
        String str = hashTable.toString();
        assertTrue(str.contains("key=123"));
    }

    @Test
    public void testClearAndPutAll() {
        hashTable.put("A", 1);
        hashTable.put("B", 2);
        hashTable.clear();

        assertTrue(hashTable.isEmpty());
        assertEquals(0, hashTable.size());

        Map<String, Number> map = new HashMap<>();
        map.put("C", 3);
        map.put("D", 4);
        hashTable.putAll(map);

        assertEquals(2, hashTable.size());
        assertEquals(3, hashTable.get("C"));
        assertEquals(4, hashTable.get("D"));
    }

    @Test
    public void testConstructorsAndExceptions() {
        assertThrows(IllegalArgumentException.class, () -> new HashTable<>(-1, 0.75f));
        assertThrows(IllegalArgumentException.class, () -> new HashTable<>(16, -1.0f));
        assertThrows(IllegalArgumentException.class, () -> new HashTable<>(16, Float.NaN));

        HashTable<String, Number> customTable = new HashTable<>(32, 0.5f);
        assertTrue(customTable.isEmpty());
    }

    @Test
    public void testMapEntryMethods() {
        MapEntry<String, Number> entry = new MapEntry<>("key", 1);

        assertEquals("key", entry.getKey());
        assertEquals(1, entry.getValue());

        // Проверяем setValue (он возвращает старое значение)
        Number oldValue = entry.setValue(2);
        assertEquals(1, oldValue);
        assertEquals(2, entry.getValue());

        // Проверяем equals
        MapEntry<String, Number> sameEntry = new MapEntry<>("key", 2);
        MapEntry<String, Number> diffKeyEntry = new MapEntry<>("other", 2);
        MapEntry<String, Number> diffValueEntry = new MapEntry<>("key", 3);

        assertEquals(entry, sameEntry);
        assertEquals(entry, entry); // Проверка на самого себя
        assertNotEquals(entry, diffKeyEntry);
        assertNotEquals(entry, diffValueEntry);
        assertNotEquals(entry, null);
        assertNotEquals(entry, new Object()); // Проверка на другой тип

        // Проверяем hashCode
        assertEquals(entry.hashCode(), sameEntry.hashCode());

        // Проверяем toString
        assertEquals("key=2", entry.toString());
    }

    @Test
    public void testEntrySetAndKeySetAndValues() {
        hashTable.put("A", 1);
        hashTable.put("B", 2);

        // Проверка keySet
        assertTrue(hashTable.keySet().contains("A"));
        assertEquals(2, hashTable.keySet().size());

        // Проверка values
        assertTrue(hashTable.values().contains(1));
        assertEquals(2, hashTable.values().size());

        // Проверка entrySet
        assertEquals(2, hashTable.entrySet().size());
        boolean found = false;
        for (Map.Entry<String, Number> entry : hashTable.entrySet()) {
            if (entry.getKey().equals("A") && entry.getValue().equals(1)) {
                found = true;
            }
        }
        assertTrue(found);
    }
}