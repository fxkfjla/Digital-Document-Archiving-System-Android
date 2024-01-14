package com.ddas.androidapp.ui.main.fragment.list;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ddas.androidapp.R;
import com.ddas.androidapp.application.App;
import com.ddas.androidapp.application.AppConstants;
import com.ddas.androidapp.network.client.FileManagerApi;
import com.ddas.androidapp.util.ActivityManager;
import com.ddas.androidapp.util.FileManager;
import com.ddas.androidapp.util.PreferencesManager;

import java.io.File;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;

public class FileListAdapter extends RecyclerView.Adapter<FileListAdapter.ViewHolder>
{
    public FileListAdapter(List<FileModel> fileModelList, ShowTopMenu showTopMenu, PreferencesManager preferencesManager)
    {
        this.fileModelList = fileModelList;
        this.showTopMenu = showTopMenu;
        this.preferencesManager = preferencesManager;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_files, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position)
    {
        holder.bind(fileModelList.get(position));
    }

    @Override
    public int getItemCount()
    {
        return fileModelList.size();
    }

    public void deleteSelected()
    {
        FileManager.deleteFromFileList(selectedFiles);
        fileModelList.removeAll(selectedFiles);

        enableSelectMode(false);
    }

    public void shareSelected()
    {
        boolean isUserLoggedIn = preferencesManager.getBoolean(AppConstants.USER_LOGGED_IN);

        if(isUserLoggedIn)
        {
           fileManagerApi.upload(selectedFiles, (response, statusCode) ->
           {
               if(statusCode == HttpURLConnection.HTTP_OK)
               {
                   Log.d("DEVELOPMENT:FileListAdapter", "upload:success:" + response);
                   Toast.makeText(App.getCurrentActivity(), "Pliki przesłane!", Toast.LENGTH_SHORT).show();
               }
               else
               {
                   Log.d("DEVELOPMENT:FileListAdapter", "upload:failure:" + response);
                   Toast.makeText(App.getCurrentActivity(), "Nie udało sie przesłać plików!", Toast.LENGTH_SHORT).show();
               }
            });
        }
        else
        {
            Toast.makeText(App.getCurrentActivity(), "Zaloguj się aby wysłać pliki!", Toast.LENGTH_SHORT).show();
        }

        enableSelectMode(false);
        notifyDataSetChanged();
    }

    public void editSelected()
    {
        if(selectedFiles.size() == 1)
        {
            FileModel selectedFile = selectedFiles.get(0);
            FileManager.setSelectedFileToEdit(selectedFile.getFilePath());
            ActivityManager.openNewActivity(App.getCurrentActivity(), EditFileActivity.class);
        }
        else
        {
            Toast.makeText(App.getCurrentActivity(), "Możesz edytować tylko jeden plik na raz!", Toast.LENGTH_SHORT).show();
        }
    }

    private void toggleFileSelect(FileModel item, ImageView itemSelected)
    {
        if(itemSelected.getVisibility() == View.INVISIBLE)
        {
            selectedFiles.add(item);
            itemSelected.setVisibility(View.VISIBLE);
        }
        else
        {
            selectedFiles.remove(item);
            itemSelected.setVisibility(View.INVISIBLE);
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        public ViewHolder(View view)
        {
            super(view);

            fileName = view.findViewById(R.id.textViewFileName);
            itemSelected = view.findViewById(R.id.itemSelected);
        }

        public void bind(FileModel item)
        {
            itemSelected.setVisibility(View.INVISIBLE);
            fileName.setText(item.getName());
            fileName.setOnClickListener(view -> onClickListener(fileName.getContext(), item));
            fileName.setOnLongClickListener(view -> onLongClickListener(item));
        }

        private void onClickListener(Context context, FileModel item)
        {
            if(isInSelectMode)
            {
                toggleFileSelect(item, itemSelected);

                if(selectedFiles.size() == 0)
                {
                    enableSelectMode(false);
                }
            }
            else
            {
                FileManager.openPdf(context, item.getName());
            }
        }

        private boolean onLongClickListener(FileModel item)
        {
            enableSelectMode(true);

            toggleFileSelect(item, itemSelected);

            if(selectedFiles.size() == 0)
            {
                enableSelectMode(false);
            }

            return true;
        }

        private final TextView fileName;
        private ImageView itemSelected;
    }

    private void enableSelectMode(boolean enableSelectMode)
    {
        if(enableSelectMode)
        {
            isInSelectMode = true;
            showTopMenu.showTopMenu(true);
        }
        else
        {
            isInSelectMode = false;
            showTopMenu.showTopMenu(false);
            selectedFiles.clear();
        }
    }

    private static final FileManagerApi fileManagerApi = new FileManagerApi(App.getCurrentActivity());

    private List<FileModel> fileModelList;

    private List<FileModel> selectedFiles = new ArrayList<>();
    private boolean isInSelectMode = false;
    private ShowTopMenu showTopMenu;
    private final PreferencesManager preferencesManager;
}
