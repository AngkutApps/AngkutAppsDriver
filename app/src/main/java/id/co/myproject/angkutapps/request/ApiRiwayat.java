package id.co.myproject.angkutapps.request;

import java.util.List;

import id.co.myproject.angkutapps.model.data_access_object.LoadVoucher;
import id.co.myproject.angkutapps.model.data_access_object.loadView_rw_perjalanan;
import id.co.myproject.angkutapps.model.data_access_object.loadView_rw_voucher_penggunaan;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiRiwayat {

    @GET("read_rw_perjalanan_driver.php")
    Call<List<loadView_rw_perjalanan>> getRiwayatPerjalanan(
            @Query("no_hp") String no_hp
    );

    @GET("read_rw_voucher_penggunaan_driver.php")
    Call<List<loadView_rw_voucher_penggunaan>> getRiwayatPenggunaanVoucher(
            @Query("no_hp") String no_hp
    );

    @GET("read_rw_voucher_pembelian_driver.php")
    Call<List<LoadVoucher>> getRiwayatPembelianVoucher(
            @Query("no_hp") String no_hp
    );

}
