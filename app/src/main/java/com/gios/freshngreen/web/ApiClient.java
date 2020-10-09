package com.gios.freshngreen.web;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.gios.freshngreen.utils.Urls.BASE_URL;

public class ApiClient {
    private static Retrofit retrofit;
    public static Retrofit getApiClient() {
        OkHttpClient okHttpClient = new OkHttpClient();
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.level(HttpLoggingInterceptor.Level.BODY);
        if (retrofit == null)
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(okHttpClient.newBuilder().connectTimeout(5 * 60 * 1000, TimeUnit.MILLISECONDS)
                            .readTimeout(5 * 60 * 1000, TimeUnit.MILLISECONDS)
                            .writeTimeout(5 * 60 * 1000, TimeUnit.MILLISECONDS)
                            .addInterceptor(interceptor)
                            .build())
                    .build();
        return retrofit;
    }
}
