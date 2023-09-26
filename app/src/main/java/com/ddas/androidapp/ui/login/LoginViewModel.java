package com.ddas.androidapp.ui.login;

import android.text.TextUtils;

import androidx.annotation.StringRes;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.ddas.androidapp.R;

public class LoginViewModel extends ViewModel
{
    public LoginViewModel()
    {
        loginRequest = new MutableLiveData<>(new LoginRequest());
        credentialsAreValid = new MutableLiveData<>();
        loginStatus = new MutableLiveData<>();
    }

    public void login()
    {

        if(credentialsAreValid())
        {
            // TODO: Authorize user and change login status
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
}
