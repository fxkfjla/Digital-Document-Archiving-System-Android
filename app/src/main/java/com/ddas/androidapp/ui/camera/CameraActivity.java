package com.ddas.androidapp.ui.camera;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.ddas.androidapp.R;
import com.ddas.androidapp.databinding.ActivityCameraBinding;

public class CameraActivity extends AppCompatActivity implements CameraConstants
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        initialize();
        lookForPermissions();
    }

    private void initialize()
    {
        // Initialize ViewModel
        viewModel = new ViewModelProvider(this).get(CameraViewModel.class);

        // Initialize binding
        binding = ActivityCameraBinding.inflate(getLayoutInflater());
        binding.setViewModel(viewModel);
        binding.setLifecycleOwner(this);
        setContentView(binding.getRoot());

        // Initialize NavController
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
        navController = navHostFragment.getNavController();
    }

    private void lookForPermissions()
    {
        if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)
        {
            requestPermissions(REQUIRED_PERMISSIONS, CAMERA_REQUEST_CODE);
        }
    }

    private ActivityCameraBinding binding;
    private CameraViewModel viewModel;
    private NavController navController;
}