package com.ddas.androidapp.network.client;

import android.content.Context;

import com.ddas.androidapp.network.RetrofitClient;
import com.ddas.androidapp.network.api.ApiCallback;
import com.ddas.androidapp.network.api.FileService;
import com.ddas.androidapp.network.exception.ServerNoResponseException;
import com.ddas.androidapp.network.server.model.ApiResponse;
import com.ddas.androidapp.network.server.model.RetrofitErrorBody;
import com.ddas.androidapp.ui.main.fragment.list.FileModel;

import java.io.File;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Converter;
import retrofit2.Response;
import retrofit2.Retrofit;

public class FileManagerApi
{
    public FileManagerApi(Context context)
    {
        retrofit = RetrofitClient.getInstance(context);
        fileService = retrofit.create(FileService.class);
    }

    public void upload(List<FileModel> files, ApiCallback<String> callback)
    {
        for(FileModel file : files)
        {
            MultipartBody.Part filePart = MultipartBody.Part.createFormData("file", file.getName(),
                    RequestBody.create(new File(file.getFilePath()), MediaType.parse("multipart/form-data")));

            List<String> tags = Arrays.asList(file.getTags().trim().split(","));

            Call<ApiResponse<String>> call = fileService.upload(filePart, file.getName(), file.getDescription(), tags);

            enqueue(call, callback);
        }
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
                throw new ServerNoResponseException("No response from server!" + t);
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
    private final FileService fileService;
}
