package com.maple.audiometry.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.maple.audiometry.R;
import com.maple.audiometry.chat.MelodyView;

/**
 * 结果界面
 * 
 * @author shaoshuai
 * 
 */
public class ResultActivity extends Activity implements OnClickListener {
	@ViewInject(R.id.ll_left_result)
	private LinearLayout ll_left_result;// 左耳测试结果
	@ViewInject(R.id.ll_right_result)
	private LinearLayout ll_right_result;// 右耳测试结果
	@ViewInject(R.id.bt_look)
	private Button bt_look;// 查看结果

	private MelodyView mLeftEar;// 左耳检测结果
	private MelodyView mRightEar;// 左耳检测结果
	/** 左耳检测数据 */
	private int leftEarDatas[];
	/** 右耳检测数据 */
	private int rightEarDatas[];

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_result);
		ViewUtils.inject(this);

		Intent intent = getIntent();
		Bundle bundle = intent.getExtras();
		leftEarDatas = bundle.getIntArray("left");
		rightEarDatas = bundle.getIntArray("right");

		initView();

		bt_look.setOnClickListener(this);

	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.bt_look:// 查看结果
			toEndPager();
			break;

		default:
			break;
		}
	}

	/**
	 * 去结果页面
	 */
	private void toEndPager() {
		Intent intent = new Intent(ResultActivity.this, EndActivity.class);
		Bundle bundle = new Bundle();
		bundle.putIntArray("left", leftEarDatas);// 左耳听力数据
		bundle.putIntArray("right", rightEarDatas);// 右耳听力数据
		intent.putExtras(bundle);
		startActivity(intent);
	}

	/**
	 * 初始化折线图
	 */
	private void initView() {
		LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.MATCH_PARENT,
				LinearLayout.LayoutParams.MATCH_PARENT);
		// 初始化左耳折线图
		mLeftEar = new MelodyView(getApplicationContext(), MelodyView.ONLY_LEFT);
		mLeftEar.setYScope(120, -10);// 设置最大最小值
		ll_left_result.removeAllViews();
		ll_left_result.addView(mLeftEar, param);
		// 初始化左耳折线图
		mRightEar = new MelodyView(getApplicationContext(),
				MelodyView.ONLY_RIGHT);
		mRightEar.setYScope(120, -10);// 设置最大最小值
		ll_right_result.removeAllViews();
		ll_right_result.addView(mRightEar, param);
		initData();
	}

	/**
	 * 初始化检测数据
	 */
	private void initData() {
		// 更新左耳检测数据
		for (int i = 0; i < mLeftEar.getLeftDate().length; i++) {
			mLeftEar.updateData(i, leftEarDatas[i], true);// 更新折线图
		}
		// 更新右检测数据
		for (int i = 0; i < mRightEar.getRightDate().length; i++) {
			mRightEar.updateData(i, rightEarDatas[i], false);// 更新折线图
		}
	}

}
