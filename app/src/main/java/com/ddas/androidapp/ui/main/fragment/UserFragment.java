package com.ddas.androidapp.ui.main.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.ddas.androidapp.databinding.FragmentUserBinding;
import com.ddas.androidapp.ui.login.LoginActivity;
import com.ddas.androidapp.ui.main.MainViewModel;
import com.ddas.androidapp.util.ActivityManager;

public class UserFragment extends Fragment
{
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        binding = FragmentUserBinding.inflate(inflater, container, false);

        viewModel = new ViewModelProvider(this).get(MainViewModel.class);

        binding.setViewModel(viewModel);
        binding.setLifecycleOwner(this);

        initialize();

        return binding.getRoot();
    }

    private void initialize()
    {
        // Set listeners
        binding.loginButton.setOnClickListener(unused -> ActivityManager.openNewActivity(getActivity(), LoginActivity.class));
    }


    @Override
    public void onDestroyView()
    {
        super.onDestroyView();
        binding = null;
    }

    private FragmentUserBinding binding;
    private MainViewModel viewModel;
}
