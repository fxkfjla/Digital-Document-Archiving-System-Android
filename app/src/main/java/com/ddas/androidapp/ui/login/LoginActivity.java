package com.ddas.androidapp.ui.login;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.ddas.androidapp.R;
import com.ddas.androidapp.databinding.ActivityLoginBinding;
import com.ddas.androidapp.ui.main.MainActivity;
import com.ddas.androidapp.ui.register.RegisterActivity;
import com.ddas.androidapp.util.ActivityManager;

public class LoginActivity extends AppCompatActivity
{
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_login);
        binding.setLifecycleOwner(this);

        initialize();
    }

    private void initialize()
    {
        // Initialize view model
        viewModel = new ViewModelProvider(this).get(LoginViewModel.class);
        binding.setViewModel(viewModel);

        // Set observers
        viewModel.getLoginStatus().observe(this, loginStatus ->
        {
            if(loginStatus)
            {
                Log.d("DEVELOPMENT:LoginActivity", "redirectToMainActivity:success");
                ActivityManager.redirectToActivity(this, MainActivity.class);
            }
            else
            {
                Log.d("DEVELOPMENT:LoginActivity", "redirectToMainActivity:failure");
            }
        });
        viewModel.getCredentialsAreValid().observe(this, credentialsAreValid ->
        {
            if(!credentialsAreValid)
            {
                // TODO: PopupWindow with error message
                Toast.makeText(this, viewModel.getErrorMessageResId(), Toast.LENGTH_SHORT).show();
            }
        });

        // Set listeners
        binding.loginRedirectRegister.setOnClickListener(unused -> ActivityManager.redirectToActivity(this, RegisterActivity.class));
        binding.loginButton.setOnClickListener(unused -> viewModel.login());
    }

    private ActivityLoginBinding binding;
    private LoginViewModel viewModel;
}
