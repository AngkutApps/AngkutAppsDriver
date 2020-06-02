package id.co.myproject.angkutapps.request;

import java.util.List;

import id.co.myproject.angkutapps.model.JenisKendaraan;
import id.co.myproject.angkutapps.model.Penjemputan;
import id.co.myproject.angkutapps.model.Penumpang;
import id.co.myproject.angkutapps.model.Driver;
import id.co.myproject.angkutapps.model.Value;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ApiRequest {
    @FormUrlEncoded
    @POST("login_driver.php")
    Call<Value> loginUserRequest(
            @Field("no_hp") String noHp
    );

    @FormUrlEncoded
    @POST("cek_no_hp_driver.php")
    Call<Value> cekNoHpRequest(
            @Field("no_hp") String nohp
    );

    @FormUrlEncoded
    @POST("registrasi_driver.php")
    Call<Value> registrasiUserRequest(
            @Field("kode_driver") String kodeDriver,
            @Field("nama") String nama,
            @Field("email") String email,
            @Field("no_hp") String np_hp,
            @Field("jk") String jk,
            @Field("merk_mobil") String merkMobil,
            @Field("id_jenis_kendaraan") String idJenisKendaraan,
            @Field("plat_mobil") String platMobil
    );


    @FormUrlEncoded
    @POST("cek_email.php")
    Call<Value> cekEmailRequest(
            @Field("email") String email
    );

    @FormUrlEncoded
    @POST("lupa_password.php")
    Call<Value> lupaPasswordRequest(
            @Field("id_user") int idUser,
            @Field("password") String password
    );

    @GET("tampil_user.php")
    Call<Driver> userByIdRequest(
            @Query("id_user") int idUser
    );


    @FormUrlEncoded
    @POST("edit_profil.php")
    Call<Value> editProfilRequest(
            @Field("id_user") int idUser,
            @Field("email") String email,
            @Field("nama") String nama,
            @Field("ktp") String ktp,
            @Field("merk_mobil") String merk_mobil,
            @Field("plat") String plat,
            @Field("jk") String jk,
            @Field("foto") String foto,
            @Field("alamat") String alamat,
            @Field("no_hp") String noHp
    );


    @FormUrlEncoded
    @POST("input_riwayat.php")
    Call<Value> inputRiwayatRequest(
            @Field("id_keberangkatan") String idKeberangkatan,
            @Field("id_driver") int idDriver
    );

    @GET("tampil_penjemputan.php")
    Call<List<Penjemputan>> getPenjemputan(
            @Query("tempat") String tempat
    );

    @GET("tampil_penumpang.php")
    Call<Penumpang> getPenumpangById(
            @Query("id_penumpang") String idPenumpang
    );

    @GET("tampil_jenis.php")
    Call<List<JenisKendaraan>> getJenisKendaraan();
}
