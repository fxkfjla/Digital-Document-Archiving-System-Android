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
import androidx.camera.core.Camera;
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

        binding = ActivityCameraBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        viewModel = new ViewModelProvider(this).get(CameraViewModel.class);

        binding.setViewModel(viewModel);
        binding.setLifecycleOwner(this);

        if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)
        {
            requestPermissions(new String[]{ Manifest.permission.CAMERA }, CAMERA_REQUEST_CODE);
        }

        startCamera();
        // TODO: check privileges before accessing the camera

        binding.captureButton.setOnClickListener(unused -> takePhoto());
    }

    private void startCamera()
    {
        cameraProviderFuture = ProcessCameraProvider.getInstance(this);

        cameraProviderFuture.addListener(() ->
        {
            Preview preview = new Preview.Builder().build();
            preview.setSurfaceProvider(binding.previewView.getSurfaceProvider());

            imageCapture = new ImageCapture.Builder().build();

            CameraSelector cameraSelector = new CameraSelector.Builder().requireLensFacing(CameraSelector.LENS_FACING_BACK).build();

            try
            {
                ProcessCameraProvider cameraProvider = cameraProviderFuture.get();
                cameraProvider.unbindAll();

                Camera camera = cameraProvider.bindToLifecycle(this, cameraSelector, preview, imageCapture);
            }
            catch (ExecutionException | InterruptedException e)
            {
                throw new RuntimeException(e);
            }
        }, ContextCompat.getMainExecutor(this));
    }

    private void takePhoto()
    {
        String name = new SimpleDateFormat("yyyyMMdd-HHmmss", Locale.US).format(System.currentTimeMillis());

        ContentValues contentValues = new ContentValues();
        contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, name);
        contentValues.put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg");

        if(Build.VERSION.SDK_INT > Build.VERSION_CODES.P)
        {
            contentValues.put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/CameraX-Image");
        }

        ImageCapture.OutputFileOptions outputFileOptions = new ImageCapture.OutputFileOptions.Builder
        (
            getContentResolver(),
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            contentValues
        ).build();

        imageCapture.takePicture(outputFileOptions, ContextCompat.getMainExecutor(this), new ImageCapture.OnImageSavedCallback() {
            @Override
            public void onImageSaved(@NonNull ImageCapture.OutputFileResults outputFileResults) {
                Toast.makeText(getBaseContext(), "Photo capture succeeded! " + outputFileResults.getSavedUri(), Toast.LENGTH_SHORT).show();
                Log.d("CameraX", "Photo saved: " + outputFileResults.getSavedUri());
            }

            @Override
            public void onError(@NonNull ImageCaptureException exception) {
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