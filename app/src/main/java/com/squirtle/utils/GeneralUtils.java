package com.squirtle.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

import com.squirtle.R;


public class GeneralUtils {

    public static int get_icon(String icon_str){
        int icon;
        switch (icon_str){
            case "sun":
                icon = R.drawable.ic_sun;
                break;
            case "moon":
                icon = R.drawable.ic_moon;
                break;
            case "ice":
                icon = R.drawable.ic_ice;
                break;
            case "cloud":
                icon = R.drawable.ic_cloud;
                break;
            case "leaf":
                icon = R.drawable.ic_leaf;
                break;
            case "plant":
                icon = R.drawable.ic_plant;
                break;
            default:
                icon = R.drawable.ic_device;
                break;
        }
        return icon;
    }

    public static void alert(Context context, String title, String message, String button_ok, String button_cancel, CustomCallback callback) {
        try {
            AlertDialog dialog = new AlertDialog.Builder(context).create();
            dialog.setTitle(title);
            dialog.setMessage(message);
            dialog.setCanceledOnTouchOutside(true);
            dialog.setButton(DialogInterface.BUTTON_NEGATIVE, button_ok, (dialog13, which) -> {
                if (callback != null) {
                    callback.onTrueCallback();
                }
                dialog13.dismiss();
                if (!dialog.isShowing()) {
                    dialog.dismiss();
                }
            });
            dialog.setButton(DialogInterface.BUTTON_POSITIVE, button_cancel, (dialog1, which) -> {
                if (callback != null) {
                    callback.onFalseCallback();
                }
                dialog1.dismiss();
                if (!dialog.isShowing()) {
                    dialog.dismiss();
                }
            });

            if (!dialog.isShowing()) {
                dialog.show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
