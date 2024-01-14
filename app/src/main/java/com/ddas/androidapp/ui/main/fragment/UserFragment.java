package com.ddas.androidapp.ui.main.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.ddas.androidapp.application.App;
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
        // Initialize ViewModel
        viewModel = new ViewModelProvider(requireActivity()).get(MainViewModel.class);

        // Initialize binding
        binding = FragmentUserBinding.inflate(inflater, container, false);
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

    private void initialize()
    {
        // Set observers
        viewModel.getUserIsAuthenticated().observe(requireActivity(), userIsAuthenticated ->
        {
            if(userIsAuthenticated)
            {
                binding.logoutButton.setVisibility(View.VISIBLE);
                binding.logoImageView.setVisibility(View.VISIBLE);
                binding.userEmailTextView.setText(viewModel.getUserEmail());
                binding.userEmailTextView.setVisibility(View.VISIBLE);
                binding.logoutButton.setOnClickListener(unused ->viewModel.logout());
            }
            else
            {
                ActivityManager.redirectToActivity(getActivity(), LoginActivity.class);
            }
        });
    }


    private FragmentUserBinding binding;
    private MainViewModel viewModel;
}
