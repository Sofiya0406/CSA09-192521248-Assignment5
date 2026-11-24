# TASK A2 – Don't Read What You Don't Need

## A2.1 Predict the cost
A 50 MB file contains approximately 50 × 1,024 × 1,024 = 52,428,800 bytes (if “MB” means MiB). A loop that calls `FileInputStream.read()` once per byte makes about 52.4 million Java read calls. Each call has method-call and stream overhead; without buffering, many small reads can cause frequent lower-level I/O work, making it slow.

## A2.2 Buffered redesign
```java
try (BufferedInputStream in =
         new BufferedInputStream(new FileInputStream("attendance.log"))) {
    long lines = 0;
    int value;
    while ((value = in.read()) != -1) {
        if (value == '\n') lines++;
    }
    System.out.println("Lines: " + lines);
}
```
A buffer (commonly several KB) fetches a block at a time. The Java loop still calls `read()` per byte, but the underlying stream is accessed in larger blocks, greatly reducing operating-system I/O requests. A byte-array approach can read a whole block and count newline bytes within that block.

## A2.3 When buffering gives little benefit
If the data is already in memory, or the program reads a tiny file only once, the extra buffer may provide little measurable improvement. It can also add little benefit when the source itself already provides buffered/block reads.

## Alternative: Files.readAllLines()
`Files.readAllLines(path)` reads and stores all lines in memory. Both approaches require O(n) time for n bytes/characters. A streaming buffered reader uses O(B) extra memory, where B is the buffer size; `readAllLines()` uses O(n) memory for the file contents and line objects. Use `readAllLines()` for small files that comfortably fit in memory and when all lines are needed. Use streaming for large files such as a 50 MB log or when only a count is needed.
