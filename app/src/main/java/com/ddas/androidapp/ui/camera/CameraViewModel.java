package com.ddas.androidapp.ui.camera;

import android.app.Application;
import android.content.ContentValues;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.pdf.PdfDocument;
import android.os.Build;
import android.provider.MediaStore;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageCapture;
import androidx.camera.core.ImageCaptureException;
import androidx.camera.core.ImageProxy;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.concurrent.Executor;


public class CameraViewModel extends AndroidViewModel implements CameraConstants
{
    public CameraViewModel(Application app)
    {
        super(app);
    }

    public void capturePhoto(Executor executor)
    {
        imageCapture.takePicture(executor, new ImageCapture.OnImageCapturedCallback()
        {
            @Override
            public void onCaptureSuccess(@NonNull ImageProxy image)
            {
                Bitmap bitmap = imageProxyToBitmap(image);
                imageBitmap.setValue(bitmap);

                Toast.makeText(getApplication().getBaseContext(), "Photo capture succeeded!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(@NonNull ImageCaptureException exception)
            {
                // Handle capture error
                exception.printStackTrace();
                Toast.makeText(getApplication().getBaseContext(), "Photo capture failed! " + exception.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void savePhoto(Executor executor)
    {
        // TODO: extract path setup and output file options
        String name = new SimpleDateFormat(DATE_FORMAT, Locale.US).format(System.currentTimeMillis());

        ContentValues contentValues = new ContentValues();
        contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, name);
        contentValues.put(MediaStore.MediaColumns.MIME_TYPE, PHOTO_FORMAT);

        if(Build.VERSION.SDK_INT > Build.VERSION_CODES.P)
        {
            contentValues.put(MediaStore.Images.Media.RELATIVE_PATH, RELATIVE_PATH);
        }

        ImageCapture.OutputFileOptions outputFileOptions = new ImageCapture.OutputFileOptions.Builder
        (
                getApplication().getContentResolver(),
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                contentValues
        ).build();

        imageCapture.takePicture(outputFileOptions, executor, new ImageCapture.OnImageSavedCallback()
        {
            @Override
            public void onImageSaved(@NonNull ImageCapture.OutputFileResults outputFileResults)
            {
                Toast.makeText(getApplication().getBaseContext(), "Photo capture succeeded! " + outputFileResults.getSavedUri(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(@NonNull ImageCaptureException exception)
            {
                Toast.makeText(getApplication().getBaseContext(), "Photo capture failed! " + exception.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void savePdf()
    {
        Bitmap bitmap = getBitmapForPreview();

        PdfDocument pdfDocument = new PdfDocument();

        int bitmapWidth = bitmap.getWidth();
        int bitmapHeight = bitmap.getHeight();

        // Convert the pixel dimensions to points (1 inch = 72 points)
        int pointsPerInch = 72;
        int pageWidth = (int) (bitmapWidth / pointsPerInch);
        int pageHeight = (int) (bitmapHeight / pointsPerInch);

        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(pageWidth, pageHeight, 1).create();
        PdfDocument.Page page = pdfDocument.startPage(pageInfo);

        Canvas canvas = page.getCanvas();
        canvas.drawBitmap(bitmap, null, new Rect(0, 0, pageInfo.getPageWidth(), pageInfo.getPageHeight()), null);

        pdfDocument.finishPage(page);
        // Specify the output file for the PDF

        File path = new File(getApplication().getFilesDir(), "myfolder");
        path.mkdirs();

        File output = new File(path, "mypdf.pdf");

        try {
            OutputStream os = new FileOutputStream(output);
            pdfDocument.writeTo(os);
            pdfDocument.close();

            // Now the PDF is saved to the app's private storage.
            Toast.makeText(getApplication(), "PDF saved to " + output.getAbsolutePath(), Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(getApplication(), "Failed to save PDF: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }

//        String pdfFileName = new SimpleDateFormat("yyyyMMdd-HHmmss", Locale.US).format(System.currentTimeMillis()) + ".pdf";
//
//        // Get the app's private storage directory
//        File storageDir = new File(getApplication().getFilesDir(), RELATIVE_PATH);
//
//        // Create the directory if it doesn't exist
//        if (!storageDir.exists()) {
//            storageDir.mkdirs();
//        }
//
//        // Create a file for your PDF within the directory
//        File pdfFile = new File(storageDir, pdfFileName);
//
//        try {
//            OutputStream os = new FileOutputStream(pdfFile);
//            pdfDocument.writeTo(os);
//            pdfDocument.close();
//
//            // Now the PDF is saved to the app's private storage.
//            Toast.makeText(getApplication(), "PDF saved to " + pdfFile.getAbsolutePath(), Toast.LENGTH_SHORT).show();
//        } catch (IOException e) {
//            e.printStackTrace();
//            Toast.makeText(getApplication(), "Failed to save PDF: " + e.getMessage(), Toast.LENGTH_SHORT).show();
//        }

//        File pdfFile = new File(getApplication().getExternalFilesDir(null), "my_pdf.pdf");
//        try {
//            OutputStream os = new FileOutputStream(pdfFile);
//            pdfDocument.writeTo(os);
//            pdfDocument.close();
//
//            // Now the PDF is saved to the specified file.
//            Toast.makeText(getApplication(), "PDF saved to " + pdfFile.getAbsolutePath(), Toast.LENGTH_SHORT).show();
//        } catch (IOException e) {
//            e.printStackTrace();
//            Toast.makeText(getApplication(), "Failed to save PDF: " + e.getMessage(), Toast.LENGTH_SHORT).show();
//        }
//
//        pdfDocument.close();
    }

    private Bitmap imageProxyToBitmap(ImageProxy image)
    {
        ByteBuffer buffer = image.getPlanes()[0].getBuffer();
        byte[] bytes = new byte[buffer.capacity()];
        buffer.get(bytes);
        Bitmap bitmapImage = BitmapFactory.decodeByteArray(bytes, 0, bytes.length, null);
        image.close();

        return rotateBitmap(bitmapImage, 90);
    }

    private Bitmap rotateBitmap(Bitmap bitmap, int degrees)
    {
        Matrix matrix = new Matrix();
        matrix.postRotate(degrees);

        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    }

    private void convertToPdf()
    {

    }

    // Getters, setters
    public ImageCapture getImageCapture() { return imageCapture; }
    public CameraSelector getCameraSelector() { return new CameraSelector.Builder().requireLensFacing(lensFacing).build();}
    public LiveData<Bitmap> getImageBitmap() { return imageBitmap; }
    public Bitmap getBitmapForPreview() { return imageBitmap.getValue(); }

    private final MutableLiveData<Bitmap> imageBitmap = new MutableLiveData<>();

    private final ImageCapture imageCapture = new ImageCapture.Builder().build();
    private int lensFacing = CameraSelector.LENS_FACING_BACK;
}
