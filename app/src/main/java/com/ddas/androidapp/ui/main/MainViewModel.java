package com.ddas.androidapp.ui.main;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.ddas.androidapp.application.AppConstants;
import com.ddas.androidapp.network.client.AuthManager;
import com.ddas.androidapp.util.PreferencesManager;

public class MainViewModel extends AndroidViewModel
{
    public MainViewModel(Application app)
    {
        super(app);

        userIsAuthenticated = new MutableLiveData<>(false);
        authManager = new AuthManager(app);
        preferencesManager = new PreferencesManager(app, AppConstants.PREFS_FILE_NAME, Context.MODE_PRIVATE);
    }

    public void logout()
    {
        String token = preferencesManager.getString(AppConstants.CURRENT_USER_TOKEN);

        authManager.logout(token, (response, statusCode) ->
        {
            preferencesManager.clear();
            userIsAuthenticated.setValue(false);

            Log.d("DEVELOPMENT:MainViewModel", "register:" + response);
        });
    }

    private final MutableLiveData<Boolean> userIsAuthenticated;
    private final PreferencesManager preferencesManager;
    private final AuthManager authManager;
}
