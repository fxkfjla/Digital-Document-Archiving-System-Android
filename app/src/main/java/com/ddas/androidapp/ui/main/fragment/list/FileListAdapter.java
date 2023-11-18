package com.ddas.androidapp.ui.main.fragment.list;

import android.content.Context;
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
import com.ddas.androidapp.network.client.FileManagerApi;
import com.ddas.androidapp.util.FileManager;

import java.io.File;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;

public class FileListAdapter extends RecyclerView.Adapter<FileListAdapter.ViewHolder>
{
    public FileListAdapter(List<FileModel> fileModelList, ShowTopMenu showTopMenu)
    {
        this.fileModelList = fileModelList;
        this.showTopMenu = showTopMenu;
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

    public List<FileModel> deleteSelected()
    {
        FileManager.deleteFromFileList(selectedFiles);
        fileModelList.removeAll(selectedFiles);

        enableSelectMode(false);

        return fileModelList;
    }

    public void shareSelected()
    {
        List<File> files = FileManager.getSelectedFiles(selectedFiles);
        fileManagerApi.upload(files, (response, statusCode) ->
        {
            if(statusCode == HttpURLConnection.HTTP_OK)
            {
                Log.d("DEVELOPMENT:FileListAdapter", "upload:success:" + response);
                Toast.makeText(App.getCurrentActivity(), "Files uploaded!", Toast.LENGTH_SHORT).show();
            }
            else
            {
                Log.d("DEVELOPMENT:FileListAdapter", "upload:failure:" + response);
                Toast.makeText(App.getCurrentActivity(), "Failed to upload files!", Toast.LENGTH_SHORT).show();
            }
        });
        enableSelectMode(false);
        notifyDataSetChanged();
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
}
