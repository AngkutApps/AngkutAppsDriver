package id.co.myproject.angkutapps.request;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiRequestPromo {

    private static final String Base_URL = "https://angkutapps.com/angkut_api/promo/";
    private static ApiRequestPromo apiClient;
    private static Retrofit retrofit;

    private ApiRequestPromo(){
        retrofit = new Retrofit.Builder()
                .baseUrl(Base_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public static synchronized ApiRequestPromo getInstance(){
        if(apiClient == null){
            apiClient = new ApiRequestPromo();
        }
        return apiClient;
    }

    public ApiPromo getApi(){
        return retrofit.create(ApiPromo.class);
    }
}
