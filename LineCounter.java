import java.io.*;
import java.nio.file.*;

public class LineCounter {
    static long countUnbuffered(Path p) throws IOException {
        long lines = 0;
        try (InputStream in = new FileInputStream(p.toFile())) {
            int b;
            while ((b = in.read()) != -1) if (b == '\n') lines++;
        }
        return lines;
    }
    static long countBuffered(Path p) throws IOException {
        long lines = 0;
        try (InputStream in = new BufferedInputStream(
                new FileInputStream(p.toFile()))) {
            int b;
            while ((b = in.read()) != -1) if (b == '\n') lines++;
        }
        return lines;
    }
    public static void main(String[] args) throws Exception {
        if (args.length != 1) {
            System.out.println("Usage: java LineCounter <file>");
            return;
        }
        Path p = Path.of(args[0]);
        long start = System.nanoTime();
        long a = countUnbuffered(p);
        long unbufferedMs = (System.nanoTime() - start) / 1_000_000;
        start = System.nanoTime();
        long b = countBuffered(p);
        long bufferedMs = (System.nanoTime() - start) / 1_000_000;
        System.out.println("Unbuffered lines: " + a + ", time: " + unbufferedMs + " ms");
        System.out.println("Buffered lines: " + b + ", time: " + bufferedMs + " ms");
    }
}
