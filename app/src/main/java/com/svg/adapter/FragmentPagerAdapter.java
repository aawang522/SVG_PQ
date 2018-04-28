package com.svg.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import java.util.List;

/**
 * 工单详情的fragmentAdapter
 * 这里将FragmentPagerAdapter改为FragmentStatePagerAdapter，避免fragment被回收
 * Created by aawang on 2017/3/24.
 */

public class FragmentPagerAdapter extends android.support.v4.app.FragmentPagerAdapter {

    private List<String> mTitles;
//    Fragment fragment=null;
    private List<Fragment> fragmentList;

    public FragmentPagerAdapter(FragmentManager fm, List<String> mTitles, List<Fragment> fragmentList) {
        super(fm);
        this.mTitles = mTitles;
        this.fragmentList = fragmentList;
    }

    @Override
    public Fragment getItem(int position) {
        return fragmentList.get(position);
    }


    @Override
    public int getCount() {
        return mTitles.size();
    }

    //ViewPager与TabLayout绑定后，这里获取到PageTitle就是Tab的Text
    @Override
    public CharSequence getPageTitle(int position) {
        return mTitles.get(position);
    }

}
