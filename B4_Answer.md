# TASK B4 – Final Engineering Decision

## B4.1 Migration strategy
Read the existing text file line by line. Parse and validate each record (field count, pass ID, register number, route number and amount). Add valid records to batches and insert them in a transaction. Record invalid rows with line number and reason in a failure report. Commit only after successful completion; rollback on database failure. For very large files, use bounded batches so memory remains controlled.

## B4.2 Database metadata
Use `DatabaseMetaData` to print database product name, database version, JDBC driver name/version and table names. Metadata helps identify server/driver differences during deployment and allows the application to inspect available schema objects rather than assuming every server is identical.

## B4.3 Production decision
Choose a relational database for the 12-campus system. It supports concurrent counters, transactions, indexed searching, constraints, backup and centralized access control. Text files are simple but weak for concurrent updates and multi-user queries. Java serialization is Java-specific and does not provide relational querying or safe multi-user transaction handling. Add indexes for commonly searched fields such as pass ID and route number, and use prepared statements and transactions.

## Alternative: one serialized file per campus
Each campus could write its own file, but a global search may need to scan all 12 files, O(total records), and updates must coordinate across files. Space is approximately the sum of all campus files plus serialization overhead. Concurrent counters need file locks or a single-writer service per campus; cross-campus transfers/updates are difficult to make atomic. A shared database provides centralized indexing, transactions and concurrency control, so it is the stronger production choice.
