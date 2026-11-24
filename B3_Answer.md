# TASK B3 – Statement vs PreparedStatement vs Batch

## B3.1 Correctness on small data
Insert 10 test records using each method. Run `SELECT * FROM bus_pass` and verify all ten rows. Use `ResultSetMetaData` to print each column's name and SQL type. Clear the test table between methods to avoid duplicate-key conflicts.

## B3.2 Data-size experiment
Run 1,000, 10,000 and 100,000 rows for each method. Measure elapsed time with `System.nanoTime()`. Record actual results:

| Rows | Statement (ms) | PreparedStatement (ms) | PreparedStatement batch (ms) |
|---:|---:|---:|---:|
| 1,000 | Measure | Measure | Measure |
| 10,000 | Measure | Measure | Measure |
| 100,000 | Measure | Measure | Measure |

Use the same database, machine, schema and generated values. Repeat runs and compare averages.

## B3.3 Auto-commit vs transaction
With auto-commit enabled, each successful insert may be committed immediately. If the program fails halfway, the earlier rows remain. With auto-commit disabled, all batch executions are part of one transaction; rollback removes all rows inserted in that transaction if a later insert fails.

## B3.4 Engineering trade-off
For a nightly import of 100,000 passes, use PreparedStatement batching inside a transaction, with bounded batches (e.g. 1,000) and validation/error reporting. For one pass issued at a counter, use a single PreparedStatement update/insert inside an appropriate transaction. PreparedStatement is safer than Statement because it binds values.

## Complete the Code
See `PassLoader.java`. It sets all five parameters, adds each row to the batch, executes every 1,000 rows, executes the remainder, commits on success and rolls back on failure.
