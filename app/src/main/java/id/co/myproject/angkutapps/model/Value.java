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

    @SerializedName("kode_driver")
    @Expose
    private String kodeDriver;

    @SerializedName("no_hp")
    @Expose
    private String noHpDriver;

    @SerializedName("id_usulan")
    @Expose
    private int idUsulan;


    public Value(int value, String message, String kodeDriver, String noHpDriver, int idUsulan) {
        this.value = value;
        this.message = message;
        this.kodeDriver = kodeDriver;
        this.noHpDriver = noHpDriver;
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

    public String getKodeDriver() {
        return kodeDriver;
    }

    public void setKodeDriver(String kodeDriver) {
        this.kodeDriver = kodeDriver;
    }

    public String getNoHpDriver() {
        return noHpDriver;
    }

    public void setNoHpDriver(String noHpDriver) {
        this.noHpDriver = noHpDriver;
    }

    public int getIdUsulan() {
        return idUsulan;
    }

    public void setIdUsulan(int idUsulan) {
        this.idUsulan = idUsulan;
    }
}
