package id.co.myproject.angkutapps.request;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class FCMClient {

    private static Retrofit retrofit = null;
    public static Retrofit getClient(String fcmUrl){
        if (retrofit == null){
            retrofit = new Retrofit.Builder()
                    .baseUrl(fcmUrl)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}
