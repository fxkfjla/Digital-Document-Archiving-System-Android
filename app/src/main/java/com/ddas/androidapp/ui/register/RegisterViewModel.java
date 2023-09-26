package com.ddas.androidapp.ui.register;

import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.StringRes;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.ddas.androidapp.R;
import com.ddas.androidapp.network.client.AuthManager;

public class RegisterViewModel extends ViewModel
{
    public RegisterViewModel()
    {
        registerRequest = new MutableLiveData<>(new RegisterRequest());
        credentialsAreValid = new MutableLiveData<>();
        registrationIsSuccessful = new MutableLiveData<>();
        authManager = new AuthManager();
    }

    public void register()
    {
        if(credentialsAreValid())
        {
            // TODO: Create user's account and set registration is successful flag

            authManager.register(registerRequest.getValue(), response ->
            {
                Log.d("DEVELOPMENT:RegisterViewModel", "register:success:" + response.getData());
            });
        }

        // TODO: PopupWindows when user is waiting for task execution
        // TODO: Snackbar for email verification
    }

    private boolean credentialsAreValid()
    {
        // TODO: Make error messages more informative

        String email = registerRequest.getValue().getEmail();
        String password = registerRequest.getValue().getPassword();
        String rePassword = registerRequest.getValue().getRePassword();

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
        else if(TextUtils.isEmpty(rePassword))
        {
            errorMessageResId = R.string.REPEAT_THE_PASSWORD;
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
    public LiveData<RegisterRequest> getRegisterRequest() { return registerRequest; }
    public LiveData<Boolean> getCredentialsAreValid() { return credentialsAreValid; }
    public LiveData<Boolean> getRegistrationIsSuccessful() { return registrationIsSuccessful; }

    @StringRes
    private int errorMessageResId;
    private final MutableLiveData<RegisterRequest> registerRequest;
    private final MutableLiveData<Boolean> credentialsAreValid;
    private final MutableLiveData<Boolean> registrationIsSuccessful;
    private final AuthManager authManager;
}
