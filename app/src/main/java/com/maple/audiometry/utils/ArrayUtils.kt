package com.maple.audiometry.utils

/**
 * 数组工具类
 *
 * @author shao
 */
object ArrayUtils {

    /**
     * 求一个数组的总和
     */
    @JvmStatic
    fun sum(datas: IntArray): Int {
        var sum = 0
        for (s in datas) {
            sum += s
        }
        return sum
    }

    /**
     * 求一个数组的总和
     */
    @JvmStatic
    fun sum(datas: List<Double>): Double {
        var sum = 0.0
        for (i in datas.indices) {
            sum += datas[i]
        }
        return sum
    }

    /**
     * 求一个数组的平均数
     */
    @JvmStatic
    fun avg(datas: IntArray): Int {
        return if (datas.isEmpty()) {
            0
        } else {
            sum(datas) / datas.size
        }
    }

    /**
     * 求一个数组的平均数
     */
    @JvmStatic
    fun avg(datas: List<Double>): Double {
        return if (datas.isEmpty()) {
            0.0
        } else {
            sum(datas) / datas.size
        }
    }

    /**
     * 截取数组
     */
    @JvmStatic
    fun sub(allVolume: List<Double>, num: Int): DoubleArray {
        val dous = DoubleArray(num)
        if (allVolume.size < num) {// 长度不够
            for (i in allVolume.indices) {
                dous[i] = allVolume[i]
            }
        } else {// 长度足够。选取后半部分
            val index = allVolume.size - num
            for (i in 0 until num) {
                dous[i] = allVolume[index + i]
            }
        }
        return dous
    }
}
