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

    private ModbusResponseListner responseListner;
    private List<TextView> textList = new ArrayList<>();
    private Handler handler;
    private Timer timer;
    // 因为这里首次进来不会进入onHidden里改变状态，所以初设为false
    private boolean isHidden = false;
    private boolean isPaused = false;
    private LoginingAnimation loginingAnimation;

    /**
     * 计时器，每隔5s更新数据
     */
    private void startTimer(){
        timer = new Timer();
        // 0无延时，间隔5s
        timer.schedule(new TimerTask() {
            public void run() {
                Message message = new Message();
                message.what = 1020;
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
        View view = inflater.inflate(R.layout.fragment_yaoce_data2, null);
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
        loginingAnimation = new LoginingAnimation(getContext());

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

        yaoce_zxwcdl.setFilters(new InputFilter[]{new MoneyValueFilter().setDigits(1)});
        yaoce_axwcjbdl.setFilters(new InputFilter[]{new MoneyValueFilter().setDigits(1)});
        yaoce_bxwcjbdl.setFilters(new InputFilter[]{new MoneyValueFilter().setDigits(1)});
        yaoce_cxwcjbdl.setFilters(new InputFilter[]{new MoneyValueFilter().setDigits(1)});
        yaoce_zxwcjbdl.setFilters(new InputFilter[]{new MoneyValueFilter().setDigits(1)});
        yaoce_axwcdlthd.setFilters(new InputFilter[]{new MoneyValueFilter().setDigits(1)});
        yaoce_bxwcdlthd.setFilters(new InputFilter[]{new MoneyValueFilter().setDigits(1)});
        yaoce_cxwcdlthd.setFilters(new InputFilter[]{new MoneyValueFilter().setDigits(1)});
        yaoce_zxwcdlthd.setFilters(new InputFilter[]{new MoneyValueFilter().setDigits(1)});
        yaoce_axnbqdl.setFilters(new InputFilter[]{new MoneyValueFilter().setDigits(1)});
        yaoce_bxnbqdl.setFilters(new InputFilter[]{new MoneyValueFilter().setDigits(1)});
        yaoce_cxnbqdl.setFilters(new InputFilter[]{new MoneyValueFilter().setDigits(1)});
        yaoce_axdywd.setFilters(new InputFilter[]{new MoneyValueFilter().setDigits(1)});
        yaoce_bxdywd.setFilters(new InputFilter[]{new MoneyValueFilter().setDigits(1)});
        yaoce_cxdywd.setFilters(new InputFilter[]{new MoneyValueFilter().setDigits(1)});
    }

    private void initData(){
        if(null != loginingAnimation) {
            loginingAnimation.showLoading();
        }
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

    @Override
    public void failedResponse() {
        Message message = new Message();
        message.what = 802;
        handler.sendMessageDelayed(message, 1000);
    }

    /**
     * 获取提交返回报文的回调
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
        switch (msg.what){
            case 102:
                Log.d("dingshi",  "获取遥测2");
                List<Float> dataList = new ArrayList<>();
                dataList = ConnectModbus.from32Fudian((byte[])msg.obj);
                if (null != dataList && 0 < dataList.size()) {
                    for (int i = 0; i < textList.size(); i++) {
                        if (null != textList.get(i)) {
                            textList.get(i).setText(String.valueOf(dataList.get(i)));
                        }
                    }
                }
                if(null != loginingAnimation && loginingAnimation.isShowed()) {
                    loginingAnimation.dismissLoading();
                }
                break;
            case 1020:
                initData();
                break;
            case 802:
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
        if(null != timer){
            timer.cancel();
        }
        if(!hidden){
            startTimer();
        } else {
            if(null != handler) {
                handler.removeMessages(1020);
                handler.removeMessages(102);
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
            handler.removeMessages(1020);
            handler.removeMessages(102);
        }
    }
}
