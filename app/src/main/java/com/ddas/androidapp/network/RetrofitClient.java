package com.ddas.androidapp.network;

import android.content.Context;

import com.ddas.androidapp.application.AppConstants;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient
{
    public static Retrofit getInstance(Context context)
    {
        if(retrofit == null)
        {
            OkHttpClient client = new OkHttpClient.Builder()
            .addInterceptor(new AuthInterceptor(context))
            .build();

            retrofit = new Retrofit.Builder()
            .baseUrl(AppConstants.API_BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build();
        }

        return retrofit;
    }

    private static Retrofit retrofit;
}
