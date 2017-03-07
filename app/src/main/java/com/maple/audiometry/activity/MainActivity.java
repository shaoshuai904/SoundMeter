package com.maple.audiometry.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.maple.audiometry.R;
import com.maple.audiometry.dialog.TwoButtonDialog;
import com.maple.audiometry.dialog.TwoButtonDialog.LoginInputListener;

/**
 * 功能主界面
 * 
 * @author shaoshuai
 * 
 */
public class MainActivity extends FragmentActivity implements OnClickListener,
		LoginInputListener {
	@ViewInject(R.id.noise)
	private Button noise;// 检测噪音
	@ViewInject(R.id.voice)
	private Button voice;// 纯音测试

	private TwoButtonDialog dia;
	private FragmentManager fm = getSupportFragmentManager();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		ViewUtils.inject(this);

		noise.setOnClickListener(this);
		voice.setOnClickListener(this);

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

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.noise:
			toCheckNoise();// 检测噪音
			break;
		case R.id.voice:
			toCheckEar();// 检测耳朵
			break;
		default:
			break;
		}
	}

	/**
	 * 去检测噪音
	 */
	private void toCheckNoise() {
		Intent intent = new Intent(MainActivity.this, NoiseCheckActivity.class);
		startActivity(intent);
	}

	/**
	 * 去检查耳朵
	 */
	private void toCheckEar() {
		Intent intent = new Intent(MainActivity.this, VoiceActivity.class);
		startActivity(intent);
	}

}
