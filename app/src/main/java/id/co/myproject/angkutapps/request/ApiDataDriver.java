package id.co.myproject.angkutapps.request;

import java.util.List;

import id.co.myproject.angkutapps.model.data_access_object.DataDriver;
import id.co.myproject.angkutapps.model.data_access_object.LoadVoucher;
import id.co.myproject.angkutapps.model.data_access_object.Value;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ApiDataDriver {

    @GET("read_data_driver.php")
    Call<List<DataDriver>> getDataDriver(
            @Query("no_hp") String no_hp
    );

    @FormUrlEncoded
    @POST("edit_profil_driver.php")
    Call<List<DataDriver>> editProfilDriver(
            @Field("foto") String foto,
            @Field("email") String email,
            @Field("no_hp") String no_hp
    );

}
