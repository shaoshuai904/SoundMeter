package com.maple.audiometry.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.maple.audiometry.R;
import com.maple.audiometry.base.BaseFragmentActivity;
import com.maple.audiometry.utils.AudioTrackManager;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 左耳右耳检测界面
 *
 * @author shaoshuai
 */
public class VoiceActivity extends BaseFragmentActivity implements OnClickListener {
    @BindView(R.id.bt_test) Button bt_test;
    @BindView(R.id.bt_left) Button bt_left;// 左耳检测
    @BindView(R.id.bt_right) Button bt_right;// 右耳检测

    AudioTrackManager audio;
    float volume;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voice);
        ButterKnife.bind(this);

        AudioManager mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        volume = mAudioManager.getStreamVolume(AudioManager.STREAM_SYSTEM);// 获取系统音量
        AudioTrackManager.setVolume(volume);

        bt_left.setOnClickListener(this);
        bt_right.setOnClickListener(this);
        bt_test.setOnClickListener(this);
    }

    @Override
    protected void onRestart() {
        if (audio != null)
            audio.stop();
        super.onRestart();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bt_left:// 左耳
                toTestPager(true);
                break;
            case R.id.bt_right:// 右耳
                toTestPager(false);
                break;
            case R.id.bt_test:// 测试设备
                new MyThread().start();
                break;
            default:
                break;
        }
    }

    class MyThread extends Thread {
        public void run() {
            try {
                // 左耳
                VoiceActivity.this.start(1000, 50, true);
                Thread.sleep(600);
                VoiceActivity.this.start(1000, 50, true);
                Thread.sleep(800);
                VoiceActivity.this.start(1000, 50, true);

                Thread.sleep(1000);
                // 右耳
                VoiceActivity.this.start(1000, 50, false);
                Thread.sleep(600);
                VoiceActivity.this.start(1000, 50, false);
                Thread.sleep(800);
                VoiceActivity.this.start(1000, 50, false);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 播放声音
     *
     * @param hz     频率角标
     * @param isLeft 是否是左耳
     */
    public void start(int hz, int db, boolean isLeft) {
        if (audio != null)
            audio.stop();
        audio = new AudioTrackManager();
        audio.setRate(hz, db);// 设置频率
        if (isLeft) {
            audio.start(AudioTrackManager.LEFT);
        } else {
            audio.start(AudioTrackManager.RIGHT);
        }
        audio.play();
    }

    /**
     * 前往测试界面
     */
    private void toTestPager(boolean isLeft) {
        Intent intent = new Intent(this, TestActivity.class);
        Bundle bundle = new Bundle();
        bundle.putBoolean("isLeft", isLeft);
        intent.putExtras(bundle);
        startActivity(intent);
    }
}
