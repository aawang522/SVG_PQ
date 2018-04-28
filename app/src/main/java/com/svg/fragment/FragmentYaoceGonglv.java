package com.svg.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.svg.R;

/**
 * 待办Fragment
 * Created by aawang on 2017/3/24.
 */
public class FragmentYaoceGonglv extends Fragment  {

    private String mTitle;

    public static FragmentYaoceGonglv getInstance(String title) {
        FragmentYaoceGonglv sf = new FragmentYaoceGonglv();
        sf.mTitle = title;
        return sf;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_yaoce_gonglv, container, false);
        init(view);
        Log.d("fragment_gonglv", "onCreateView");
        return view;
    }

    /**
     * 初始化控件
     * @param view
     */
    private void init(View view){

    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if(!hidden){
            Log.d("fragment_gonglv", "onHiddenChanged_show");
        } else {
            Log.d("fragment_gonglv", "onHiddenChanged_hide");
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d("fragment_gonglv", "onResume");
    }
}
