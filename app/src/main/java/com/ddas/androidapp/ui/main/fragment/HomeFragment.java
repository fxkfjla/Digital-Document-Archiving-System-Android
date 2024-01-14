package com.ddas.androidapp.ui.main.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.ddas.androidapp.databinding.FragmentHomeBinding;
import com.ddas.androidapp.ui.main.MainViewModel;

public class HomeFragment extends Fragment
{
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        // Initialize ViewModel
        viewModel = new ViewModelProvider(requireActivity()).get(MainViewModel.class);

        // Initialize binding
        binding = FragmentHomeBinding.inflate(inflater, container, false);
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

    private FragmentHomeBinding binding;
    private MainViewModel viewModel;
}
