package com.svg.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.flyco.tablayout.SlidingTabLayout;
import com.svg.R;
import com.svg.adapter.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * 遥测Fragment
 * Created by aawang on 2017/3/24.
 */
public class FragmentYaoce extends Fragment{
    private TabLayout mTab;
    // tab标题
    private List<String> mTitles = new ArrayList<>();
    private FragmentManager fragmentManager;
    private FragmentYaoceData1 fg1;
    private FragmentYaoceData2 fg2;
    private FragmentYaoceData3 fg3;
    private int position = 0;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_yaoce, container, false);
        init(view);
        return view;
    }

    /**
     * 初始化控件
     * @param view
     */
    private void init(View view){
        mTab = (TabLayout) view.findViewById(R.id.mTab);
        fragmentManager = getActivity().getSupportFragmentManager();

        // 设置界面的默认值
        setDefaultInfo();
    }
    /**
     * 设置界面的默认数据
     */
    private void setDefaultInfo(){
        // tab标题栏添加文字
        mTitles.add("数据1");
        mTitles.add("数据2");
        mTitles.add("数据3");
        //设置tablayout模式
        mTab.setTabMode(TabLayout.MODE_FIXED);
        //tablayout获取集合中的名称
        mTab.addTab(mTab.newTab().setText(mTitles.get(0)));
        mTab.addTab(mTab.newTab().setText(mTitles.get(1)));
        mTab.addTab(mTab.newTab().setText(mTitles.get(2)));
        setChioceItem(position);
        mTab.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                position = tab.getPosition();
                setChioceItem(position);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    /**
     * 动态设置显示或隐藏fragment
     * @param index
     */
    private void setChioceItem(int index) {
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        hideFragments(fragmentTransaction);
        switch (index) {
            case 0:
                if (null == fg1) {
                    fg1 = new FragmentYaoceData1();
                    fragmentTransaction.add(R.id.yaoce_fragment_container, fg1);
                } else {
                    // 如果不为空，则直接将它显示出来
                    fragmentTransaction.show(fg1);
                }
                break;
            case 1:
                if (null == fg2) {
                    fg2 = new FragmentYaoceData2();
                    fragmentTransaction.add(R.id.yaoce_fragment_container, fg2);
                } else {
                    fragmentTransaction.show(fg2);
                }
                break;
            case 2:
                if (null == fg3) {
                    fg3 = new FragmentYaoceData3();
                    fragmentTransaction.add(R.id.yaoce_fragment_container, fg3);
                } else {
                    fragmentTransaction.show(fg3);
                }
                break;
        }
        fragmentTransaction.commit(); // 提交
    }

    /**
     * 隐藏所有的fragment
     * @param fragmentTransaction
     */
    private void hideFragments(FragmentTransaction fragmentTransaction) {
        if (null != fg1) {
            fragmentTransaction.hide(fg1);
        }
        if (null != fg2) {
            fragmentTransaction.hide(fg2);
        }
        if (null != fg3) {
            fragmentTransaction.hide(fg3);
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if(!hidden){
            setChioceItem(position);
        } else {
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            hideFragments(fragmentTransaction);
            fragmentTransaction.commit(); // 提交
        }
    }

//    @Override
//    public void onResume() {
//        super.onResume();
//        setChioceItem(position);
//    }
}
