package com.ddas.androidapp.ui.main.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.MenuProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.ddas.androidapp.R;
import com.ddas.androidapp.databinding.FragmentFilesBinding;
import com.ddas.androidapp.ui.camera.CameraActivity;
import com.ddas.androidapp.ui.main.MainViewModel;
import com.ddas.androidapp.ui.main.fragment.list.FileListAdapter;
import com.ddas.androidapp.ui.main.fragment.list.FileModel;
import com.ddas.androidapp.util.ActivityManager;
import com.ddas.androidapp.util.FileManager;

import java.util.List;
import java.util.zip.Inflater;

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
        requireActivity().removeMenuProvider(menuProvider);
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

        showTopMenu(false);
        loadFileList(FileManager.getFileList(requireActivity()));
    }

    private void initialize()
    {
        // Initialize menu
        requireActivity().addMenuProvider(getMenuProvider());

        // Set listeners
        binding.scanButton.setOnClickListener(unused -> ActivityManager.openNewActivity(getActivity(), CameraActivity.class));
    }

    private void loadFileList(List<FileModel> fileList)
    {
        this.fileList = fileList;

        adapter = new FileListAdapter(fileList, this::showTopMenu);
        binding.recyclerView.setAdapter(adapter);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    private MenuProvider getMenuProvider()
    {
        return menuProvider = new MenuProvider()
        {
            @Override
            public void onCreateMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater)
            {
                topMenu = menu;
                menuInflater.inflate(R.menu.top_menu, topMenu);
                showTopMenu(false);
            }

            @Override
            public boolean onMenuItemSelected(@NonNull MenuItem item)
            {
                if(item.getItemId() == R.id.delete_selected)
                {
                    loadFileList(adapter.deleteSelected());

                }
                else if(item.getItemId() == R.id.share_selected)
                {
                    adapter.shareSelected();
                }

                return false;
            }
        };
    }

    private void showTopMenu(boolean show)
    {
        topMenu.findItem(R.id.delete_selected).setVisible(show);
        topMenu.findItem(R.id.share_selected).setVisible(show);
    }

    private FragmentFilesBinding binding;
    private MainViewModel viewModel;

    private List<FileModel> fileList;
    private FileListAdapter adapter;
    private MenuProvider menuProvider;
    private Menu topMenu;
}
