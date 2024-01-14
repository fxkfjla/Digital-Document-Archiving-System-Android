package com.ddas.androidapp.ui.camera.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.ddas.androidapp.databinding.FragmentEditFileBinding;
import com.ddas.androidapp.ui.camera.CameraViewModel;

import java.util.concurrent.Executor;

public class EditFragment extends Fragment
{
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        // Initialize ViewModel
        viewModel = new ViewModelProvider(requireActivity()).get(CameraViewModel.class);

        // Initialize binding
        binding = FragmentEditFileBinding.inflate(inflater, container, false);
        binding.setViewModel(viewModel);
        binding.setLifecycleOwner(this);

        return binding.getRoot();
    }

    @Override
    public void onDestroyView()
    {
        super.onDestroyView();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);

        initialize(view);
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
        binding.saveButton.setOnClickListener(unused ->
        {
            String name = binding.editTextName.getText().toString();
            String description = binding.editTextDescription.getText().toString();
            String tags = binding.editTextTags.getText().toString();

            viewModel.saveToPdf(name, description, tags);
            context.finish();
        });
    }

    private FragmentEditFileBinding binding;
    private CameraViewModel viewModel;
    private FragmentActivity context;
    private Executor executor;
    private NavController navController;
}
