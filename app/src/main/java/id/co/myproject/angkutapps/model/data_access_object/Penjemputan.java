package id.co.myproject.angkutapps.model.data_access_object;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Penjemputan {
    @SerializedName("id_keberangkatan")
    @Expose
    private String idKeberangkatan;

    @SerializedName("id_penumpang")
    @Expose
    private String idPenumpang;

    @SerializedName("tempat")
    @Expose
    private String tempat;

    @SerializedName("jumlah_orang")
    @Expose
    private String jumlahOrang;

    @SerializedName("barang_bawaan")
    @Expose
    private String barangBawaan;


    public Penjemputan(String idKeberangkatan, String idPenumpang, String tempat, String jumlahOrang, String barangBawaan) {
        this.idKeberangkatan = idKeberangkatan;
        this.idPenumpang = idPenumpang;
        this.tempat = tempat;
        this.jumlahOrang = jumlahOrang;
        this.barangBawaan = barangBawaan;
    }

    public String getIdKeberangkatan() {
        return idKeberangkatan;
    }

    public void setIdKeberangkatan(String idKeberangkatan) {
        this.idKeberangkatan = idKeberangkatan;
    }

    public String getIdPenumpang() {
        return idPenumpang;
    }

    public void setIdPenumpang(String idPenumpang) {
        this.idPenumpang = idPenumpang;
    }

    public String getTempat() {
        return tempat;
    }

    public void setTempat(String tempat) {
        this.tempat = tempat;
    }

    public String getJumlahOrang() {
        return jumlahOrang;
    }

    public void setJumlahOrang(String jumlahOrang) {
        this.jumlahOrang = jumlahOrang;
    }

    public String getBarangBawaan() {
        return barangBawaan;
    }

    public void setBarangBawaan(String barangBawaan) {
        this.barangBawaan = barangBawaan;
    }
}
