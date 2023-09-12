package com.ddas.androidapp.ui.main;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.ddas.androidapp.R;
import com.ddas.androidapp.ui.login.LoginActivity;
import com.ddas.androidapp.util.ActivityManager;

public class MainActivity extends AppCompatActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        checkAuthentication();
    }

    private void checkAuthentication()
    {
        // TODO: Authenticate user and save token to shared preferences
        ActivityManager.redirectToActivity(this, LoginActivity.class);
    }
}