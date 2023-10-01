package com.ddas.androidapp.application;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.res.Configuration;
import android.util.Log;

import java.util.Locale;

public class App extends Application
{
    @Override
    public void onCreate()
    {
        super.onCreate();

        instance = this;
        registerActivityLifecycleCallbacks(lifecycle);
    }

    public static Activity getCurrentActivity()
    {
        return instance.lifecycle.getCurrentActivity();
    }

    @Override
    protected void attachBaseContext(Context base)
    {
        super.attachBaseContext(updateBaseContextLocale(base));
    }

    private Context updateBaseContextLocale(Context context)
    {
        String language = Locale.getDefault().getLanguage();

        if (languageIsAvailable(context, language))
        {
            Log.d("DEVELOPMENT:App", "updateBaseContextLocale:success");
            return updateLocale(context, language);
        }
        else
        {
            // Fallback to default English language
            Log.d("DEVELOPMENT:App", "updateBaseContextLocale:fallback");
            return updateLocale(context, "en");
        }
    }

    private boolean languageIsAvailable(Context context, String language)
    {
        // TODO: Implement logic to determine if language is available

        return true;
    }

    private Context updateLocale(Context context, String language)
    {
        Locale locale = new Locale(language);
        Configuration conf = new Configuration(context.getResources().getConfiguration());
        conf.setLocale(locale);

        Log.d("DEVELOPMENT:App", "updateLocale:success");

        return context.createConfigurationContext(conf);
    }

    private static App instance;
    private final AppActivityLifecycle lifecycle = new AppActivityLifecycle();
}