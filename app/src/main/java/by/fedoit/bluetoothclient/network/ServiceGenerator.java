package by.fedoit.bluetoothclient.network;

import android.support.annotation.NonNull;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by kostya on 24.01.2017.
 */

public class ServiceGenerator {
    public static final String BASE_URL = "http://178.124.147.109:54545/";

    @NonNull
    public static Retrofit.Builder createJsonBuilder(String baseUrl) {
        return new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create());
    }

    private ServiceGenerator() { /* private constructor cuz its static class*/ }

    public static <S> S createService(Class<S> serviceClass, Retrofit.Builder builder) {
        OkHttpClient okHttpClient = new OkHttpClient();
        okHttpClient = okHttpClient.newBuilder()
                .build();
        Retrofit retrofit = builder.client(okHttpClient).build();
        return retrofit.create(serviceClass);
    }

}
