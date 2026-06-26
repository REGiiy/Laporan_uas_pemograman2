package tokoberkah;

import java.sql.Connection;
import java.sql.DriverManager;

public class Koneksi {

    private static Connection conn;

    public static Connection getConnection() {
        try {
            String url = "jdbc:mysql://localhost:3306/toko_berkah";
            String user = "root";
            String pass = ""; // kosongkan password

            conn = DriverManager.getConnection(url, user, pass);

            System.out.println("Koneksi berhasil!");

        } catch (Exception e) {
            System.out.println("Koneksi gagal: " + e.getMessage());
        }

        return conn;
    }

    public static Connection getKoneksi() {
        return getConnection();
    }
}