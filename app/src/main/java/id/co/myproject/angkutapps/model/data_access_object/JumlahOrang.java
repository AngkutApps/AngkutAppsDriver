package id.co.myproject.angkutapps.model.data_access_object;

public class JumlahOrang {
    private String jumlahDewasa, jumlahAnak;

    public JumlahOrang() {
    }

    public JumlahOrang(String jumlahDewasa, String jumlahAnak) {
        this.jumlahDewasa = jumlahDewasa;
        this.jumlahAnak = jumlahAnak;
    }

    public String getJumlahDewasa() {
        return jumlahDewasa;
    }

    public void setJumlahDewasa(String jumlahDewasa) {
        this.jumlahDewasa = jumlahDewasa;
    }

    public String getJumlahAnak() {
        return jumlahAnak;
    }

    public void setJumlahAnak(String jumlahAnak) {
        this.jumlahAnak = jumlahAnak;
    }
}
