package com.maple.audiometry.utils;

/*
 * 这个类可以获得具体的音源数据值。 将一段音源数据用read(byte[] audioData, int
 * offsetInBytes, int sizeInBytes)方法 从缓冲区读取到我们传入的字节数组audioData后，
 * 便可以对其进行操作，如求平方和或绝对值的平均值。 这样可以避免个别极端值的影响，使计算的结果更加稳定。
 * 求得平均值之后，如果是平方和则代入常数系数为10的公式中， 如果是绝对值的则代入常数系数为20的公式中，算出分贝值。
 */
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.util.Log;

/**
 * 获取噪音分贝值
 */
public class AudioRecordDemo {

	private static final String TAG = "AudioRecord";
	static final int SAMPLE_RATE_IN_HZ = 8000;
	static final int BUFFER_SIZE = AudioRecord.getMinBufferSize(
			SAMPLE_RATE_IN_HZ, AudioFormat.CHANNEL_IN_DEFAULT,
			AudioFormat.ENCODING_PCM_16BIT);
	AudioRecord mAudioRecord;
	boolean isGetVoiceRun;
	Object mLock;

	public AudioRecordDemo() {
		mLock = new Object();
	}

	/**
	 * 获取噪音水平
	 */
	public void getNoiseLevel() {
		if (isGetVoiceRun) {
			Log.e(TAG, "还在录着呢");
			return;
		}
		mAudioRecord = new AudioRecord(MediaRecorder.AudioSource.MIC,
				SAMPLE_RATE_IN_HZ, AudioFormat.CHANNEL_IN_DEFAULT,
				AudioFormat.ENCODING_PCM_16BIT, BUFFER_SIZE);
		if (mAudioRecord == null) {
			Log.e("sound", "mAudioRecord初始化失败");
		}
		isGetVoiceRun = true;

		new Thread(new Runnable() {
			@Override
			public void run() {
				mAudioRecord.startRecording();
				short[] buffer = new short[BUFFER_SIZE];
				while (isGetVoiceRun) {
					double volume = getVolume(buffer);
					Log.e(TAG, "分贝值:" + volume);
					// 大概一秒十次
					synchronized (mLock) {
						try {
							mLock.wait(100);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				}
				mAudioRecord.stop();
				mAudioRecord.release();
				mAudioRecord = null;
			}

		}).start();
	}

	/**
	 * 计算分贝值
	 * 
	 * @param buffer
	 * @return
	 */
	private double getVolume(short[] buffer) {
		/** 方式一：求平方和。计算平均值。代入常数系数为10的公式中 */
		// r是实际读取的数据长度，一般而言r会小于buffersize
		int r = mAudioRecord.read(buffer, 0, BUFFER_SIZE);
		long v = 0;
		// 将 buffer 内容取出，进行平方和运算
		for (int i = 0; i < buffer.length; i++) {
			Log.e("", "声音数据：" + buffer[i]);
			v += buffer[i] * buffer[i];
		}
		// 平方和除以数据总长度，得到音量大小。
		double mean = v / (double) r;
		double volume = 10 * Math.log10(mean);
		return volume;
	}

	// /**
	// * 计算分贝值
	// *
	// * @param buffer
	// * @return
	// */
	// private double getdB(short[] buffer) {
	// /** 方式二：计算绝对值的平均值。代入常数系数为20的公式中 */
	// // r是实际读取的数据长度，一般而言r会小于buffersize
	// int r = mAudioRecord.read(buffer, 0, BUFFER_SIZE);
	// long num = 0;
	// // 将 buffer 内容取出，进行平方和运算
	// for (int i = 0; i < buffer.length; i++) {
	// num += Math.abs(buffer[i]);
	// }
	// // 平方和除以数据总长度，得到音量大小。
	// double avg = num / (double) r;
	// double dbValue = 20 * Math.log10(avg);
	// Log.e(TAG, "分贝值222:" + dbValue);
	// return dbValue;
	// }
}