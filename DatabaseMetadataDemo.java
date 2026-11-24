import java.sql.*;

public class DatabaseMetadataDemo {
    public static void main(String[] args) throws Exception {
        String url = "jdbc:mysql://localhost:3306/assignment5";
        try (Connection con = DriverManager.getConnection(url, "root", "CHANGE_ME")) {
            DatabaseMetaData md = con.getMetaData();
            System.out.println("Database: " + md.getDatabaseProductName());
            System.out.println("Database version: " + md.getDatabaseProductVersion());
            System.out.println("Driver: " + md.getDriverName());
            System.out.println("Driver version: " + md.getDriverVersion());
            try (ResultSet rs = md.getTables(con.getCatalog(), null, "%",
                    new String[]{"TABLE"})) {
                System.out.println("Tables:");
                while (rs.next()) System.out.println(rs.getString("TABLE_NAME"));
            }
        }
    }
}
