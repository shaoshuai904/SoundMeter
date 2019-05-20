package com.maple.audiometry.ui.noise

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.view.KeyEvent
import android.widget.FrameLayout.LayoutParams
import com.maple.audiometry.R
import com.maple.audiometry.ui.base.BaseActivity
import com.maple.audiometry.ui.detection.DetectionActivity
import com.maple.audiometry.utils.ArrayUtils
import com.maple.audiometry.utils.MediaRecorderDemo
import com.maple.audiometry.utils.permission.RxPermissions
import com.maple.audiometry.view.BrokenLineView
import com.maple.msdialog.AlertDialog
import kotlinx.android.synthetic.main.activity_noise.*
import java.util.*

/**
 * 噪音检测
 *
 * @author shaoshuai
 */
class NoiseCheckActivity : BaseActivity() {
    companion object {
        /** 检测时间最大时间  */
        const val checkTime = 15 * 1000
        /** 更新噪音标志  */
        const val UPDATE_NOISE_VALUE = 1
    }

    /** 检测噪音的开始时间  */
    private var startTime: Long = 0
    /** 检测噪音工具类  */
    private var media: MediaRecorderDemo? = null
    private lateinit var mBrokenLine: BrokenLineView

    private var maxVolume = 0.0
    private var minVolume = 99990.0
    /** 检测到的所有噪音分贝值  */
    private val allVolume = ArrayList<Double>()
    /** 噪音分贝值 的说明文字  */
    private lateinit var dbExplain: Array<String>

    @SuppressLint("HandlerLeak")
    private val handler = object : Handler() {
        override fun handleMessage(msg: Message) {
            when (msg.what) {
                UPDATE_NOISE_VALUE// 更新噪音值
                -> {
                    val db = msg.obj as Double
                    val time = System.currentTimeMillis() - startTime
                    if (time >= checkTime) {// 检测完成
                        media!!.stopRecord()
                        showDialog()
                    }
                    mBrokenLine.updateDate(ArrayUtils.sub(allVolume, mBrokenLine.maxCacheNum))
                    updateNoise(db)
                }
                2// 进入下一个界面
                -> {
                }
            }
        }

    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_noise)

        mBrokenLine = BrokenLineView(this)
        ll_chart_view.addView(mBrokenLine.execute(), LayoutParams(
                LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT))

        dbExplain = resources.getStringArray(R.array.db_explain_arr)
        RxPermissions(this)
                .request(Manifest.permission.RECORD_AUDIO)
                .subscribe { b ->
                    if (b) {
                        handler.post(checkNoise)
                    }
                }
    }

    /**
     * 检测噪音
     */
    private var checkNoise: Runnable = Runnable {
        // 波动较大。用的较多
        media = MediaRecorderDemo { noiseValue ->
            val msg = Message.obtain()
            msg.what = UPDATE_NOISE_VALUE
            msg.obj = noiseValue
            handler.sendMessage(msg)
        }
        media!!.startRecord()
        startTime = System.currentTimeMillis()
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        // // 按下的如果是BACK，同时没有重复
        if (keyCode == KeyEvent.KEYCODE_BACK && event.repeatCount == 0) {
            AlertDialog(this@NoiseCheckActivity)
                    .setScaleWidth(0.7)
                    .setTitle("是否退出检测？")
                    .setLeftButton("取消", null)
                    .setRightButton("退出") { finish() }
                    .show()
        }
        return false
    }

    override fun onDestroy() {
        if (media != null) {
            media!!.stopRecord()// 停止检测
        }
        super.onDestroy()
    }

    /**
     * 去检查耳朵
     */
    private fun toCheckEar() {
        finish()
        val intent = Intent(this, DetectionActivity::class.java)
        startActivity(intent)
    }

    /**
     * 返回主界面
     */
    private fun toMainPager() {
        finish()
        // Intent intent = new Intent(NoiseCheckActivity.this,
        // MainActivity.class);
        // startActivity(intent);
    }

    /**
     * 更新噪音分贝值
     *
     * @param db
     */
    @SuppressLint("SetTextI18n")
    private fun updateNoise(db: Double) {
        // Log.e("", "noiseValue：" + db);
        // 更新当前值
        tv_noise_value.text = db.toInt().toString() + " dB"
        // 更新最大值
        if (db > maxVolume) {
            maxVolume = db
            tv_max_value.text = "最高分贝:\n " + maxVolume.toInt() + " dB"
        }
        // 更新最小值
        if (db < minVolume && db != 0.0) {
            minVolume = db
            tv_min_value.text = "最低分贝:\n " + minVolume.toInt() + " dB"
        }
        // 更新平均值
        if (db != 0.0) {
            allVolume.add(db)
            val avgVolume = ArrayUtils.avg(allVolume)
            tv_db_explain1.text = dbExplain[(avgVolume / 10).toInt()]
            tv_db_explain2.text = dbExplain[(avgVolume / 10).toInt() + 1]
            tv_avg_value.text = "平均分贝:\n " + avgVolume.toInt() + " dB"
        }
    }

    private fun showDialog() {
        // 平均噪音分贝 > 40dB
        if (ArrayUtils.avg(allVolume) > 40) {
            AlertDialog(mContext)
                    .setScaleWidth(0.7)
                    .setMessage("您的监测环境不适合后面的测试，请您到较安静的环境下测试。")
                    .setLeftButton("取消", null)
                    .setRightButton("重新检测") { toMainPager() }
                    .show()
        } else {
            AlertDialog(mContext)
                    .setScaleWidth(0.7)
                    .setMessage("您的测试环境良好，可以继续后面测试。")
                    .setLeftButton("取消", null)
                    .setRightButton("进入测试") { toCheckEar() }
                    .show()
        }
    }
}
