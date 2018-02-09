package com.maple.audiometry.base;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;


/**
 * @author maple
 * @time 16/4/13
 */
public class BaseFragmentActivity extends FragmentActivity {
//    public RelativeLayout rl_top_bar;
//    public TextView tv_left_title;
//    public TextView tv_title;
//    public TextView tv_right_title;
//    public LinearLayout ll_root;

    public Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);// 保持竖屏

//        setContentView(R.layout.activity_base_fragment);
        mContext = getBaseContext();

        findView();
    }


    private void findView() {
//        rl_top_bar = (RelativeLayout) findViewById(R.id.rl_top_bar);
//        tv_left_title = (TextView) findViewById(R.id.tv_left_title);
//        tv_right_title = (TextView) findViewById(R.id.tv_right_title);
//        tv_title = (TextView) findViewById(R.id.tv_title);
//        ll_root = (LinearLayout) findViewById(R.id.ll_root);
    }

//    public void setBaseContentView(int layoutID) {
//        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        inflater.inflate(layoutID, ll_root);
//    }
//
//    public void isShowTopBar(boolean isShow) {
//        if (isShow) {
//            rl_top_bar.setVisibility(View.VISIBLE);
//        } else {
//            rl_top_bar.setVisibility(View.GONE);
//        }
//
//    }

    // ------------------ fragment ------------------

//    public void addView(Fragment fgView) {
//        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
//        ft.add(R.id.fl_content, fgView).commit();
//    }
//
//    public void replaceView(Fragment fgView) {
//        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
//        ft.replace(R.id.fl_content, fgView);
//        ft.addToBackStack(null);
//        ft.commit();
//    }
    // ----------- left right button ------------------

//    public void setTitle(String str) {
//        tv_title.setText(str);
//    }
//
//    public void setLeftBtnClickListener(View.OnClickListener listener) {
//        tv_left_title.setOnClickListener(listener);
//    }
//
//    public void setRightBtnClickListener(View.OnClickListener listener) {
//        tv_right_title.setOnClickListener(listener);
//    }
//
//    public void setLeftBtnState(String str, int visibility, boolean isEnabled) {
//        tv_left_title.setText(str);
//        setLeftBtnState(visibility, isEnabled);
//    }
//
//    public void setLeftBtnState(int visibility, boolean isEnabled) {
//        tv_left_title.setVisibility(visibility);
//        tv_left_title.setEnabled(isEnabled);
//    }
//
//    public void setRightBtnState(String str, int visibility, boolean isEnabled) {
//        tv_right_title.setText(str);
//        setRightBtnState(visibility, isEnabled);
//    }
//
//    public void setRightBtnState(int visibility, boolean isEnabled) {
//        tv_right_title.setVisibility(visibility);
//        tv_right_title.setEnabled(isEnabled);
//    }
}
