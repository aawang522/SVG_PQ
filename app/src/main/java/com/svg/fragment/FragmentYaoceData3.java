package com.svg.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.svg.ConnectModbus;
import com.svg.R;
import com.svg.common.MyApp;
import com.svg.utils.ModbusResponseListner;

import java.util.ArrayList;
import java.util.List;

/**
 * 遥测数据1
 * Created by aawang on 2017/3/24.
 */
public class FragmentYaoceData3 extends Fragment implements ModbusResponseListner, Handler.Callback {

    private TextView yaoce_a1cxb;
    private TextView yaoce_a2cxb;
    private TextView yaoce_a3cxb;
    private TextView yaoce_a4cxb;
    private TextView yaoce_a5cxb;
    private TextView yaoce_a6cxb;
    private TextView yaoce_a7cxb;
    private TextView yaoce_a8cxb;
    private TextView yaoce_a9cxb;
    private TextView yaoce_a10cxb;
    private TextView yaoce_a11cxb;
    private TextView yaoce_a12cxb;
    private TextView yaoce_a13cxb;

    private String mTitle;
    private ModbusResponseListner responseListner;
    private List<TextView> textList = new ArrayList<>();
    private Handler handler;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_yaoce_data3, null);
        init(view);
        Log.d("fragment3", "onCreateView");
//        initData();
        return view;
    }

    public static FragmentYaoceData3 getInstance(String title) {
        FragmentYaoceData3 sf = new FragmentYaoceData3();
        sf.mTitle = title;
        return sf;
    }

    /**
     * 初始化控件
     * @param view
     */
    private void init(View view){
        yaoce_a1cxb = (TextView)view.findViewById(R.id.yaoce_a1cxb);
        yaoce_a2cxb = (TextView)view.findViewById(R.id.yaoce_a2cxb);
        yaoce_a3cxb = (TextView)view.findViewById(R.id.yaoce_a3cxb);
        yaoce_a4cxb = (TextView)view.findViewById(R.id.yaoce_a4cxb);
        yaoce_a5cxb = (TextView)view.findViewById(R.id.yaoce_a5cxb);
        yaoce_a6cxb = (TextView)view.findViewById(R.id.yaoce_a6cxb);
        yaoce_a7cxb = (TextView)view.findViewById(R.id.yaoce_a7cxb);
        yaoce_a8cxb = (TextView)view.findViewById(R.id.yaoce_a8cxb);
        yaoce_a9cxb = (TextView)view.findViewById(R.id.yaoce_a9cxb);
        yaoce_a10cxb = (TextView)view.findViewById(R.id.yaoce_a10cxb);
        yaoce_a11cxb = (TextView)view.findViewById(R.id.yaoce_a11cxb);
        yaoce_a12cxb = (TextView)view.findViewById(R.id.yaoce_a12cxb);
        yaoce_a13cxb = (TextView)view.findViewById(R.id.yaoce_a13cxb);
        textList.add(yaoce_a1cxb);
        textList.add(yaoce_a2cxb);
        textList.add(yaoce_a3cxb);
        textList.add(yaoce_a4cxb);
        textList.add(yaoce_a5cxb);
        textList.add(yaoce_a6cxb);
        textList.add(yaoce_a7cxb);
        textList.add(yaoce_a8cxb);
        textList.add(yaoce_a9cxb);
        textList.add(yaoce_a10cxb);
        textList.add(yaoce_a11cxb);
        textList.add(yaoce_a12cxb);
        textList.add(yaoce_a13cxb);
    }

    private void initData(){
        responseListner = this;
        handler = new Handler(this);
        // 设置请求报文
        byte[] requestOriginalData = setRequestData();
        // 调用连接modbus函数
        ConnectModbus.connectServerWithTCPSocket(MyApp.socket, requestOriginalData, responseListner);
    }

    /**
     * 请求报文
     * @return
     */
    private byte[] setRequestData(){
        // 01 03 07 D0 00 08 81 44
        // 01设备号；03读；080E是起始位2062的十六进制；001A是26的十六进制，相当于获取13个数
        // 创建一个byte类型的buffer字节数组，用于存放询问报文
        byte buffer1[] = new byte[6];
        buffer1[0] = 0x01;
        buffer1[1] = 0x03;
        buffer1[2] = 0x08;
        buffer1[3] = (byte)0x0E;
        buffer1[4] = 0x00;
        buffer1[5] = 0x1A;
        return buffer1;
    }

    /**
     * 获取返回报文的回调
     * @param data
     */
    @Override
    public void getResponseData(byte[] data) {
        Message message = new Message();
        message.what = 103;
        message.obj = data;
        handler.sendMessage(message);
    }

    /**
     * 获取提交返回报文的回调
     * @param data
     */
    @Override
    public void getSubmitResponseData(byte[] data) {
    }

    @Override
    public boolean handleMessage(Message msg) {
        switch (msg.what){
            case 103:
                List<Float> dataList = new ArrayList<>();
                dataList = ConnectModbus.from32Fudian((byte[])msg.obj);
                for (int i = 0; i<textList.size();i++){
                    if(null != textList.get(i)) {
                        textList.get(i).setText(String.valueOf(dataList.get(i)));
                    }
                }
                break;
        }
        return false;
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if(!hidden){
            initData();
            Log.d("fragment3", "onHiddenChanged_show");
        } else {
            Log.d("fragment3", "onHiddenChanged_hide");
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        initData();
        Log.d("fragment3", "onResume");
    }
}
