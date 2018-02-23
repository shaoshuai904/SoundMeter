package com.maple.audiometry.base;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * @author maple
 * @time 16/4/13
 */
public abstract class BaseFragment extends Fragment {
    public View view;
    public Context mContext;
    public FragmentManager fm;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mContext = getActivity();
        this.fm = getFragmentManager();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = initView(inflater);
        view.setClickable(true);
        return view;
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        initData(savedInstanceState);
        initListener();
        super.onActivityCreated(savedInstanceState);
    }


    public abstract View initView(LayoutInflater inflater);


    public abstract void initData(Bundle savedInstanceState);


    public abstract void initListener();


}
