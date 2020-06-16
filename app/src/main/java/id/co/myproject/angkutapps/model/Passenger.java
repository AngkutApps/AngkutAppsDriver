package id.co.myproject.angkutapps.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Passenger implements Parcelable {

    @SerializedName("email")
    @Expose
    private String email;

    @SerializedName("nama_user")
    @Expose
    private String nama;

    @SerializedName("jenis_kelamin")
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

    public Passenger() {
    }

    public Passenger(String email, String nama, String jk, String foto, String alamat, String noHp) {
        this.email = email;
        this.nama = nama;
        this.jk = jk;
        this.foto = foto;
        this.alamat = alamat;
        this.noHp = noHp;
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
        dest.writeString(this.email);
        dest.writeString(this.nama);
        dest.writeString(this.jk);
        dest.writeString(this.foto);
        dest.writeString(this.alamat);
        dest.writeString(this.noHp);
    }

    protected Passenger(Parcel in) {
        this.email = in.readString();
        this.nama = in.readString();
        this.jk = in.readString();
        this.foto = in.readString();
        this.alamat = in.readString();
        this.noHp = in.readString();
    }

    public static final Creator<Passenger> CREATOR = new Creator<Passenger>() {
        @Override
        public Passenger createFromParcel(Parcel source) {
            return new Passenger(source);
        }

        @Override
        public Passenger[] newArray(int size) {
            return new Passenger[size];
        }
    };
}
