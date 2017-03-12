package com.maple.audiometry.utils;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;

public class AudioTrackManager {
	/** 采样频率 */
	public static int RATE = 44100;

	public static final int LEFT = 1;
	public static final int RIGHT = 2;
	public static final int DOUBLE = 3;
	/** 频率 */
	private int Hz;
	/** 分贝 */
	private int dB = 40;
	/** 音量 **/
	static float volume;

	AudioTrack audioTrack;
	/** 声道 **/
	int channel;
	/** 总长度 **/
	int length;
	/** 一个正弦波的长度 **/
	int waveLen;
	/** 频率 **/

	/** 正弦波 **/
	byte[] wave;

	public AudioTrackManager() {
		wave = new byte[RATE];
	}

	/**
	 * 设置音量
	 */
	public static void setVolume(float v) {
		volume = v;
	}

	/**
	 * 设置频率，分贝
	 */
	public void setRate(int hz, int db) {
		this.Hz = hz;
		this.dB = db;
	}

	public void start() {
		start(DOUBLE);
	}

	/**
	 * 
	 * @param channel
	 *            左耳，右耳，默认
	 */
	public void start(int channel) {
		stop();
		if (Hz > 0) {
			waveLen = RATE / Hz;
			length = waveLen * Hz;
			audioTrack = new AudioTrack(AudioManager.STREAM_SYSTEM,// 类型
					RATE,// 频率
					AudioFormat.CHANNEL_CONFIGURATION_STEREO, // CHANNEL_CONFIGURATION_MONO,
					AudioFormat.ENCODING_PCM_8BIT, // 位数
					length,// 缓冲区
					AudioTrack.MODE_STREAM// 模式
			);
			// 生成正弦波
			switch (channel) {
			case LEFT:
				audioTrack.setStereoVolume(volume, 0f);
				break;
			case RIGHT:
				
				audioTrack.setStereoVolume(0f, volume);
				break;
			case DOUBLE:
				audioTrack.setStereoVolume(volume, volume);
				break;
			}
			SinWave.updateDB(Hz, dB);
			wave = SinWave.sin(waveLen, length);
			if (audioTrack != null) {
				audioTrack.play();
			}
		} else {
			return;
		}

	}

	/**
	 * 写入数据
	 */
	public void play() {
		if (audioTrack != null) {
			audioTrack.write(wave, 0, length);
		}
	}

	/**
	 * 停止播放
	 */
	public void stop() {
		if (audioTrack != null) {
			audioTrack.stop();
			audioTrack.release();
			audioTrack = null;
		}
	}

	// /**
	// * 设置声道
	// *
	// * @param channel
	// */
	// public void setChannel(int channel) {
	// this.channel = channel;
	// setVolume(volume);
	// }
}
