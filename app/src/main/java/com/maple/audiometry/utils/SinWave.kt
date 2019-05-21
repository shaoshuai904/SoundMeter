package com.maple.audiometry.utils

object SinWave {
    /** 正弦波的高度  */
    var HEIGHT = 127.0
    /** 2PI  */
    const val TWOPI = 2 * 3.1415

    /**
     * 生成正弦波
     *
     * @param waveLen   每段正弦波的长度
     * @param length    总长度
     * @return
     */
    @JvmStatic
    fun sin(waveLen: Int, length: Int): ByteArray {
        val wave = ByteArray(length)
        for (i in 0 until length) {
            wave[i] = (HEIGHT * (1 - Math.sin(TWOPI * (i % waveLen * 1.00 / waveLen)))).toByte()
        }
        return wave
    }

    /**
     * 更新声音的分贝
     *
     * @param hz
     * @param dB
     */
    @JvmStatic
    fun updateDB(hz: Int, dB: Int) {
        var temp = 0.0
        when (hz) {
            1000 -> temp = 7.0// 1000频率
            2000 -> temp = 9.0// 2000频率
            4000 -> temp = 9.5// 4000频率
            8000 -> temp = 13.0// 8000频率
            500 -> temp = 11.5// 500频率
            250 -> temp = 25.5// 250频率
        }
        HEIGHT = Math.pow(10.0, (dB - temp) / 20)
    }
}
