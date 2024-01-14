package com.ddas.androidapp.ui.register;

import android.app.Application;
import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.widget.Toast;

import androidx.annotation.StringRes;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.ddas.androidapp.R;
import com.ddas.androidapp.application.App;
import com.ddas.androidapp.application.AppConstants;
import com.ddas.androidapp.network.client.AuthManagerApi;
import com.ddas.androidapp.util.PreferencesManager;

import java.net.HttpURLConnection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegisterViewModel extends AndroidViewModel
{
    public RegisterViewModel(Application app)
    {
        super(app);

        registerRequest = new MutableLiveData<>(new RegisterRequest());
        credentialsAreValid = new MutableLiveData<>();
        registrationIsSuccessful = new MutableLiveData<>();
        authManagerApi = new AuthManagerApi(app);
        preferencesManager = new PreferencesManager(app, AppConstants.PREFS_FILE_NAME, Context.MODE_PRIVATE);
    }

    public void register()
    {
        if(credentialsAreValid())
        {
            authManagerApi.register(registerRequest.getValue(), (response, statusCode) ->
            {
                if(statusCode == HttpURLConnection.HTTP_OK)
                {
                    registrationIsSuccessful.setValue(true);
                }
                else
                {
                    Toast.makeText(App.getCurrentActivity(), response.getMessage(), Toast.LENGTH_SHORT).show();
                }
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
        else if(!Patterns.EMAIL_ADDRESS.matcher(email).matches())
        {
            errorMessageResId = R.string.EMAIL_IS_NOT_VALID;
            credentialsAreValid.setValue(false);
        }
        else if(!isPasswordValid(password))
        {
            errorMessageResId = R.string.PASSWORD_IS_NOT_VALID;
            credentialsAreValid.setValue(false);
        }
        else if(!password.equals(rePassword))
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

    private boolean isPasswordValid(String password)
    {
        String regex = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(password);

        return matcher.matches();
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
    private final AuthManagerApi authManagerApi;
    private final PreferencesManager preferencesManager;
}
