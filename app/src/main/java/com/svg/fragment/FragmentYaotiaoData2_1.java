package com.svg.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.util.ArrayMap;
import android.text.InputFilter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.luoshihai.xxdialog.DialogViewHolder;
import com.luoshihai.xxdialog.XXDialog;
import com.svg.ConnectModbus;
import com.svg.R;
import com.svg.common.MyApp;
import com.svg.utils.CommUtil;
import com.svg.utils.HistoryResponseListner;
import com.svg.utils.LoginingAnimation;
import com.svg.utils.ModbusResponseListner;
import com.svg.utils.MoneyValueFilter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 遥调数据1
 * Created by aawang on 2017/3/24.
 */
public class FragmentYaotiaoData2_1 extends Fragment implements ModbusResponseListner, HistoryResponseListner,
        Handler.Callback, View.OnClickListener {

    private EditText yaotiao_eddl;
    private EditText yaotiao_blts;
    private EditText yaotiao_ctbb;
    private EditText yaotiao_yxms;
    private EditText yaotiao_wgbcms;
    private EditText yaotiao_xbbcms;
    private EditText yaotiao_svgrl;
    private EditText yaotiao_1gbrl;
    private EditText yaotiao_2gbrl;
    private EditText yaotiao_3gbrl;
    private EditText yaotiao_4dxfbrl;
    private EditText yaotiao_5dxfbrl;
    private EditText yaotiao_6dxfbrl;
    private Button btn_data21commit;

    private HistoryResponseListner responseListner1;
    private ModbusResponseListner responseListner2;
    private List<EditText> textList = new ArrayList<>();
    private Handler handler;
    private boolean isHidden = false;
    private boolean isPaused = false;
    private LoginingAnimation loginingAnimation;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_yaotiao_data2_1, container, false);
        init(view);
        getData();
        return view;
    }

    /**
     * 初始化控件
     * @param view
     */
    private void init(View view){
        responseListner1 = this;
        responseListner2 = this;
        handler = new Handler(this);
        loginingAnimation = new LoginingAnimation(getContext());

        yaotiao_eddl = (EditText)view.findViewById(R.id.yaotiao2_eddl);
        yaotiao_blts = (EditText)view.findViewById(R.id.yaotiao2_blts);
        yaotiao_ctbb = (EditText)view.findViewById(R.id.yaotiao2_ctbb);
        yaotiao_yxms = (EditText)view.findViewById(R.id.yaotiao2_yxms);
        yaotiao_wgbcms = (EditText)view.findViewById(R.id.yaotiao2_wgbcms);
        yaotiao_xbbcms = (EditText)view.findViewById(R.id.yaotiao2_xbbcms);
        yaotiao_svgrl = (EditText)view.findViewById(R.id.yaotiao2_svgrl);
        yaotiao_1gbrl = (EditText)view.findViewById(R.id.yaotiao2_1gbrl);
        yaotiao_2gbrl = (EditText)view.findViewById(R.id.yaotiao2_2gbrl);
        yaotiao_3gbrl = (EditText)view.findViewById(R.id.yaotiao2_3gbrl);
        yaotiao_4dxfbrl = (EditText)view.findViewById(R.id.yaotiao2_4dxfbrl);
        yaotiao_5dxfbrl = (EditText)view.findViewById(R.id.yaotiao2_5dxfbrl);
        yaotiao_6dxfbrl = (EditText)view.findViewById(R.id.yaotiao2_6dxfbrl);
//        textList.add(yaotiao_eddl);
//        textList.add(yaotiao_blts);
//        textList.add(yaotiao_ctbb);
//        textList.add(yaotiao_yxms);
//        textList.add(yaotiao_wgbcms);
//        textList.add(yaotiao_xbbcms);
        textList.add(yaotiao_svgrl);
        textList.add(yaotiao_1gbrl);
        textList.add(yaotiao_2gbrl);
        textList.add(yaotiao_3gbrl);
        textList.add(yaotiao_4dxfbrl);
        textList.add(yaotiao_5dxfbrl);
        textList.add(yaotiao_6dxfbrl);

        btn_data21commit = (Button) view.findViewById(R.id.btn_data21commit);
        btn_data21commit.setOnClickListener(this);

        // 设置小数点位数
        yaotiao_eddl.setFilters(new InputFilter[]{new MoneyValueFilter().setDigits(1)});
        yaotiao_blts.setFilters(new InputFilter[]{new MoneyValueFilter().setDigits(0)});
        yaotiao_ctbb.setFilters(new InputFilter[]{new MoneyValueFilter().setDigits(0)});
        yaotiao_yxms.setFilters(new InputFilter[]{new MoneyValueFilter().setDigits(0)});
        yaotiao_wgbcms.setFilters(new InputFilter[]{new MoneyValueFilter().setDigits(0)});
        yaotiao_xbbcms.setFilters(new InputFilter[]{new MoneyValueFilter().setDigits(0)});
    }

    @Override
    public void onClick(View v){
        switch (v.getId()){
            case R.id.btn_data21commit:
                showSubmitDialog();
                break;
        }
    }

    private void getData(){
        if(null != loginingAnimation) {
            loginingAnimation.showLoading();
        }
        // 设置A请求报文
        byte[] requestOriginalData1 = setRequestData1();
        // 设置B请求报文
        byte[] requestOriginalData2 = setRequestData2();
        // 设置C请求报文
        byte[] requestOriginalData3 = setRequestData3();
        // 调用连接modbus函数
        ConnectModbus.connectServerGetHistoryABC(MyApp.socket, requestOriginalData1, requestOriginalData2,
                requestOriginalData3, responseListner1);
    }

    /**
     * 请求报文1
     * @return
     */
    private byte[] setRequestData1(){
        // 01 03 07 D0 00 08 81 44
        // 01设备号；03读；0898是起始位2200的十六进制；000A是10的十六进制，相当于获取5个数
        // 创建一个byte类型的buffer字节数组，用于存放询问报文
        byte buffer1[] = new byte[6];
        buffer1[0] = 0x01;
        buffer1[1] = 0x03;
        buffer1[2] = 0x08;
        buffer1[3] = (byte)0x98;
        buffer1[4] = 0x00;
        buffer1[5] = 0x0A;
        return buffer1;
    }

    /**
     * 请求报文2
     * @return
     */
    private byte[] setRequestData2(){
        // 01 03 07 D0 00 08 81 44
        // 01设备号；03读；0906是起始位2310的十六进制；0006是6的十六进制，相当于获取3个数
        // 创建一个byte类型的buffer字节数组，用于存放询问报文
        byte buffer1[] = new byte[6];
        buffer1[0] = 0x01;
        buffer1[1] = 0x03;
        buffer1[2] = 0x09;
        buffer1[3] = 0x06;
        buffer1[4] = 0x00;
        buffer1[5] = 0x06;
        return buffer1;
    }

    /**
     * 请求报文3
     * @return
     */
    private byte[] setRequestData3(){
        // 01 03 07 D0 00 08 81 44
        // 01设备号；03读；0917是起始位2327的十六进制；0007是7的十六进制，相当于获取7个数(16位)
        // 创建一个byte类型的buffer字节数组，用于存放询问报文
        byte buffer1[] = new byte[6];
        buffer1[0] = 0x01;
        buffer1[1] = 0x03;
        buffer1[2] = 0x09;
        buffer1[3] = 0x17;
        buffer1[4] = 0x00;
        buffer1[5] = 0x07;
        return buffer1;
    }

    /**
     * 提交信息
     */
    private void submitData(){
        if(null != loginingAnimation) {
            loginingAnimation.showLoading();
        }
        // 设置A请求报文
        byte[] requestOriginalData1 = setSubmitRequestData1();
        // 设置B请求报文
        byte[] requestOriginalData2 = setSubmitRequestData2();
        // 设置C请求报文
        byte[] requestOriginalData3 = setSubmitRequestData3();
        // 调用连接modbus函数
        ConnectModbus.submitDataWithThree(requestOriginalData1, requestOriginalData2, requestOriginalData3, responseListner2);
    }

    /**
     * 提交请求报文
     * @return
     */
    private byte[] setSubmitRequestData1(){
        // 01 03 07 D0 00 08 81 44
        // 01设备号；10写；0898是起始位2200的十六进制；5个数，10个字(0x000A)，20个字节数(0x14)
        // 创建一个byte类型的buffer字节数组，用于存放询问报文
        byte buffer1[] = new byte[27];
        buffer1[0] = 0x01;
        buffer1[1] = 0x10;
        buffer1[2] = 0x08;
        buffer1[3] = (byte)0x98;
        buffer1[4] = 0x00;
        buffer1[5] = 0x0A;
        buffer1[6] = 0x14;

        // 额定电流
        byte[] bytes = ConnectModbus.dataToByteArray(yaotiao_eddl);
        buffer1[7] = bytes[0];
        buffer1[8] = bytes[1];
        buffer1[9] = bytes[2];
        buffer1[10] = bytes[3];

        // 并联台数
        byte[] bytes2 = ConnectModbus.dataToByteArray(yaotiao_blts);
        buffer1[19] = bytes2[0];
        buffer1[20] = bytes2[1];
        buffer1[21] = bytes2[2];
        buffer1[22] = bytes2[3];

        // CT变比
        byte[] bytes3 = ConnectModbus.dataToByteArray(yaotiao_ctbb);
        buffer1[23] = bytes3[0];
        buffer1[24] = bytes3[1];
        buffer1[25] = bytes3[2];
        buffer1[26] = bytes3[3];
        return buffer1;
    }

    /**
     * 提交请求报文
     * @return
     */
    private byte[] setSubmitRequestData2(){
        // 01 03 07 D0 00 08 81 44
        // 01设备号；10写；0906是起始位2200的十六进制；3个数，6个字(0x0006)，12个字节数(0x0C)
        // 创建一个byte类型的buffer字节数组，用于存放询问报文
        byte buffer1[] = new byte[19];
        buffer1[0] = 0x01;
        buffer1[1] = 0x10;
        buffer1[2] = 0x09;
        buffer1[3] = 0x06;
        buffer1[4] = 0x00;
        buffer1[5] = 0x06;
        buffer1[6] = 0x0C;

        // 运行模式
        byte[] bytes = ConnectModbus.dataToByteArray(yaotiao_yxms);
        buffer1[7] = bytes[0];
        buffer1[8] = bytes[1];
        buffer1[9] = bytes[2];
        buffer1[10] = bytes[3];

        // 无功补偿模式
        byte[] bytes2 = ConnectModbus.dataToByteArray(yaotiao_wgbcms);
        buffer1[11] = bytes2[0];
        buffer1[12] = bytes2[1];
        buffer1[13] = bytes2[2];
        buffer1[14] = bytes2[3];

        // 谐波补偿模式
        byte[] bytes3 = ConnectModbus.dataToByteArray(yaotiao_xbbcms);
        buffer1[15] = bytes3[0];
        buffer1[16] = bytes3[1];
        buffer1[17] = bytes3[2];
        buffer1[18] = bytes3[3];
        return buffer1;
    }

    /**
     * 提交请求报文
     * @return
     */
    private byte[] setSubmitRequestData3(){
        // 01 03 07 D0 00 08 81 44
        // 01设备号；10写；0917是起始位2327的十六进制；7个数，7个字(0x0007)，14个字节数(0x0E)
        // 创建一个byte类型的buffer字节数组，用于存放询问报文
        byte buffer1[] = new byte[21];
        buffer1[0] = 0x01;
        buffer1[1] = 0x10;
        buffer1[2] = 0x09;
        buffer1[3] = 0x17;
        buffer1[4] = 0x00;
        buffer1[5] = 0x07;
        buffer1[6] = 0x0E;

        for (int i =0;i<textList.size();i++){
            byte[] bytes = ConnectModbus.dataToByteArray(textList.get(i));
            buffer1[2*i+7] = bytes[2];
            buffer1[2*i+8] = bytes[3];
        }
        return buffer1;
    }

    /**
     * 获取返回报文的回调
     */
    @Override
    public void getResponseData(Map<String, byte[]> map) {
        Message message = new Message();
        message.what = 1201;
        message.obj = map;
        handler.sendMessage(message);
    }

    /**
     * 获取返回报文的回调
     * @param data
     */
    @Override
    public void getResponseData(byte[] data) {
    }

    @Override
    public void failedResponse() {
        Message message = new Message();
        message.what = 813;
        handler.sendMessageDelayed(message, 1000);
    }

    /**
     * 获取提交返回报文的回调
     * @param data
     */
    @Override
    public void getSubmitResponseData(byte[] data) {
        Message message = new Message();
        message.what = 1211;
        message.obj = data;
        handler.sendMessage(message);
    }

    @Override
    public void submitFailedResponse() {
        Message message = new Message();
        message.what = 8130;
        handler.sendMessage(message);
    }

    @Override
    public boolean handleMessage(Message msg) {
        switch (msg.what){
            case 1201:
                Map<String, byte[]> map = new ArrayMap<>();
                map = (Map<String, byte[]>)msg.obj;
                setContentDatas(map);
                if(null != loginingAnimation && loginingAnimation.isShowed()){
                    loginingAnimation.dismissLoading();
                }
                break;
            case 1211:
                if(null != loginingAnimation && loginingAnimation.isShowed()) {
                    loginingAnimation.dismissLoading();
                }
                byte[] data = (byte[])msg.obj;
                Toast.makeText(getContext(), "提交成功", Toast.LENGTH_SHORT).show();
                getData();
                break;
            case 813:
                if(null != loginingAnimation && loginingAnimation.isShowed()) {
                    loginingAnimation.dismissLoading();
                }
                if(CommUtil.isNetworkConnected(getContext())) {
                    CommUtil.showToast(getContext(), "数据刷新失败");
                }
                break;
            case 8130:
                if(null != loginingAnimation && loginingAnimation.isShowed()) {
                    loginingAnimation.dismissLoading();
                }
                if(CommUtil.isNetworkConnected(getContext())) {
                    CommUtil.showToast(getContext(), "数据提交失败");
                }
                break;
        }
        return false;
    }

    private void setContentDatas(Map<String, byte[]> map){
        // 第一块数据
        if(ConnectModbus.checkReturnCRC(map.get("data1"))) {
            byte[] buffer1 = map.get("data1");
            if(null != buffer1 && 3 < buffer1.length && 0 < buffer1[2]) {
                int dataLength = buffer1[2] / 4;
                // 根据数据的个数，一一展示在textview中
                for (int i = 0; i < dataLength; i++) {
                    int[] shorts = new int[4];
                    shorts[0] = 0x00FF & buffer1[4 * i + 3];
                    shorts[1] = 0x00FF & buffer1[4 * i + 3 + 1];
                    shorts[2] = 0x00FF & buffer1[4 * i + 3 + 2];
                    shorts[3] = 0x00FF & buffer1[4 * i + 3 + 3];

                    // 第0位是32位浮点数，额定电流
                    if (i == 0) {
                        // 将10进制转换成16进制
                        StringBuilder data1Str = ConnectModbus.bytesToHexFun3(shorts);
                        // 将16进制单精度浮点型转换为10进制浮点型，这就是计算出来的数据
                        Float data1 = ConnectModbus.parseHex2Float(data1Str.toString());
                        yaotiao_eddl.setText(String.valueOf(data1));
                    }
                    // 第3位是32位无符号数，并联台数
                    if (i == 3) {
                        long a = shorts[0] * 0x1000000 + shorts[1] * 0x10000 + shorts[2] * 0x100 + shorts[3];
                        yaotiao_blts.setText(String.valueOf(a));
                    }
                    // 第4位是32位浮点数，CT变比
                    if (i == 4) {
                        // 将10进制转换成16进制
                        StringBuilder data1Str = ConnectModbus.bytesToHexFun3(shorts);
                        // 将16进制单精度浮点型转换为10进制浮点型，这就是计算出来的数据
                        Float data1 = ConnectModbus.parseHex2Float(data1Str.toString());
                        yaotiao_ctbb.setText(String.valueOf(data1));
                    }
                }
            }
        }
        // 第二块数据
        if(ConnectModbus.checkReturnCRC(map.get("data2"))) {
            byte[] buffer2 = map.get("data2");
            if(null != buffer2 && 3 < buffer2.length && 0 < buffer2[2]) {
                int dataLength = buffer2[2] / 4;
                // 根据数据的个数，一一展示在textview中
                for (int i = 0; i < dataLength; i++) {
                    int[] shorts = new int[4];
                    shorts[0] = 0x00FF & buffer2[4 * i + 3];
                    shorts[1] = 0x00FF & buffer2[4 * i + 3 + 1];
                    shorts[2] = 0x00FF & buffer2[4 * i + 3 + 2];
                    shorts[3] = 0x00FF & buffer2[4 * i + 3 + 3];

                    // 第0位是32位浮点数，运行模式
                    if (i == 0) {
                        // 将10进制转换成16进制
                        StringBuilder data1Str = ConnectModbus.bytesToHexFun3(shorts);
                        // 将16进制单精度浮点型转换为10进制浮点型，这就是计算出来的数据
                        Float data1 = ConnectModbus.parseHex2Float(data1Str.toString());
                        yaotiao_yxms.setText(String.valueOf(data1));
                    }
                    // 第1位是32位无符号数，无功补偿模式
                    if (i == 1) {
                        long a = shorts[0] * 0x1000000 + shorts[1] * 0x10000 + shorts[2] * 0x100 + shorts[3];
                        yaotiao_wgbcms.setText(String.valueOf(a));
                    }
                    // 第2位是32位无符号数，谐波补偿模式
                    if (i == 2) {
                        long a = shorts[0] * 0x1000000 + shorts[1] * 0x10000 + shorts[2] * 0x100 + shorts[3];
                        yaotiao_xbbcms.setText(String.valueOf(a));
                    }
                }
            }
        }
        // 第三块数据
        if(ConnectModbus.checkReturnCRC(map.get("data3"))) {
            byte[] buffer3 = map.get("data3");
            if(null != buffer3 && 3 < buffer3.length && 0 < buffer3[2]) {
                int dataLength3 = buffer3[2] / 2;
                // 根据数据的个数，一一展示在textview中
                for (int i = 0; i < dataLength3; i++) {
                    int[] shorts2 = new int[2];
                    shorts2[0] = 0x00FF & buffer3[2 * i + 3];
                    shorts2[1] = 0x00FF & buffer3[2 * i + 3 + 1];
                    long b = shorts2[0]*0x100 +shorts2[1];
                    textList.get(i).setText(String.valueOf(b));
                }
            }
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        isHidden = hidden;
        if(!hidden){
            getData();
        } else {
            if(null != handler) {
                handler.removeMessages(1201);
                handler.removeMessages(1211);
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        // 开屏时，判断如果是在当前界面又是刚从关屏状态过来，就继续定时更新
        if(!isHidden && isPaused) {
            isPaused = false;
            getData();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        // 关屏时，记录isPaused状态位，清空消息，停止定时更新
        isPaused = true;
        if(null != handler) {
            handler.removeMessages(1201);
            handler.removeMessages(1211);
        }
    }

    /**
     * 显示提交对话框
     */
    private void showSubmitDialog(){
        // 引用lsh的github的万能对话框
        final XXDialog dialog = new XXDialog(getActivity(), R.layout.view_dialog_confirm) {
            @Override
            public void convert(DialogViewHolder dialogViewHolder) {
                dialogViewHolder.setText(R.id.title, "是否确定提交？");
                // 确认按钮监听
                dialogViewHolder.setText(R.id.confirm, "确定")
                        .setOnClick(R.id.confirm, new View.OnClickListener(){@Override
                        public void onClick(View view) {
                            submitData();
                            dismiss();
                        }
                        });
                // 取消按钮监听
                dialogViewHolder.setText(R.id.cancel, "取消")
                        .setOnClick(R.id.cancel, new View.OnClickListener(){@Override
                        public void onClick(View view) {
                            dismiss();
                        }
                        });
            }
        };
        // 设置外界点击不取消对话框
        dialog.setCancelAble(false);
        int width = CommUtil.getScreenWidth(getActivity()) * 3/4;
        int height = CommUtil.getScreenHeight(getActivity()) * 1/3;
        dialog.backgroundLight(0.5).setWidthAndHeight(width, height).showDialog();
    }
}
