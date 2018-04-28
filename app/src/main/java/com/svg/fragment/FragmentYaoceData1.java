package com.svg.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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
public class FragmentYaoceData1 extends Fragment implements ModbusResponseListner, Handler.Callback {

    private TextView yaoce_yggl;
    private TextView yaoce_wggl;
    private TextView yaoce_glys;
    private TextView yaoce_wyglys;
    private TextView yaoce_pl;
    private TextView yaoce_abxdy;
    private TextView yaoce_bcxdy;
    private TextView yaoce_caxdy;
    private TextView yaoce_abxdythd;
    private TextView yaoce_bcxdythd;
    private TextView yaoce_caxdythd;
    private TextView yaoce_axzldy;
    private TextView yaoce_dlbphd;
    private TextView yaoce_axwcdl;
    private TextView yaoce_bxwcdl;
    private TextView yaoce_cxxwcdl;

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
        View view = inflater.inflate(R.layout.fragment_yaoce_data1, null);
        init(view);
//        initData();
        Log.d("fragment1", "onCreateView");
        return view;
    }

    /**
     * 初始化控件
     * @param view
     */
    private void init(View view){
        yaoce_yggl = (TextView)view.findViewById(R.id.yaoce_yggl);
        yaoce_wggl = (TextView)view.findViewById(R.id.yaoce_wggl);
        yaoce_glys = (TextView)view.findViewById(R.id.yaoce_glys);
        yaoce_wyglys = (TextView)view.findViewById(R.id.yaoce_wyglys);
        yaoce_pl = (TextView)view.findViewById(R.id.yaoce_pl);
        yaoce_abxdy = (TextView)view.findViewById(R.id.yaoce_abxdy);
        yaoce_bcxdy = (TextView)view.findViewById(R.id.yaoce_bcxdy);
        yaoce_caxdy = (TextView)view.findViewById(R.id.yaoce_caxdy);
        yaoce_abxdythd = (TextView)view.findViewById(R.id.yaoce_abxdythd);
        yaoce_bcxdythd = (TextView)view.findViewById(R.id.yaoce_bcxdythd);
        yaoce_caxdythd = (TextView)view.findViewById(R.id.yaoce_caxdythd);
        yaoce_axzldy = (TextView)view.findViewById(R.id.yaoce_axzldy);
        yaoce_dlbphd = (TextView)view.findViewById(R.id.yaoce_dlbphd);
        yaoce_axwcdl = (TextView)view.findViewById(R.id.yaoce_axwcdl);
        yaoce_bxwcdl = (TextView)view.findViewById(R.id.yaoce_bxwcdl);
        yaoce_cxxwcdl = (TextView)view.findViewById(R.id.yaoce_cxxwcdl);
        textList.add(yaoce_yggl);
        textList.add(yaoce_wggl);
        textList.add(yaoce_glys);
        textList.add(yaoce_wyglys);
        textList.add(yaoce_pl);
        textList.add(yaoce_abxdy);
        textList.add(yaoce_bcxdy);
        textList.add(yaoce_caxdy);
        textList.add(yaoce_abxdythd);
        textList.add(yaoce_bcxdythd);
        textList.add(yaoce_caxdythd);
        textList.add(yaoce_axzldy);
        textList.add(yaoce_dlbphd);
        textList.add(yaoce_axwcdl);
        textList.add(yaoce_bxwcdl);
        textList.add(yaoce_cxxwcdl);
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
        // 01设备号；03读；07D0是起始位2000的十六进制；0020是32的十六进制，相当于获取16个数
        // 创建一个byte类型的buffer字节数组，用于存放询问报文
        byte buffer1[] = new byte[6];
        buffer1[0] = 0x01;
        buffer1[1] = 0x03;
        buffer1[2] = 0x07;
        buffer1[3] = (byte)0xD0;
        buffer1[4] = 0x00;
        buffer1[5] = 0x20;
        return buffer1;
    }

    /**
     * 获取返回报文的回调
     * @param data
     */
    @Override
    public void getResponseData(byte[] data) {
        Message message = new Message();
        message.what = 101;
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
            case 101:
                List<Float> dataList = new ArrayList<>();
                dataList = ConnectModbus.from32Fudian((byte[])msg.obj);
                if(null != dataList && 0 < dataList.size()) {
                    for (int i = 0; i < textList.size(); i++) {
                        if (null != textList.get(i) && i < textList.size()) {
                            textList.get(i).setText(String.valueOf(dataList.get(i)));
                        }
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
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        initData();
    }
}
