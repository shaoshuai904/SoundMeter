package com.maple.audiometry.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;

import com.maple.audiometry.R;
import com.maple.audiometry.adapter.GuideViewPagerAdapter;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 引导界面
 *
 * @author shaoshuai
 */
public class SplashActivity extends Activity {
    @BindView(R.id.guide_viewpager) ViewPager guide_viewpager;// vp

    private Button guide_start_btn;// 进入按钮
    private ImageView[] guide_dot_iv;// 点集合
    private View guideView1, guideView2, guideView3;// 各个页面
    private ArrayList<View> guideViews;// 页面集合
    private GuideViewPagerAdapter guideViewPagerAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);
        ButterKnife.bind(this);

        initViews();
        initListeners();
    }

    /**
     * 初始化视图
     */
    @SuppressLint("InflateParams")
    private void initViews() {
        // 初始化点
        guide_dot_iv = new ImageView[3];
        guide_dot_iv[0] = (ImageView) findViewById(R.id.guide_dot1_iv);
        guide_dot_iv[1] = (ImageView) findViewById(R.id.guide_dot2_iv);
        guide_dot_iv[2] = (ImageView) findViewById(R.id.guide_dot3_iv);
        // 初始化页面
        guideView1 = LayoutInflater.from(this).inflate(
                R.layout.activity_guide_view1, null);
        guideView2 = LayoutInflater.from(this).inflate(
                R.layout.activity_guide_view2, null);
        guideView3 = LayoutInflater.from(this).inflate(
                R.layout.activity_guide_view3, null);
        guide_start_btn = (Button) guideView3.findViewById(R.id.next);
        // 添加页面
        guideViews = new ArrayList<View>();
        guideViews.add(guideView1);
        guideViews.add(guideView2);
        guideViews.add(guideView3);
        guideViewPagerAdapter = new GuideViewPagerAdapter(guideViews);
        guide_viewpager.setAdapter(guideViewPagerAdapter);
    }

    /**
     * 初始化监听
     */
    private void initListeners() {
        guide_start_btn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                goMainUi();
            }
        });

        guide_viewpager.setOnPageChangeListener(new OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                selectPage(position);
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {

            }

            @Override
            public void onPageScrollStateChanged(int arg0) {

            }
        });
    }

    /**
     * 前往主界面
     */
    private void goMainUi() {
        finish();
        // overridePendingTransition(android.R.anim.slide_in_left,
        // android.R.anim.slide_out_right);
        Intent intent = new Intent(SplashActivity.this, MainActivity.class);
        startActivity(intent);
    }

    /**
     * 浮点显示控制
     *
     * @param current
     */
    private void selectPage(int current) {
        for (int i = 0; i < guide_dot_iv.length; i++) {
            guide_dot_iv[current].setImageResource(R.drawable.guide_dot_pressed);
            if (current != i) {
                guide_dot_iv[i].setImageResource(R.drawable.guide_dot_normal);
            }
        }
    }

}
