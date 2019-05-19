package com.maple.audiometry.ui.welcome

import android.Manifest
import android.content.Intent
import android.os.Bundle
import android.support.v4.view.ViewPager.OnPageChangeListener
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.ImageView
import com.maple.audiometry.R
import com.maple.audiometry.ui.base.BaseActivity
import com.maple.audiometry.ui.home.MainActivity
import com.maple.audiometry.utils.permission.RxPermissions
import kotlinx.android.synthetic.main.activity_guide.*
import java.util.*

/**
 * 引导界面
 *
 * @author shaoshuai
 */
class SplashActivity : BaseActivity() {
    private lateinit var guideDotList: Array<ImageView> // 点集合
    private lateinit var guideViews: ArrayList<View> // 页面集合
    private var guideViewPagerAdapter: GuideViewPagerAdapter? = null

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_guide)

        initViews()
        RxPermissions(this)
                .request(
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.RECORD_AUDIO
                )
                .subscribe()
    }

    /**
     * 初始化视图
     */
    private fun initViews() {
        // 初始化页面
        val inflater = LayoutInflater.from(this)
        val guideView1 = inflater.inflate(R.layout.activity_guide_view1, null)
        val guideView2 = inflater.inflate(R.layout.activity_guide_view2, null)
        val guideView3 = inflater.inflate(R.layout.activity_guide_view3, null)
        val guideStartBtn: Button = guideView3.findViewById(R.id.next)
        guideStartBtn.setOnClickListener { goMainUi() }
        // 初始化点
        guideDotList = arrayOf(
                findViewById(R.id.guide_dot1_iv),
                findViewById(R.id.guide_dot2_iv),
                findViewById(R.id.guide_dot3_iv)
        )
        // 添加页面
        guideViews = arrayListOf(
                guideView1,
                guideView2,
                guideView3
        )
        guideViewPagerAdapter = GuideViewPagerAdapter(guideViews)
        guide_viewpager.adapter = guideViewPagerAdapter
        guide_viewpager.addOnPageChangeListener(object : OnPageChangeListener {

            override fun onPageSelected(position: Int) {
                selectPage(position)
            }

            override fun onPageScrolled(arg0: Int, arg1: Float, arg2: Int) {}

            override fun onPageScrollStateChanged(arg0: Int) {}
        })
    }


    /**
     * 前往主界面
     */
    private fun goMainUi() {
        finish()
        // overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }

    /**
     * 浮点显示控制
     *
     * @param current
     */
    private fun selectPage(current: Int) {
        for (i in guideDotList.indices) {
            guideDotList[current].setImageResource(R.drawable.guide_dot_pressed)
            if (current != i) {
                guideDotList[i].setImageResource(R.drawable.guide_dot_normal)
            }
        }
    }

}
