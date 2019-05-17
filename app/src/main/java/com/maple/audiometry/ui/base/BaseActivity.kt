package com.maple.audiometry.ui.base

import android.content.Context
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity
import com.maple.audiometry.R

/**
 * @author maple
 * @time 2018/10/11
 */
abstract class BaseActivity : FragmentActivity() {
    lateinit var mContext: Context

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT// 保持竖屏
        mContext = this
    }

    override fun onBackPressed() {
        val fragment = supportFragmentManager.findFragmentById(R.id.fl_content)
        if (fragment is BaseFragment) {
            if (!fragment.onKeyBackPressed()) {
                backFragment()
            }
        } else {
            super.onBackPressed()
        }
    }

    // ------------------ fragment method ------------------

    @JvmOverloads
    fun addView(fgView: Fragment, containerViewId: Int = R.id.fl_content) {
        val ft = supportFragmentManager.beginTransaction()
        ft.add(containerViewId, fgView).commit()
    }

    @JvmOverloads
    fun replaceView(fgView: Fragment, containerViewId: Int = R.id.fl_content) {
        val ft = supportFragmentManager.beginTransaction()
        ft.replace(containerViewId, fgView)
        ft.addToBackStack(null)
        ft.commit()
    }

    private fun backFragment() {
        if (supportFragmentManager.backStackEntryCount > 0) {
            supportFragmentManager.popBackStack()
        } else {
            finish()
        }
    }

    //    public boolean onNext() {
    //        int nextPage = getSupportFragmentManager().getBackStackEntryCount() + 1;
    //        if (fragmentList != null && fragmentList.size() > nextPage) {
    //            replaceView(fragmentList.get(nextPage));
    //            return true;
    //        }
    //        return false;
    //    }

}