package com.ddas.androidapp.ui.main.fragment.list;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ddas.androidapp.R;

import java.util.List;

public class FileListAdapter extends RecyclerView.Adapter<FileListAdapter.ViewHolder>
{
    public FileListAdapter(List<FileModel> fileModelList)
    {
        this.fileModelList = fileModelList;
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
        holder.getFileName().setText(fileModelList.get(position).getName());
    }
    @Override
    public int getItemCount()
    {
        return fileModelList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder
    {
        public ViewHolder(View view)
        {
            super(view);

            fileName = view.findViewById(R.id.textViewFileName);
        }

        public TextView getFileName()
        {
            return fileName;
        }

        private final TextView fileName;
    }

    private List<FileModel> fileModelList;
}
