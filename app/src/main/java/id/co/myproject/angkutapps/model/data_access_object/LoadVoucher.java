package id.co.myproject.angkutapps.model.data_access_object;

public class LoadVoucher {

    private int id_pembelian_voucher;
    private String kode_voucher, nama_voucher, masa_berlaku;
    private int harga, point;
    private String deskripsi, foto_url, hari_pembelian, tgl_pembelian;

    public LoadVoucher(int id_pembelian_voucher, String kode_voucher, String nama_voucher, String masa_berlaku, int harga, int point, String deskripsi, String foto_url, String hari_pembelian, String tgl_pembelian) {
        this.id_pembelian_voucher = id_pembelian_voucher;
        this.kode_voucher = kode_voucher;
        this.nama_voucher = nama_voucher;
        this.masa_berlaku = masa_berlaku;
        this.harga = harga;
        this.point = point;
        this.deskripsi = deskripsi;
        this.foto_url = foto_url;
        this.hari_pembelian = hari_pembelian;
        this.tgl_pembelian = tgl_pembelian;
    }

    public int getId_pembelian_voucher() {
        return id_pembelian_voucher;
    }

    public void setId_pembelian_voucher(int id_pembelian_voucher) {
        this.id_pembelian_voucher = id_pembelian_voucher;
    }

    public String getKode_voucher() {
        return kode_voucher;
    }

    public void setKode_voucher(String kode_voucher) {
        this.kode_voucher = kode_voucher;
    }

    public String getNama_voucher() {
        return nama_voucher;
    }

    public void setNama_voucher(String nama_voucher) {
        this.nama_voucher = nama_voucher;
    }

    public String getMasa_berlaku() {
        return masa_berlaku;
    }

    public void setMasa_berlaku(String masa_berlaku) {
        this.masa_berlaku = masa_berlaku;
    }

    public int getHarga() {
        return harga;
    }

    public void setHarga(int harga) {
        this.harga = harga;
    }

    public int getPoint() {
        return point;
    }

    public void setPoint(int point) {
        this.point = point;
    }

    public String getDeskripsi() {
        return deskripsi;
    }

    public void setDeskripsi(String deskripsi) {
        this.deskripsi = deskripsi;
    }

    public String getFoto_url() {
        return foto_url;
    }

    public void setFoto_url(String foto_url) {
        this.foto_url = foto_url;
    }

    public String getHari_pembelian() {
        return hari_pembelian;
    }

    public void setHari_pembelian(String hari_pembelian) {
        this.hari_pembelian = hari_pembelian;
    }

    public String getTgl_pembelian() {
        return tgl_pembelian;
    }

    public void setTgl_pembelian(String tgl_pembelian) {
        this.tgl_pembelian = tgl_pembelian;
    }
}
