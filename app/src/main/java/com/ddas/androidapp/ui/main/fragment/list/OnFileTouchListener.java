package com.ddas.androidapp.ui.main.fragment.list;

import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ddas.androidapp.util.FileManager;

import java.util.List;

public class OnFileTouchListener implements RecyclerView.OnItemTouchListener
{
//    public OnFileTouchListener(List<FileModel> fileList)
//    {
//        this.fileList = fileList;
//    }

    @Override
    public boolean onInterceptTouchEvent(@NonNull RecyclerView rv, @NonNull MotionEvent e)
    {
        View childView = rv.findChildViewUnder(e.getX(), e.getY());

        if (childView != null)
        {
            int position = rv.getChildAdapterPosition(childView);

            // For some reason if list is passed via constructor, the list here is empty
            List<FileModel> fileList = FileManager.getFileList(rv.getContext());
            FileModel item = fileList.get(position);
            FileManager.openPdf(rv.getContext(), item.getName());

            return true;
        }

        return false;
    }

    @Override
    public void onTouchEvent(@NonNull RecyclerView rv, @NonNull MotionEvent e)
    {

    }

    @Override
    public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept)
    {

    }

//    private final List<FileModel> fileList;
}
