package id.co.myproject.angkutapps.model.data_access_object;

public class loadView_rw_perjalanan {

    int biaya;
    String dari, tujuan, hari_keberangkatan, tgl_berangkat, tgl_sampai, nama_user;
    char jenis_kelamin;
    int penumpang_dewasa, penumpang_anak, id_destinasi;

    public loadView_rw_perjalanan(int biaya, String dari, String tujuan, String hari_keberangkatan, String tgl_berangkat, String tgl_sampai, String nama_user, char jenis_kelamin, int penumpang_dewasa, int penumpang_anak, int id_destinasi) {
        this.biaya = biaya;
        this.dari = dari;
        this.tujuan = tujuan;
        this.hari_keberangkatan = hari_keberangkatan;
        this.tgl_berangkat = tgl_berangkat;
        this.tgl_sampai = tgl_sampai;
        this.nama_user = nama_user;
        this.jenis_kelamin = jenis_kelamin;
        this.penumpang_dewasa = penumpang_dewasa;
        this.penumpang_anak = penumpang_anak;
        this.id_destinasi = id_destinasi;
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

    public String getTujuan() {
        return tujuan;
    }

    public void setTujuan(String tujuan) {
        this.tujuan = tujuan;
    }

    public String getHari_keberangkatan() {
        return hari_keberangkatan;
    }

    public void setHari_keberangkatan(String hari_keberangkatan) {
        this.hari_keberangkatan = hari_keberangkatan;
    }

    public String getTgl_berangkat() {
        return tgl_berangkat;
    }

    public void setTgl_berangkat(String tgl_berangkat) {
        this.tgl_berangkat = tgl_berangkat;
    }

    public String getTgl_sampai() {
        return tgl_sampai;
    }

    public void setTgl_sampai(String tgl_sampai) {
        this.tgl_sampai = tgl_sampai;
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
}
