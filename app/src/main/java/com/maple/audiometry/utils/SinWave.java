package com.maple.audiometry.utils;

public class SinWave {
	/** 正弦波的高度 **/
	public static double HEIGHT = 127;
	/** 2PI **/
	public static final double TWOPI = 2 * 3.1415;

	/**
	 * 生成正弦波
	 * 
	 * @param waveLen
	 *            每段正弦波的长度
	 * @param length
	 *            总长度
	 * @return
	 */
	public static byte[] sin(int waveLen, int length) {
		byte[] wave = new byte[length];
		for (int i = 0; i < length; i++) {
			wave[i] = (byte) (HEIGHT * (1 - Math.sin(TWOPI
					* ((i % waveLen) * 1.00 / waveLen))));
		}
		return wave;
	}

	/**
	 * 更新声音的分贝
	 * 
	 * @param hz
	 * @param dB
	 */
	public static void updateDB(int hz, int dB) {
		double temp = 0;
		switch (hz) {
		case 1000:// 1000频率
			temp = 7;
			break;
		case 2000:// 2000频率
			temp = 9;
			break;
		case 4000:// 4000频率
			temp = 9.5;
			break;
		case 8000:// 8000频率
			temp = 13;
			break;
		case 500:// 500频率
			temp = 11.5;
			break;
		case 250:// 250频率
			temp = 25.5;
			break;
		}
		HEIGHT = Math.pow(10, (dB - temp) / 20);
	}
}
