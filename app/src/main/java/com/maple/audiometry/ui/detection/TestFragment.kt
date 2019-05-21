package com.maple.audiometry.ui.detection

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.maple.audiometry.R
import com.maple.audiometry.ui.base.BaseFragment
import com.maple.audiometry.utils.ArrayUtils
import com.maple.audiometry.utils.AudioTrackManager
import com.maple.audiometry.utils.T
import com.maple.msdialog.AlertDialog
import kotlinx.android.synthetic.main.activity_base_top_bar.*
import kotlinx.android.synthetic.main.fragment_test.*

/**
 * 测试界面
 *
 * @author shaoshuai
 */
class TestFragment : BaseFragment() {
    companion object {
        const val defCurDB = 10
        const val defCurHZ = 0
    }

    private val hzArr = intArrayOf(1000, 2000, 4000, 8000, 500, 250)
    private val dBArr = intArrayOf(-10, -5, 0, 5, 10, 15, 20, 25, 30, 35, 40, 45, 50, 55, 60, 65, 70, 75, 80, 85, 90, 95, 100, 105, 110, 115, 120)
    private var lDBMinVal = Array(6) { IntArray(2) }
    private var rDBMinVal = Array(6) { IntArray(2) }
    private var curDB = defCurDB// 当前分贝索引
    private var curHZ = defCurHZ// 当前频率索引
    private var isFirst = true// 是否第一次听到
    private var isLeft = true// 是否测试左耳
    private var leftCheckOver = false// 左耳测试完成
    private var rightCheckOver = false// 右耳测试完成

    private lateinit var mActivity: DetectionActivity
    private var audio: AudioTrackManager? = null


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val view = inflater.inflate(R.layout.fragment_test, container, false)
        view.isClickable = true
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        mActivity = activity as DetectionActivity

        tv_left.setOnClickListener { mActivity.onBackPressed() }
        tv_title.text = "测试"

        isLeft = mActivity.isLeft

        if (isLeft) {
            tv_explain.text = "左耳"
        } else {
            tv_explain.text = "右耳"
        }

        bt_play.setOnClickListener {
            startPlay(hzArr[curHZ], dBArr[curDB], isLeft)
            toResult()
        }
        bt_yes.setOnClickListener { yesAction() }
        bt_no.setOnClickListener { clickNo() }

    }

    override fun onResume() {
        super.onResume()
        if (audio != null)
            audio!!.stop()
    }

    override fun onKeyBackPressed(): Boolean {
        AlertDialog(mContext)
                .setTitle("是否退出当前测试？")
                .setLeftButton("取消", null)
                .setRightButton("退出") { mActivity.backFragment() }
                .show()
        return true
    }

    private fun clickNo() {
        if (isLeft) {
            if (lDBMinVal[curHZ][0] != 0) {
                isFirst = false// 不是第一次了
            }
        } else {
            if (rDBMinVal[curHZ][0] != 0) {
                isFirst = false// 不是第一次了
            }
        }
        curDB = revise(curDB + 1, 0, dBArr.size - 1)
        tv_current_db.text = "当前分贝:\n ${dBArr[curDB]} dB"
        // updateDB(hzArr[curHZ], dBArr[curDB]);
    }

    /**
     * 听到-执行动作
     */
    private fun yesAction() {
        if (isFirst) {// 第一次
            if (isLeft) {
                lDBMinVal[curHZ][0] = dBArr[curDB]// 保存左耳当前频率的第一次听到数据
            } else {
                rDBMinVal[curHZ][0] = dBArr[curDB]// 保存左耳当前频率的第一次听到数据
            }
            curDB = revise(curDB - 2, 0, dBArr.size - 1)// 更换分贝 （ 减10分贝）
            tv_current_db.text = "当前分贝:\n ${dBArr[curDB]} dB"
            // updateDB(hzArr[curHZ], dBArr[curDB]);
        } else {// 第二次测试左耳最低值
            var sub = 0
            if (isLeft) {
                lDBMinVal[curHZ][1] = dBArr[curDB]// 保存当前频率的第二次听到数据
                // 同第一次做对比。
                sub = lDBMinVal[curHZ][1] - lDBMinVal[curHZ][0]
                Log.e("计算左耳差值", lDBMinVal[curHZ][0].toString() + "减去"
                        + lDBMinVal[curHZ][1] + " = " + sub)
                // 取两次平均值，更新视图
                // mMelodyView.updateData(curHZ,
                // ArrayUtils.avg(lDBMinVal[curHZ]),
                // isLeft);// 更新折线图
            } else {
                // Log.e("左耳第二次听到", hzArr[curHZ] + "Hz记录:" + dBArr[curDB]);
                rDBMinVal[curHZ][1] = dBArr[curDB]// 保存当前频率的第二次听到数据
                // 同第一次做对比。
                sub = rDBMinVal[curHZ][1] - rDBMinVal[curHZ][0]
                // Log.e("计算右耳差值", rDBMinVal[curHZ][0] + "减去"
                // + rDBMinVal[curHZ][1] + " = " + sub);
                // 取两次平均值，更新视图
                // mMelodyView.updateData(curHZ,
                // ArrayUtils.avg(rDBMinVal[curHZ]),
                // isLeft);// 更新折线图
            }
            if (Math.abs(sub) > 20) {
                T.showShort(mContext, "因为您的两次测试结果相差巨大，需要重测该频率")
                // 如果相差巨大，重新测试。
                lDBMinVal[curHZ][0] = 0
                lDBMinVal[curHZ][1] = 0
                curDB = defCurDB// 重置分贝
                isFirst = true// 又是第一次
                updateDes()

                // mMelodyView.updateData(curHZ, dBArr[curDB], isLeft);// 更新折线图
                // updateDB(hzArr[curHZ], dBArr[curDB]);
            } else {
                // 如果相差不大。进入下一频率
                if (curHZ < hzArr.size - 1) {
                    curHZ++// 更换频率
                    curDB = defCurDB// 重置分贝
                    isFirst = true// 又是第一次
                    updateDes()

                    // mMelodyView.updateData(curHZ, dBArr[curDB], isLeft);//
                    // 更新折线图
                    // updateDB(hzArr[curHZ], dBArr[curDB]);
                } else {
                    // 切换耳朵。或者进入结果页面
                    if (isLeft) {// 当前测试完成-左耳。
                        leftCheckOver = true
                        if (rightCheckOver) {
                            toResult()
                        } else {
                            checkRight()// 测试右耳
                        }
                    } else {// 当前测试完成-右耳
                        rightCheckOver = true// 标记右耳检测完毕
                        if (leftCheckOver) {
                            toResult()
                        } else {
                            checkLeft()// 测试左耳
                        }
                    }
                }
            }
        }
    }

    /**
     * 检测右耳
     */
    private fun checkRight() {
        isLeft = false
        isFirst = true
        rDBMinVal = Array(6) { IntArray(2) }
        curDB = defCurDB
        curHZ = defCurHZ
        updateDes()
        tv_explain.text = "右耳"
    }

    /**
     * 检查左耳
     */
    private fun checkLeft() {
        isLeft = true
        isFirst = true
        lDBMinVal = Array(6) { IntArray(2) }
        curDB = defCurDB
        curHZ = defCurHZ
        updateDes()
        tv_explain.text = "左耳"
    }

    @SuppressLint("SetTextI18n")
    private fun updateDes() {
        tv_play_des.text = "${hzArr[curHZ]} Hz\n${dBArr[curDB]} dB"
        tv_current_hz.text = "当前频率:\n ${hzArr[curHZ]} Hz"
        tv_current_db.text = "当前分贝:\n ${dBArr[curDB]} dB"
    }

    /**
     * 去结果页面
     */
    private fun toResult() {
        val lDB = IntArray(6)
        val rDB = IntArray(6)
        for (i in lDB.indices) {
            lDB[i] = ArrayUtils.avg(lDBMinVal[i])
            rDB[i] = ArrayUtils.avg(rDBMinVal[i])
        }

        mActivity.leftEarData = lDB
        mActivity.rightEarData = rDB
        mActivity.replaceView(ResultFragment())
    }

    /**
     * 播放声音
     *
     * @param hz     频率角标
     * @param isLeft 是否是左耳
     */
    private fun startPlay(hz: Int, db: Int, isLeft: Boolean) {
        if (audio != null)
            audio!!.stop()
        audio = AudioTrackManager()
        audio!!.setRate(hz, db)// 设置频率
        if (isLeft) {
            audio!!.start(AudioTrackManager.LEFT)
        } else {
            audio!!.start(AudioTrackManager.RIGHT)
        }
        audio!!.play()
    }

    /**
     * 修正值，防止越界
     *
     * @param min
     * @param max
     */
    private fun revise(num: Int, min: Int, max: Int): Int {
        var num = num
        if (num < min) {
            num = min
        }
        if (num > max) {
            num = max
        }
        return num
    }

}
