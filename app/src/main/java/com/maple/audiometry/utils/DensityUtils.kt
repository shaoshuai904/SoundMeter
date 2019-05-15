package com.maple.audiometry.utils

import android.content.Context
import android.util.TypedValue

/**
 * @author maple
 * @time 2016/6/3
 */
object DensityUtils {

    @JvmStatic
    fun dp2px(context: Context, dpVal: Float): Int {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpVal, context.resources
                .displayMetrics).toInt()
    }

    @JvmStatic
    fun px2dp(context: Context, pxVal: Float): Float {
        val scale = context.resources.displayMetrics.density
        return pxVal / scale
    }

    @JvmStatic
    fun sp2px(context: Context, spVal: Float): Int {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, spVal, context.resources
                .displayMetrics).toInt()
    }

    @JvmStatic
    fun px2sp(context: Context, pxVal: Float): Float {
        return pxVal / context.resources.displayMetrics.scaledDensity
    }
}
