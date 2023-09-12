package com.ddas.androidapp.network.api;

import com.ddas.androidapp.network.model.User;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface UserService
{
    @GET("/api/v1/users")
    Call<List<User>> findAll();
    @GET("/api/v1/find-by-id")
    Call<User> findUserById(Long id);
}
