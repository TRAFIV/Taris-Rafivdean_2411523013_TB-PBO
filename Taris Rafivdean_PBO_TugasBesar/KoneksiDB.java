//Program ini dibuat oleh:
//NAMA  : Taris Rafivdean
//NIM   : 2411523013
//KELAS : Pemrograman Berorientasi Objek - C

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class KoneksiDB {
    private static Connection koneksi;

    public static Connection getKoneksi() {
        if (koneksi == null) {
            try {
                String url = "jdbc:mysql://localhost:3306/duba_kebab"; // Nama Database
                String user = "root";
                String password = "";

                DriverManager.registerDriver(new com.mysql.cj.jdbc.Driver());
                koneksi = DriverManager.getConnection(url, user, password);
            } catch (SQLException e) {
                System.out.println("Gagal Koneksi Database: " + e.getMessage());
            }
        }
        return koneksi;
    }
}