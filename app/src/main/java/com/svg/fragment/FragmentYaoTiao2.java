package com.svg.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.svg.R;

import java.util.ArrayList;
import java.util.List;

/**
 * 一般用户查看的遥调Fragment
 * Created by aawang on 2017/3/24.
 */
public class FragmentYaoTiao2 extends Fragment  {
    private TabLayout mTab;
    // tab标题
    private List<String> mTitles = new ArrayList<>();
    private FragmentManager fragmentManager;
//    private FragmentYaotiaoData2_1 fg1;
//    private FragmentYaotiaoData2_2 fg2;
    private FragmentKongzhi fg3;
    private int position = 0;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_yaotiao, container, false);
        init(view);
        return view;
    }

    /**
     * 初始化控件
     * @param view
     */
    private void init(View view){
        mTab = (TabLayout) view.findViewById(R.id.mTabYaotiao);
        fragmentManager = getActivity().getSupportFragmentManager();

        // 设置界面的默认值
        setDefaultInfo();
    }
    /**
     * 设置界面的默认数据
     */
    private void setDefaultInfo(){
        // tab标题栏添加文字
//        mTitles.add("数据1");
//        mTitles.add("数据2");
        mTitles.add("控制");
        //设置tablayout模式
        mTab.setTabMode(TabLayout.MODE_FIXED);
        //tablayout获取集合中的名称
        mTab.addTab(mTab.newTab().setText(mTitles.get(0)));
//        mTab.addTab(mTab.newTab().setText(mTitles.get(1)));
//        mTab.addTab(mTab.newTab().setText(mTitles.get(2)));
        setChioceItem(0);
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
//                if (null == fg1) {
//                    fg1 = new FragmentYaotiaoData2_1();
//                    fragmentTransaction.add(R.id.yaotiao_fragment_container, fg1);
//                } else {
//                    // 如果不为空，则直接将它显示出来
//                    fragmentTransaction.show(fg1);
//                }
//                break;
//            case 1:
//                if (null == fg2) {
//                    fg2 = new FragmentYaotiaoData2_2();
//                    fragmentTransaction.add(R.id.yaotiao_fragment_container, fg2);
//                } else {
//                    fragmentTransaction.show(fg2);
//                }
//                break;
//            case 2:
                if (null == fg3) {
                    fg3 = new FragmentKongzhi();
                    fragmentTransaction.add(R.id.yaotiao_fragment_container, fg3);
                } else {
                    fragmentTransaction.show(fg3);
                }
                break;
        }
        fragmentTransaction.commit(); // 提交
    }

    /**
     * 隐藏所以的fragment
     * @param fragmentTransaction
     */
    private void hideFragments(FragmentTransaction fragmentTransaction) {
//        if (null != fg1) {
//            fragmentTransaction.hide(fg1);
//        }
//        if (null != fg2) {
//            fragmentTransaction.hide(fg2);
//        }
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
//        Log.d("fragment_yaotiao", "onResume");
//    }
}
