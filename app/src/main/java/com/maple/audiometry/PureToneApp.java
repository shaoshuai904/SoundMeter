package com.maple.audiometry;

import android.app.Application;

/**
 * 纯音测听APP
 *
 * @author shaoshuai
 */
public class PureToneApp extends Application {
    private static PureToneApp app;

    @Override
    public void onCreate() {
        super.onCreate();
        app = this;
    }

    public static PureToneApp app() {
        return app;
    }

}
