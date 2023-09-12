package com.ddas.androidapp.util;

import android.app.Activity;
import android.content.Intent;

public class ActivityManager
{
    public static void redirectToActivity(Activity currentActivity, Class<? extends Activity> targetActivity)
    {
        redirectToActivity(currentActivity, targetActivity, Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
    }

    public static void redirectToActivity(Activity currentActivity, Class<? extends Activity> targetActivity, int flags)
    {
        Intent intent = new Intent(currentActivity, targetActivity);
        intent.setFlags(flags);
        currentActivity.startActivity(intent);
        currentActivity.finish();
    }
}