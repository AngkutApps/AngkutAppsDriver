package id.co.myproject.angkutapps.model.data_access_object;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Driver implements Parcelable {
    @SerializedName("kode_driver")
    @Expose
    private String kodeDriver;

    @SerializedName("email")
    @Expose
    private String email;

    @SerializedName("nama")
    @Expose
    private String nama;

    @SerializedName("merk_mobil")
    @Expose
    private String merkMobil;

    @SerializedName("plat_mobil")
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

    @SerializedName("id_jenis_kendaraan")
    @Expose
    private String idJenisKendaraan;

    public Driver(String kodeDriver, String email, String nama, String merkMobil, String plat, String jk, String foto, String alamat, String noHp, String idJenisKendaraan) {
        this.kodeDriver = kodeDriver;
        this.email = email;
        this.nama = nama;
        this.merkMobil = merkMobil;
        this.plat = plat;
        this.jk = jk;
        this.foto = foto;
        this.alamat = alamat;
        this.noHp = noHp;
        this.idJenisKendaraan = idJenisKendaraan;
    }

    public Driver() {
    }

    public String getIdJenisKendaraan() {
        return idJenisKendaraan;
    }

    public void setIdJenisKendaraan(String idJenisKendaraan) {
        this.idJenisKendaraan = idJenisKendaraan;
    }

    public String getKodeDriver() {
        return kodeDriver;
    }

    public void setKodeDriver(String kodeDriver) {
        this.kodeDriver = kodeDriver;
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


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.kodeDriver);
        dest.writeString(this.email);
        dest.writeString(this.nama);
        dest.writeString(this.merkMobil);
        dest.writeString(this.plat);
        dest.writeString(this.jk);
        dest.writeString(this.foto);
        dest.writeString(this.alamat);
        dest.writeString(this.noHp);
        dest.writeString(this.idJenisKendaraan);
    }

    protected Driver(Parcel in) {
        this.kodeDriver = in.readString();
        this.email = in.readString();
        this.nama = in.readString();
        this.merkMobil = in.readString();
        this.plat = in.readString();
        this.jk = in.readString();
        this.foto = in.readString();
        this.alamat = in.readString();
        this.noHp = in.readString();
        this.idJenisKendaraan = in.readString();
    }

    public static final Parcelable.Creator<Driver> CREATOR = new Parcelable.Creator<Driver>() {
        @Override
        public Driver createFromParcel(Parcel source) {
            return new Driver(source);
        }

        @Override
        public Driver[] newArray(int size) {
            return new Driver[size];
        }
    };
}
