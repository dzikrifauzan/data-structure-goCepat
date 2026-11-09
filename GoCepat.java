// Interface = "kontrak". Setiap class yang memakai interface ini
// wajib punya method hitungTarif().
interface Kendaraan {
    double hitungTarif(double jarakKm);
}

// Rumus tarif semua kendaraan sama: tarif dasar + (tarif per km x jarak).
// Yang membedakan hanya nilai kedua tarifnya.
class Motor implements Kendaraan {
    // private      : hanya bisa diakses dari dalam class ini
    // static final : nilai tetap (konstanta) milik class
    private static final double TARIF_DASAR = 2000;   // biaya awal, dalam Rupiah
    private static final double PER_KM = 2500;        // biaya tiap 1 km

    public double hitungTarif(double jarakKm) {
        return TARIF_DASAR + PER_KM * jarakKm;
    }
}

class Mobil implements Kendaraan {
    private static final double TARIF_DASAR = 5000;   // biaya awal, dalam Rupiah
    private static final double PER_KM = 4000;        // biaya tiap 1 km

    public double hitungTarif(double jarakKm) {
        return TARIF_DASAR + PER_KM * jarakKm;
    }
}

class MobilListrik implements Kendaraan {
    private static final double TARIF_DASAR = 8000;   // lebih mahal dari Mobil biasa
    private static final double PER_KM = 3000;        // lebih murah dari Mobil biasa

    public double hitungTarif(double jarakKm) {
        return TARIF_DASAR + PER_KM * jarakKm;
    }
}

class SepedaListrik implements Kendaraan {
    private static final double TARIF_DASAR = 3000;   // biaya awal, dalam Rupiah
    private static final double PER_KM = 1500;        // biaya tiap 1 km

    public double hitungTarif(double jarakKm) {
        return TARIF_DASAR + PER_KM * jarakKm;
    }
}

// Trip menyimpan satu perjalanan: kendaraan apa dan siapa driver-nya.
class Trip {
    // Tipenya Kendaraan (interface), bukan Motor atau Mobil,
    // jadi Trip bisa menerima jenis kendaraan apa pun.
    private final Kendaraan kendaraan;
    private final String namaDriver;

    // Constructor: dijalankan saat objek dibuat dengan "new Trip(...)".
    public Trip(Kendaraan kendaraan, String namaDriver) {
        this.kendaraan = kendaraan;
        this.namaDriver = namaDriver;
    }

    double hitungBiaya(double jarakKm) {
        // Polymorphism: rumus yang dipakai mengikuti jenis kendaraan aslinya.
        return kendaraan.hitungTarif(jarakKm);
    }

    String getNamaDriver() {
        return namaDriver;
    }
}

public class GoCepat {
    // Program mulai berjalan dari method main.
    public static void main(String[] args) {
        // Satu array bertipe Kendaraan bisa berisi berbagai jenis kendaraan.
        Kendaraan[] daftar = {
            new Motor(), new Mobil(), new MobilListrik(), new SepedaListrik()
        };
        double jarak = 10;   // dalam km

        // Cetak tarif setiap kendaraan untuk jarak yang sama.
        for (Kendaraan k : daftar) {
            System.out.println(k.getClass().getSimpleName()
                    + " | tarif " + jarak + " km = Rp " + k.hitungTarif(jarak));
        }

        // Contoh satu perjalanan memakai MobilListrik.
        Trip trip = new Trip(new MobilListrik(), "Budi");
        System.out.println("Trip driver " + trip.getNamaDriver()
                + ", biaya 10 km = Rp " + trip.hitungBiaya(10));
    }
}
