package com.maple.audiometry.ui.activity;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;

import com.maple.audiometry.R;
import com.maple.audiometry.base.BaseFragmentActivity;
import com.maple.audiometry.utils.permission.PermissionFragment;
import com.maple.msdialog.AlertDialog;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 功能主界面
 *
 * @author shaoshuai
 */
public class MainActivity extends BaseFragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);


        PermissionFragment.getPermissionFragment(this)
                .checkPermissions(new String[]{
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.RECORD_AUDIO
                }, null);
    }

    // 去检测噪音
    @OnClick(R.id.noise)
    public void toCheckNoise() {
        Intent intent = new Intent(this, NoiseCheckActivity.class);
        startActivity(intent);
    }

    // 去检查耳朵
    @OnClick(R.id.voice)
    public void toCheckEar() {
        Intent intent = new Intent(this, VoiceActivity.class);
        startActivity(intent);
    }
    // 1024398
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            new AlertDialog(this)
                    .setScaleWidth(0.7)
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
