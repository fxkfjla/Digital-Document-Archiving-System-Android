package com.ddas.androidapp.network.api;

import com.ddas.androidapp.network.server.model.ApiResponse;

public interface ApiCallback<T>
{
    void onResponse(ApiResponse<T> apiResponse, int statusCode);
}
