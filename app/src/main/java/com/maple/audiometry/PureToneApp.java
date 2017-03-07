package com.maple.audiometry;

import java.io.File;
import java.io.PrintStream;
import java.lang.Thread.UncaughtExceptionHandler;

import android.app.Application;
import android.os.Handler;
import android.util.Log;

/**
 * 纯音测听APP
 * 
 * @author shaoshuai
 * 
 */
public class PureToneApp extends Application {
	private static PureToneApp app;
	private static Handler sHandler = new Handler();

	@Override
	public void onCreate() {
		app = this;
		super.onCreate();
		Log.e("PureToneApp", "app创建");

		// 设置异常的处理类
		// Thread.currentThread().setUncaughtExceptionHandler(
		// new MyUncaughtExceptionHandler());
	}

	private class MyUncaughtExceptionHandler implements
			UncaughtExceptionHandler {
		// 异常处理代码
		@Override
		public void uncaughtException(Thread thread, Throwable ex) {
			// 死前一言
			System.out.println("发现一个异常，但是被哥捕获了");
			PrintStream err;
			try {
				File file = new File("/mnt/sdcard/err.txt");
				if (!file.exists()) {
					file.createNewFile();
				}
				err = new PrintStream(file);
				ex.printStackTrace(err);
			} catch (Exception e) {
				e.printStackTrace();
			}
			android.os.Process.killProcess(android.os.Process.myPid()); // 杀死进程，自杀，闪退
			// 防崩设计
		}
	}

	/**
	 * 返回LeaderApp对象
	 */
	public static PureToneApp app() {
		return app;
	}

	public static void postUi(Runnable run) {
		sHandler.post(run);
	}
}
