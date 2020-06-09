package com.example.gestorheme.ApiServices;

import android.content.Context;

import com.example.gestorheme.Common.CommonFunctions;
import com.example.gestorheme.Common.Preferencias;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClientInstance {

    private static Retrofit retrofit;
    private static final String BASE_URL = "https://gestor.djmrbug.com:8443/api/";

    public static Retrofit getRetrofitInstance(Context context) {
        if (retrofit == null) {
            OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
            httpClient.addInterceptor(new Interceptor() {
                  @Override
                  public Response intercept(Interceptor.Chain chain) throws IOException {
                      Request original = chain.request();
                      Request request = original.newBuilder()
                              .header("UniqueDeviceId", CommonFunctions.getDeviceId(context))
                              .header("Authorization", "Bearer " + Preferencias.getTokenFromSharedPreferences(context))
                              .method(original.method(), original.body())
                              .build();
                      return chain.proceed(request);
                  }
            });
            OkHttpClient client = httpClient.build();
            retrofit = new retrofit2.Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(client)
                    .build();
        }
        return retrofit;
    }
}
