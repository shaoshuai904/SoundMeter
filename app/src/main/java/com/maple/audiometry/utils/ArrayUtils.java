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

    /**
     * 截取数组
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
