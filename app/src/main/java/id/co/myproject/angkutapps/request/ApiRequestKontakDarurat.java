package id.co.myproject.angkutapps.request;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiRequestKontakDarurat {

    private static final String Base_URL = "https://angkutapps.com/angkut_api/";
    private static ApiRequestKontakDarurat apiClient;
    private static Retrofit retrofit;

    private ApiRequestKontakDarurat(){
        retrofit = new Retrofit.Builder()
                .baseUrl(Base_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public static synchronized ApiRequestKontakDarurat getInstance(){
        if(apiClient == null){
            apiClient = new ApiRequestKontakDarurat();
        }
        return apiClient;
    }

    public ApiKontakDarurat getApi(){
        return retrofit.create(ApiKontakDarurat.class);
    }

}
