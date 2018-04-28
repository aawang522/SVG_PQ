package com.svg.common;

import android.app.Application;

import com.svg.ConnectModbus;
import com.yanzhenjie.nohttp.NoHttp;

import java.net.Socket;

/**
 * Created by Administrator on 2017/6/27.
 */

public class MyApp extends Application {
    private static MyApp application;
    public static Socket socket;
    public String mainAction = "com.example.administrator.oa.view.activity.MainActivity.broadcast";
    @Override
    public void onCreate() {
        super.onCreate();
        application = this;
    }
}
