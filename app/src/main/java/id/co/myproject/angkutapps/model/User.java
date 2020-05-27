package id.co.myproject.angkutapps.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class User {

    @SerializedName("id_user")
    @Expose
    private String idUser;

    @SerializedName("email")
    @Expose
    private String email;

    @SerializedName("nama")
    @Expose
    private String nama;

    @SerializedName("ktp")
    @Expose
    private String ktp;

    @SerializedName("merk_mobil")
    @Expose
    private String merkMobil;

    @SerializedName("plat")
    @Expose
    private String plat;

    @SerializedName("jk")
    @Expose
    private String jk;

    @SerializedName("foto")
    @Expose
    private String foto;

    @SerializedName("alamat")
    @Expose
    private String alamat;

    @SerializedName("no_hp")
    @Expose
    private String noHp;

    public User(String idUser, String email, String nama, String ktp, String merkMobil, String plat, String jk, String foto, String alamat, String noHp) {
        this.idUser = idUser;
        this.email = email;
        this.nama = nama;
        this.ktp = ktp;
        this.merkMobil = merkMobil;
        this.plat = plat;
        this.jk = jk;
        this.foto = foto;
        this.alamat = alamat;
        this.noHp = noHp;
    }

    public String getIdUser() {
        return idUser;
    }

    public void setIdUser(String idUser) {
        this.idUser = idUser;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getKtp() {
        return ktp;
    }

    public void setKtp(String ktp) {
        this.ktp = ktp;
    }

    public String getMerkMobil() {
        return merkMobil;
    }

    public void setMerkMobil(String merkMobil) {
        this.merkMobil = merkMobil;
    }

    public String getPlat() {
        return plat;
    }

    public void setPlat(String plat) {
        this.plat = plat;
    }

    public String getJk() {
        return jk;
    }

    public void setJk(String jk) {
        this.jk = jk;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public String getAlamat() {
        return alamat;
    }

    public void setAlamat(String alamat) {
        this.alamat = alamat;
    }

    public String getNoHp() {
        return noHp;
    }

    public void setNoHp(String noHp) {
        this.noHp = noHp;
    }
}
