package com.ddas.androidapp.ui.login;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

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

        // Initialize ViewModel
        viewModel = new ViewModelProvider(this).get(LoginViewModel.class);

        // Initialize binding
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        binding.setViewModel(viewModel);
        binding.setLifecycleOwner(this);
        setContentView(binding.getRoot());

        initialize();
    }

    private void initialize()
    {
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
        binding.loginRedirectRegister.setOnClickListener(unused -> ActivityManager.openNewActivity(this, RegisterActivity.class));
        binding.loginButton.setOnClickListener(unused -> viewModel.login());
    }

    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        this.finish();
        ActivityManager.redirectToActivity(this, MainActivity.class);
    }

    private ActivityLoginBinding binding;
    private LoginViewModel viewModel;
}
