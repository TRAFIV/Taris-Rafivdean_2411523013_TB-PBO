//Program ini dibuat oleh:
//NAMA  : Taris Rafivdean
//NIM   : 2411523013
//KELAS : Pemrograman Berorientasi Objek - C

public class Menu {
    // Atribut menggunakan access modifier protected untuk penerapan Inheritance
    protected int id;
    protected String nama;
    protected int harga;
    protected int stok;
    protected String kategori;

    // Constructor
    public Menu(int id, String nama, int harga, int stok, String kategori) {
        this.id = id;
        this.nama = nama;
        this.harga = harga;
        this.stok = stok;
        this.kategori = kategori;
    }

    // Getter
    public int getId() {
        return id;
    }

    public String getNama() {
        return nama;
    }

    public int getHarga() {
        return harga;
    }

    public int getStok() {
        return stok;
    }

    public String getKategori() {
        return kategori;
    }

    // Manipulasi Method String (toUpperCase)
    public String getNamaStruk() {
        return nama.toUpperCase();
    }
}
