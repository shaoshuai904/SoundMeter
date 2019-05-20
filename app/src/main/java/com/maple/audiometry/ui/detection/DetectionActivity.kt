package com.maple.audiometry.ui.detection

import android.os.Bundle
import android.view.View

import com.maple.audiometry.R
import com.maple.audiometry.ui.base.BaseActivity
import kotlinx.android.synthetic.main.activity_base_top_bar.*

/**
 * 检测
 *
 * @author maple
 * @time 2019-05-20
 */
class DetectionActivity : BaseActivity() {
    var leftEarData = IntArray(6)
    var rightEarData = IntArray(6)
    var isLeft = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_base_fragment)

        addView(VoiceFragment())
    }

}
