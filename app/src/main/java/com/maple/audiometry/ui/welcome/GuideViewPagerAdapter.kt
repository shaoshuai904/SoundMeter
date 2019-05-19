package com.maple.audiometry.ui.welcome

import android.support.v4.view.PagerAdapter
import android.view.View
import android.view.ViewGroup
import java.util.*

/**
 * 引导界面的适配器
 *
 * @author maple
 */
class GuideViewPagerAdapter(
        private val Views: ArrayList<View>
) : PagerAdapter() {

    override fun getCount(): Int {
        return Views.size
    }

    override fun isViewFromObject(view: View, obj: Any): Boolean {
        return view == obj
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val view = Views[position]
        container.addView(view)
        return view
    }

    override fun destroyItem(container: ViewGroup, position: Int, obj: Any) {
        container.removeView(obj as View)
    }
}
