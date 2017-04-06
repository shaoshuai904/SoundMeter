package com.maple.audiometry.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.KeyEvent;
import android.view.View;

import com.maple.audiometry.R;
import com.maple.msdialog.AlertDialog;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 功能主界面
 *
 * @author shaoshuai
 */
public class MainActivity extends FragmentActivity {
//    @BindView(R.id.noise) Button noise;// 检测噪音
//    @BindView(R.id.voice) Button voice;// 纯音测试


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }

    // 去检测噪音
    @OnClick(R.id.noise)
    public void toCheckNoise() {
        Intent intent = new Intent(MainActivity.this, NoiseCheckActivity.class);
        startActivity(intent);
    }

    // 去检查耳朵
    @OnClick(R.id.voice)
    public void toCheckEar() {
        Intent intent = new Intent(MainActivity.this, VoiceActivity.class);
        startActivity(intent);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // // 按下的如果是BACK，同时没有重复
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            new AlertDialog(MainActivity.this)
                    .setTitle("是否退出？")
                    .setLeftButton("取消", null)
                    .setRightButton("退出", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            finish();
                        }
                    })
                    .show();
        }
        return false;
    }

}
