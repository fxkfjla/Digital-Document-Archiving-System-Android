package com.ddas.androidapp.ui.main.fragment.list;

public class FileModel
{
    public FileModel(String name, String filePath, String thumbNailPath)
    {
        this.name = name;
        this.filePath = filePath;
        this.thumbNailPath = thumbNailPath;
    }

    public String getName()
    {
        return name;
    }

    public String getFilePath()
    {
        return filePath;
    }

    public String getThumbNailPath()
    {
        return thumbNailPath;
    }

    private String name;
    private String filePath;
    private String thumbNailPath;
}
