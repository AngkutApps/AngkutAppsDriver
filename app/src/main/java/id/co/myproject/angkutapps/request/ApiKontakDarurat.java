package id.co.myproject.angkutapps.request;

import java.util.List;

import id.co.myproject.angkutapps.model.data_access_object.LoadKontakDarurat;
import id.co.myproject.angkutapps.model.data_access_object.LoadSKVoucher;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiKontakDarurat {

    @GET("read_kontak_darurat_driver_travel.php")
    Call<List<LoadKontakDarurat>> getKontakDarurat(
            @Query("no_hp") String no_hp
    );
}
