import java.io.*;
import java.nio.file.*;
import java.sql.*;
import java.util.*;

public class Migration {
    static final String URL = "jdbc:mysql://localhost:3306/assignment5";
    static final String USER = "root";
    static final String PASSWORD = "CHANGE_ME";

    public static void main(String[] args) throws Exception {
        if (args.length != 1) {
            System.out.println("Usage: java Migration <passes.csv>");
            return;
        }
        List<String[]> valid = new ArrayList<>();
        List<String> failures = new ArrayList<>();
        int lineNo = 0;
        for (String line : Files.readAllLines(Path.of(args[0]))) {
            lineNo++;
            String[] p = line.split(",", -1);
            try {
                if (p.length != 5) throw new IllegalArgumentException("Expected 5 fields");
                Integer.parseInt(p[3]);
                double amount = Double.parseDouble(p[4]);
                if (amount < 0) throw new IllegalArgumentException("Negative amount");
                valid.add(p);
            } catch (RuntimeException ex) {
                failures.add("Line " + lineNo + ": " + ex.getMessage() + " | " + line);
            }
        }
        try (Connection con = DriverManager.getConnection(URL, USER, PASSWORD)) {
            int count = PassLoader.loadAll(con, valid);
            System.out.println("Imported records: " + count);
        }
        Files.write(Path.of("migration_failures.txt"), failures);
        System.out.println("Rejected records: " + failures.size());
        System.out.println("Failure report: migration_failures.txt");
    }
}
