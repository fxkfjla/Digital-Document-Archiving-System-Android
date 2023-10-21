package com.ddas.androidapp.ui.main.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ddas.androidapp.databinding.FragmentFilesBinding;
import com.ddas.androidapp.ui.camera.CameraActivity;
import com.ddas.androidapp.ui.main.MainViewModel;
import com.ddas.androidapp.ui.main.fragment.list.FileListAdapter;
import com.ddas.androidapp.ui.main.fragment.list.FileModel;
import com.ddas.androidapp.ui.main.fragment.list.OnFileTouchListener;
import com.ddas.androidapp.util.ActivityManager;
import com.ddas.androidapp.util.FileManager;

import java.util.ArrayList;
import java.util.List;

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
    }

    @Override
    public void onResume()
    {
        super.onResume();

        loadFileList();
    }

    private void initialize()
    {
        // Set listeners
        binding.scanButton.setOnClickListener(unused -> ActivityManager.openNewActivity(getActivity(), CameraActivity.class));
    }

    private void loadFileList()
    {
        fileList = FileManager.getFileList(requireActivity());

        FileListAdapter adapter = new FileListAdapter(fileList);
        binding.recyclerView.setAdapter(adapter);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.recyclerView.addOnItemTouchListener(new OnFileTouchListener());
    }

    private FragmentFilesBinding binding;
    private MainViewModel viewModel;
    private List<FileModel> fileList;
}
