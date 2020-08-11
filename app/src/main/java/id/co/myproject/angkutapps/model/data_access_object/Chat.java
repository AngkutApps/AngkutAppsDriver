package id.co.myproject.angkutapps.model.data_access_object;

public class Chat {

    String kondisi, message, waktu;

    public Chat(String kondisi, String message, String waktu) {
        this.kondisi = kondisi;
        this.message = message;
        this.waktu = waktu;
    }

    public String getKondisi() {
        return kondisi;
    }

    public void setKondisi(String kondisi) {
        this.kondisi = kondisi;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getWaktu() {
        return waktu;
    }

    public void setWaktu(String waktu) {
        this.waktu = waktu;
    }
}
