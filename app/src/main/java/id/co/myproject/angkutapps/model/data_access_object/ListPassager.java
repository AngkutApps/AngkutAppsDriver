package id.co.myproject.angkutapps.model.data_access_object;

public class ListPassager {
    private String id_destinasi;
    private String id_list;
    private String no_hp_user;

    public ListPassager() {
    }

    public ListPassager(String id_destinasi, String id_list, String no_hp_user) {
        this.id_destinasi = id_destinasi;
        this.id_list = id_list;
        this.no_hp_user = no_hp_user;
    }

    public String getId_destinasi() {
        return id_destinasi;
    }

    public void setId_destinasi(String id_destinasi) {
        this.id_destinasi = id_destinasi;
    }

    public String getId_list() {
        return id_list;
    }

    public void setId_list(String id_list) {
        this.id_list = id_list;
    }

    public String getNo_hp_user() {
        return no_hp_user;
    }

    public void setNo_hp_user(String no_hp_user) {
        this.no_hp_user = no_hp_user;
    }
}
