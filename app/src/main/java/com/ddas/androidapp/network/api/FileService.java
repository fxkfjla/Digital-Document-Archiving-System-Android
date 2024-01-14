package com.ddas.androidapp.network.api;

import com.ddas.androidapp.network.server.model.ApiResponse;

import java.util.List;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface FileService
{
    @Multipart
    @POST("/api/v1/file/upload")
    Call<ApiResponse<String>> upload
    (
        @Part MultipartBody.Part file,
        @Query("name") String name,
        @Query("description") String description,
        @Query("tags") List<String> tags
    );
}
