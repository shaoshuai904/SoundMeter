package com.maple.audiometry.ui.noise

import android.os.Bundle
import com.maple.audiometry.R
import com.maple.audiometry.ui.base.BaseActivity

/**
 * 噪音检测
 *
 * @author shaoshuai
 */
class NoiseCheckActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_base_fragment)

        addView(NoiseCheckFragment())
    }

}
