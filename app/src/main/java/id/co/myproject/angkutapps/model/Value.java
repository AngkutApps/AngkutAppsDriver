package id.co.myproject.angkutapps.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Value {
    @SerializedName("value")
    @Expose
    private int value;

    @SerializedName("message")
    @Expose
    private String message;

    @SerializedName("id_user")
    @Expose
    private int idUser;

    @SerializedName("id_usulan")
    @Expose
    private int idUsulan;


    public Value(int value, String message, int idUser, int idUsulan) {
        this.value = value;
        this.message = message;
        this.idUser = idUser;
        this.idUsulan = idUsulan;
    }


    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getIdUser() {
        return idUser;
    }

    public void setIdUser(int idUser) {
        this.idUser = idUser;
    }

    public int getIdUsulan() {
        return idUsulan;
    }

    public void setIdUsulan(int idUsulan) {
        this.idUsulan = idUsulan;
    }
}
