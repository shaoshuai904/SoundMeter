package com.maple.audiometry.ui.base

import android.content.Context
import android.support.v4.app.Fragment

/**
 * @author maple
 * @time 2018/10/11
 */
abstract class BaseFragment : Fragment() {
    lateinit var mContext: Context

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    }

    open fun onKeyBackPressed(): Boolean {
        // 是否消耗掉back事件
        return false
    }

}

