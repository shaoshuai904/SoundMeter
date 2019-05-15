package com.maple.audiometry.utils

import android.content.Context
import android.widget.Toast


/**
 * @author maple
 * @time 2017/3/12
 */
object T {
    private const val isShow = true

    @JvmStatic
    fun showShort(context: Context, msg: String) {
        if (isShow) {
            Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
        }
    }

    @JvmStatic
    fun showLong(context: Context, msg: String) {
        if (isShow) {
            Toast.makeText(context, msg, Toast.LENGTH_LONG).show()
        }
    }

}
