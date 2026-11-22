import org.junit.jupiter.api.Test;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

public class SubstringFinderTest {

    @Test
    void smallFile() throws Exception {
        Path tmp = Files.createTempFile("small", ".txt");
        try {
            Files.writeString(tmp, "абракадабра", StandardCharsets.UTF_8);
            assertEquals(List.of(1L, 8L), SubstringFinder.find(tmp.toString(), "бра"));
        } finally {
            Files.deleteIfExists(tmp);
        }
    }

    @Test
    void overlapping() throws Exception {
        Path tmp = Files.createTempFile("ov", ".txt");
        try {
            Files.writeString(tmp, "аааа", StandardCharsets.UTF_8);
            assertEquals(List.of(0L, 1L), SubstringFinder.find(tmp.toString(), "аа"));
        } finally {
            Files.deleteIfExists(tmp);
        }
    }

    @Test
    void patternLongerThanFile() throws Exception {
        Path tmp = Files.createTempFile("short", ".txt");
        try {
            Files.writeString(tmp, "hi", StandardCharsets.UTF_8);
            assertEquals(List.of(), SubstringFinder.find(tmp.toString(), "hello"));
        } finally {
            Files.deleteIfExists(tmp);
        }
    }

    @Test
    void bigFile() throws Exception {
        Path tmp = Files.createTempFile("big", ".txt");
        try (var w = Files.newBufferedWriter(tmp, StandardCharsets.UTF_8)) {
            for (int i = 0; i < 1_000_000; i++) w.write("абракадабра ");
        }
        List<Long> r = SubstringFinder.find(tmp.toString(), "бра");
        assertEquals(2_000_000, r.size());
        Files.deleteIfExists(tmp);
    }
}