package com.ddas.androidapp.application;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class AppActivityLifecycle implements Application.ActivityLifecycleCallbacks
{
    public Activity getCurrentActivity()
    {
        return currentActivity;
    }

    @Override
    public void onActivityCreated(@NonNull Activity activity, @Nullable Bundle bundle)
    {

    }

    @Override
    public void onActivityStarted(@NonNull Activity activity)
    {

    }

    @Override
    public void onActivityResumed(@NonNull Activity activity)
    {
        currentActivity = activity;
    }

    @Override
    public void onActivityPaused(@NonNull Activity activity)
    {

    }

    @Override
    public void onActivityStopped(@NonNull Activity activity)
    {

    }

    @Override
    public void onActivitySaveInstanceState(@NonNull Activity activity, @NonNull Bundle bundle)
    {

    }

    @Override
    public void onActivityDestroyed(@NonNull Activity activity)
    {

    }

    private Activity currentActivity;
}
