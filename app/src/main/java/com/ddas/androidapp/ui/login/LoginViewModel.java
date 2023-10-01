package com.ddas.androidapp.ui.login;

import android.app.Application;
import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.StringRes;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.ddas.androidapp.R;
import com.ddas.androidapp.application.AppConstants;
import com.ddas.androidapp.network.client.AuthManager;
import com.ddas.androidapp.util.PreferencesManager;

import java.net.HttpURLConnection;

public class LoginViewModel extends AndroidViewModel
{
    public LoginViewModel(Application app)
    {
        super(app);

        loginRequest = new MutableLiveData<>(new LoginRequest());
        credentialsAreValid = new MutableLiveData<>();
        loginStatus = new MutableLiveData<>();
        authManager = new AuthManager(app);
        preferencesManager = new PreferencesManager(app, AppConstants.PREFS_FILE_NAME, Context.MODE_PRIVATE);
    }

    public void login()
    {
        if(credentialsAreValid())
        {
            // TODO: Authorize user and change login status
            authManager.login(loginRequest.getValue(), (response, statusCode) ->
            {
                if(statusCode == HttpURLConnection.HTTP_OK)
                {
                    String token = response.getData();
                    String email = loginRequest.getValue().getEmail();

                    preferencesManager.putString(AppConstants.CURRENT_USER_EMAIL, email);
                    preferencesManager.putString(AppConstants.CURRENT_USER_TOKEN, token);
                    preferencesManager.putBoolean(AppConstants.USER_LOGGED_IN, true);

                    loginStatus.setValue(true);
                    Log.d("DEVELOPMENT:LoginViewModel", "login:success:" + response);
                }
                else
                {
                    Log.d("DEVELOPMENT:LoginViewModel", "login:failure:" + response);
                }
            });
        }

        // TODO: PopupWindows when user is waiting for task execution
    }

    private boolean credentialsAreValid()
    {
        // TODO: Make error messages more informative

        String email = loginRequest.getValue().getEmail();
        String password = loginRequest.getValue().getPassword();

        if(TextUtils.isEmpty(email))
        {
            errorMessageResId = R.string.EMAIL_IS_REQUIRED;
            credentialsAreValid.setValue(false);
        }
        else if(TextUtils.isEmpty(password))
        {
            errorMessageResId = R.string.PASSWORD_IS_REQUIRED;
            credentialsAreValid.setValue(false);
        }
        else
        {
            credentialsAreValid.setValue(true);
        }

        return credentialsAreValid.getValue();
    }

    // Getters
    public int getErrorMessageResId() { return errorMessageResId; }
    public LiveData<LoginRequest> getLoginRequest() { return loginRequest; }
    public LiveData<Boolean> getCredentialsAreValid() { return credentialsAreValid; }
    public LiveData<Boolean> getLoginStatus() { return loginStatus; }

    @StringRes
    private int errorMessageResId;
    private final MutableLiveData<LoginRequest>  loginRequest;
    private final MutableLiveData<Boolean> credentialsAreValid;
    private final MutableLiveData<Boolean> loginStatus;
    private final AuthManager authManager;
    private final PreferencesManager preferencesManager;
}
