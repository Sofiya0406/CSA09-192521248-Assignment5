# TASK A1 – Design a File Format

## A1.1 Record representation

**Delimited text file:** one pass per line, fields in this order:
`passId,regNo,name,routeNo,boardingStop,validityDate,amountPaid`

Example:
`P1001,192311001,Arun,12,Main Gate,2026-12-31,4500.00`

Use UTF-8. To handle a comma or quote inside a name/boarding stop, use CSV quoting: surround the field with double quotes and represent an embedded double quote as two double quotes. A CSV parser should be used; simple `split(",")` is not sufficient for quoted commas.

**Binary file using DataOutputStream:** write fields in the same order:
1. `writeUTF(passId)`
2. `writeUTF(regNo)`
3. `writeUTF(name)`
4. `writeInt(routeNo)`
5. `writeUTF(boardingStop)`
6. `writeUTF(validityDate)` (ISO `yyyy-MM-dd`)
7. `writeDouble(amountPaid)`

`writeUTF` stores a length prefix followed by modified UTF-8 bytes; integer and double fields have fixed binary sizes. The record is variable length because strings vary. Store a record length or use a documented framing format if random access is required.

## A1.2 Operations – pseudocode

### Add a pass
```text
ALGORITHM AddPass(pass)
BEGIN
    OPEN file using FileWriter in append mode
    WRAP FileWriter with BufferedWriter
    VALIDATE all required fields
    CONVERT each text field to a safely quoted CSV field
    WRITE the seven fields separated by commas
    WRITE a new line
    FLUSH the BufferedWriter
    CLOSE writer
END
```

### Find by pass ID
```text
ALGORITHM FindPass(passId)
BEGIN
    OPEN file using FileReader
    WRAP with BufferedReader
    WHILE a line can be read
        PARSE line using a CSV parser
        IF line is damaged or has other than seven fields
            SKIP line and record a warning
        ELSE IF field[0] equals passId
            RETURN the seven fields
        END IF
    END WHILE
    CLOSE reader
    RETURN NOT FOUND
END
```

### List passes on a route
```text
ALGORITHM ListByRoute(route)
BEGIN
    OPEN file using FileReader
    WRAP with BufferedReader
    WHILE a line can be read
        PARSE line as CSV
        IF valid record has seven fields AND route field equals route
            DISPLAY record
        END IF
    END WHILE
    CLOSE reader
END
```

`BufferedReader` and `BufferedWriter` reduce expensive underlying I/O operations by transferring data in blocks. `FileReader`/`FileWriter` are character streams suited to UTF-8 text when configured with an explicit charset (prefer `Files.newBufferedReader/newBufferedWriter` with UTF-8). `FileInputStream`/`FileOutputStream` are byte streams and are suitable for binary files, not the preferred choice for parsing text lines.

## A1.3 Damaged data and recovery
- **Damaged line:** validate field count, numeric route/amount and date format. Skip the invalid line, log its line number and continue; do not silently treat it as a valid record.
- **Missing file:** check existence. For a read, report a clear “file not found” error. For a new store, create the file and its parent directory if permitted.
- **Incomplete write/power failure:** write a complete new version to a temporary file, flush and close it, then atomically rename it over the original where supported. Keep a backup and use checksums or record framing to detect truncation. For append-only files, a final incomplete line can be detected and rejected.

## Alternative: RandomAccessFile with fixed-length records
Assign each record a fixed record size. Record `i` is located at `i × recordSize`, so direct access by record index is O(1). However, finding a pass by ID still requires O(n) scanning unless an ID-to-record-index index is maintained. Fixed-width name/stop fields need padding and may waste space; fields longer than their allocated size need validation or rejection. Text-file search is O(n), while indexed lookup can be approximately O(1) average with a hash index. Fixed-length records simplify seeking but reduce flexibility.
