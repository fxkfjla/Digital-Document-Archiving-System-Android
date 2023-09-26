package com.ddas.androidapp.network.client;

import android.util.Log;

import com.ddas.androidapp.network.RetrofitClient;
import com.ddas.androidapp.network.api.ApiCallback;
import com.ddas.androidapp.network.api.AuthService;
import com.ddas.androidapp.network.server.model.ApiResponse;
import com.ddas.androidapp.network.exception.ServerNoResponseException;
import com.ddas.androidapp.ui.login.LoginRequest;
import com.ddas.androidapp.ui.register.RegisterRequest;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AuthManager
{
    public AuthManager()
    {
        authService = RetrofitClient.getInstance().create(AuthService.class);
    }

    public void register(RegisterRequest user, ApiCallback<String> callback)
    {
        Call<ApiResponse<String>> call = authService.register(user.getEmail(), user.getPassword(), user.getRePassword());

        enqueue(call, callback);
    }

    public void login(LoginRequest user, ApiCallback<String> callback)
    {
        Call<ApiResponse<String>> call = authService.login(user.getEmail(), user.getPassword());

        enqueue(call, callback);
    }

    public void logout(String token, ApiCallback<String> callback)
    {
        Call<ApiResponse<String>> call = authService.logout(token);

        enqueue(call, callback);
    }

    private <T> void enqueue(Call<ApiResponse<T>> call, ApiCallback<T> callback)
    {
        call.enqueue(new Callback<ApiResponse<T>>()
        {
            // TODO: Implement proper exception handling
            @Override
            public void onResponse(Call<ApiResponse<T>> call, Response<ApiResponse<T>> response)
            {
                Log.d("DEVELOPMENT:AuthManager", "enqueue:onResponse:" + response.body());

                if(response.isSuccessful())
                {
                    callback.onResponse(response.body());
                }
                else
                {
                    try
                    {
                        Log.d("DEVELOPMENT:AuthManager", "enqueue:onResponse:" + response.errorBody().string());
                    }
                    catch(Exception e)
                    {
                        throw new RuntimeException(e);
                    }
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<T>> call, Throwable t)
            {
                throw new ServerNoResponseException("No response from server!" + t);
            }
        });
    }

    private final AuthService authService;
}
