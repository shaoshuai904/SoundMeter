package com.maple.audiometry.ui.detection

import android.content.Context
import android.media.AudioManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.maple.audiometry.R
import com.maple.audiometry.ui.base.BaseFragment
import com.maple.audiometry.utils.AudioTrackManager
import kotlinx.android.synthetic.main.fragment_voice.*

/**
 * 左耳右耳检测界面
 *
 * @author shaoshuai
 */
class VoiceFragment : BaseFragment() {
    var audio: AudioTrackManager? = null
    var volume = 0F

    private lateinit var mActivity: DetectionActivity

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val view = inflater.inflate(R.layout.fragment_voice, container, false)
        view.isClickable = true
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        mActivity = activity as DetectionActivity

        val mAudioManager = mActivity.getSystemService(Context.AUDIO_SERVICE) as AudioManager
        volume = mAudioManager.getStreamVolume(AudioManager.STREAM_SYSTEM).toFloat()// 获取系统音量
        AudioTrackManager.setVolume(volume)

        bt_left.setOnClickListener { toTestPager(true) }// 左耳
        bt_right.setOnClickListener { toTestPager(false) }// 右耳
        bt_test.setOnClickListener { MyThread().start() }// 测试设备
    }

    override fun onResume() {
        super.onResume()
        audio?.stop()
    }

    inner class MyThread : Thread() {
        override fun run() {
            try {
                // 左耳
                startPlay(1000, 50, true)
                sleep(600)
                startPlay(1000, 50, true)
                sleep(800)
                startPlay(1000, 50, true)
                sleep(1000)
                // 右耳
                startPlay(1000, 50, false)
                sleep(600)
                startPlay(1000, 50, false)
                sleep(800)
                startPlay(1000, 50, false)
            } catch (e: InterruptedException) {
                e.printStackTrace()
            }
        }
    }

    /**
     * 播放声音
     *
     * @param hz     频率角标
     * @param isLeft 是否是左耳
     */
    fun startPlay(hz: Int, db: Int, isLeft: Boolean) {
        audio?.stop()
        audio = AudioTrackManager()
        audio?.setRate(hz, db)// 设置频率
        if (isLeft) {
            audio?.start(AudioTrackManager.LEFT)
        } else {
            audio?.start(AudioTrackManager.RIGHT)
        }
        audio?.play()
    }

    /**
     * 前往测试界面
     */
    private fun toTestPager(isLeft: Boolean) {
        mActivity.isLeft = isLeft
        mActivity.replaceView(TestFragment())
    }
}
