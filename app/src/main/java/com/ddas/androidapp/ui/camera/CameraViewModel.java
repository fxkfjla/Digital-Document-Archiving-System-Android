package com.ddas.androidapp.ui.camera;

import android.app.Application;
import android.content.ContentValues;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Build;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageCapture;
import androidx.camera.core.ImageCaptureException;
import androidx.camera.core.ImageProxy;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

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

    // Getters, setters
    public ImageCapture getImageCapture() { return imageCapture; }
    public CameraSelector getCameraSelector() { return new CameraSelector.Builder().requireLensFacing(lensFacing).build();}
    public LiveData<Bitmap> getImageBitmap() { return imageBitmap; }
    public Bitmap getBitmapForPreview() { return imageBitmap.getValue(); }

    private final MutableLiveData<Bitmap> imageBitmap = new MutableLiveData<>();

    private final ImageCapture imageCapture = new ImageCapture.Builder().build();
    private int lensFacing = CameraSelector.LENS_FACING_BACK;
}
