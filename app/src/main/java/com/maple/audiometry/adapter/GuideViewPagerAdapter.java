package com.maple.audiometry.adapter;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;

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

    /**
     * 判断pager的一个view是否和instantiateItem方法返回的object有关联，并决定是否由对象生成界面
     */
    @Override
    public boolean isViewFromObject(View container, Object object) {
        return container == object;
    }

    /**
     * PagerAdapter适配器选择哪个对象
     */
    @Override
    public Object instantiateItem(View container, int position) {
        ((ViewPager) container).addView(Views.get(position));
        return Views.get(position);
    }


    @Override
    public void destroyItem(View container, int position, Object object) {
        ((ViewPager) container).removeView((View) object);
    }

}
