package com.ddas.androidapp.ui.main.fragment.list;

public class FileModel
{
    public FileModel(String name, String thumbNailPath)
    {
        this.name = name;
        this.thumbNailPath = thumbNailPath;
    }

    public String getName()
    {
        return name;
    }

    public String getThumbNailPath()
    {
        return thumbNailPath;
    }

    private String name;
    private String thumbNailPath;
}
