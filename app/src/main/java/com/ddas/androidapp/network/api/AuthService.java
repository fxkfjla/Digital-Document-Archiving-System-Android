package com.ddas.androidapp.network.api;

import com.ddas.androidapp.network.server.model.ApiResponse;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface AuthService
{
    @FormUrlEncoded
    @POST("/api/v1/auth/register")
    Call<ApiResponse<String>> register(@Field("email") String email, @Field("password") String password, @Field("rePassword") String rePassword);
    @FormUrlEncoded
    @POST("/api/v1/auth/login")
    Call<ApiResponse<String>> login(@Field("email") String email, @Field("password") String password);
    @FormUrlEncoded
    @POST("/api/v1/auth/logout")
    Call<ApiResponse<String>> logout(@Field("token") String token);
}
