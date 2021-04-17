package com.shopping.bloom.restService;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.shopping.bloom.utils.Const.*;

public class RetrofitBuilder {

    private static Retrofit retrofit;

    private static Gson gson = new GsonBuilder().setLenient().create();
    private static GsonConverterFactory gsonFactory = GsonConverterFactory.create(gson);
    private static OkHttpClient client = new OkHttpClient.Builder()
            .connectTimeout(CONNECTION_TIMEOUT, TimeUnit.SECONDS)   // establish connection to server
            .readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)            // time between each byte read from the server
            .writeTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS)          // time between each byte sent to server
            .build();


    public static Retrofit getInstance(String baseURL){
        return new Retrofit.Builder()
                .addConverterFactory(gsonFactory)
                .baseUrl(baseURL)
                .client(client)
                .build();
    }
}
