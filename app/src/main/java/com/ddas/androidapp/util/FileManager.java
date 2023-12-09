package com.ddas.androidapp.util;

import static com.ddas.androidapp.util.FileManagerConstants.PDFS_FOLDER;
import static com.ddas.androidapp.util.FileManagerConstants.PDF_FORMAT;
import static com.ddas.androidapp.util.FileManagerConstants.PROVIDER_AUTHORITY;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.widget.Toast;

import androidx.camera.core.ImageProxy;
import androidx.core.content.FileProvider;

import com.ddas.androidapp.ui.main.fragment.list.FileModel;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

public class FileManager
{
    private static final List<FileModel> fileList = new ArrayList<>();

    private static String selectedFileToEditPath;

    public static void setSelectedFileToEdit(String selectedFileToEditPath)
    {
        FileManager.selectedFileToEditPath = selectedFileToEditPath;
    }

    public static FileModel getSelectedFileToEdit()
    {
        return fileList.stream()
        .filter(fileModel -> fileModel.getFilePath().equals(selectedFileToEditPath))
        .findFirst()
        .get();
    }

    public static void updateSelectedFile(FileModel file)
    {
//        FileModel fileModel = fileList.stream().filter(_fileModel -> _fileModel.equals(file)).findFirst().get();
        fileList.remove(file);
        fileList.add(file);

//        fileList.stream()
//        .filter(fileModel -> fileModel.getFilePath().equals(selectedFileToEditPath))
//        .findFirst()
//        .get();
//        fileList.stream()
//        .filter(fileModel -> fileModel.getFilePath().equals(file.getFilePath()))
//        .findFirst().(fileModel -> {
//            fileModel.setName(file.getName());
//                fileModel.setName(file.getName());
//                fileModel.setName(file.getName());
//                });
    }

    public static void deleteFromFileList(List<FileModel> selectedFiles)
    {
        fileList.removeAll(selectedFiles);
        selectedFiles.forEach(file -> deletefile(file.getFilePath()));
    }

    public static boolean deletefile(String filePath)
    {
        File file = new File(filePath);

        return file.delete();
    }

    public static List<FileModel> getFileList()
    {
        return fileList;
    }

    public static void openPdf(Context context, String name)
    {
        String filePath = context.getFilesDir() + "/" + PDFS_FOLDER + "/" + name + ".pdf";
        File file = new File(filePath);

        if (file.exists())
        {
            Uri uri = FileProvider.getUriForFile(context, PROVIDER_AUTHORITY, file);

            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setDataAndType(uri, PDF_FORMAT);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

            try
            {
                context.startActivity(intent);
            }
            catch (ActivityNotFoundException e)
            {

            }
        }
    }

    public static void saveBitmapToPdf(Context context, Bitmap bitmap, String name, String description, String tags)
    {
        String directoryPath = context.getFilesDir() + "/" + PDFS_FOLDER;
        String filePath = directoryPath + "/" + name + ".pdf";

        new File(directoryPath).mkdirs();
        File output = new File(filePath);

        fileList.add(new FileModel(name, filePath, description, tags));

        try
        {
            OutputStream os = new FileOutputStream(output);

            PdfDocument pdf = convertBitmapToPdf(bitmap);
            pdf.writeTo(os);
            pdf.close();

            Toast.makeText(context, "PDF saved to " + output.getAbsolutePath(), Toast.LENGTH_SHORT).show();
        }
        catch (IOException e)
        {
            e.printStackTrace();
            Toast.makeText(context, "Failed to save PDF: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    public static PdfDocument convertBitmapToPdf(Bitmap bitmap)
    {
        PdfDocument pdfDocument = new PdfDocument();
        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(bitmap.getWidth(), bitmap.getHeight(), 1).create();
        PdfDocument.Page page = pdfDocument.startPage(pageInfo);

        Canvas canvas = page.getCanvas();
        canvas.drawBitmap(bitmap, null, new Rect(0, 0, pageInfo.getPageWidth(), pageInfo.getPageHeight()), null);

        pdfDocument.finishPage(page);

        return pdfDocument;

    }

    public static Bitmap convertImageToBitmap(ImageProxy image)
    {
        ByteBuffer buffer = image.getPlanes()[0].getBuffer();
        byte[] bytes = new byte[buffer.capacity()];
        buffer.get(bytes);
        Bitmap bitmapImage = BitmapFactory.decodeByteArray(bytes, 0, bytes.length, null);
        image.close();

        return rotateBitmap(bitmapImage, 90);
    }

    public static Bitmap rotateBitmap(Bitmap bitmap, int degrees)
    {
        Matrix matrix = new Matrix();
        matrix.postRotate(degrees);

        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    }
}
