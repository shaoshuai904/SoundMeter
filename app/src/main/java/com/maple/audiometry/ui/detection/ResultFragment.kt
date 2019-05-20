package com.maple.audiometry.ui.detection

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import com.maple.audiometry.R
import com.maple.audiometry.ui.base.BaseFragment
import com.maple.audiometry.view.MelodyView
import kotlinx.android.synthetic.main.activity_base_top_bar.*
import kotlinx.android.synthetic.main.fragment_result.*

/**
 * 结果界面
 *
 * @author shaoshuai
 */
class ResultFragment : BaseFragment() {
    private lateinit var mActivity: DetectionActivity

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val view = inflater.inflate(R.layout.fragment_result, container, false)
        view.isClickable = true
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        mActivity = activity as DetectionActivity

        tv_left.setOnClickListener { mActivity.onBackPressed() }
        tv_title.text = "测试结果"

        initView()
    }

    // 初始化折线图
    private fun initView() {
        val param = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT)
        // 初始化左耳折线图
        val mLeftEar = MelodyView(mContext, MelodyView.ONLY_LEFT)
        mLeftEar.setYScope(120, -10)// 设置最大最小值
        ll_left_result.removeAllViews()
        ll_left_result.addView(mLeftEar, param)
        // 初始化左耳折线图
        val mRightEar = MelodyView(mContext, MelodyView.ONLY_RIGHT)
        mRightEar.setYScope(120, -10)// 设置最大最小值
        ll_right_result.removeAllViews()
        ll_right_result.addView(mRightEar, param)

        // 更新左耳检测数据
        for (i in 0 until mLeftEar.leftDate.size) {
            mLeftEar.updateData(i, mActivity.leftEarData[i], true)// 更新折线图
        }
        // 更新右检测数据
        for (i in 0 until mRightEar.rightDate.size) {
            mRightEar.updateData(i, mActivity.rightEarData[i], false)// 更新折线图
        }

        // 去结果页面
        bt_look.setOnClickListener { mActivity.replaceView(EndFragment()) }
    }

}
