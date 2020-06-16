package id.co.myproject.angkutapps.model;

public class Destinasi {
    private String idDestinasi;
    private String address;
    private String city;

    public Destinasi() {
    }

    public Destinasi(String idDestinasi, String address, String city) {
        this.idDestinasi = idDestinasi;
        this.address = address;
        this.city = city;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getIdDestinasi() {
        return idDestinasi;
    }

    public void setIdDestinasi(String idDestinasi) {
        this.idDestinasi = idDestinasi;
    }
}
