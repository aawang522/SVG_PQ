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
import com.svg.utils.CommUtil;
import com.svg.utils.LoginingAnimation;
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

    private List<TextView> textList = new ArrayList<>();
    private ModbusResponseListner responseListner;
    private Handler handler;
    private Timer timer;
    private boolean isHidden = false;
    private boolean isPaused = false;
    private LoginingAnimation loginingAnimation;

    /**
     * 计时器，每隔5s更新数据
     */
    private void startTimer() {
        timer = new Timer();
        // 0无延时，间隔5s
        timer.schedule(new TimerTask() {
            public void run() {
                Message message = new Message();
                message.what = 1010;
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
        View view = inflater.inflate(R.layout.fragment_yaoce_data1, null);
        init(view);
        if (null != timer) {
            timer.cancel();
        }
        startTimer();
        return view;
    }

    /**
     * 初始化控件
     *
     * @param view
     */
    private void init(View view) {
        responseListner = this;
        handler = new Handler(this);
        loginingAnimation = new LoginingAnimation(getContext());

        yaoce_yggl = (TextView) view.findViewById(R.id.yaoce_yggl);
        yaoce_wggl = (TextView) view.findViewById(R.id.yaoce_wggl);
        yaoce_glys = (TextView) view.findViewById(R.id.yaoce_glys);
        yaoce_wyglys = (TextView) view.findViewById(R.id.yaoce_wyglys);
        yaoce_pl = (TextView) view.findViewById(R.id.yaoce_pl);
        yaoce_abxdy = (TextView) view.findViewById(R.id.yaoce_abxdy);
        yaoce_bcxdy = (TextView) view.findViewById(R.id.yaoce_bcxdy);
        yaoce_caxdy = (TextView) view.findViewById(R.id.yaoce_caxdy);
        yaoce_abxdythd = (TextView) view.findViewById(R.id.yaoce_abxdythd);
        yaoce_bcxdythd = (TextView) view.findViewById(R.id.yaoce_bcxdythd);
        yaoce_caxdythd = (TextView) view.findViewById(R.id.yaoce_caxdythd);
        yaoce_axzldy = (TextView) view.findViewById(R.id.yaoce_axzldy);
        yaoce_dlbphd = (TextView) view.findViewById(R.id.yaoce_dlbphd);
        yaoce_axwcdl = (TextView) view.findViewById(R.id.yaoce_axwcdl);
        yaoce_bxwcdl = (TextView) view.findViewById(R.id.yaoce_bxwcdl);
        yaoce_cxxwcdl = (TextView) view.findViewById(R.id.yaoce_cxxwcdl);
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

        yaoce_yggl.setFilters(new InputFilter[]{new MoneyValueFilter().setDigits(1)});
        yaoce_wggl.setFilters(new InputFilter[]{new MoneyValueFilter().setDigits(1)});
        yaoce_glys.setFilters(new InputFilter[]{new MoneyValueFilter().setDigits(3)});
        yaoce_wyglys.setFilters(new InputFilter[]{new MoneyValueFilter().setDigits(3)});
        yaoce_pl.setFilters(new InputFilter[]{new MoneyValueFilter().setDigits(1)});
        yaoce_abxdy.setFilters(new InputFilter[]{new MoneyValueFilter().setDigits(1)});
        yaoce_bcxdy.setFilters(new InputFilter[]{new MoneyValueFilter().setDigits(1)});
        yaoce_caxdy.setFilters(new InputFilter[]{new MoneyValueFilter().setDigits(1)});
        yaoce_abxdythd.setFilters(new InputFilter[]{new MoneyValueFilter().setDigits(1)});
        yaoce_bcxdythd.setFilters(new InputFilter[]{new MoneyValueFilter().setDigits(1)});
        yaoce_caxdythd.setFilters(new InputFilter[]{new MoneyValueFilter().setDigits(1)});
        yaoce_axzldy.setFilters(new InputFilter[]{new MoneyValueFilter().setDigits(1)});
        yaoce_dlbphd.setFilters(new InputFilter[]{new MoneyValueFilter().setDigits(1)});
        yaoce_axwcdl.setFilters(new InputFilter[]{new MoneyValueFilter().setDigits(1)});
        yaoce_bxwcdl.setFilters(new InputFilter[]{new MoneyValueFilter().setDigits(1)});
        yaoce_cxxwcdl.setFilters(new InputFilter[]{new MoneyValueFilter().setDigits(1)});
    }

    private void initData() {
        if(null != loginingAnimation) {
            loginingAnimation.showLoading();
        }
        // 设置请求报文
        byte[] requestOriginalData = setRequestData();
        // 调用连接modbus函数
        ConnectModbus.connectServerWithTCPSocket(requestOriginalData, responseListner);
    }
    /**
     * 请求报文
     *
     * @return
     */
    private byte[] setRequestData() {
        // 01 03 07 D0 00 08 81 44
        // 01设备号；03读；07D0是起始位2000的十六进制；0020是32的十六进制，相当于获取16个数
        // 创建一个byte类型的buffer字节数组，用于存放询问报文
        byte buffer1[] = new byte[6];
        buffer1[0] = 0x01;
        buffer1[1] = 0x03;
        buffer1[2] = 0x07;
        buffer1[3] = (byte) 0xD0;
        buffer1[4] = 0x00;
        buffer1[5] = 0x20;
        return buffer1;
    }

    /**
     * 获取返回报文的回调
     *
     * @param data
     */
    @Override
    public void getResponseData(byte[] data) {
        Message message = new Message();
        message.what = 101;
        message.obj = data;
        handler.sendMessage(message);
    }

    @Override
    public void failedResponse() {
        Message message = new Message();
        message.what = 801;
        handler.sendMessageDelayed(message, 1000);
    }

    /**
     * 获取提交返回报文的回调
     *
     * @param data
     */
    @Override
    public void getSubmitResponseData(byte[] data) {
    }

    @Override
    public void submitFailedResponse() {
    }

    @Override
    public boolean handleMessage(Message msg) {
        switch (msg.what) {
            case 101:
                Log.d("dingshi",  "获取遥测1");
                List<Float> dataList = new ArrayList<>();
                dataList = ConnectModbus.from32Fudian((byte[]) msg.obj);
                int length = 0;
                if (null != dataList && null != textList) {
                    if (dataList.size() < textList.size()) {
                        length = dataList.size();
                    } else {
                        length = textList.size();
                    }
                    for (int i = 0; i < length; i++) {
                        if (null != textList.get(i)) {
                            try {
                                textList.get(i).setText(String.valueOf(dataList.get(i)));
                            } catch (IndexOutOfBoundsException e) {
                                e.printStackTrace();
                            }

                        }
//                        }
                    }
                }
                if(null != loginingAnimation && loginingAnimation.isShowed()) {
                    loginingAnimation.dismissLoading();
                }
                break;
            case 1010:
                initData();
                break;
            case 801:
                if(null != loginingAnimation && loginingAnimation.isShowed()) {
                    loginingAnimation.dismissLoading();
                }
                if(CommUtil.isNetworkConnected(getContext())) {
                    CommUtil.showToast(getContext(), "数据刷新失败");
                }
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
            startTimer();
        } else {
            if (null != handler) {
                handler.removeMessages(1010);
                handler.removeMessages(101);
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
            handler.removeMessages(1010);
            handler.removeMessages(101);
        }
    }
}
