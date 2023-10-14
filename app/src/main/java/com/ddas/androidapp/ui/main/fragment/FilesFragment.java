package com.ddas.androidapp.ui.main.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.ddas.androidapp.databinding.FragmentFilesBinding;
import com.ddas.androidapp.ui.camera.CameraActivity;
import com.ddas.androidapp.ui.main.MainViewModel;
import com.ddas.androidapp.util.ActivityManager;

public class FilesFragment extends Fragment
{
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        binding = FragmentFilesBinding.inflate(inflater, container, false);

        viewModel = new ViewModelProvider(this).get(MainViewModel.class);

        binding.setViewModel(viewModel);
        binding.setLifecycleOwner(this);

        initialize();

        return binding.getRoot();
    }

    private void initialize()
    {
        binding.scanButton.setOnClickListener(unused -> ActivityManager.openNewActivity(getActivity(), CameraActivity.class));
    }

    private FragmentFilesBinding binding;
    private MainViewModel viewModel;

}
