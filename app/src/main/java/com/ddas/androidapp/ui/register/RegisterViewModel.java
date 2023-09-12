package com.ddas.androidapp.ui.register;

import android.text.TextUtils;

import androidx.annotation.StringRes;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.ddas.androidapp.R;
import com.ddas.androidapp.util.CredentialsValidator;

public class RegisterViewModel extends ViewModel
{
    public RegisterViewModel()
    {
        registerRequest = new MutableLiveData<>(new RegisterRequest());
        credentialsAreValid = new MutableLiveData<>();
        registrationIsSuccessful = new MutableLiveData<>();
    }

    public void register()
    {
        String email = registerRequest.getValue().getEmail();
        String password = registerRequest.getValue().getPassword();
        String rePassword = registerRequest.getValue().getRePassword();

        if(credentialsAreValid(email, password, rePassword))
        {
            // TODO: Create user's account and set registration is successful flag
        }

        // TODO: PopupWindows when user is waiting for task execution
        // TODO: Snackbar for email verification
    }

    private boolean credentialsAreValid(String email, String password, String rePassword)
    {
        // TODO: Make error messages more informative

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
        else if(!CredentialsValidator.emailIsValid(email))
        {
            errorMessageResId = R.string.EMAIL_IS_NOT_VALID;
            credentialsAreValid.setValue(false);
        }
        else if(!CredentialsValidator.passwordIsValid(password))
        {
            errorMessageResId = R.string.PASSWORD_IS_NOT_VALID;
            credentialsAreValid.setValue(false);
        }
        else if(!CredentialsValidator.passwordsMatch(password, rePassword))
        {
            errorMessageResId = R.string.PASSWORDS_NOT_MATCH;
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
}
