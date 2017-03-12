package com.maple.audiometry.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.KeyEvent;
import android.widget.Button;

import com.maple.audiometry.R;
import com.maple.audiometry.dialog.TwoButtonDialog;
import com.maple.audiometry.dialog.TwoButtonDialog.LoginInputListener;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 功能主界面
 *
 * @author shaoshuai
 */
public class MainActivity extends FragmentActivity implements LoginInputListener {
//    @BindView(R.id.noise) Button noise;// 检测噪音
//    @BindView(R.id.voice) Button voice;// 纯音测试

    private TwoButtonDialog dia;
    private FragmentManager fm = getSupportFragmentManager();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // // 按下的如果是BACK，同时没有重复
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            dia = new TwoButtonDialog("是否退出？", "退出", "取消");
            dia.show(fm, "EXIT");
        }
        return false;
    }

    @Override
    public void onLoginInputComplete(String message) {
        if (message.equals("退出")) {
            finish(); // 退出
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    /**
     * 去检测噪音
     */
    @OnClick(R.id.noise)
    public void toCheckNoise() {
        Intent intent = new Intent(MainActivity.this, NoiseCheckActivity.class);
        startActivity(intent);
    }

    /**
     * 去检查耳朵
     */
    @OnClick(R.id.voice)
    public void toCheckEar() {
        Intent intent = new Intent(MainActivity.this, VoiceActivity.class);
        startActivity(intent);
    }

}
