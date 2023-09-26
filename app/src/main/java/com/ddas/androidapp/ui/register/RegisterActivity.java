package com.ddas.androidapp.ui.register;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.ddas.androidapp.R;
import com.ddas.androidapp.databinding.ActivityRegisterBinding;
import com.ddas.androidapp.ui.login.LoginActivity;
import com.ddas.androidapp.util.ActivityManager;

public class RegisterActivity extends AppCompatActivity
{
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_register);
        binding.setLifecycleOwner(this);

        initialize();
    }

    private void initialize()
    {
        // Initialize view model
        viewModel = new ViewModelProvider(this).get(RegisterViewModel.class);
        binding.setViewModel(viewModel);

        // Set observers
        viewModel.getRegistrationIsSuccessful().observe(this, registrationIsSuccessful ->
        {
            if(registrationIsSuccessful)
            {
                Log.d("DEVELOPMENT:RegisterActivity", "redirectToLoginActivity:success");
                ActivityManager.redirectToActivity(this, LoginActivity.class);
            }
            else
            {
                Log.d("DEVELOPMENT:RegisterActivity", "redirectToLoginActivity:failure");
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
        binding.registerRedirectLogin.setOnClickListener(unused -> ActivityManager.redirectToActivity(this, LoginActivity.class));
        binding.registerButton.setOnClickListener(unused -> viewModel.register());
    }

    private ActivityRegisterBinding binding;
    private RegisterViewModel viewModel;
}