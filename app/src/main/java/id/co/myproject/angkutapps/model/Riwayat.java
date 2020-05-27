package id.co.myproject.angkutapps.model;

import com.google.gson.annotations.SerializedName;

public class Riwayat {
    private String harga;
    private String asal;
    private String tujuan;
    private String hari;
    private String tanggal;

    public Riwayat(String harga, String asal, String tujuan, String hari, String tanggal) {
        this.harga = harga;
        this.asal = asal;
        this.tujuan = tujuan;
        this.hari = hari;
        this.tanggal = tanggal;
    }

    public String getHarga() {
        return harga;
    }

    public void setHarga(String harga) {
        this.harga = harga;
    }

    public String getAsal() {
        return asal;
    }

    public void setAsal(String asal) {
        this.asal = asal;
    }

    public String getTujuan() {
        return tujuan;
    }

    public void setTujuan(String tujuan) {
        this.tujuan = tujuan;
    }

    public String getHari() {
        return hari;
    }

    public void setHari(String hari) {
        this.hari = hari;
    }

    public String getTanggal() {
        return tanggal;
    }

    public void setTanggal(String tanggal) {
        this.tanggal = tanggal;
    }
}
