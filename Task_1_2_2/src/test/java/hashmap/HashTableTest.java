package hashmap;

import java.util.ConcurrentModificationException;
import java.util.Iterator;
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
    public void testContainsKey() {
        hashTable.put("test", 100);
        assertTrue(hashTable.containsKey("test"));
        assertFalse(hashTable.containsKey("other"));
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
    }

    @Test
    public void testToString() {
        hashTable.put("key", 123);
        String str = hashTable.toString();
        assertTrue(str.contains("key=123"));
    }
}