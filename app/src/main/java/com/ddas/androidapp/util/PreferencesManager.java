package com.ddas.androidapp.util;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Wrapper around SharedPreferences.
 * <p>
 * Allows calling SharedPreferences methods without requiring the confirmation.
 */
public class PreferencesManager
{
    public PreferencesManager(Context context, String fileName, int operatingMode)
    {
        preferences = context.getSharedPreferences(fileName, operatingMode);
    }

    public void putString(String name, String value)
    {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(name, value);
        editor.apply();
    }

    public void putInt(String name, int value)
    {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt(name, value);
        editor.apply();
    }

    public void putBoolean(String name, boolean value)
    {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(name, value);
        editor.apply();
    }

    public String getString(String name)
    {
        return preferences.getString(name, "");
    }

    public int getInt(String name)
    {
        return preferences.getInt(name, 0);
    }

    public boolean getBoolean(String name)
    {
        return preferences.getBoolean(name, false);
    }

    public void clear()
    {
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.apply();
    }

    private final SharedPreferences preferences;
}