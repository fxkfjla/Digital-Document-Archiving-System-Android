package com.ddas.androidapp.ui.main.fragment;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.ddas.androidapp.databinding.FragmentFilesBinding;
import com.ddas.androidapp.ui.camera.CameraActivity;
import com.ddas.androidapp.ui.main.MainViewModel;
import com.ddas.androidapp.util.ActivityManager;
import com.ddas.androidapp.util.FileManager;

import java.io.File;

public class FilesFragment extends Fragment
{
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        // Initialize ViewModel
        viewModel = new ViewModelProvider(requireActivity()).get(MainViewModel.class);

        // Initialize binding
        binding = FragmentFilesBinding.inflate(inflater, container, false);
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

        initialize();
//        FileManager.openPdf(requireActivity(), "mypdf");
        getPdf();
    }

    private void initialize()
    {
        // Set listeners
        binding.scanButton.setOnClickListener(unused -> ActivityManager.openNewActivity(getActivity(), CameraActivity.class));
    }

    private void getPdf()
    {
        String filePath = "/data/user/0/com.ddas.androidapp/files/Scans/mypdf.pdf";

        File file = new File(filePath);

        if (file.exists())
        {
            Uri uri = FileProvider.getUriForFile(getActivity(), "com.ddas.androidapp.provider", file);

            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setDataAndType(uri, "application/pdf");
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

            try
            {
                startActivity(intent);
            }
            catch (ActivityNotFoundException e)
            {

            }
        }
        else
        {

        }
    }

    private FragmentFilesBinding binding;
    private MainViewModel viewModel;
}
