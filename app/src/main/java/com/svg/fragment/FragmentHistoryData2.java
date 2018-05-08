package com.svg.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.util.ArrayMap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;

import com.svg.ConnectModbus;
import com.svg.R;
import com.svg.common.MyApp;
import com.svg.linechart.LineChartInViewPager;
import com.svg.linechart.SetLineChart;
import com.svg.utils.HistoryResponseListner;
import com.svg.utils.RadioGroupEx;
import com.svg.utils.SysCode;

import java.util.Map;


/**
 * LineChart 表示整个图表类
 XAxis 表示X轴类
 YAxis 表示Y轴类
 LineDataSet 表示一条曲线数据集（曲线类）
 LineData 表示LineChart的数据源类
 Legend 曲线图例类
 MarkerView 点击坐标点弹出提示框（CustomMarkerView为自定义的提示框）
 Entry 表示Y轴的数据值对象，如new Entry(123f, i),其中123f是该坐标点的Y轴value值，i是一个指针，代表给第i个点赋值123f
 *
 * 年历史数据Fragment
 * Created by aawang on 2017/3/24.
 */
public class FragmentHistoryData2 extends Fragment implements HistoryResponseListner, Handler.Callback {
    // 图表控件
    private LineChartInViewPager lineChart;
    private RadioGroupEx radioGroupEx;
    private HistoryResponseListner responseListner;
    private Handler handler;
    private byte start1;
    private byte start2;
    private byte after1;
    private byte after2;
    private byte A1;
    private byte A2;
    private byte B1;
    private byte B2;
    private byte C1;
    private byte C2;
    private String danwei;
    private byte length;
    private boolean showThreeOrTwo = true;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_historydata2, container, false);
        init(view);
        return view;
    }

    /**
     * 初始化控件
     * @param view
     */
    private void init(View view){
        responseListner = this;
        handler = new Handler(this);

        // 2450
//        start1 = 0x09;
//        start2 = (byte) 0x92;
        // 2850
//        after1 = 0x0B;
//        after2 = (byte) 0x22;
        A1 = 0x09;
        A2 = (byte) 0x92;
        B1 = 0x0C;
        B2 = (byte) 0xB2;
        C1 = 0x0F;
        C2 = (byte) 0xD2;
        danwei = "A";

        lineChart = (LineChartInViewPager) view.findViewById(R.id.new_lineChart2);
        radioGroupEx = (RadioGroupEx)view.findViewById(R.id.rgex2);

        radioGroupEx.check(R.id.rbBCQ2);
        radioGroupEx.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                switch (checkedId){
                    case R.id.rbBCQ2:
                        // 2450
//                        start1 = 0x09;
//                        start2 = (byte) 0x92;
//                        // 2850
//                        after1 = 0x0B;
//                        after2 = (byte) 0x22;
                        A1 = 0x09;
                        A2 = (byte) 0x92;
                        B1 = 0x0C;
                        B2 = (byte) 0xB2;
                        C1 = 0x0F;
                        C2 = (byte) 0xD2;
                        danwei = "A";
                        showThreeOrTwo = true;
                        break;
                    case R.id.rbBCH2:
                        // 3250
//                        start1 = 0x0C;
//                        start2 = (byte) 0xB2;
//                        // 3650
//                        after1 = 0x0E;
//                        after2 = 0x42;
                        A1 = 0x0B;
                        A2 = (byte) 0x22;
                        B1 = 0x0E;
                        B2 = 0x42;
                        C1 = 0x11;
                        C2 = 0x62;
                        danwei = "A";
                        showThreeOrTwo = true;
                        break;
//                    case R.id.rbCX2:
//                        // 4050
//                        start1 = 0x0F;
//                        start2 = (byte) 0xD2;
//                        // 4450
//                        after1 = 0x11;
//                        after2 = 0x62;
//                        danwei = "A";
//                        break;
                    case R.id.rbGLYS2:
                        // 4850
                        start1 = 0x12;
                        start2 = (byte) 0xF2;
                        // 5250
                        after1 = 0x14;
                        after2 = (byte) 0x82;
                        danwei = "%";
                        showThreeOrTwo = false;
                        break;
                    case R.id.rbXBHL2:
                        // 5650
                        start1 = 0x16;
                        start2 = (byte) 0x12;
                        // 6050
                        after1 = 0x17;
                        after2 = (byte) 0xA2;
                        danwei = "%";
                        showThreeOrTwo = false;
                        break;
                    case R.id.rbBPHD2:
                        // 6450
                        start1 = 0x19;
                        start2 = 0x32;
                        // 6850
                        after1 = 0x1A;
                        after2 = (byte) 0xC2;
                        danwei = "%";
                        showThreeOrTwo = false;
                        break;
                }
                // 获取数据
                if(showThreeOrTwo) {
                    initDataABC(A1,A2,B1,B2,C1,C2);
                } else {
                    initData(start1, start2, after1, after2);
                }
            }
        });
    }

    /**
     * 获取补偿前和补偿后的数据
     * @param start1
     * @param start2
     * @param after1
     * @param after2
     */
    private void initData(byte start1, byte start2, byte after1, byte after2){
        // 设置补偿前请求报文
        byte[] requestOriginalData1 = setRequestData(start1, start2);
        // 设置补偿后请求报文
        byte[] requestOriginalData2 = setRequestData(after1, after2);
        // 调用连接modbus函数
        ConnectModbus.connectServerGetHistory(requestOriginalData1, requestOriginalData2, responseListner);
    }

    /**
     * 获取ABC补偿前和补偿后的数据
     */
    private void initDataABC(byte A1, byte A2, byte B1, byte B2, byte C1, byte C2){
        // 设置A请求报文
        byte[] requestOriginalData1 = setRequestData(A1, A2);
        // 设置B请求报文
        byte[] requestOriginalData2 = setRequestData(B1, B2);
        // 设置C请求报文
        byte[] requestOriginalData3 = setRequestData(C1, C2);
        // 调用连接modbus函数
        ConnectModbus.connectServerGetHistoryABC(MyApp.socket, requestOriginalData1, requestOriginalData2,
                requestOriginalData3, responseListner);
    }

    /**
     * 请求报文
     * @return
     */
    private byte[] setRequestData(byte start1, byte start2){
        // 01 03 07 D0 00 08 81 44
        // 01设备号；03读；0992是起始位2450的十六进制；31个日数据加上前面的年月日，共34个数据，0x0044
        // 创建一个byte类型的buffer字节数组，用于存放询问报文
        byte buffer1[] = new byte[6];
        buffer1[0] = 0x01;
        buffer1[1] = 0x03;
        buffer1[2] = start1;
        buffer1[3] = start2;
        buffer1[4] = 0x00;
        buffer1[5] = 0x44;
        length = 0x44;
        return buffer1;
    }

    /**
     * 获取返回报文的回调
     * @param map
     */
    @Override
    public void getResponseData(Map<String, byte[]> map) {
        Message message = new Message();
        message.what = 3002;
        message.obj = map;
        handler.sendMessage(message);
    }

    @Override
    public boolean handleMessage(Message msg) {
        switch (msg.what){
            case 3002:
                Map<String, byte[]> map = new ArrayMap<>();
                map = (Map<String, byte[]>)msg.obj;
                if(showThreeOrTwo) {
                    SetLineChart.setLineDataABC(getContext(), lineChart, map.get("data1"), map.get("data2"),
                            map.get("data3"), danwei, SysCode.HISTYORY_MONTH, length);
                } else {
                    SetLineChart.setLineData(getContext(), lineChart, map.get("data1"), map.get("data2"),
                            danwei, SysCode.HISTYORY_MONTH, length);
                }
                break;
        }
        return false;
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if(!hidden){
            // 获取数据
            if(showThreeOrTwo) {
                initDataABC(A1,A2,B1,B2,C1,C2);
            } else {
                initData(start1, start2, after1, after2);
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        // 获取数据
        if(showThreeOrTwo) {
            initDataABC(A1,A2,B1,B2,C1,C2);
        } else {
            initData(start1, start2, after1, after2);
        }
    }
}
