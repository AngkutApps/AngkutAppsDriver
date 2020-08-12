package id.co.myproject.angkutapps.model.data_access_object;

public class loadView_rw_voucher_penggunaan {

    String nama_voucher, foto_url, deskripsi;
    int id_penggunaan_voucher;
    String tgl_penggunaan, hari_penggunaan;

    public loadView_rw_voucher_penggunaan(String nama_voucher, String foto_url, String deskripsi, int id_penggunaan_voucher, String tgl_penggunaan, String hari_penggunaan) {
        this.nama_voucher = nama_voucher;
        this.foto_url = foto_url;
        this.deskripsi = deskripsi;
        this.id_penggunaan_voucher = id_penggunaan_voucher;
        this.tgl_penggunaan = tgl_penggunaan;
        this.hari_penggunaan = hari_penggunaan;
    }

    public String getNama_voucher() {
        return nama_voucher;
    }

    public void setNama_voucher(String nama_voucher) {
        this.nama_voucher = nama_voucher;
    }

    public String getFoto_url() {
        return foto_url;
    }

    public void setFoto_url(String foto_url) {
        this.foto_url = foto_url;
    }

    public String getDeskripsi() {
        return deskripsi;
    }

    public void setDeskripsi(String deskripsi) {
        this.deskripsi = deskripsi;
    }

    public int getId_penggunaan_voucher() {
        return id_penggunaan_voucher;
    }

    public void setId_penggunaan_voucher(int id_penggunaan_voucher) {
        this.id_penggunaan_voucher = id_penggunaan_voucher;
    }

    public String getTgl_penggunaan() {
        return tgl_penggunaan;
    }

    public void setTgl_penggunaan(String tgl_penggunaan) {
        this.tgl_penggunaan = tgl_penggunaan;
    }

    public String getHari_penggunaan() {
        return hari_penggunaan;
    }

    public void setHari_penggunaan(String hari_penggunaan) {
        this.hari_penggunaan = hari_penggunaan;
    }
}
