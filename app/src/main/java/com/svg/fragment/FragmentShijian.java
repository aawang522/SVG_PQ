package com.svg.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.svg.ConnectModbus;
import com.svg.R;
import com.svg.adapter.AdapterShijian;
import com.svg.bean.ShijianBean;
import com.svg.common.MyApp;
import com.svg.utils.ModbusResponseListner;

import java.util.ArrayList;
import java.util.List;

/**
 * 事件Fragment
 * Created by aawang on 2017/3/24.
 */
public class FragmentShijian extends Fragment  implements ModbusResponseListner, Handler.Callback {

    private ListView listShijian;
    private AdapterShijian adapterShijian;
    private List<ShijianBean> listInfo = new ArrayList<>();

    private ModbusResponseListner responseListner;
    private Handler handler;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_shijian, container, false);
        init(view);
        return view;
    }

    /**
     * 初始化控件
     * @param view
     */
    private void init(View view){
        listShijian = (ListView) view.findViewById(R.id.listShijian);

    }

    private void initData(){
        responseListner = this;
        handler = new Handler(this);
        // 设置请求报文
        byte[] requestOriginalData = setRequestData();
        // 调用连接modbus函数
        ConnectModbus.connectServerWithTCPSocket(requestOriginalData, responseListner);
    }

    /**
     * 请求报文
     * @return
     */
    private byte[] setRequestData(){
        // 01设备号；03读；1C20是起始位7200的十六进制；0078是120的十六进制，相当于获取12*10个数，两个事件
        // 创建一个byte类型的buffer字节数组，用于存放询问报文
        byte buffer1[] = new byte[6];
        buffer1[0] = 0x01;
        buffer1[1] = 0x03;
        buffer1[2] = 0x1C;
        buffer1[3] = 0x20;
        buffer1[4] = 0x00;
        buffer1[5] = 0x78;
        return buffer1;
    }

    /**
     * 获取返回报文的回调
     * @param data
     */
    @Override
    public void getResponseData(byte[] data) {
        Message message = new Message();
        message.what = 4001;
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
            case 4001:
                List<Float> dataList = new ArrayList<>();
                listInfo = ConnectModbus.parsingWei_Shijian((byte[])msg.obj);
                if(null != listInfo && 0<listInfo.size()) {
                    adapterShijian = new AdapterShijian(getContext(), listInfo);
                    listShijian.setAdapter(adapterShijian);
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
