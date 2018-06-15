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
import android.widget.ListView;

import com.svg.ConnectModbus;
import com.svg.R;
import com.svg.adapter.AdapterShijian;
import com.svg.bean.ShijianBean;
import com.svg.utils.ModbusResponseListner;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

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
    private int count = 0;
    private Timer timer;
    private boolean isHidden = false;
    private boolean isPaused = false;

    /**
     * 计时器，每隔5s更新数据
     */
    private void startTimer() {
        timer = new Timer();
        // 0无延时，间隔5s
        timer.schedule(new TimerTask() {
            public void run() {
                Message message = new Message();
                message.what = 4010;
                handler.sendMessage(message);
            }
        }, 0, 1000 * 5); //启动timer
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_shijian, container, false);
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

        listShijian = (ListView) view.findViewById(R.id.listShijian);
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
        // 01设备号；03读；1C20是起始位7200的十六进制；0078是120的十六进制，相当于获取12*10个数，10个事件
        // 创建一个byte类型的buffer字节数组，用于存放询问报文
        byte buffer1[] = new byte[6];
        buffer1[0] = 0x01;
        buffer1[1] = 0x03;
        // 计算起始位置，每次以10条事件读取
        String crcStr = Integer.toHexString(7200 + count * 10 * 12);
        byte[] byte2 = ConnectModbus.hexStringToBytes(crcStr);
        if(null != byte2 && 1<byte2.length) {
            buffer1[2] = byte2[0];
            buffer1[3] = byte2[1];
        }
//        buffer1[2] = 0x1C;
//        buffer1[3] = 0x20;
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
                Log.d("dingshi",  "获取事件");
                // 清空数据
                if(0 == count && null != listInfo && null != adapterShijian){
                    listInfo.clear();
                    adapterShijian.notifyDataSetChanged();
                }
                List<ShijianBean> dataList = new ArrayList<>();
                dataList = ConnectModbus.parsingWei_Shijian((byte[])msg.obj);
                if(null != dataList && 0 < dataList.size() && 5 > count) {
                    listInfo.addAll(dataList);
                    if(null == adapterShijian) {
                        adapterShijian = new AdapterShijian(getContext(), listInfo);
                        listShijian.setAdapter(adapterShijian);
                    } else {
                        adapterShijian.notifyDataSetChanged();
                    }
                }
                count++;
                if(5 > count) {
                    initData();
                }
                break;
            case 4010:
                count = 0;
                initData();
                break;
        }
        return false;
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        isHidden = hidden;
        if (null != timer) {
            timer.cancel();
        }
        if (!hidden) {
            count = 0;
            startTimer();
        } else {
            if (null != handler) {
                handler.removeMessages(4010);
                handler.removeMessages(4001);
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
            handler.removeMessages(4010);
            handler.removeMessages(4001);
        }
    }
}
