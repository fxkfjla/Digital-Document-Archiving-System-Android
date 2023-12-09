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

        findViewById(R.id.saveButton).setOnClickListener(view -> {
            String name = ((TextView) findViewById(R.id.editTextName)).getText().toString();
            String description = ((TextView) findViewById(R.id.editTextDescription)).getText().toString();
            String tags = ((TextView) findViewById(R.id.editTextTags)).getText().toString();

            FileModel fileModel = FileManager.getSelectedFileToEdit();
            fileModel.setName(name);
            fileModel.setDescription(description);
            fileModel.setTags(tags);

            FileManager.updateSelectedFile(fileModel);
            ActivityManager.redirectToActivity(this, MainActivity.class);
        });
    }
}
