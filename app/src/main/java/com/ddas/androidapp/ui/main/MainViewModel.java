package com.ddas.androidapp.ui.main;

import android.app.Application;
import android.content.Context;

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
        authManager = new AuthManager();
        preferencesManager = new PreferencesManager(app, AppConstants.PREFS_FILE_NAME, Context.MODE_PRIVATE);
    }

    public void checkAuthentication()
    {
        boolean userIsLoggedIn = preferencesManager.getBoolean(AppConstants.USER_LOGGED_IN);

        if(userIsLoggedIn)
        {
            userIsAuthenticated.setValue(true);
        }
    }

    public void logout()
    {
        String email = preferencesManager.getString(AppConstants.CURRENT_USER);
        String token = preferencesManager.getString(email);

        authManager.logout(token, response ->
        {
            preferencesManager.clear();
            userIsAuthenticated.setValue(false);
        });
    }

    // Getters
    public MutableLiveData<Boolean> getUserIsAuthenticated() { return userIsAuthenticated; }

    private final MutableLiveData<Boolean> userIsAuthenticated;
    private final PreferencesManager preferencesManager;
    private final AuthManager authManager;
}
