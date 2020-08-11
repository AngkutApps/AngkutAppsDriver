package id.co.myproject.angkutapps.model.data_access_object;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class JenisKendaraan {

    @SerializedName("id_jenis_kendaraan")
    @Expose
    private String idJenisKendaraan;

    @SerializedName("nama_jenis_kendaraan")
    @Expose
    private String namaJenisKendaraan;

    public JenisKendaraan(String idJenisKendaraan, String namaJenisKendaraan) {
        this.idJenisKendaraan = idJenisKendaraan;
        this.namaJenisKendaraan = namaJenisKendaraan;
    }

    public String getIdJenisKendaraan() {
        return idJenisKendaraan;
    }

    public String getNamaJenisKendaraan() {
        return namaJenisKendaraan;
    }
}
