package com.svg.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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
 * 状态2Fragment
 * Created by aawang on 2017/3/24.
 */
public class FragmentZhuangtaiData2 extends Fragment implements ModbusResponseListner, Handler.Callback {

    private TextView txtFBDRQ1A;
    private TextView txtFBDRQ1B;
    private TextView txtFBDRQ1C;
    private TextView txtFBDRQ2A;
    private TextView txtFBDRQ2B;
    private TextView txtFBDRQ2C;
    private TextView txtFBDRQ3A;
    private TextView txtFBDRQ3B;
    private TextView txtFBDRQ3C;
    private TextView txtGBDRQ1;
    private TextView txtGBDRQ2;
    private TextView txtGBDRQ3;
    private ModbusResponseListner responseListner;
    private Handler handler;
    private List<TextView> textList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_zhuangtaidata2, container, false);
        init(view);
        return view;
    }

    /**
     * 初始化控件
     * @param view
     */
    private void init(View view){
        txtFBDRQ1A = (TextView)view.findViewById(R.id.txtFBDRQ1A);
        txtFBDRQ1B = (TextView)view.findViewById(R.id.txtFBDRQ1B);
        txtFBDRQ1C = (TextView)view.findViewById(R.id.txtFBDRQ1C);
        txtFBDRQ2A = (TextView)view.findViewById(R.id.txtFBDRQ2A);
        txtFBDRQ2B = (TextView)view.findViewById(R.id.txtFBDRQ2B);
        txtFBDRQ2C = (TextView)view.findViewById(R.id.txtFBDRQ2C);
        txtFBDRQ3A = (TextView)view.findViewById(R.id.txtFBDRQ3A);
        txtFBDRQ3B = (TextView)view.findViewById(R.id.txtFBDRQ3B);
        txtFBDRQ3C = (TextView)view.findViewById(R.id.txtFBDRQ3C);
        txtGBDRQ1 = (TextView)view.findViewById(R.id.txtGBDRQ1);
        txtGBDRQ2 = (TextView)view.findViewById(R.id.txtGBDRQ2);
        txtGBDRQ3 = (TextView)view.findViewById(R.id.txtGBDRQ3);
        textList.add(txtFBDRQ1A);
        textList.add(txtFBDRQ1B);
        textList.add(txtFBDRQ1C);
        textList.add(txtFBDRQ2A);
        textList.add(txtFBDRQ2B);
        textList.add(txtFBDRQ2C);
        textList.add(txtFBDRQ3A);
        textList.add(txtFBDRQ3B);
        textList.add(txtFBDRQ3C);
        textList.add(txtGBDRQ1);
        textList.add(txtGBDRQ2);
        textList.add(txtGBDRQ3);
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
        // 01设备号；03读；088A是起始位2185的十六进制；0020是32的十六进制，相当于获取16个数
        // 创建一个byte类型的buffer字节数组，用于存放询问报文
        byte buffer1[] = new byte[6];
        buffer1[0] = 0x01;
        buffer1[1] = 0x03;
        buffer1[2] = 0x08;
        buffer1[3] = (byte) 0x8A;
        buffer1[4] = 0x00;
        buffer1[5] = 0x01;
        return buffer1;
    }

    /**
     * 获取返回报文的回调
     * @param data
     */
    @Override
    public void getResponseData(byte[] data) {
        Message message = new Message();
        message.what = 202;
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
            case 202:
                List<Boolean> dataList = new ArrayList<>();
                dataList = ConnectModbus.parsingWei_ZhuangtaiData2((byte[])msg.obj);
                for (int i = 0; i<dataList.size();i++){
                        if (dataList.get(i)) {
                            textList.get(i).setBackgroundResource(R.drawable.btn_login_pre);
                        } else {
                            textList.get(i).setBackgroundResource(R.drawable.bg_login_passwd);
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
