package id.co.myproject.angkutapps.request;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiRequestRiwayat {

    private static final String Base_URL = "https://angkutapps.com/angkut_api/riwayat/";
    private static ApiRequestRiwayat apiClient;
    private static Retrofit retrofit;

    private ApiRequestRiwayat(){
        retrofit = new Retrofit.Builder()
                .baseUrl(Base_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public static synchronized ApiRequestRiwayat getInstance(){
        if(apiClient == null){
            apiClient = new ApiRequestRiwayat();
        }
        return apiClient;
    }

    public ApiRiwayat getApi(){
        return retrofit.create(ApiRiwayat.class);
    }

}
