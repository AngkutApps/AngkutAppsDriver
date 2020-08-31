package id.co.myproject.angkutapps.model.data_access_object;

public class DataDriver {

    String kode_driver, nama_driver, email;
    char jenis_kelamin;
    String foto, merk_mobil, nama_jenis_kendaraan, plat_mobil, warna_kendaraan;

    public DataDriver() {
    }

    public DataDriver(String kode_driver, String nama_driver, String email, char jenis_kelamin, String foto, String merk_mobil, String nama_jenis_kendaraan, String plat_mobil, String warna_kendaraan) {
        this.kode_driver = kode_driver;
        this.nama_driver = nama_driver;
        this.email = email;
        this.jenis_kelamin = jenis_kelamin;
        this.foto = foto;
        this.merk_mobil = merk_mobil;
        this.nama_jenis_kendaraan = nama_jenis_kendaraan;
        this.plat_mobil = plat_mobil;
        this.warna_kendaraan = warna_kendaraan;
    }

    public String getKode_driver() {
        return kode_driver;
    }

    public void setKode_driver(String kode_driver) {
        this.kode_driver = kode_driver;
    }

    public String getNama_driver() {
        return nama_driver;
    }

    public void setNama_driver(String nama_driver) {
        this.nama_driver = nama_driver;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public char getJenis_kelamin() {
        return jenis_kelamin;
    }

    public void setJenis_kelamin(char jenis_kelamin) {
        this.jenis_kelamin = jenis_kelamin;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public String getMerk_mobil() {
        return merk_mobil;
    }

    public void setMerk_mobil(String merk_mobil) {
        this.merk_mobil = merk_mobil;
    }

    public String getNama_jenis_kendaraan() {
        return nama_jenis_kendaraan;
    }

    public void setNama_jenis_kendaraan(String nama_jenis_kendaraan) {
        this.nama_jenis_kendaraan = nama_jenis_kendaraan;
    }

    public String getPlat_mobil() {
        return plat_mobil;
    }

    public void setPlat_mobil(String plat_mobil) {
        this.plat_mobil = plat_mobil;
    }

    public String getWarna_kendaraan() {
        return warna_kendaraan;
    }

    public void setWarna_kendaraan(String warna_kendaraan) {
        this.warna_kendaraan = warna_kendaraan;
    }
}
