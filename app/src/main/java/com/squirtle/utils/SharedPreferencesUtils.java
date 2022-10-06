package com.squirtle.utils;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.util.ArraySet;

import androidx.annotation.RequiresApi;

import java.util.Set;

public class SharedPreferencesUtils {

    public static SharedPreferences getSharedPreferences(Context context, String sharedname){
        return (SharedPreferences) context.getSharedPreferences(sharedname, MODE_PRIVATE);

    }

    public static SharedPreferences.Editor setSharedPreferences(Context context, String sharedname){
        SharedPreferences.Editor editor;
        SharedPreferences sharedPreferences;

        sharedPreferences = SharedPreferencesUtils.getSharedPreferences(context, sharedname);
        editor = sharedPreferences.edit();
        return editor;
    }

    public static void setString(Context context, String sharedname, String sharedKey, String sharedValue){
        SharedPreferences.Editor editor;
        editor = SharedPreferencesUtils.setSharedPreferences(context, sharedname);
        editor.putString(sharedKey, sharedValue);
        editor.commit();
    }

    public static void setBoolean(Context context, String sharedname, String sharedKey, Boolean sharedValue){
        SharedPreferences.Editor editor;
        editor = SharedPreferencesUtils.setSharedPreferences(context, sharedname);
        editor.putBoolean(sharedKey, sharedValue);
        editor.commit();
    }

    public static void setStringSet(Context context, String sharedname, String sharedKey, Set<String> sharedValue){
        SharedPreferences.Editor editor;
        editor = SharedPreferencesUtils.setSharedPreferences(context, sharedname);
        editor.putStringSet(sharedKey, sharedValue);
        editor.commit();
    }

    public static void setLong(Context context, String sharedname, String sharedKey, Long sharedValue){
        SharedPreferences.Editor editor;
        editor = SharedPreferencesUtils.setSharedPreferences(context, sharedname);
        editor.putLong(sharedKey, sharedValue);
        editor.commit();
    }

    public static void setInt(Context context, String sharedname, String sharedKey, Integer sharedValue){
        SharedPreferences.Editor editor;
        editor = SharedPreferencesUtils.setSharedPreferences(context, sharedname);
        editor.putInt(sharedKey, sharedValue);
        editor.commit();
    }

    public static String getString(Context context, String sharedname, String sharedKey){
        SharedPreferences sharedPreferences;
        sharedPreferences = SharedPreferencesUtils.getSharedPreferences(context, sharedname);
        return sharedPreferences.getString(sharedKey, "");
    }
    public static Boolean getBoolean(Context context, String sharedname, String sharedKey){
        SharedPreferences sharedPreferences;
        sharedPreferences = SharedPreferencesUtils.getSharedPreferences(context, sharedname);
        return sharedPreferences.getBoolean(sharedKey, false);
    }
    @RequiresApi(api = Build.VERSION_CODES.M)
    public static Set<String> getStringSet(Context context, String sharedname, String sharedKey){
        SharedPreferences sharedPreferences;
        sharedPreferences = SharedPreferencesUtils.getSharedPreferences(context, sharedname);
        return sharedPreferences.getStringSet(sharedKey, new ArraySet<>());
    }
    public static Long getLong(Context context, String sharedname, String sharedKey){
        SharedPreferences sharedPreferences;
        sharedPreferences = SharedPreferencesUtils.getSharedPreferences(context, sharedname);
        return sharedPreferences.getLong(sharedKey, 0L);
    }
    public static Integer getInt(Context context, String sharedname, String sharedKey){
        SharedPreferences sharedPreferences;
        sharedPreferences = SharedPreferencesUtils.getSharedPreferences(context, sharedname);
        return sharedPreferences.getInt(sharedKey, 0);
    }
    public static void clearUsuario(Context context){
        SharedPreferences.Editor editor;
        editor = SharedPreferencesUtils.setSharedPreferences(context, "usuario");
        editor.clear();
        editor.commit();
    }
}
