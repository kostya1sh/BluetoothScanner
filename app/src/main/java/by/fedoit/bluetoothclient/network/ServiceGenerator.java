package by.fedoit.bluetoothclient.network;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by kostya on 24.01.2017.
 */

public class ServiceGenerator {
    public static final String BASE_URL = "http://192.168.1.221:1337/";

    private static Retrofit.Builder baseJsonBuilder =
            new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create());


    private ServiceGenerator() { /* private constructor cuz its static class*/ }

    public static <S> S createService(Class<S> serviceClass) {
        OkHttpClient okHttpClient = new OkHttpClient();
        okHttpClient = okHttpClient.newBuilder()
                .build();
        Retrofit retrofit = baseJsonBuilder.client(okHttpClient).build();
        return retrofit.create(serviceClass);
    }

}
