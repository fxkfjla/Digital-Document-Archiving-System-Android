package com.ddas.androidapp.ui.camera.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageCapture;
import androidx.camera.core.ImageCaptureException;
import androidx.camera.core.ImageProxy;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.ddas.androidapp.R;
import com.ddas.androidapp.databinding.FragmentPreviewBinding;
import com.ddas.androidapp.ui.camera.CameraViewModel;
import com.google.common.util.concurrent.ListenableFuture;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;

public class PreviewFragment extends Fragment
{
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        // Initialize ViewModel
        viewModel = new ViewModelProvider(requireActivity()).get(CameraViewModel.class);

        // Initialize binding
        binding = FragmentPreviewBinding.inflate(inflater, container, false);
        binding.setViewModel(viewModel);
        binding.setLifecycleOwner(this);

        return binding.getRoot();
    }

    @Override
    public void onDestroyView()
    {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);

        initialize(view);
        startCamera();
    }

    private void initialize(View view)
    {
        // Initialize context
        context = requireActivity();

        // Initialize executor
        executor = ContextCompat.getMainExecutor(context);

        // Initialize NavController
        navController = Navigation.findNavController(view);

        // Set listeners
        binding.captureButton.setOnClickListener(unused -> viewModel.capturePhoto(executor));

        // Set observers
        viewModel.getImageBitmap().observe(context, imageBitmap -> navController.navigate(R.id.navigation_edit));
    }

    private void startCamera()
    {
        cameraProviderFuture = ProcessCameraProvider.getInstance(context);
        cameraProviderFuture.addListener(this::provideCamera, executor);
    }

    private void provideCamera()
    {
        Preview preview = new Preview.Builder().build();
        preview.setSurfaceProvider(binding.previewView.getSurfaceProvider());

        try
        {
            ProcessCameraProvider cameraProvider = cameraProviderFuture.get();
            cameraProvider.unbindAll();

            cameraProvider.bindToLifecycle(context, viewModel.getCameraSelector(), preview, viewModel.getImageCapture());
        }
        catch (ExecutionException | InterruptedException e)
        {
            // TODO: handle exception
            throw new RuntimeException(e);
        }
    }

    private FragmentPreviewBinding binding;
    private CameraViewModel viewModel;
    private FragmentActivity context;
    private Executor executor;
    private NavController navController;
    private ListenableFuture<ProcessCameraProvider> cameraProviderFuture;
}
