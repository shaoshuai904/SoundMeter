package com.maple.audiometry.utils;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by Maple on 2017/3/12.
 */
public class T {
    public static final boolean isShowm = true;

    public static void showShort(Context context, String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }

    public static void showLong(Context context, String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
    }
}
