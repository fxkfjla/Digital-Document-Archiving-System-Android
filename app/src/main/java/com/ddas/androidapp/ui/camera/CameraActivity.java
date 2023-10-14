package com.ddas.androidapp.ui.camera;

import android.Manifest;
import android.content.ContentValues;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageCapture;
import androidx.camera.core.ImageCaptureException;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;

import com.ddas.androidapp.databinding.ActivityCameraBinding;
import com.google.common.util.concurrent.ListenableFuture;

import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.concurrent.ExecutionException;

public class CameraActivity extends AppCompatActivity implements CameraConstants
{
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        initialize();
        startCamera();
    }

    private void initialize()
    {
        // Initialize view model
        viewModel = new ViewModelProvider(this).get(CameraViewModel.class);

        // Initialize binding
        binding = ActivityCameraBinding.inflate(getLayoutInflater());
        binding.setViewModel(viewModel);
        binding.setLifecycleOwner(this);
        setContentView(binding.getRoot());

        // Set listeners
        binding.captureButton.setOnClickListener(unused -> takePhoto());
    }

    private void startCamera()
    {
        if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)
        {
            requestPermissions(REQUIRED_PERMISSIONS, CAMERA_REQUEST_CODE);
        }

        cameraProviderFuture = ProcessCameraProvider.getInstance(this);
        cameraProviderFuture.addListener(this::provideCamera, ContextCompat.getMainExecutor(this));
    }

    private void provideCamera()
    {
        Preview preview = new Preview.Builder().build();
        preview.setSurfaceProvider(binding.previewView.getSurfaceProvider());

        imageCapture = new ImageCapture.Builder().build();

        CameraSelector cameraSelector = new CameraSelector.Builder().requireLensFacing(CameraSelector.LENS_FACING_BACK).build();

        try
        {
            ProcessCameraProvider cameraProvider = cameraProviderFuture.get();
            cameraProvider.unbindAll();

            cameraProvider.bindToLifecycle(this, cameraSelector, preview, imageCapture);
        }
        catch (ExecutionException | InterruptedException e)
        {
            // TODO: handle exception
            throw new RuntimeException(e);
        }
    }

    private void takePhoto()
    {
        String name = new SimpleDateFormat(DATE_FORMAT, Locale.US).format(System.currentTimeMillis());

        ContentValues contentValues = new ContentValues();
        contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, name);
        contentValues.put(MediaStore.MediaColumns.MIME_TYPE, PHOTO_FORMAT);

        if(Build.VERSION.SDK_INT > Build.VERSION_CODES.P)
        {
            contentValues.put(MediaStore.Images.Media.RELATIVE_PATH, RELATIVE_PATH);
        }

        ImageCapture.OutputFileOptions outputFileOptions = new ImageCapture.OutputFileOptions.Builder
        (
            getContentResolver(),
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            contentValues
        ).build();

        imageCapture.takePicture(outputFileOptions, ContextCompat.getMainExecutor(this), new ImageCapture.OnImageSavedCallback()
        {
            @Override
            public void onImageSaved(@NonNull ImageCapture.OutputFileResults outputFileResults)
            {
                Toast.makeText(getBaseContext(), "Photo capture succeeded! " + outputFileResults.getSavedUri(), Toast.LENGTH_SHORT).show();
                Log.d("CameraX", "Photo saved: " + outputFileResults.getSavedUri());
            }

            @Override
            public void onError(@NonNull ImageCaptureException exception)
            {
                Toast.makeText(getBaseContext(), "Photo capture failed! " + exception.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("CameraX", "Error capturing photo: " + exception.getMessage());
            }
        });
    }

    private ActivityCameraBinding binding;
    private CameraViewModel viewModel;
    private ListenableFuture<ProcessCameraProvider> cameraProviderFuture;
    private ImageCapture imageCapture;
}