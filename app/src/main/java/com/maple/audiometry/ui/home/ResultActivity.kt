package com.maple.audiometry.ui.home

import android.content.Intent
import android.os.Bundle
import android.widget.LinearLayout
import com.maple.audiometry.R
import com.maple.audiometry.ui.base.BaseActivity
import com.maple.audiometry.ui.chat.MelodyView
import kotlinx.android.synthetic.main.activity_result.*

/**
 * 结果界面
 *
 * @author shaoshuai
 */
class ResultActivity : BaseActivity() {
    private lateinit var mLeftEar: MelodyView // 左耳检测结果
    private lateinit var mRightEar: MelodyView // 左耳检测结果

    private lateinit var leftEarDatas: IntArray // 左耳检测数据
    private lateinit var rightEarDatas: IntArray // 右耳检测数据

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result)

        val bundle = intent.extras
        leftEarDatas = bundle.getIntArray("left")
        rightEarDatas = bundle.getIntArray("right")

        initView()

        bt_look.setOnClickListener { toEndPager() }
    }

    /**
     * 去结果页面
     */
    fun toEndPager() {
        val intent = Intent(this, EndActivity::class.java)
        val bundle = Bundle()
        bundle.putIntArray("left", leftEarDatas)// 左耳听力数据
        bundle.putIntArray("right", rightEarDatas)// 右耳听力数据
        intent.putExtras(bundle)
        startActivity(intent)
    }

    /**
     * 初始化折线图
     */
    private fun initView() {
        val param = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT)
        // 初始化左耳折线图
        mLeftEar = MelodyView(mContext, MelodyView.ONLY_LEFT)
        mLeftEar.setYScope(120, -10)// 设置最大最小值
        ll_left_result.removeAllViews()
        ll_left_result.addView(mLeftEar, param)
        // 初始化左耳折线图
        mRightEar = MelodyView(mContext, MelodyView.ONLY_RIGHT)
        mRightEar.setYScope(120, -10)// 设置最大最小值
        ll_right_result.removeAllViews()
        ll_right_result.addView(mRightEar, param)
        initData()
    }

    /**
     * 初始化检测数据
     */
    private fun initData() {
        // 更新左耳检测数据
        for (i in 0 until mLeftEar.leftDate.size) {
            mLeftEar.updateData(i, leftEarDatas[i], true)// 更新折线图
        }
        // 更新右检测数据
        for (i in 0 until mRightEar.rightDate.size) {
            mRightEar.updateData(i, rightEarDatas[i], false)// 更新折线图
        }
    }

}
