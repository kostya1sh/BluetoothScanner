package by.fedoit.bluetoothclient.network.interceptors;

import android.util.Log;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * Created by kostya on 03.01.2017.
 */

/**
 * Class for logging server response
 */
public class LogResponseInterceptor implements Interceptor {

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();

        Response response = chain.proceed(request);
        String raw = response.body().string();
        logResponseBody(raw);

        // Re-create the response before returning it because body can be read only once
        return response.newBuilder()
                .body(ResponseBody.create(response.body().contentType(), raw)).build();
    }

    public static void logResponseBody(String rawJson) {
        if (rawJson.length() > 200) {
            String str = "";
            int i = 0;
            Log.d("Interceptor", "raw response is:\n");
            do {
                if (200 * (i + 1) > rawJson.length()) {
                    str = rawJson.substring(200 * i, rawJson.length() - 1);
                } else {
                    str = rawJson.substring(200 * i, 200 * (i + 1));
                }
                Log.d("Interceptor", String.format("%s", str));
                i++;
            } while (str.length() == 200);
        } else {
            Log.d("Interceptor", String.format("raw response is: %s", rawJson));
        }
    }
}
