package com.squirtle.utils;

import android.content.Context;
import android.content.Intent;

import com.squirtle.activities.NovoDispositivo;

public class LogoutUtils {
    public static void clearShared(Context context){
        SharedPreferencesUtils.clearUsuario(context);
    }
    public static void logout(Context context) {
        Intent intent = new Intent(context, NovoDispositivo.class);
        context.startActivity(intent);
    }
}
