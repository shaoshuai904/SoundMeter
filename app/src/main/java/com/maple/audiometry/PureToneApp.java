package com.maple.audiometry;

import android.app.Application;
import android.os.Handler;
import android.util.Log;

/**
 * 纯音测听APP
 *
 * @author shaoshuai
 */
public class PureToneApp extends Application {
    private static PureToneApp app;
    private static Handler sHandler = new Handler();

    @Override
    public void onCreate() {
        super.onCreate();
        app = this;
        Log.e("PureToneApp", "app创建");
    }

    public static PureToneApp app() {
        return app;
    }

    public static void postUi(Runnable run) {
        sHandler.post(run);
    }
}
