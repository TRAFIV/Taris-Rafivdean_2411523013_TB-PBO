//Program ini dibuat oleh:
//NAMA  : Taris Rafivdean
//NIM   : 2411523013
//KELAS : Pemrograman Berorientasi Objek - C

public class Kebab extends Menu {
    // Constructor Sub Class
    public Kebab(int id, String nama, int harga, int stok) {
        // Menggunakan 'super' untuk memanggil constructor dari Super Class
        super(id, nama, harga, stok, "Makanan");
    }

    // Polymorphism: Mengubah perilaku method induk
    @Override
    public String getNamaStruk() {
        // Logika String: Jika ada kata "Monster", tambahkan label JUMBO
        if (super.getNama().contains("Monster")) {
            return super.getNamaStruk() + " [SIZE JUMBO]";
        }
        return super.getNamaStruk();
    }
}
