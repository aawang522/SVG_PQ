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
public class FragmentYaoceData2 extends Fragment implements ModbusResponseListner, Handler.Callback {

    private TextView yaoce_zxwcdl;
    private TextView yaoce_axwcjbdl;
    private TextView yaoce_bxwcjbdl;
    private TextView yaoce_cxwcjbdl;
    private TextView yaoce_zxwcjbdl;
    private TextView yaoce_axwcdlthd;
    private TextView yaoce_bxwcdlthd;
    private TextView yaoce_cxwcdlthd;
    private TextView yaoce_zxwcdlthd;
    private TextView yaoce_axnbqdl;
    private TextView yaoce_bxnbqdl;
    private TextView yaoce_cxnbqdl;
    private TextView yaoce_axdywd;
    private TextView yaoce_bxdywd;
    private TextView yaoce_cxdywd;

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
        View view = inflater.inflate(R.layout.fragment_yaoce_data2, null);
        init(view);
//        initData();
        Log.d("fragment2", "onCreateView");
        return view;
    }

    public static FragmentYaoceData2 getInstance(String title) {
        FragmentYaoceData2 sf = new FragmentYaoceData2();
        sf.mTitle = title;
        return sf;
    }

    /**
     * 初始化控件
     * @param view
     */
    private void init(View view){
        yaoce_zxwcdl = (TextView)view.findViewById(R.id.yaoce_zxwcdl);
        yaoce_axwcjbdl = (TextView)view.findViewById(R.id.yaoce_axwcjbdl);
        yaoce_bxwcjbdl = (TextView)view.findViewById(R.id.yaoce_bxwcjbdl);
        yaoce_cxwcjbdl = (TextView)view.findViewById(R.id.yaoce_cxwcjbdl);
        yaoce_zxwcjbdl = (TextView)view.findViewById(R.id.yaoce_zxwcjbdl);
        yaoce_axwcdlthd = (TextView)view.findViewById(R.id.yaoce_axwcdlthd);
        yaoce_bxwcdlthd = (TextView)view.findViewById(R.id.yaoce_bxwcdlthd);
        yaoce_cxwcdlthd = (TextView)view.findViewById(R.id.yaoce_cxwcdlthd);
        yaoce_zxwcdlthd = (TextView)view.findViewById(R.id.yaoce_zxwcdlthd);
        yaoce_axnbqdl = (TextView)view.findViewById(R.id.yaoce_axnbqdl);
        yaoce_bxnbqdl = (TextView)view.findViewById(R.id.yaoce_bxnbqdl);
        yaoce_cxnbqdl = (TextView)view.findViewById(R.id.yaoce_cxnbqdl);
        yaoce_axdywd = (TextView)view.findViewById(R.id.yaoce_axdywd);
        yaoce_bxdywd = (TextView)view.findViewById(R.id.yaoce_bxdywd);
        yaoce_cxdywd = (TextView)view.findViewById(R.id.yaoce_cxdywd);
        textList.add(yaoce_zxwcdl);
        textList.add(yaoce_axwcjbdl);
        textList.add(yaoce_bxwcjbdl);
        textList.add(yaoce_cxwcjbdl);
        textList.add(yaoce_zxwcjbdl);
        textList.add(yaoce_axwcdlthd);
        textList.add(yaoce_bxwcdlthd);
        textList.add(yaoce_cxwcdlthd);
        textList.add(yaoce_zxwcdlthd);
        textList.add(yaoce_axnbqdl);
        textList.add(yaoce_bxnbqdl);
        textList.add(yaoce_cxnbqdl);
        textList.add(yaoce_axdywd);
        textList.add(yaoce_bxdywd);
        textList.add(yaoce_cxdywd);
    }

    private void initData(){
        responseListner = this;
        handler = new Handler(this);
        // 设置请求报文
        byte[] requestOriginalData = setRequestData();
        // 调用连接modbus函数
        ConnectModbus .connectServerWithTCPSocket(requestOriginalData, responseListner);
    }

    /**
     * 请求报文
     * @return
     */
    private byte[] setRequestData(){
        // 01 03 07 D0 00 08 81 44
        // 01设备号；03读；07F0是起始位2032的十六进制；001E是30的十六进制，相当于获取15个数
        // 创建一个byte类型的buffer字节数组，用于存放询问报文
        byte buffer1[] = new byte[6];
        buffer1[0] = 0x01;
        buffer1[1] = 0x03;
        buffer1[2] = 0x07;
        buffer1[3] = (byte)0xF0;
        buffer1[4] = 0x00;
        buffer1[5] = 0x1E;
        return buffer1;
    }

    /**
     * 获取返回报文的回调
     * @param data
     */
    @Override
    public void getResponseData(byte[] data) {
        Message message = new Message();
        message.what = 102;
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
            case 102:
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
            Log.d("fragment2", "onHiddenChanged_show");
        } else {
            Log.d("fragment2", "onHiddenChanged_hide");
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        initData();
        Log.d("fragment2", "onResume");
    }
}
