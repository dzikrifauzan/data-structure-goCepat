// Peta kota disimpan sebagai graph:
// simpul = lokasi, dan jalan = garis yang menghubungkan dua simpul.
public class PetaKota {
    private final int jumlahSimpul;

    // Adjacency list: tetangga[u] berisi simpul-simpul yang terhubung ke u.
    private int[][] tetangga;

    // Banyaknya tetangga yang sudah terisi pada setiap simpul.
    private int[] jumlahTetangga;

    // Jarak (dalam hop) dari pencarian terakhir. Nilai -1 berarti belum ada hasil.
    // 1 hop = melewati 1 jalan.
    private int hopTerakhir = -1;

    public PetaKota(int jumlahSimpul) {
        this.jumlahSimpul = jumlahSimpul;
        this.tetangga = new int[jumlahSimpul][2];   // awalnya tiap simpul muat 2 tetangga
        this.jumlahTetangga = new int[jumlahSimpul];
    }

    private void tambahTetangga(int dari, int ke) {
        // Ukuran array tidak bisa berubah. Kalau penuh, buat array baru
        // yang 2x lebih besar, lalu salin isi array lama ke sana.
        if (jumlahTetangga[dari] == tetangga[dari].length) {
            int[] baru = new int[tetangga[dari].length * 2];
            for (int i = 0; i < jumlahTetangga[dari]; i++) {
                baru[i] = tetangga[dari][i];
            }
            tetangga[dari] = baru;
        }

        // Simpan tetangga di posisi kosong berikutnya, lalu tambah hitungannya.
        tetangga[dari][jumlahTetangga[dari]++] = ke;
    }

    public void tambahJalan(int a, int b) {
        // Jalan bisa dilewati dua arah, jadi dicatat di kedua simpul.
        tambahTetangga(a, b);
        tambahTetangga(b, a);
    }

    /**
     * Mencari driver terdekat dari simpul asal memakai BFS (Breadth-First Search).
     * BFS memeriksa simpul berjarak 1 hop dulu, lalu 2 hop, dan seterusnya,
     * sehingga driver yang pertama ditemukan pasti yang paling dekat.
     *
     * @return nomor simpul driver terdekat, atau -1 jika tidak ada driver yang terjangkau
     */
    public int bfsCariTerdekat(int asal, boolean[] adalahDriver) {
        int n = jumlahSimpul;
        boolean[] sudahDikunjungi = new boolean[n];   // agar simpul tidak diproses dua kali
        int[] antrian = new int[n];                   // queue dari array biasa
        int[] hop = new int[n];                       // jarak setiap simpul dari asal

        // depan    : posisi elemen yang akan diambil dari antrian
        // belakang : posisi kosong untuk elemen berikutnya
        int depan = 0, belakang = 0;

        // Mulai dari simpul asal.
        antrian[belakang++] = asal;
        sudahDikunjungi[asal] = true;

        // Selama antrian masih berisi, ambil simpul paling depan.
        while (depan < belakang) {
            int u = antrian[depan++];

            // Driver ditemukan: simpan jaraknya, lalu selesai.
            if (adalahDriver[u]) {
                hopTerakhir = hop[u];
                return u;
            }

            // Masukkan semua tetangga yang belum dikunjungi ke antrian.
            for (int i = 0; i < jumlahTetangga[u]; i++) {
                int v = tetangga[u][i];
                if (!sudahDikunjungi[v]) {
                    sudahDikunjungi[v] = true;
                    hop[v] = hop[u] + 1;   // satu langkah lebih jauh dari u
                    antrian[belakang++] = v;
                }
            }
        }

        // Antrian habis dan driver tidak ditemukan.
        hopTerakhir = -1;
        return -1;
    }

    public int getHopTerakhir() {
        return hopTerakhir;
    }

    public static void main(String[] args) {
        PetaKota peta = new PetaKota(8);   // simpul 0 sampai 7

        // Setiap pasangan {a, b} berarti ada jalan antara simpul a dan b.
        int[][] jalan = {
            {0, 1}, {0, 2}, {1, 3}, {2, 3},
            {3, 4}, {4, 5}, {5, 6}, {6, 7}, {1, 4}
        };
        for (int[] j : jalan) {
            peta.tambahJalan(j[0], j[1]);
        }

        // driver[i] bernilai true jika ada driver di simpul i.
        boolean[] driver = new boolean[8];
        driver[4] = true;
        driver[6] = true;

        // Cari driver terdekat untuk setiap lokasi penumpang.
        int[] penumpang = {0, 7};
        for (int p : penumpang) {
            int d = peta.bfsCariTerdekat(p, driver);
            System.out.println("Penumpang di simpul " + p
                    + " -> driver terdekat di simpul " + d
                    + " (" + peta.getHopTerakhir() + " hop)");
        }
    }
}
