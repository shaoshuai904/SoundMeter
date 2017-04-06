package com.maple.audiometry.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.maple.audiometry.R;
import com.maple.audiometry.utils.AudioTrackManager;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 左耳右耳检测界面
 *
 * @author shaoshuai
 */
public class VoiceActivity extends Activity implements OnClickListener {
    @BindView(R.id.bt_left) Button bt_left;// 左耳检测
    @BindView(R.id.bt_right) Button bt_right;// 右耳检测
    @BindView(R.id.bt_test) Button bt_test;// 下一步

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
     * @param isleft 是否是左耳
     */
    public void start(int hz, int db, boolean isleft) {
        if (audio != null)
            audio.stop();
        audio = new AudioTrackManager();
        audio.setRate(hz, db);// 设置频率
        if (isleft) {
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
        Intent intent = new Intent(VoiceActivity.this, TestActivity.class);
        Bundle bundle = new Bundle();// 封装数据 bundle 捆 类似于Map
        bundle.putBoolean("isLeft", isLeft);// 音量得分
        intent.putExtras(bundle); // 发送数据 extra 附加物
        startActivity(intent);
    }
}
