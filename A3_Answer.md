# TASK A3 – Discover the Data-Access Design Pattern

## A3.1 Common and changing parts
Repeated steps: obtain a database connection, prepare/execute SQL, bind values, read `ResultSet`, map rows to Java objects, handle SQL exceptions and close resources. Changing parts: table name, columns, entity type, primary-key type and row-to-object mapping.

## A3.2 Generic DAO interface
```java
public interface Dao<T, K> {
    void insert(T value) throws java.sql.SQLException;
    T findById(K id) throws java.sql.SQLException;
    java.util.List<T> findAll() throws java.sql.SQLException;
    void update(T value) throws java.sql.SQLException;
    void delete(K id) throws java.sql.SQLException;
}
```
Example implementation outline:
```java
public class StudentDao implements Dao<Student, Integer> {
    private final java.sql.Connection con;
    public StudentDao(java.sql.Connection con) { this.con = con; }

    public void insert(Student s) throws java.sql.SQLException {
        String sql = "INSERT INTO student(id, name, email) VALUES (?, ?, ?)";
        try (var ps = con.prepareStatement(sql)) {
            ps.setInt(1, s.getId());
            ps.setString(2, s.getName());
            ps.setString(3, s.getEmail());
            ps.executeUpdate();
        }
    }
    public Student findById(Integer id) throws java.sql.SQLException {
        String sql = "SELECT id, name, email FROM student WHERE id = ?";
        try (var ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (var rs = ps.executeQuery()) {
                if (!rs.next()) return null;
                return new Student(rs.getInt("id"),
                                   rs.getString("name"),
                                   rs.getString("email"));
            }
        }
    }
    // findAll, update and delete follow the same pattern:
    // use try-with-resources, parameterized SQL and map columns to fields.
    public java.util.List<Student> findAll() throws java.sql.SQLException {
        var result = new java.util.ArrayList<Student>();
        try (var ps = con.prepareStatement(
                "SELECT id, name, email FROM student");
             var rs = ps.executeQuery()) {
            while (rs.next()) result.add(new Student(rs.getInt("id"),
                rs.getString("name"), rs.getString("email")));
        }
        return result;
    }
    public void update(Student s) throws java.sql.SQLException {
        try (var ps = con.prepareStatement(
                "UPDATE student SET name=?, email=? WHERE id=?")) {
            ps.setString(1, s.getName()); ps.setString(2, s.getEmail());
            ps.setInt(3, s.getId()); ps.executeUpdate();
        }
    }
    public void delete(Integer id) throws java.sql.SQLException {
        try (var ps = con.prepareStatement("DELETE FROM student WHERE id=?")) {
            ps.setInt(1, id); ps.executeUpdate();
        }
    }
}
```
Assume `Student` has constructor and getters for `id`, `name`, and `email`.

## A3.3 Object-to-table mapping
`Student.id` → `student.id` (PRIMARY KEY); `Student.name` → `student.name`; `Student.email` → `student.email`.

## Alternative: SQL in service/GUI classes
If SQL is embedded in several service or GUI classes, a column rename may require editing every location containing that SQL. With DAO, SQL and mapping are centralized in the DAO implementation, so fewer classes need changes. Both designs have the same asymptotic database-operation cost and similar space cost; DAO mainly improves maintainability, reuse and testability, not query complexity.
