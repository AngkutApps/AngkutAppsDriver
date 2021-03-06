package id.co.myproject.angkutapps.model.data_access_object;

public class loadView_rw_perjalanan {

    int biaya;
    String dari, alamat_dari, tujuan, alamat_tujuan, tgl_berangkat, hari_keberangkatan, tgl_sampai, no_hp, nama_user;
    char jenis_kelamin;
    int penumpang_dewasa, penumpang_anak, id_destinasi, id_perjalanan;

    public loadView_rw_perjalanan(int biaya, String dari, String alamat_dari, String tujuan, String alamat_tujuan, String tgl_berangkat, String hari_keberangkatan, String tgl_sampai, String no_hp, String nama_user, char jenis_kelamin, int penumpang_dewasa, int penumpang_anak, int id_destinasi, int id_perjalanan) {
        this.biaya = biaya;
        this.dari = dari;
        this.alamat_dari = alamat_dari;
        this.tujuan = tujuan;
        this.alamat_tujuan = alamat_tujuan;
        this.tgl_berangkat = tgl_berangkat;
        this.hari_keberangkatan = hari_keberangkatan;
        this.tgl_sampai = tgl_sampai;
        this.no_hp = no_hp;
        this.nama_user = nama_user;
        this.jenis_kelamin = jenis_kelamin;
        this.penumpang_dewasa = penumpang_dewasa;
        this.penumpang_anak = penumpang_anak;
        this.id_destinasi = id_destinasi;
        this.id_perjalanan = id_perjalanan;
    }

    public int getBiaya() {
        return biaya;
    }

    public void setBiaya(int biaya) {
        this.biaya = biaya;
    }

    public String getDari() {
        return dari;
    }

    public void setDari(String dari) {
        this.dari = dari;
    }

    public String getAlamat_dari() {
        return alamat_dari;
    }

    public void setAlamat_dari(String alamat_dari) {
        this.alamat_dari = alamat_dari;
    }

    public String getTujuan() {
        return tujuan;
    }

    public void setTujuan(String tujuan) {
        this.tujuan = tujuan;
    }

    public String getAlamat_tujuan() {
        return alamat_tujuan;
    }

    public void setAlamat_tujuan(String alamat_tujuan) {
        this.alamat_tujuan = alamat_tujuan;
    }

    public String getTgl_berangkat() {
        return tgl_berangkat;
    }

    public void setTgl_berangkat(String tgl_berangkat) {
        this.tgl_berangkat = tgl_berangkat;
    }

    public String getHari_keberangkatan() {
        return hari_keberangkatan;
    }

    public void setHari_keberangkatan(String hari_keberangkatan) {
        this.hari_keberangkatan = hari_keberangkatan;
    }

    public String getTgl_sampai() {
        return tgl_sampai;
    }

    public void setTgl_sampai(String tgl_sampai) {
        this.tgl_sampai = tgl_sampai;
    }

    public String getNo_hp() {
        return no_hp;
    }

    public void setNo_hp(String no_hp) {
        this.no_hp = no_hp;
    }

    public String getNama_user() {
        return nama_user;
    }

    public void setNama_user(String nama_user) {
        this.nama_user = nama_user;
    }

    public char getJenis_kelamin() {
        return jenis_kelamin;
    }

    public void setJenis_kelamin(char jenis_kelamin) {
        this.jenis_kelamin = jenis_kelamin;
    }

    public int getPenumpang_dewasa() {
        return penumpang_dewasa;
    }

    public void setPenumpang_dewasa(int penumpang_dewasa) {
        this.penumpang_dewasa = penumpang_dewasa;
    }

    public int getPenumpang_anak() {
        return penumpang_anak;
    }

    public void setPenumpang_anak(int penumpang_anak) {
        this.penumpang_anak = penumpang_anak;
    }

    public int getId_destinasi() {
        return id_destinasi;
    }

    public void setId_destinasi(int id_destinasi) {
        this.id_destinasi = id_destinasi;
    }

    public int getId_perjalanan() {
        return id_perjalanan;
    }

    public void setId_perjalanan(int id_perjalanan) {
        this.id_perjalanan = id_perjalanan;
    }
}
