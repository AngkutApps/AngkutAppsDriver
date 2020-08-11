package id.co.myproject.angkutapps.model.data_access_object;

public class DestinasiPassenger {
    private String address, biaya, city, fromLocation, idDestinasi, jumlahBarang;
    private JumlahOrang jumlahOrang;

    public DestinasiPassenger() {
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getBiaya() {
        return biaya;
    }

    public void setBiaya(String biaya) {
        this.biaya = biaya;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getFromLocation() {
        return fromLocation;
    }

    public void setFromLocation(String fromLocation) {
        this.fromLocation = fromLocation;
    }

    public String getIdDestinasi() {
        return idDestinasi;
    }

    public void setIdDestinasi(String idDestinasi) {
        this.idDestinasi = idDestinasi;
    }

    public String getJumlahBarang() {
        return jumlahBarang;
    }

    public void setJumlahBarang(String jumlahBarang) {
        this.jumlahBarang = jumlahBarang;
    }

    public JumlahOrang getJumlahOrang() {
        return jumlahOrang;
    }

    public void setJumlahOrang(JumlahOrang jumlahOrang) {
        this.jumlahOrang = jumlahOrang;
    }
}
