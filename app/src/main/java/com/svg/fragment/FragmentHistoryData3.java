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
public class FragmentHistoryData3 extends Fragment implements HistoryResponseListner, Handler.Callback {
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
        View view = inflater.inflate(R.layout.fragment_historydata3, container, false);
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

        // 2520
//        start1 = 0x09;
//        start2 = (byte) 0xD8;
        // 2920
//        after1 = 0x0B;
//        after2 = (byte) 0x68;
        A1 = 0x09;
        A2 = (byte) 0xD8;
        B1 = 0x0C;
        B2 = (byte) 0xF8;
        C1 = 0x10;
        C2 = 0x18;
        danwei = "A";

        lineChart = (LineChartInViewPager) view.findViewById(R.id.new_lineChart3);
        radioGroupEx = (RadioGroupEx)view.findViewById(R.id.rgex3);

        radioGroupEx.check(R.id.rbBCQ3);
        radioGroupEx.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                switch (checkedId){
                    case R.id.rbBCQ3:
                        // 2520
//                        start1 = 0x09;
//                        start2 = (byte) 0xD8;
//                        // 2920
//                        after1 = 0x0B;
//                        after2 = (byte) 0x68;
                        A1 = 0x09;
                        A2 = (byte) 0xD8;
                        B1 = 0x0C;
                        B2 = (byte) 0xF8;
                        C1 = 0x10;
                        C2 = 0x18;
                        danwei = "A";
                        showThreeOrTwo = true;
                        break;
                    case R.id.rbBCH3:
                        // 3320
//                        start1 = 0x0C;
//                        start2 = (byte) 0xF8;
//                        // 3720
//                        after1 = 0x0E;
//                        after2 = (byte) 0x88;
                        A1 = 0x0B;
                        A2 = (byte) 0x68;
                        B1 = 0x0E;
                        B2 = (byte) 0x88;
                        C1 = 0x11;
                        C2 = (byte) 0xA8;
                        danwei = "A";
                        showThreeOrTwo = true;
                        break;
//                    case R.id.rbCX3:
//                        // 4120
//                        start1 = 0x10;
//                        start2 = 0x18;
//                        // 4520
//                        after1 = 0x11;
//                        after2 = (byte) 0xA8;
//                        danwei = "A";
//                        break;
                    case R.id.rbGLYS3:
                        // 4920
                        start1 = 0x13;
                        start2 = 0x38;
                        // 5320
                        after1 = 0x14;
                        after2 = (byte) 0xC8;
                        danwei = "%";
                        showThreeOrTwo = false;
                        break;
                    case R.id.rbXBHL3:
                        // 5720
                        start1 = 0x16;
                        start2 = (byte) 0x58;
                        // 6120
                        after1 = 0x17;
                        after2 = (byte) 0xE8;
                        danwei = "%";
                        showThreeOrTwo = false;
                        break;
                    case R.id.rbBPHD3:
                        // 6520
                        start1 = 0x19;
                        start2 = 0x78;
                        // 6920
                        after1 = 0x1B;
                        after2 = 0x08;
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
        // 01设备号；03读；09D8是起始位2520的十六进制；查询24小时每4个时段共100条，再加上前面5个年月日时分，共105条数据，210的十六进制是00D2
        // 创建一个byte类型的buffer字节数组，用于存放询问报文
        byte buffer1[] = new byte[6];
        buffer1[0] = 0x01;
        buffer1[1] = 0x03;
        buffer1[2] = start1;
        buffer1[3] = start2;
        buffer1[4] = 0x00;
        buffer1[5] = (byte)0xCA;
        length = (byte)0xCA;
        return buffer1;
    }

    /**
     * 获取返回报文的回调
     * @param map
     */
    @Override
    public void getResponseData(Map<String, byte[]> map) {
        Message message = new Message();
        message.what = 3003;
        message.obj = map;
        handler.sendMessage(message);
    }

    @Override
    public boolean handleMessage(Message msg) {
        switch (msg.what){
            case 3003:
                Map<String, byte[]> map = new ArrayMap<>();
                map = (Map<String, byte[]>)msg.obj;
                if(showThreeOrTwo) {
                    SetLineChart.setLineDataABC(getContext(), lineChart, map.get("data1"), map.get("data2"),
                            map.get("data3"), danwei, SysCode.HISTYORY_DAY, length);
                } else {
                    SetLineChart.setLineData(getContext(), lineChart, map.get("data1"), map.get("data2"),
                            danwei, SysCode.HISTYORY_DAY, length);
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
