package com.maple.audiometry.ui.home

import android.content.Intent
import android.os.Bundle
import com.maple.audiometry.R
import com.maple.audiometry.ui.base.BaseActivity
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
    fun toCheckNoise() {
        val intent = Intent(this, NoiseCheckActivity::class.java)
        startActivity(intent)
    }

    // 去检查耳朵
    fun toCheckEar() {
        val intent = Intent(this, VoiceActivity::class.java)
        startActivity(intent)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        AlertDialog(this)
                .setScaleWidth(0.7)
                .setTitle("是否退出？")
                .setLeftButton("取消", null)
                .setRightButton("退出") { v -> finish() }
                .show()
    }


}
