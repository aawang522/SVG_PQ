package com.svg.activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.Toast;

import com.svg.R;
import com.svg.common.MyApp;
import com.svg.fragment.FragmentHistory;
import com.svg.fragment.FragmentShijian;
import com.svg.fragment.FragmentYaoTiao;
import com.svg.fragment.FragmentYaoTiao2;
import com.svg.fragment.FragmentYaoce;
import com.svg.fragment.FragmentZhuangtai;
import com.svg.utils.CommUtil;
import com.svg.utils.SPUtils;
import com.svg.utils.SysCode;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private TabLayout mTab;
    // tab标题
    private List<String> mTitles = new ArrayList<>();
    private FragmentManager fragmentManager;
    private FragmentYaoce fg1;
    private FragmentYaoTiao fg2;
    private FragmentYaoTiao2 fg2_2;
    private FragmentShijian fg3;
    private FragmentZhuangtai fg4;
    private FragmentHistory fg5;
//    private Socket connetSocket;
    //记录用户首次点击返回键的时间
    private long firstTime=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mTab = (TabLayout) findViewById(R.id.mTab);
        fragmentManager = getSupportFragmentManager();

        // 设置界面的默认值
        setDefaultInfo();

    }

    /**
     * 设置界面的默认数据
     */
    private void setDefaultInfo(){
        // tab标题栏添加文字
        mTitles.add("遥测");
        mTitles.add("遥调");
        mTitles.add("事件");
        mTitles.add("实时状态");
        mTitles.add("历史曲线");
        //设置tablayout模式
        mTab.setTabMode(TabLayout.MODE_SCROLLABLE);
        //tablayout获取集合中的名称
        mTab.addTab(mTab.newTab().setText(mTitles.get(0)));
        mTab.addTab(mTab.newTab().setText(mTitles.get(1)));
        mTab.addTab(mTab.newTab().setText(mTitles.get(2)));
        mTab.addTab(mTab.newTab().setText(mTitles.get(3)));
        mTab.addTab(mTab.newTab().setText(mTitles.get(4)));

        setChioceItem(0);

        mTab.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                setChioceItem(tab.getPosition());
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
        // 隐藏软键盘
        CommUtil.hideIputKeyboard(MainActivity.this);
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        hideFragments(fragmentTransaction);
        switch (index) {
            case 0:
                if (fg1 == null) {
                    fg1 = new FragmentYaoce();
                    fragmentTransaction.add(R.id.main_fragment_container, fg1);
                } else {
                    // 如果不为空，则直接将它显示出来
                    fragmentTransaction.show(fg1);
                }
                break;
            case 1:
                if(SysCode.LOGIN_SUPPER.equals(SPUtils.getString(MainActivity.this, SysCode.USER_OR_SUPPER))) {
                    if (fg2 == null) {
                        fg2 = new FragmentYaoTiao();
                        fragmentTransaction.add(R.id.main_fragment_container, fg2);
                    } else {
                        fragmentTransaction.show(fg2);
                    }
                } else {
                    if (fg2_2 == null) {
                        fg2_2 = new FragmentYaoTiao2();
                        fragmentTransaction.add(R.id.main_fragment_container, fg2_2);
                    } else {
                        fragmentTransaction.show(fg2_2);
                    }
                }
                break;
            case 2:
                if (fg3 == null) {
                    fg3 = new FragmentShijian();
                    fragmentTransaction.add(R.id.main_fragment_container, fg3);
                } else {
                    fragmentTransaction.show(fg3);
                }
                break;
            case 3:
                if (fg4 == null) {
                    fg4 = new FragmentZhuangtai();
                    fragmentTransaction.add(R.id.main_fragment_container, fg4);
                } else {
                    fragmentTransaction.show(fg4);
                }
                break;
            case 4:
                if (fg5 == null) {
                    fg5 = new FragmentHistory();
                    fragmentTransaction.add(R.id.main_fragment_container, fg5);
                } else {
                    fragmentTransaction.show(fg5);
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
        if (null != fg2_2) {
            fragmentTransaction.hide(fg2_2);
        }
        if (null != fg3) {
            fragmentTransaction.hide(fg3);
        }
        if (null != fg4) {
            fragmentTransaction.hide(fg4);
        }
        if (null != fg5) {
            fragmentTransaction.hide(fg5);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        closeSocket();
    }

    /**
     * 关闭socket
     */
    public void closeSocket(){
        if (null != MyApp.socket && MyApp.socket.isConnected()) {
            try {
                MyApp.socket.close();
                MyApp.socket = null;
                Log.d("ConnectModbus", "closeSocket");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 返回键监听
     *
     * @param keyCode
     * @param event
     * @return
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode==KeyEvent.KEYCODE_BACK && event.getAction()==KeyEvent.ACTION_DOWN){
            if (System.currentTimeMillis()-firstTime>2000){
                Toast.makeText(MainActivity.this, "再按一次退出程序", Toast.LENGTH_LONG).show();
                firstTime=System.currentTimeMillis();
            } else {
                closeSocket();
                finish();
                System.exit(0);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

}
