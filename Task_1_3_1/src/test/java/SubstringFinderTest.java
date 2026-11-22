import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * Tests for SubstringFinder.
 */
public class SubstringFinderTest {

    @Test
    void smallFile() throws Exception {
        Path tmp = Files.createTempFile("small", ".txt");
        try {
            Files.writeString(tmp, "абракадабра", StandardCharsets.UTF_8);
            Assertions.assertEquals(List.of(1L, 8L), SubstringFinder.find(tmp.toString(), "бра"));
        } finally {
            Files.deleteIfExists(tmp);
        }
    }

    @Test
    void overlapping() throws Exception {
        Path tmp = Files.createTempFile("ov", ".txt");
        try {
            Files.writeString(tmp, "аааа", StandardCharsets.UTF_8);
            Assertions.assertEquals(List.of(0L, 1L, 2L), SubstringFinder.find(tmp.toString(), "аа"));
        } finally {
            Files.deleteIfExists(tmp);
        }
    }

    @Test
    void patternLongerThanFile() throws Exception {
        Path tmp = Files.createTempFile("short", ".txt");
        try {
            Files.writeString(tmp, "hi", StandardCharsets.UTF_8);
            Assertions.assertEquals(List.of(), SubstringFinder.find(tmp.toString(), "hello"));
        } finally {
            Files.deleteIfExists(tmp);
        }
    }

    @Test
    void bigFile() throws Exception {
        Path tmp = Files.createTempFile("big", ".txt");
        try (var writer = Files.newBufferedWriter(tmp, StandardCharsets.UTF_8)) {
            for (int i = 0; i < 1_000_000; i++) {
                writer.write("абракадабра ");
            }
        }
        List<Long> r = SubstringFinder.find(tmp.toString(), "бра");
        Assertions.assertEquals(2_000_000, r.size());
        Files.deleteIfExists(tmp);
    }
}