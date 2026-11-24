# TASK B2 – Engineer an Adversarial Input

## Unsafe and safe login
Create a local test database with a test-only account. The unsafe SQL concatenates input into the query. A password input such as `' OR '1'='1' -- ` can change the WHERE clause and may cause the unsafe query to accept an invalid login. The exact outcome depends on the SQL dialect and query text. The PreparedStatement version binds the whole input as a value, so it is not interpreted as SQL syntax and should reject the invalid password.

## Damaged bus-pass file tests
Use these lines:
```text
P2001,192311001,Arun,12,4500.0
P2002,DAMAGED
P2003,192311003,Divya,7,not-a-number
<empty line>
P2004,192311004,Meena,8,3900.0
```
The simple `PassStore.find()` from B1 skips lines with the wrong number of fields. It does not parse the amount, so a five-field line with a non-numeric amount can still be returned. A stronger validator must parse route as an integer and amount as a double and reject invalid values. An empty line is skipped because it has one field, not five.

## Alternative: escaping or regular-expression validation
Escaping quotes or validating input before concatenating SQL is weaker because it is easy to miss encodings, database-specific syntax, comments, or unexpected input forms. PreparedStatement separates SQL structure from values and is the recommended defence. Regex validation costs O(m) for input length m, which is usually small, but it does not replace parameterized queries. PreparedStatement parameter binding also has low per-query overhead and can reuse compiled statements.
