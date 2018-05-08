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
 * 状态1Fragment
 * Created by aawang on 2017/3/24.
 */
public class FragmentZhuangtaiData1 extends Fragment implements ModbusResponseListner, Handler.Callback {

    private TextView txtCSZT;
    private TextView txtYCDZT;
    private TextView txtHZZT;
    private TextView txtJXZT;
    private TextView txtXYZT;
    private TextView txtKAI;
    private ModbusResponseListner responseListner;
    private Handler handler;
    private List<TextView> textList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_zhuangtaidata1, container, false);
        init(view);
        return view;
    }

    /**
     * 初始化控件
     * @param view
     */
    private void init(View view){
        txtCSZT = (TextView)view.findViewById(R.id.txtCSZT);
        txtYCDZT = (TextView)view.findViewById(R.id.txtYCDZT);
        txtHZZT = (TextView)view.findViewById(R.id.txtHZZT);
        txtJXZT = (TextView)view.findViewById(R.id.txtJXZT);
        txtXYZT = (TextView)view.findViewById(R.id.txtXYZT);
        txtKAI = (TextView)view.findViewById(R.id.txtKAI);
        textList.add(txtCSZT);
        textList.add(txtYCDZT);
        textList.add(txtHZZT);
        textList.add(txtJXZT);
        textList.add(txtXYZT);
        textList.add(txtKAI);
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
        // 01 03 07 D0 00 08 81 44
        // 01设备号；03读；0889是起始位2185的十六进制；0020是32的十六进制，相当于获取16个数
        // 创建一个byte类型的buffer字节数组，用于存放询问报文
        byte buffer1[] = new byte[6];
        buffer1[0] = 0x01;
        buffer1[1] = 0x03;
        buffer1[2] = 0x08;
        buffer1[3] = (byte) 0x89;
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
        message.what = 201;
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
            case 201:
                List<Boolean> dataList = new ArrayList<>();
                dataList = ConnectModbus.parsingWei_ZhuangtaiData1((byte[])msg.obj);
                for (int i = 0; i<dataList.size();i++){
                    // 如果是最末尾的，则是开关
                    if(i == dataList.size()-1){
                        if(dataList.get(i)) {
//                            textList.get(i).setText("开");
                            textList.get(i).setBackgroundResource(R.drawable.btn_login_red);
                        } else {
//                            textList.get(i).setText("关");
                            textList.get(i).setBackgroundResource(R.drawable.bg_login_passwd);
                        }
                    } else {
                        if (dataList.get(i)) {
                            textList.get(i).setBackgroundResource(R.drawable.btn_login_pre);
                        } else {
                            textList.get(i).setBackgroundResource(R.drawable.bg_login_passwd);
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
