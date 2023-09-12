package com.ddas.androidapp.network.client;

import android.util.Log;

import com.ddas.androidapp.network.RetrofitClient;
import com.ddas.androidapp.network.api.UserService;
import com.ddas.androidapp.network.model.User;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserManager
{
    public UserManager()
    {
        userService = RetrofitClient.getInstance().create(UserService.class);
    }

    public void findAll()
    {
        Call<List<User>> call = userService.findAll();
        call.enqueue(new Callback<List<User>>()
        {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response)
            {
                List<User> users = response.body();
                Log.d("DEVELOPMENT:UserManager", "response:success");
            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t)
            {
                Log.d("DEVELOPMENT:UserManager", "response:failure. Reason: " + t.getMessage());
                System.out.println("Failed to retrieve users.");
            }
        });
    }

    private UserService userService;
}
