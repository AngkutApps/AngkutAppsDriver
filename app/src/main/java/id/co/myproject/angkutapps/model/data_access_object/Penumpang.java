package id.co.myproject.angkutapps.model.data_access_object;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Penumpang {
    @SerializedName("id_penumpang")
    @Expose
    private String idPenumpang;

    @SerializedName("email")
    @Expose
    private String email;

    @SerializedName("nama")
    @Expose
    private String nama;

    @SerializedName("no_hp")
    @Expose
    private String noHp;

    @SerializedName("foto")
    @Expose
    private String foto;

    public Penumpang(String idPenumpang, String email, String nama, String noHp, String foto) {
        this.idPenumpang = idPenumpang;
        this.email = email;
        this.nama = nama;
        this.noHp = noHp;
        this.foto = foto;
    }

    public String getIdPenumpang() {
        return idPenumpang;
    }

    public void setIdPenumpang(String idPenumpang) {
        this.idPenumpang = idPenumpang;
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

    public String getNoHp() {
        return noHp;
    }

    public void setNoHp(String noHp) {
        this.noHp = noHp;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }
}
