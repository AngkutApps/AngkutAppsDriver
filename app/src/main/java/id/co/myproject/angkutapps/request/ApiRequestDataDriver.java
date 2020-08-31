package id.co.myproject.angkutapps.request;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiRequestDataDriver {

    private static final String Base_URL = "https://angkutapps.com/angkut_api/driver/";
    private static ApiRequestDataDriver apiClient;
    private static Retrofit retrofit;

    private ApiRequestDataDriver(){
        retrofit = new Retrofit.Builder()
                .baseUrl(Base_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public static synchronized ApiRequestDataDriver getInstance(){
        if(apiClient == null){
            apiClient = new ApiRequestDataDriver();
        }
        return apiClient;
    }

    public ApiDataDriver getApi(){
        return retrofit.create(ApiDataDriver.class);
    }

}
