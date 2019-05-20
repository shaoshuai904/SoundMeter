package com.maple.audiometry.ui.home

import android.content.Intent
import android.os.Bundle
import com.maple.audiometry.R
import com.maple.audiometry.ui.base.BaseActivity
import com.maple.audiometry.ui.detection.DetectionActivity
import com.maple.audiometry.ui.noise.NoiseCheckActivity
import com.maple.msdialog.AlertDialog
import kotlinx.android.synthetic.main.activity_main.*

/**
 * 功能主界面
 *
 * @author maple
 */
class MainActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        bt_noise.setOnClickListener { toCheckNoise() }
        bt_voice.setOnClickListener { toCheckEar() }

    }

    // 去检测噪音
    private fun toCheckNoise() {
        val intent = Intent(this, NoiseCheckActivity::class.java)
        startActivity(intent)
    }

    // 去检查耳朵
    private fun toCheckEar() {
        val intent = Intent(this, DetectionActivity::class.java)
        startActivity(intent)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        AlertDialog(this)
                .setScaleWidth(0.7)
                .setTitle("是否退出？")
                .setLeftButton("取消", null)
                .setRightButton("退出") { finish() }
                .show()
    }

}
