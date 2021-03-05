package com.maple.audiometry.ui.detection

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.maple.audiometry.R
import com.maple.audiometry.ui.base.BaseFragment
import com.maple.audiometry.ui.home.MainActivity
import kotlinx.android.synthetic.main.activity_base_top_bar.*
import kotlinx.android.synthetic.main.fragment_end.*
import java.text.DecimalFormat

/**
 * 测试结果
 *
 * @author shaoshuai
 */
class EndFragment : BaseFragment() {
    private lateinit var mActivity: DetectionActivity

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val view = inflater.inflate(R.layout.fragment_end, container, false)
        view.isClickable = true
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        mActivity = activity as DetectionActivity

        tv_left.setOnClickListener { mActivity.onBackPressed() }
        tv_title.text = "分析报告"

        initView()
    }

    private fun initView() {
        val fbl = mActivity.leftEarData
        val fbr = mActivity.rightEarData
        // 给出平均听力=（1000Hz测试的结果+2000Hz测试得到结果+500Hz测试得到结果）/3
        val left = (fbl[0] + fbl[1] + fbl[4]) / 3.0
        val right = (fbr[0] + fbr[1] + fbr[4]) / 3.0
        // 结果
        val leftRank = computeRank(left)
        val rightRank = computeRank(right)
        // 听力等级
        val df = DecimalFormat("##.00")
        val hearingRank = resources.getStringArray(R.array.hearing_rank_arr)
        val leftStr = "左耳：" + df.format(left) + "(" + hearingRank[leftRank] + ")"
        val rightStr = "右耳：" + df.format(right) + "(" + hearingRank[rightRank] + ")"
        tv_check_result.text = leftStr + "\n" + rightStr
        // 分析和建议
        // 分析建议的说明文字
        val dbExplain = resources.getStringArray(R.array.propose_arr)
        if (leftRank == rightRank) {
            tv_propose.text = "双耳：\n" + dbExplain[leftRank]
        } else {
            tv_propose.text = "左耳：\n" + dbExplain[leftRank] + "\n右耳：\n" + dbExplain[rightRank]
        }

        bt_end.setOnClickListener { toMainPager() }
    }

    /**
     * 根据平均听力计算听力等级
     */
    private fun computeRank(dous: Double): Int {
        var rank = 0
        if (dous <= 25) {
            rank = 0
        } else if (dous > 25 && dous <= 40) {// 轻度
            rank = 1
        } else if (dous > 40 && dous <= 55) {// 中度
            rank = 2
        } else if (dous > 55 && dous <= 70) {// 中重度
            rank = 3
        } else if (dous > 70 && dous <= 90) {// 重度
            rank = 4
        } else if (dous > 90) {// 极重度
            rank = 5
        }
        return rank
    }

    /**
     * 返回主界面
     */
    private fun toMainPager() {
        mActivity.finish()
        val intent = Intent(mActivity, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        startActivity(intent)
    }
}
