package com.ddas.androidapp.util;

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

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;

public class FileManager implements FileManagerConstants
{
    public static void openPdf(Context context, String name)
    {
        String filePath = context.getFilesDir() + "/files/" + PDFS_FOLDER + "/" + name;

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
        else
        {

        }
    }

    public static void saveBitmapToPdf(Context context, Bitmap bitmap)
    {
        File path = new File(context.getFilesDir(), PDFS_FOLDER);
        path.mkdirs();

//        File output = new File(path, System.currentTimeMillis() + ".pdf");
        File output = new File(path,"mypdf.pdf");

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

    public static PdfDocument convertImageToPdf(ImageProxy image)
    {
        PdfDocument pdfDocument = new PdfDocument();
        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(image.getWidth(), image.getHeight(), 1).create();
        PdfDocument.Page page = pdfDocument.startPage(pageInfo);

        Bitmap bitmap = convertImageToBitmap(image);

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
