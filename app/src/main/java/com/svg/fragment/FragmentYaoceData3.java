package com.svg.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.text.InputFilter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.svg.ConnectModbus;
import com.svg.R;
import com.svg.utils.ModbusResponseListner;
import com.svg.utils.MoneyValueFilter;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

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

    private ModbusResponseListner responseListner;
    private List<TextView> textList = new ArrayList<>();
    private Handler handler;
    private Timer timer;
    // 因为这里首次进来不会进入onHidden里改变状态，所以初设为false
    private boolean isHidden = false;
    private boolean isPaused = false;

    /**
     * 计时器，每隔5s更新数据
     */
    private void startTimer(){
        timer = new Timer();
        // 0无延时，间隔5s
        timer.schedule(new TimerTask() {
            public void run() {
                Message message = new Message();
                message.what = 1030;
                handler.sendMessage(message);
            }
        }, 0, 1000 * 5); //启动timer
    }

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
        if (null != timer) {
            timer.cancel();
        }
        startTimer();
        return view;
    }

    /**
     * 初始化控件
     * @param view
     */
    private void init(View view){
        responseListner = this;
        handler = new Handler(this);

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
        yaoce_a1cxb.setFilters(new InputFilter[]{new MoneyValueFilter().setDigits(1)});
        yaoce_a2cxb.setFilters(new InputFilter[]{new MoneyValueFilter().setDigits(1)});
        yaoce_a3cxb.setFilters(new InputFilter[]{new MoneyValueFilter().setDigits(1)});
        yaoce_a4cxb.setFilters(new InputFilter[]{new MoneyValueFilter().setDigits(1)});
        yaoce_a5cxb.setFilters(new InputFilter[]{new MoneyValueFilter().setDigits(1)});
        yaoce_a6cxb.setFilters(new InputFilter[]{new MoneyValueFilter().setDigits(1)});
        yaoce_a7cxb.setFilters(new InputFilter[]{new MoneyValueFilter().setDigits(1)});
        yaoce_a8cxb.setFilters(new InputFilter[]{new MoneyValueFilter().setDigits(1)});
        yaoce_a9cxb.setFilters(new InputFilter[]{new MoneyValueFilter().setDigits(1)});
        yaoce_a10cxb.setFilters(new InputFilter[]{new MoneyValueFilter().setDigits(1)});
        yaoce_a11cxb.setFilters(new InputFilter[]{new MoneyValueFilter().setDigits(1)});
        yaoce_a12cxb.setFilters(new InputFilter[]{new MoneyValueFilter().setDigits(1)});
        yaoce_a13cxb.setFilters(new InputFilter[]{new MoneyValueFilter().setDigits(1)});
    }

    private void initData(){
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
                Log.d("dingshi",  "获取遥测3");
                List<Float> dataList = new ArrayList<>();
                dataList = ConnectModbus.from32Fudian((byte[])msg.obj);
                if (null != dataList && 0 < dataList.size()) {
                    for (int i = 0; i < textList.size(); i++) {
                        if (null != textList.get(i)) {
                            textList.get(i).setText(String.valueOf(dataList.get(i)));
                        }
                    }
                }
                break;
            case 1030:
                initData();
                break;
        }
        return false;
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        isHidden = hidden;
        if(null != timer){
            timer.cancel();
        }
        if(!hidden){
            startTimer();
        } else {
            if(null != handler) {
                handler.removeMessages(1030);
                handler.removeMessages(103);
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        // 开屏时，判断如果是在当前界面又是刚从关屏状态过来，就继续定时更新
        if(!isHidden && isPaused) {
            isPaused = false;
            if (null != timer) {
                timer.cancel();
            }
            startTimer();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        // 关屏时，记录isPaused状态位，清空消息，停止定时更新
        isPaused = true;
        if(null != timer){
            timer.cancel();
        }
        if(null != handler) {
            handler.removeMessages(1030);
            handler.removeMessages(103);
        }
    }
}
