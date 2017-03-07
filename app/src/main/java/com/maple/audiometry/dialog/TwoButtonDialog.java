package com.maple.audiometry.dialog;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.maple.audiometry.R;
import com.maple.audiometry.utils.ScreenUtils;

/**
 * 两个按钮的DiaLog
 * 
 * @author shao
 * 
 */
public class TwoButtonDialog extends DialogFragment implements OnClickListener {
	private View dialogFrame;

	private TextView mMessage; // 内容
	private Button mLeftBtn; // 确定
	private Button mRightBtn; // 取消

	private String mTitle; // 标题
	private String leftStr = "确定";// 左按钮说明文字
	private String rightStr = "取消";// 右按钮说明文字

	public TwoButtonDialog(String title) {
		mTitle = title;
	}

	public TwoButtonDialog(String title, String leftStr, String rightStr) {
		this.mTitle = title;
		this.leftStr = leftStr;
		this.rightStr = rightStr;
	}

	/**
	 * 数据回调接口
	 */
	public interface LoginInputListener {
		void onLoginInputComplete(String message);
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		Dialog dialog = new Dialog(getActivity(), R.style.dialogTheme);

		LayoutInflater inflater = getActivity().getLayoutInflater();
		View exitView = inflater.inflate(R.layout.dialog_exit, null);
		dialog.setContentView(exitView);

		dialogFrame = exitView.findViewById(R.id.dialogFrame);

		mMessage = (TextView) exitView.findViewById(R.id.tv_message);
		mLeftBtn = (Button) exitView.findViewById(R.id.bt_left);
		mRightBtn = (Button) exitView.findViewById(R.id.bt_right);

		int size = ScreenUtils.getScreenWidth(getActivity());
		ViewGroup.LayoutParams lp = dialogFrame.getLayoutParams();
		lp.width = size * 3 / 4; // 设置宽度
		dialogFrame.setLayoutParams(lp);

		mMessage.setText(mTitle);
		mLeftBtn.setText(leftStr);
		mRightBtn.setText(rightStr);

		mLeftBtn.setOnClickListener(this);
		mRightBtn.setOnClickListener(this);

		return dialog;
	}

	@Override
	public void onClick(View v) {
		if (v == mLeftBtn) {// 点击确定
			dismiss();
			LoginInputListener listener = (LoginInputListener) getActivity();
			listener.onLoginInputComplete(leftStr);
			// android.os.Process.killProcess(android.os.Process.myPid());
		} else if (v == mRightBtn) {
			dismiss();
		}
	}
}