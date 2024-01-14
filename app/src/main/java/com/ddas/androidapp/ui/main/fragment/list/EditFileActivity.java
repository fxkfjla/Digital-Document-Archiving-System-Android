package com.ddas.androidapp.ui.main.fragment.list;

import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.ddas.androidapp.R;
import com.ddas.androidapp.ui.main.MainActivity;
import com.ddas.androidapp.util.ActivityManager;
import com.ddas.androidapp.util.FileManager;

public class EditFileActivity extends AppCompatActivity
{
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_edit_file);

        FileModel fileModel = FileManager.getSelectedFileToEdit();
        TextView editName = findViewById(R.id.editTextName);
        TextView editDescription = findViewById(R.id.editTextDescription);
        TextView editTags = findViewById(R.id.editTextTags);

        editName.setText(fileModel.getName());
        editDescription.setText(fileModel.getDescription());
        editTags.setText(fileModel.getTags());

        findViewById(R.id.saveButton).setOnClickListener(view -> {
            fileModel.setName(editName.getText().toString());
            fileModel.setDescription(editDescription.getText().toString());
            fileModel.setTags(editTags.getText().toString());

            FileManager.updateSelectedFile(fileModel);
            finish();
        });
    }
}
