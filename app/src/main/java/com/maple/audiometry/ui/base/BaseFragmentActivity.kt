package com.maple.audiometry.ui.base

import android.app.Activity
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.maple.audiometry.R

/**
 * @author maple
 * @time 2018/10/11
 */
abstract class BaseFragmentActivity : AppCompatActivity() {
    lateinit var mContext: Activity

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT// 保持竖屏
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
        supportFragmentManager
                .beginTransaction()
                .add(containerViewId, fgView)
                .commit()
    }

    @JvmOverloads
    fun replaceView(fgView: Fragment, containerViewId: Int = R.id.fl_content) {
        supportFragmentManager
                .beginTransaction()
                .replace(containerViewId, fgView)
                .addToBackStack(null)
                .commit()
    }

    fun backFragment() {
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