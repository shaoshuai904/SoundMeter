package com.maple.audiometry.base

import android.content.Context
import android.view.LayoutInflater
import android.widget.BaseAdapter

import java.util.ArrayList

/**
 * @author maple
 * @time 16/4/13 下午5:55
 */
abstract class AbsAdapter<T>(var mContext: Context, datas: MutableList<T>?) : BaseAdapter() {
    var inflater: LayoutInflater

    protected var mDatas: MutableList<T>

    init {
        inflater = LayoutInflater.from(mContext)

        if (datas == null)
            mDatas = ArrayList()
        else
            mDatas = datas
    }

    override fun getCount(): Int {
        return mDatas.size
    }

    override fun getItem(position: Int): T {
        return mDatas[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    fun remove(index: Int) {
        if (index < 0 || index >= mDatas.size) {
            return
        }
        mDatas.removeAt(index)
        this.notifyDataSetChanged()

    }

    fun remove(data: T?) {
        if (data == null) {
            return
        }
        mDatas.remove(data)
        this.notifyDataSetChanged()

    }

    fun add(t: T?) {
        if (t == null) {
            return
        }
        mDatas.add(t)
        this.notifyDataSetChanged()
    }

    fun refresh(datas: MutableList<T>?) {
        if (datas == null)
            mDatas = ArrayList()
        else
            mDatas = datas

        this.notifyDataSetChanged()
    }

}
