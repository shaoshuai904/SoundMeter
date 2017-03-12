package com.maple.audiometry.utils;

/*
 * 这个类的对象初始化比较麻烦，因为它是被设计用来录制一段完整的音频并写入文件系统中的。
 * 但是初始化之后获得振幅却比较方便，我们直接用它的无参方法getMaxAmplitude即可获得一小段时间内音源数据中的最大振幅。
 * 不过取最大值的可能弊端是会受到极端数据的影响，使得后来计算的分贝值波动比较大。不过这个方法是很多录音应用计算音量等级所采用的办法。
 * 
 * 该方法返回的是0到32767范围的16位整型，原理可能是对一段值域为-32768到32767的音源数据取其中绝对值最大的值并返回。
 * 这个值与单位为帕斯卡的声压值是有线性函数关系的。
 * 另外需要注意的是第一次调用这个方法取得的值是0，代入公式中算出的分贝值是负无穷大，故需要在代码中对这种情况做判断。
 * 可以算出，由于getMaxAmplitude返回的数值最大是32767，因此算出的最大分贝值是90.3。
 * 也就是说，博主令参考振幅值为1，计算出的分贝值正常值域为0 dB 到90.3 dB。
 */
import java.io.File;
import java.io.IOException;

import android.media.MediaRecorder;
import android.os.Handler;
import android.util.Log;

/**
 * amr音频处理
 * 
 * @author hongfa.yy
 * @version 创建时间2012-11-21 下午4:33:28
 */
public class MediaRecorderDemo {
	private final String TAG = "MediaRecord";
	public static final int MAX_LENGTH = 1000 * 60 * 10;// 最大录音时长1000*60*10;
	private MediaRecorder mMediaRecorder;
	private int BASE = 1;
	private int SPACE = 100;// 间隔取样时间
	private long startTime;
	private long endTime;
	private String filePath;
	/** 噪音回调 */
	private NoiseValueUpdateCallback mNoiseCallBack;
	private final Handler mHandler = new Handler();

	private Runnable mUpdateMicStatusTimer = new Runnable() {
		public void run() {
			updateMicStatus();
		}
	};

	/** 噪音分贝值，更新回调接口 */
	public static interface NoiseValueUpdateCallback {
		/** 更新噪音分贝值 */
		void onUpdateNoiseValue(double noiseValue);
	}

	public MediaRecorderDemo(NoiseValueUpdateCallback noiseValueUpdateCallback) {
		this.filePath = "/dev/null";
		this.mNoiseCallBack = noiseValueUpdateCallback;
	}

	public MediaRecorderDemo(File file, NoiseValueUpdateCallback noiseValueUpdateCallback) {
		this.filePath = file.getAbsolutePath();
		this.mNoiseCallBack = noiseValueUpdateCallback;
	}

	/**
	 * 开始录音 使用amr格式
	 */
	public void startRecord() {
		// 开始录音
		/* ①Initial：实例化MediaRecorder对象 */
		if (mMediaRecorder == null)
			mMediaRecorder = new MediaRecorder();
		try {
			/* ②setAudioSource/setVedioSource */
			mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);// 设置麦克风
			/* ②设置音频文件的编码：AAC/AMR_NB/AMR_MB/Default 声音的（波形）的采样 */
			mMediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.DEFAULT);
			/*
			 * ②设置输出文件的格式：THREE_GPP/MPEG-4/RAW_AMR/Default THREE_GPP(3gp格式
			 * ，H263视频/ARM音频编码)、MPEG-4、RAW_AMR(只支持音频且音频编码要求为AMR_NB)
			 */
			mMediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
			/* ③准备 */
			mMediaRecorder.setOutputFile(filePath);
			mMediaRecorder.setMaxDuration(MAX_LENGTH);
			mMediaRecorder.prepare();// 准备
			/* ④开始 */
			mMediaRecorder.start();
			// AudioRecord audioRecord.
			/* 获取开始时间* */
			startTime = System.currentTimeMillis();
			updateMicStatus();
			Log.i("ACTION_START", "开始时间" + startTime);
		} catch (IllegalStateException e) {
			Log.i(TAG, "call startAmr(File mRecAudioFile) 失败!" + e.getMessage());
		} catch (IOException e) {
			Log.i(TAG, "call startAmr(File mRecAudioFile) 失败!" + e.getMessage());
		}
	}

	/**
	 * 更新话筒状态
	 */
	private void updateMicStatus() {
		if (mMediaRecorder != null) {
			double ratio = (double) mMediaRecorder.getMaxAmplitude() / BASE;
			double db = 0;// 分贝
			if (ratio > 1)
				db = 20 * Math.log10(ratio);
			// Log.e(TAG, "分贝值：" + db);
			mNoiseCallBack.onUpdateNoiseValue(db);// 回调接口的使用
			mHandler.postDelayed(mUpdateMicStatusTimer, SPACE);
		}
	}

	/**
	 * 停止录音
	 */
	public long stopRecord() {
		if (mMediaRecorder == null)
			return 0L;
		endTime = System.currentTimeMillis();
		Log.i("ACTION_END", "结束时间" + endTime);
		mMediaRecorder.stop();
		mMediaRecorder.reset();
		mMediaRecorder.release();
		mMediaRecorder = null;
		Log.i("ACTION_LENGTH", "时间" + (endTime - startTime));
		return endTime - startTime;
	}

}