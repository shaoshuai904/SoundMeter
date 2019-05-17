package com.maple.audiometry.ui.welcome;

import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

/**
 * 引导界面的适配器
 *
 * @author maple
 */
public class GuideViewPagerAdapter extends PagerAdapter {

    private ArrayList<View> Views;

    public GuideViewPagerAdapter(ArrayList<View> views) {
        this.Views = views;
    }

    @Override
    public int getCount() {
        return Views.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
        return view == o;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View view = Views.get(position);
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }
}
