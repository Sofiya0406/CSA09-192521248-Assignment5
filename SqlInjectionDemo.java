import java.sql.*;

public class SqlInjectionDemo {
    // Configure a disposable local test database only.
    static final String URL = "jdbc:mysql://localhost:3306/assignment5";
    static final String USER = "root";
    static final String PASSWORD = "CHANGE_ME";

    public static void main(String[] args) throws Exception {
        try (Connection con = DriverManager.getConnection(URL, USER, PASSWORD)) {
            String id = args.length > 0 ? args[0] : "testuser";
            String pwd = args.length > 1 ? args[1] : "' OR '1'='1' -- ";

            String unsafe = "SELECT * FROM users WHERE id='" + id
                    + "' AND pwd='" + pwd + "'";
            try (Statement st = con.createStatement();
                 ResultSet rs = st.executeQuery(unsafe)) {
                System.out.println("Unsafe accepted: " + rs.next());
            }

            String safe = "SELECT * FROM users WHERE id=? AND pwd=?";
            try (PreparedStatement ps = con.prepareStatement(safe)) {
                ps.setString(1, id);
                ps.setString(2, pwd);
                try (ResultSet rs = ps.executeQuery()) {
                    System.out.println("PreparedStatement accepted: " + rs.next());
                }
            }
        }
    }
}
