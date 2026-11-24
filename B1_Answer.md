# TASK B1 – Implement and Instrument the File Store

## B1.1 Validation test table

| Test | Input/action | Expected behaviour | What it verifies |
|---|---|---|---|
| Normal add/find | Add P1001 and find P1001 | Matching record returned | Successful write and search |
| Missing ID | Find P9999 | `null` / not found | Missing-record handling |
| Damaged line | Add a line with only two fields | Skip line and continue | Damaged record does not stop scan |
| Invalid amount | Amount contains letters | Reject record during validation | Type validation |

## B1.2 Comparison
The implementation reads one line at a time using `BufferedReader`, checks that there are exactly five fields, and returns the matching ID. This follows the pseudocode. The provided assignment's simple comma split is sufficient for its example, but a production CSV format needs proper quoting/parsing for commas inside names.

## B1.3 Buffering experiment
Run `LineCounter` for 1 MB, 10 MB and 50 MB files. Record the actual times in the table below. Repeat each test several times and compare the average; use the same computer and file contents.

| File size | Unbuffered time (ms) – measured | Buffered time (ms) – measured |
|---|---:|---:|
| 1 MB | Fill after running | Fill after running |
| 10 MB | Fill after running | Fill after running |
| 50 MB | Fill after running | Fill after running |

## B1.4 Growth experiment
Create files containing 1,000, 10,000 and 100,000 records and search for an ID near the end. Record actual elapsed times. A linear scan is O(n), so time generally increases with record count. File-system cache and hardware can affect measured values.

## Complete the Code – TODO 1
See `PassStore.java`. It reads with `BufferedReader` in try-with-resources, skips lines that do not contain exactly five fields, and returns the matching record or null.

## Alternative: HashMap
Read the file once at startup and store each record in `HashMap<String,String[]>` using pass ID as key. Building the map takes O(n) expected time and O(n) extra memory. q searches then take O(q) expected time, compared with O(q × n) for q separate linear scans. The map is useful when many searches are performed and sufficient memory is available; the file remains the persistent source of truth.
