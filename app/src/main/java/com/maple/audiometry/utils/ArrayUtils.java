package com.maple.audiometry.utils;

import java.util.List;

/**
 * 数组工具类
 * 
 * @author shao
 */
public class ArrayUtils {

	/**
	 * 求一个数组的总和
	 */
	public static int sum(int[] datas) {
		int sum = 0;
		for (int s : datas) {
			sum += s;
		}
		return sum;
	}

	/**
	 * 求一个数组的总和
	 */
	public static double sum(List<Double> datas) {
		double sum = 0;
		for (int i = 0; i < datas.size(); i++) {
			sum += datas.get(i);
		}
		return sum;
	}

	/**
	 * 求一个数组的平均数
	 */
	public static int avg(int[] datas) {
		if (datas.length == 0) {
			return 0;
		} else {
			return sum(datas) / datas.length;
		}
	}

	/**
	 * 求一个数组的平均数
	 */
	public static double avg(List<Double> datas) {
		if (datas.size() == 0) {
			return 0;
		} else {
			return sum(datas) / datas.size();
		}
	}

	// /**
	// * 获取数组最大值
	// */
	// public static double max(double[] datas) {
	// double max = Double.MIN_VALUE;
	// for (double f : datas) {
	// max = (f > max) ? f : max;
	// }
	// return max;
	// }
	//
	// /**
	// * 获取数组最小值
	// */
	// public static short min(short[] datas) {
	// short mix = Short.MAX_VALUE;
	// for (short f : datas) {
	// mix = (f < mix) ? f : mix;
	// }
	// return mix;
	// }
	//
	// /**
	// * 获取数组最小值
	// */
	// public static float min(float[] datas) {
	// float mix = Float.MAX_VALUE;
	// for (float f : datas) {
	// mix = (f < mix) ? f : mix;
	// }
	// return mix;
	// }
	//
	// public static String toString(short[] datas) {
	// StringBuilder sb = new StringBuilder();
	// for (short s : datas) {
	// sb.append(s + "@");
	// }
	// return sb.toString();
	// }

	/**
	 * 截取数组
	 * 
	 * @param allVolume
	 * @param i
	 * @return
	 */
	public static double[] sub(List<Double> allVolume, int num) {
		double[] dous = new double[num];
		if (allVolume.size() < num) {// 长度不够
			for (int i = 0; i < allVolume.size(); i++) {
				dous[i] = allVolume.get(i);
			}
		} else {// 长度足够。选取后半部分
			int index = allVolume.size() - num;
			for (int i = 0; i < num; i++) {
				dous[i] = allVolume.get(index + i);
			}
		}
		return dous;
	}
}
