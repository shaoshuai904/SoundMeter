package com.maple.audiometry.ui.home

import android.content.Intent
import android.os.Bundle
import com.maple.audiometry.R
import com.maple.audiometry.ui.base.BaseActivity
import kotlinx.android.synthetic.main.activity_end.*
import java.text.DecimalFormat

/**
 * 测试结果
 *
 * @author shaoshuai
 */
class EndActivity : BaseActivity() {
    lateinit var fbl: IntArray
    lateinit var fbr: IntArray
    var left: Double = 0.0
    var right: Double = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_end)

        fbl = intent.getIntArrayExtra("left")
        fbr = intent.getIntArrayExtra("right")

        initView()

        bt_end.setOnClickListener { toMainPager() }
    }

    private fun initView() {
        // 给出平均听力=（1000Hz测试的结果+2000Hz测试得到结果+500Hz测试得到结果）/3
        left = (fbl[0] + fbl[1] + fbl[4]) / 3.0
        right = (fbr[0] + fbr[1] + fbr[4]) / 3.0
        val df = DecimalFormat("##.00")

        val leftRank = computeRank(left)
        val rightRank = computeRank(right)
        // 结果
        // 听力等级
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
    fun toMainPager() {
        finish()
        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        startActivity(intent)
    }
}
