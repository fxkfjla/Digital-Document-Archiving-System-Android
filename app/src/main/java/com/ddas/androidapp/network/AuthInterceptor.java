package com.ddas.androidapp.network;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.ddas.androidapp.application.App;
import com.ddas.androidapp.application.AppConstants;
import com.ddas.androidapp.ui.main.MainActivity;
import com.ddas.androidapp.util.ActivityManager;
import com.ddas.androidapp.util.PreferencesManager;

import java.io.IOException;
import java.net.HttpURLConnection;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class AuthInterceptor implements Interceptor
{
    public AuthInterceptor(Context context)
    {
        preferencesManager = new PreferencesManager(context, AppConstants.PREFS_FILE_NAME, Context.MODE_PRIVATE);
        handler = new Handler(Looper.getMainLooper());
    }

    @NonNull
    @Override
    public Response intercept(@NonNull Chain chain) throws IOException
    {
        String token = preferencesManager.getString(AppConstants.CURRENT_USER_TOKEN);
        Request req = chain.request();

        if(!token.isEmpty())
        {
            req = chain.request().newBuilder().header(AppConstants.AUTH_HEADER, AppConstants.AUTH_BEARER + token).build();
        }

        Response res = chain.proceed(req);

        if(res.code() == HttpURLConnection.HTTP_UNAUTHORIZED)
        {
            showToastOnMainThread("Log in to perform this action!");

            preferencesManager.clear();
            ActivityManager.redirectToActivity(App.getCurrentActivity(), MainActivity.class);
        }

        return res;
    }

    private void showToastOnMainThread(final String message)
    {
        handler.post(() -> Toast.makeText(App.getCurrentActivity(), message, Toast.LENGTH_SHORT).show());
    }

    private final Handler handler;
    private final PreferencesManager preferencesManager;
}
