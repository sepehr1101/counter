package com.sepehr.sa_sh.abfacounter01.NetworkCommunication;

import com.sepehr.sa_sh.abfacounter01.DifferentCompanyManager;
import com.sepehr.sa_sh.abfacounter01.NetworkHelper;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by saeid on 5/7/2017.
 */

public class ServiceGenerator {
    private static TimeUnit TIME_UNIT=TimeUnit.SECONDS;
    private static long READ_TIMEOUT=120;
    private static long WRITE_TIMEOUT=60;
    private static long CONNECT_TIMEOUT=10;
    private static boolean RETRY_ENABLED=false;

    private ServiceGenerator() {

    }

    public static OkHttpClient getHttpClient() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient
                .Builder()
                .readTimeout(READ_TIMEOUT, TIME_UNIT)
                .writeTimeout(WRITE_TIMEOUT, TIME_UNIT)
                .connectTimeout(CONNECT_TIMEOUT, TIME_UNIT)
                .retryOnConnectionFailure(RETRY_ENABLED)
                .addInterceptor(interceptor).build();
        return  client;
    }

    public static Retrofit getInstance(boolean isLocal){
        String baseUrl;
        if(isLocal){
            baseUrl= DifferentCompanyManager.getLocalBaseUrl(DifferentCompanyManager.getActiveCompanyName());
        }
        else {
            baseUrl=DifferentCompanyManager.getBaseUrl(DifferentCompanyManager.getActiveCompanyName());
        }
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(NetworkHelper.getHttpClient())//added recently
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        return retrofit;
    }
}
