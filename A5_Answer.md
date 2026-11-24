# TASK A5 – Design for Correctness and Safety

## A5.1 Transfer ₹500 using a transaction
```text
ALGORITHM Transfer(hostelWallet, canteenWallet, amount)
BEGIN
    SET autoCommit to FALSE
    TRY
        UPDATE wallet
          SET balance = balance - amount
          WHERE wallet_id = hostelWallet AND balance >= amount
        IF affected rows is not 1
            RAISE insufficient balance or missing wallet
        END IF

        UPDATE wallet
          SET balance = balance + amount
          WHERE wallet_id = canteenWallet
        IF affected rows is not 1
            RAISE missing destination wallet
        END IF

        COMMIT transaction
    CATCH any error
        ROLLBACK transaction
        REPORT failure
    FINALLY
        RESTORE previous autoCommit setting
    END TRY
END
```
Both updates belong to one transaction. Commit makes both permanent; rollback undoes both if either fails. Validate amount is positive and use appropriate row locking/isolation for concurrent transfers.

## A5.2 SQL injection
Unsafe query:
```java
String sql = "SELECT * FROM users WHERE id='" + id
           + "' AND pwd='" + pwd + "'";
```
An attacker may enter a crafted value such as `' OR '1'='1' -- ` in the password field. The input changes the SQL logic instead of being treated as data, potentially bypassing the password check.

Safe design:
```java
String sql = "SELECT * FROM users WHERE id=? AND pwd_hash=?";
try (PreparedStatement ps = con.prepareStatement(sql)) {
    ps.setString(1, id);
    ps.setString(2, passwordHash);
    try (ResultSet rs = ps.executeQuery()) {
        boolean valid = rs.next();
        System.out.println(valid ? "Login accepted" : "Login rejected");
    }
}
```
In a real application, retrieve the stored password hash by user ID and verify the entered password using a password-hashing library (e.g., Argon2/bcrypt/PBKDF2); do not compare or store plaintext passwords.

## Alternative: two auto-commit updates
If the debit succeeds and the program fails before the credit, the ₹500 is removed from the hostel wallet but not added to the canteen wallet. The system becomes inconsistent. Two independent updates may avoid transaction coordination overhead, but correctness risk is unacceptable. A transaction may add a small coordination/commit cost, but guarantees all-or-nothing behaviour.
