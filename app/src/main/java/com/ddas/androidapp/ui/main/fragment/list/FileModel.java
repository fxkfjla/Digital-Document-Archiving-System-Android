package com.ddas.androidapp.ui.main.fragment.list;

import androidx.annotation.Nullable;

public class FileModel
{
    public FileModel(String name, String filePath, String description, String tags)
    {
        this.name = name;
        this.filePath = filePath;
        this.description = description;
        this.tags = tags;
    }

    public String getName()
    {
        return name;
    }

    public String getFilePath()
    {
        return filePath;
    }

    public String getDescription() { return description; }

    public String getTags() { return tags; }

    public void setName(String name) { this.name = name; }

    public void setDescription(String description) { this.description = description; }

    public void setTags(String tags) { this.tags = tags; }

    @Override
    public boolean equals(@Nullable Object obj)
    {
        FileModel fileModel = (FileModel) obj;

        return filePath.equals(fileModel.getFilePath());
    }

    private String name;
    private String filePath;
    private String description;
    private String tags;
}
