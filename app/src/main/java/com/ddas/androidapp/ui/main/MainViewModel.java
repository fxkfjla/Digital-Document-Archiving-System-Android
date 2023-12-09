package com.ddas.androidapp.ui.main;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.ddas.androidapp.application.AppConstants;
import com.ddas.androidapp.network.client.AuthManagerApi;
import com.ddas.androidapp.util.PreferencesManager;

public class MainViewModel extends AndroidViewModel
{
    public MainViewModel(Application app)
    {
        super(app);

        userIsAuthenticated = new MutableLiveData<>(false);
        authManagerApi = new AuthManagerApi(app);
        preferencesManager = new PreferencesManager(app, AppConstants.PREFS_FILE_NAME, Context.MODE_PRIVATE);

        userIsAuthenticated.setValue(preferencesManager.getBoolean(AppConstants.USER_LOGGED_IN));
    }

    public void logout()
    {
        String token = preferencesManager.getString(AppConstants.CURRENT_USER_TOKEN);

        authManagerApi.logout(token, (response, statusCode) ->
        {
            preferencesManager.clear();
            userIsAuthenticated.setValue(false);

            Log.d("DEVELOPMENT:MainViewModel", "register:" + response);
        });
    }

    public MutableLiveData<Boolean> getUserIsAuthenticated()
    {
        return userIsAuthenticated;
    }

    private final MutableLiveData<Boolean> userIsAuthenticated;
    private final PreferencesManager preferencesManager;
    private final AuthManagerApi authManagerApi;
}
