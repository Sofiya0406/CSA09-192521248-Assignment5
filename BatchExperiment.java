import java.sql.*;
import java.util.*;

public class BatchExperiment {
    static final String URL = "jdbc:mysql://localhost:3306/assignment5";
    static final String USER = "root";
    static final String PASSWORD = "CHANGE_ME";

    static List<String[]> generate(int n) {
        List<String[]> rows = new ArrayList<>();
        for (int i = 1; i <= n; i++)
            rows.add(new String[]{"P" + i, "19231" + i, "Student" + i,
                                  String.valueOf(i % 20 + 1), "4500.00"});
        return rows;
    }
    public static void main(String[] args) throws Exception {
        try (Connection con = DriverManager.getConnection(URL, USER, PASSWORD)) {
            for (int n : new int[]{1000, 10000, 100000}) {
                try (Statement st = con.createStatement()) {
                    st.executeUpdate("DELETE FROM bus_pass");
                }
                long start = System.nanoTime();
                int inserted = PassLoader.loadAll(con, generate(n));
                long ms = (System.nanoTime() - start) / 1_000_000;
                System.out.println("Rows=" + inserted + ", batch transaction=" + ms + " ms");
            }
            try (Statement st = con.createStatement();
                 ResultSet rs = st.executeQuery("SELECT * FROM bus_pass");
                 ) {
                ResultSetMetaData md = rs.getMetaData();
                for (int i = 1; i <= md.getColumnCount(); i++)
                    System.out.println(md.getColumnName(i) + " : " + md.getColumnTypeName(i));
            }
        }
    }
}
