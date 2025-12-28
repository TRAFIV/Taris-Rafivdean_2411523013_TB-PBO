//Program ini dibuat oleh:
//NAMA  : Taris Rafivdean
//NIM   : 2411523013
//KELAS : Pemrograman Berorientasi Objek - C

import java.sql.*;
import java.util.Scanner;

public class DubaKebabApp {
    static Scanner scanner = new Scanner(System.in);
    static Connection conn = KoneksiDB.getKoneksi();

    public static void main(String[] args) {
        // Perulangan while(true) untuk menu utama agar tidak langsung keluar
        while (true) {
            System.out.println("\n=== SISTEM DUBA KEBAB ===");
            System.out.println("1. Kasir (Transaksi Baru)");
            System.out.println("2. Lihat Menu (Read)");
            System.out.println("3. Tambah Menu Baru (Create)");
            System.out.println("4. Hapus Menu (Delete)");
            System.out.println("5. Keluar");
            System.out.print("Pilih: ");

            // Exception Handling (try-catch)
            try {
                int pilih = Integer.parseInt(scanner.nextLine());
                // Percabangan (Switch Case)
                switch (pilih) {
                    case 1:
                        menuKasir();
                        break;
                    case 2:
                        lihatMenu();
                        break;
                    case 3:
                        tambahMenu();
                        break;
                    case 4:
                        hapusMenu();
                        break;
                    case 5:
                        System.out.println("Sampai Jumpa!");
                        return; // Keluar program
                    default:
                        System.out.println("Pilihan salah!");
                }
            } catch (NumberFormatException e) {
                // 5 Menangani error jika user menginput huruf
                System.out.println("Error: Masukkan angka, jangan huruf!");
            }
        }
    }

    // --- FITUR KASIR ---
    static void menuKasir() {
        Transaksi transaksi = new Transaksi();
        boolean lanjutBelanja = true;

        System.out.println("\n--- MODE KASIR ---");
        lihatMenu(); // Tampilkan menu dulu

        while (lanjutBelanja) {
            System.out.print("\nMasukkan ID Menu yang dibeli (0 untuk selesai): ");
            int id = Integer.parseInt(scanner.nextLine());

            if (id == 0)
                break;

            // Ambil data menu dari DB berdasarkan ID
            Menu menuDipilih = cariMenu(id);
            if (menuDipilih != null) {
                System.out.print("Masukkan Jumlah: ");
                int jumlah = Integer.parseInt(scanner.nextLine());

                if (jumlah <= menuDipilih.getStok()) {
                    transaksi.tambahPesanan(menuDipilih, jumlah);
                    System.out.println("Berhasil ditambahkan ke keranjang.");
                } else {
                    System.out.println("Stok tidak cukup! Sisa: " + menuDipilih.getStok());
                }
            } else {
                System.out.println("Menu tidak ditemukan!");
            }
        }

        // Pembayaran
        int total = transaksi.hitungTotalHarga();
        System.out.println("Total Belanja: Rp " + total);
        System.out.print("Masukkan Uang Bayar: Rp ");
        int bayar = Integer.parseInt(scanner.nextLine());

        if (bayar >= total) {
            transaksi.setUangBayar(bayar);
            transaksi.hitungTotalHarga(); // Hitung ulang kembalian
            transaksi.cetakStruk(); // Cetak dan Simpan ke DB
        } else {
            System.out.println("Uang tidak cukup! Transaksi Dibatalkan.");
        }
    }

    // --- FITUR DATABASE & HELPER ---

    // READ (Lihat Menu)
    static void lihatMenu() {
        try {
            String sql = "SELECT * FROM tabel_menu";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            System.out.println("\n--- DAFTAR MENU ---");
            while (rs.next()) {
                System.out.printf("[%d] %-20s | Rp%d | Stok: %d | %s\n",
                        rs.getInt("id"),
                        rs.getString("nama_menu"),
                        rs.getInt("harga"),
                        rs.getInt("stok"),
                        rs.getString("kategori"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // CREATE (Tambah Menu)
    static void tambahMenu() {
        try {
            System.out.print("Nama Menu: ");
            String nama = scanner.nextLine();
            System.out.print("Kategori (Makanan/Minuman): ");
            String kat = scanner.nextLine();
            System.out.print("Harga: ");
            int harga = Integer.parseInt(scanner.nextLine());
            System.out.print("Stok Awal: ");
            int stok = Integer.parseInt(scanner.nextLine());

            String sql = "INSERT INTO tabel_menu (nama_menu, kategori, harga, stok) VALUES (?, ?, ?, ?)";
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setString(1, nama);
            pst.setString(2, kat);
            pst.setInt(3, harga);
            pst.setInt(4, stok);
            pst.executeUpdate();
            System.out.println("Menu berhasil ditambahkan!");
        } catch (Exception e) {
            System.out.println("Gagal tambah menu: " + e.getMessage());
        }
    }

    // DELETE (Hapus Menu)
    static void hapusMenu() {
        try {
            System.out.print("Masukkan ID Menu yang akan dihapus: ");
            int id = Integer.parseInt(scanner.nextLine());

            String sql = "DELETE FROM tabel_menu WHERE id = ?";
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setInt(1, id);
            pst.executeUpdate();
            System.out.println("Menu berhasil dihapus!");
        } catch (Exception e) {
            System.out.println("Gagal hapus: " + e.getMessage());
        }
    }

    // Helper untuk mengambil objek Menu dari ID
    static Menu cariMenu(int id) {
        try {
            String sql = "SELECT * FROM tabel_menu WHERE id = " + id;
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            if (rs.next()) {
                String nama = rs.getString("nama_menu");
                int harga = rs.getInt("harga");
                int stok = rs.getInt("stok");
                String kat = rs.getString("kategori");

                // Penerapan Object Creation berdasarkan Kategori (OOP)
                if (kat.equalsIgnoreCase("Makanan")) {
                    return new Kebab(id, nama, harga, stok);
                } else {
                    return new Minuman(id, nama, harga, stok);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}