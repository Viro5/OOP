import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class SubstringFinder {

    public static List<Long> find(String filename, String pattern) throws IOException {
        byte[] pat = pattern.getBytes(StandardCharsets.UTF_8);
        int m = pat.length;
        List<Long> result = new ArrayList<>();
        if (m == 0) return result;

        final int CHUNK = 1 << 20;
        byte[] buf = new byte[CHUNK + m - 1];

        long symbolsBefore = 0;
        int tailLen = 0;

        try (InputStream in = new BufferedInputStream(new FileInputStream(filename), CHUNK)) {
            while (true) {
                int got = readFully(in, buf, tailLen, CHUNK);
                if (got == 0 && tailLen == 0) break;

                int totalLen = tailLen + got;

                int from = 0;
                while (true) {
                    int idx = indexOf(buf, totalLen, pat, from);
                    if (idx < 0) break;
                    long symIdx = symbolsBefore + countSymbols(buf, idx);
                    result.add(symIdx);
                    from = idx + 1;
                }

                int safe = Math.max(0, totalLen - (m - 1));
                while (safe > 0 && safe < totalLen && isContinuation(buf[safe])) safe--;
                symbolsBefore += countSymbols(buf, safe);

                tailLen = totalLen - safe;
                System.arraycopy(buf, safe, buf, 0, tailLen);

                if (got < CHUNK) break;
            }
        }
        return result;
    }

    private static int indexOf(byte[] hay, int hayLen, byte[] needle, int from) {
        int m = needle.length;
        for (int i = from; i + m <= hayLen; i++) {
            int j = 0;
            while (j < m && hay[i + j] == needle[j]) j++;
            if (j == m) return i;
        }
        return -1;
    }

    private static long countSymbols(byte[] hay, int len) {
        long n = 0;
        for (int i = 0; i < len; i++) {
            if (!isContinuation(hay[i])) n++;
        }
        return n;
    }

    private static boolean isContinuation(byte b) {
        return (b & 0xC0) == 0x80;
    }

    private static int readFully(InputStream in, byte[] buf, int off, int want) throws IOException {
        int total = 0;
        while (total < want) {
            int r = in.read(buf, off + total, want - total);
            if (r < 0) break;
            total += r;
        }
        return total;
    }

    public static void main(String[] args) throws Exception {
        System.out.println(find("input.txt", "бра"));
    }
}