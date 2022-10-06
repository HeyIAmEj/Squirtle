package com.squirtle.utils;

import android.content.Context;

public class LogoutUtils {
    public static void clearShared(Context context){
        SharedPreferencesUtils.clearUsuario(context);
    }
}
