//Program ini dibuat oleh:
//NAMA  : Taris Rafivdean
//NIM   : 2411523013
//KELAS : Pemrograman Berorientasi Objek - C

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

// Implementasi Interface
public class Transaksi implements Struk {
    // Collection ArrayList untuk menampung belanjaan
    private ArrayList<Menu> listBelanja = new ArrayList<>();
    private ArrayList<Integer> listJumlah = new ArrayList<>();

    // Variabel untuk menampung hasil perhitungan
    private int totalBayar = 0;
    private int uangBayar = 0;
    private int kembalian = 0;

    // Method untuk menambah item ke dalam collection
    public void tambahPesanan(Menu menu, int jumlah) {
        listBelanja.add(menu);
        listJumlah.add(jumlah);
    }

    public void setUangBayar(int uang) {
        this.uangBayar = uang;
    }

    @Override
    public int hitungTotalHarga() {
        totalBayar = 0;
        // Perulangan & Operasi Matematika
        for (int i = 0; i < listBelanja.size(); i++) {
            totalBayar += (listBelanja.get(i).getHarga() * listJumlah.get(i));
        }
        kembalian = uangBayar - totalBayar;
        return totalBayar;
    }

    @Override
    public void cetakStruk() {
        // Manipulasi Date untuk menampilkan waktu saat ini
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        String tanggal = sdf.format(new Date());

        System.out.println("=================================");
        System.out.println("       DUBA KEBAB PAINAN        ");
        System.out.println("     Tgl: " + tanggal);
        System.out.println("=================================");

        // Perulangan untuk mencetak detail item
        for (int i = 0; i < listBelanja.size(); i++) {
            Menu m = listBelanja.get(i);
            int qty = listJumlah.get(i);
            int subtotal = m.getHarga() * qty;

            // Menggunakan method getNamaStruk() dari class anak (Polymorphism)
            System.out.printf("%-20s x%d  Rp%d\n", m.getNamaStruk(), qty, subtotal);
        }

        System.out.println("---------------------------------");
        System.out.println("Total Tagihan : Rp " + totalBayar);
        System.out.println("Uang Bayar    : Rp " + uangBayar);
        System.out.println("Kembalian     : Rp " + kembalian);
        System.out.println("=================================");
        System.out.println("    Terima Kasih Kakak! :D       ");
        System.out.println("=================================");

        simpanKeDatabase(); // Otomatis simpan ke JDBC saat struk dicetak
    }

    // Method untuk menyimpan riwayat ke Database (CRUD - Create)
    private void simpanKeDatabase() {
        try {
            Connection conn = KoneksiDB.getKoneksi();

            // 1. Simpan Riwayat
            String sqlHistory = "INSERT INTO riwayat_transaksi (tanggal_transaksi, rincian_pesanan, total_harga, uang_bayar, kembalian) VALUES (NOW(), ?, ?, ?, ?)";
            PreparedStatement pst = conn.prepareStatement(sqlHistory);

            // Membuat String rincian pesanan
            StringBuilder rincian = new StringBuilder();
            for (int i = 0; i < listBelanja.size(); i++) {
                rincian.append(listBelanja.get(i).getNama()).append(" (").append(listJumlah.get(i)).append("), ");
            }

            pst.setString(1, rincian.toString());
            pst.setInt(2, totalBayar);
            pst.setInt(3, uangBayar);
            pst.setInt(4, kembalian);
            pst.executeUpdate();

            // 2. Update Stok Menu (CRUD - Update)
            String sqlUpdate = "UPDATE tabel_menu SET stok = stok - ? WHERE id = ?";
            PreparedStatement pstUpdate = conn.prepareStatement(sqlUpdate);

            for (int i = 0; i < listBelanja.size(); i++) {
                pstUpdate.setInt(1, listJumlah.get(i));
                pstUpdate.setInt(2, listBelanja.get(i).getId());
                pstUpdate.executeUpdate();
            }

        } catch (Exception e) {
            System.out.println("Error Simpan DB: " + e.getMessage());
        }
    }
}