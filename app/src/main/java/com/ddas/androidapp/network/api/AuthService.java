package com.ddas.androidapp.network.api;

import com.ddas.androidapp.network.api.model.LoginDTO;
import com.ddas.androidapp.network.api.model.RegisterDTO;
import com.ddas.androidapp.network.server.model.ApiResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface AuthService
{
    @POST("/api/v1/auth/register")
    Call<ApiResponse<String>> register(@Body RegisterDTO request);
    @POST("/api/v1/auth/login")
    Call<ApiResponse<String>> login(@Body LoginDTO request);

    // TODO: podmienic parametr na request body
    @FormUrlEncoded
    @POST("/api/v1/auth/logout")
    Call<ApiResponse<String>> logout(@Field("token") String token);
}
