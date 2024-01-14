package com.ddas.androidapp.network.client;

import android.content.Context;
import android.widget.Toast;

import com.ddas.androidapp.application.App;
import com.ddas.androidapp.network.RetrofitClient;
import com.ddas.androidapp.network.api.ApiCallback;
import com.ddas.androidapp.network.api.AuthService;
import com.ddas.androidapp.network.api.model.LoginDTO;
import com.ddas.androidapp.network.api.model.RegisterDTO;
import com.ddas.androidapp.network.api.model.TokenDTO;
import com.ddas.androidapp.network.server.model.ApiResponse;
import com.ddas.androidapp.network.exception.ServerNoResponseException;
import com.ddas.androidapp.network.server.model.RetrofitErrorBody;
import com.ddas.androidapp.ui.login.LoginRequest;
import com.ddas.androidapp.ui.register.RegisterRequest;

import java.io.IOException;
import java.lang.annotation.Annotation;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Converter;
import retrofit2.Response;
import retrofit2.Retrofit;

public class AuthManagerApi
{
    public AuthManagerApi(Context context)
    {
        retrofit = RetrofitClient.getInstance(context);
        authService = retrofit.create(AuthService.class);
    }

    public void register(RegisterRequest user, ApiCallback<String> callback)
    {
        RegisterDTO registerDTO = new RegisterDTO(user.getEmail(), user.getPassword(), user.getRePassword());
        Call<ApiResponse<String>> call = authService.register(registerDTO);

        enqueue(call, callback);
    }

    public void login(LoginRequest user, ApiCallback<String> callback)
    {
        LoginDTO request = new LoginDTO(user.getEmail(), user.getPassword());
        Call<ApiResponse<String>> call = authService.login(request);

        enqueue(call, callback);
    }

    public void logout(String token, ApiCallback<String> callback)
    {
        Call<ApiResponse<String>> call = authService.logout(new TokenDTO(token));

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
                if(response.isSuccessful())
                {
                    callback.onResponse(response.body(), response.code());
                }
                else
                {
                    ApiResponse<T> errorResponse = errorToApiResponse(response.errorBody());
                    callback.onResponse(errorResponse, response.code());
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<T>> call, Throwable t)
            {
                Toast.makeText(App.getCurrentActivity(), "Brak odpowiedzi od serwera!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private <T> ApiResponse<T> errorToApiResponse(ResponseBody errorBody)
    {
        ApiResponse<T> errorResponse = new ApiResponse<>();

        Converter<ResponseBody, RetrofitErrorBody> converter =
                retrofit.responseBodyConverter(RetrofitErrorBody.class, new Annotation[0]);

        try
        {
            RetrofitErrorBody error = converter.convert(errorBody);

            errorResponse.setStatus(error.getStatus());
            errorResponse.setPath(error.getPath());
            errorResponse.setMessage(error.getMessage());
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }

        return errorResponse;
    }

    private final Retrofit retrofit;
    private final AuthService authService;
}
