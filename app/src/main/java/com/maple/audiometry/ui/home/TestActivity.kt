package com.maple.audiometry.ui.home

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import com.maple.audiometry.R
import com.maple.audiometry.ui.base.BaseActivity
import com.maple.audiometry.utils.ArrayUtils
import com.maple.audiometry.utils.AudioTrackManager
import com.maple.audiometry.utils.T
import com.maple.msdialog.AlertDialog
import kotlinx.android.synthetic.main.activity_test.*

/**
 * 测试界面
 *
 * @author shaoshuai
 */
class TestActivity : BaseActivity() {
    companion object {
        const val defCurDB = 10
        const val defCurHZ = 0
    }

    private val hzArr = intArrayOf(1000, 2000, 4000, 8000, 500, 250)
    private val dBArr = intArrayOf(-10, -5, 0, 5, 10, 15, 20, 25, 30, 35, 40, 45, 50, 55, 60, 65, 70, 75, 80, 85, 90, 95, 100, 105, 110, 115, 120)
    private var lDBMinVal = Array(6) { IntArray(2) }
    private var rDBMinVal = Array(6) { IntArray(2) }
    // 当前分贝索引
    var curDB = defCurDB
    // 当前频率索引
    var curHZ = defCurHZ
    // 是否第一次听到
    var isFirst = true
    // 是否测试左耳
    var isLeft = true
    // 左耳测试完成
    var leftCheckOver = false
    // 右耳测试完成
    var rightCheckOver = false

    var audio: AudioTrackManager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test)

        val bundle = intent.extras
        isLeft = bundle.getBoolean("isLeft")
        if (isLeft) {
            tv_explain.text = "左耳"
        } else {
            tv_explain.text = "右耳"
        }

        bt_play.setOnClickListener {
            start(hzArr[curHZ], dBArr[curDB], isLeft)
        }
        bt_yes.setOnClickListener { yesAction() }
        bt_no.setOnClickListener { clickNo() }

    }

    override fun onRestart() {
        if (audio != null)
            audio!!.stop()
        super.onRestart()
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.repeatCount == 0) {
            AlertDialog(this@TestActivity)
                    .setTitle("是否退出当前测试？")
                    .setLeftButton("取消", null)
                    .setRightButton("退出") { v -> finish() }
                    .show()
        }
        return false
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
        tv_current_db.text = "当前分贝:\n " + dBArr[curDB] + " dB"
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
            tv_current_db.text = "当前分贝:\n " + dBArr[curDB] + " dB"
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
                T.showShort(baseContext, "因为您的两次测试结果相差巨大，需要重测该频率")
                // 如果相差巨大，重新测试。
                lDBMinVal[curHZ][0] = 0
                lDBMinVal[curHZ][1] = 0
                curDB = defCurDB// 重置分贝
                isFirst = true// 又是第一次
                tv_play_des.text = hzArr[curHZ].toString() + " Hz\n" + dBArr[curDB] + " dB"
                tv_current_hz.text = "当前频率:\n " + hzArr[curHZ] + " Hz"
                tv_current_db.text = "当前分贝:\n " + dBArr[curDB] + " dB"

                // mMelodyView.updateData(curHZ, dBArr[curDB], isLeft);// 更新折线图
                // updateDB(hzArr[curHZ], dBArr[curDB]);
            } else {
                // 如果相差不大。进入下一频率
                if (curHZ < hzArr.size - 1) {
                    curHZ++// 更换频率
                    curDB = defCurDB// 重置分贝
                    isFirst = true// 又是第一次
                    tv_play_des.text = hzArr[curHZ].toString() + " Hz\n" + dBArr[curDB] + " dB"
                    tv_current_hz.text = "当前频率:\n " + hzArr[curHZ] + " Hz"
                    tv_current_db.text = "当前分贝:\n " + dBArr[curDB] + " dB"

                    // mMelodyView.updateData(curHZ, dBArr[curDB], isLeft);//
                    // 更新折线图
                    // updateDB(hzArr[curHZ], dBArr[curDB]);
                } else {
                    // 切换耳朵。或者进入结果页面
                    if (isLeft) {// 当前测试完成-左耳。
                        leftCheckOver = true
                        if (rightCheckOver) {
                            Log.e("TestActivity", "进入结果页面")
                            toResult()
                        } else {
                            checkRight()// 测试右耳
                        }
                    } else {// 当前测试完成-右耳
                        rightCheckOver = true// 标记右耳检测完毕
                        if (leftCheckOver) {
                            Log.e("TestActivity", "进入结果页面")
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
        tv_play_des.text = hzArr[curHZ].toString() + " Hz\n" + dBArr[curDB] + " dB"
        tv_current_hz.text = "当前频率:\n " + hzArr[curHZ] + " Hz"
        tv_current_db.text = "当前分贝:\n " + dBArr[curDB] + " dB"
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
        tv_play_des.text = hzArr[curHZ].toString() + " Hz\n" + dBArr[curDB] + " dB"
        tv_current_hz.text = "当前频率:\n " + hzArr[curHZ] + " Hz"
        tv_current_db.text = "当前分贝:\n " + dBArr[curDB] + " dB"
        tv_explain.text = "左耳"
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

        finish()
        val intent = Intent(this@TestActivity, ResultActivity::class.java)
        val bundle = Bundle()
        bundle.putIntArray("left", lDB)// 左耳听力数据
        bundle.putIntArray("right", rDB)// 右耳听力数据
        intent.putExtras(bundle)
        startActivity(intent)
    }

    /**
     * 播放声音
     *
     * @param hz     频率角标
     * @param isLeft 是否是左耳
     */
    fun start(hz: Int, db: Int, isLeft: Boolean) {
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
