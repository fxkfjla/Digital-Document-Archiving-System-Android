package com.ddas.androidapp.ui.main;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import com.ddas.androidapp.R;
import com.ddas.androidapp.databinding.ActivityMainBinding;
import com.ddas.androidapp.util.ActivityManager;

public class MainActivity extends AppCompatActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        binding.setLifecycleOwner(this);

        initialize();
        viewModel.checkAuthentication();
    }

    private void initialize()
    {
        // Initialize view model
        viewModel = new ViewModelProvider(this).get(MainViewModel.class);
        binding.setViewModel(viewModel);

        // Set observers
        viewModel.getUserIsAuthenticated().observe(this, userIsAuthenticated ->
        {
            if(userIsAuthenticated)
            {
                ActivityManager.redirectToActivity(this, MainActivity.class);
            }
        });

        // Set listeners
    }

    private ActivityMainBinding binding;
    private MainViewModel viewModel;
}