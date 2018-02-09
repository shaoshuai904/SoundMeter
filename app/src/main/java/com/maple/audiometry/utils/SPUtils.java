package com.maple.audiometry.utils;


import android.content.Context;
import android.content.SharedPreferences;

/**
 * SharedPreferences Utils
 *
 * @author maple
 * @time 16/4/14 下午5:37
 */
public class SPUtils {

    public SPUtils() {
        /* cannot be instantiated */
        throw new UnsupportedOperationException("cannot be instantiated");
    }

    public static final String FILE_NAME = "share_data";

    private static SharedPreferences sp;

    // put
    public static void putStr(Context context, String key, String value) {
        if (sp == null) {
            sp = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        }
        sp.edit().putString(key, value).commit();
    }

    public static void putLong(Context context, String key, long value) {
        if (sp == null) {
            sp = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        }
        sp.edit().putLong(key, value).commit();
    }

    public static void putInt(Context context, String key, int value) {
        if (sp == null) {
            sp = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        }
        sp.edit().putInt(key, value).commit();
    }

    public static void putBol(Context context, String key, boolean value) {
        if (sp == null) {
            sp = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        }
        sp.edit().putBoolean(key, value).commit();
    }

    // get
    public static String getStr(Context context, String key, String defValue) {
        if (sp == null) {
            sp = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        }
        return sp.getString(key, defValue);
    }

    public static long getLong(Context context, String key, long defValue) {
        if (sp == null) {
            sp = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        }
        return sp.getLong(key, defValue);
    }

    public static int getInt(Context context, String key, int defValue) {
        if (sp == null) {
            sp = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        }
        return sp.getInt(key, defValue);
    }

    public static boolean getBol(Context context, String key, boolean defValue) {
        if (sp == null) {
            sp = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        }
        return sp.getBoolean(key, defValue);
    }

}
