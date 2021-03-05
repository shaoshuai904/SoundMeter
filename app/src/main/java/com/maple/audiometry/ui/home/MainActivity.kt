package com.maple.audiometry.ui.home

import android.content.Intent
import android.os.Bundle
import com.maple.audiometry.R
import com.maple.audiometry.ui.base.BaseFragmentActivity
import com.maple.audiometry.ui.detection.DetectionActivity
import com.maple.audiometry.ui.noise.NoiseCheckActivity
import com.maple.audiometry.utils.T
import kotlinx.android.synthetic.main.activity_main.*

/**
 * 功能主界面
 *
 * @author maple
 */
class MainActivity : BaseFragmentActivity() {

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

    private var exitTime: Long = 0
    override fun onBackPressed() {
        if (System.currentTimeMillis() - exitTime > 2000) {
            T.showShort(mContext, "再按一次退出程序")
            exitTime = System.currentTimeMillis()
        } else {
            finish()
        }
    }

}
