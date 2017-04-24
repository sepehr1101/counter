package com.sepehr.sa_sh.abfacounter01;
import java.util.concurrent.TimeUnit;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;

/**
 * Created by sa-sh on 8/18/2016.
 */
public final class NetworkHelper {
    private static TimeUnit TIME_UNIT=TimeUnit.SECONDS;
    private static long READ_TIMEOUT=120;
    private static long WRITE_TIMEOUT=60;
    private static long CONNECT_TIMEOUT=10;

    public NetworkHelper() {

    }

    public static OkHttpClient getHttpClient() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient
                .Builder()
                .readTimeout(READ_TIMEOUT, TIME_UNIT)
                .writeTimeout(WRITE_TIMEOUT, TIME_UNIT)
                .connectTimeout(CONNECT_TIMEOUT, TIME_UNIT)
                .retryOnConnectionFailure(false)
                .addInterceptor(interceptor).build();
        return  client;
    }

}
