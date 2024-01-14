package com.ddas.androidapp.ui.camera;

import android.app.Application;
import android.graphics.Bitmap;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageCapture;
import androidx.camera.core.ImageCaptureException;
import androidx.camera.core.ImageProxy;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.ddas.androidapp.util.FileManager;

import java.util.concurrent.Executor;

public class CameraViewModel extends AndroidViewModel implements CameraConstants
{
    public CameraViewModel(Application app)
    {
        super(app);
    }

    public void capturePicture(Executor executor)
    {
        imageCapture.takePicture(executor, new ImageCapture.OnImageCapturedCallback()
        {
            @Override
            public void onCaptureSuccess(@NonNull ImageProxy imageProxy)
            {
                imageBitmap.setValue(FileManager.convertImageToBitmap(imageProxy));
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

    public void saveToPdf(String name, String description, String tags)
    {
        FileManager.saveBitmapToPdf(getApplication(), imageBitmap.getValue(), name, description, tags);
    }

    // Getters, setters
    public ImageCapture getImageCapture() { return imageCapture; }
    public CameraSelector getCameraSelector() { return new CameraSelector.Builder().requireLensFacing(lensFacing).build();}
    public Bitmap getBitmapForPreview() { return imageBitmap.getValue(); }
    public LiveData<Bitmap> getImageBitmap() { return imageBitmap; }

    private final MutableLiveData<Bitmap> imageBitmap = new MutableLiveData<>();

    private final ImageCapture imageCapture = new ImageCapture.Builder().build();
    private int lensFacing = CameraSelector.LENS_FACING_BACK;
}