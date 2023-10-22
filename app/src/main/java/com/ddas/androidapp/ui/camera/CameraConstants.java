package com.ddas.androidapp.ui.camera;

import android.Manifest;
import android.os.Build;

public interface CameraConstants
{
    int CAMERA_REQUEST_CODE = 100;
    String[] REQUIRED_PERMISSIONS = Build.VERSION.SDK_INT > Build.VERSION_CODES.P ?
        new String[] { Manifest.permission.CAMERA } :
        new String[] { Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE };
}
