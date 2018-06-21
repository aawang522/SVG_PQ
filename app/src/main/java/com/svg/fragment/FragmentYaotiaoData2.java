package com.svg.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.InputFilter;
import android.util.Log;
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
import com.svg.utils.LoginingAnimation;
import com.svg.utils.ModbusResponseListner;
import com.svg.utils.MoneyValueFilter;

import java.util.ArrayList;
import java.util.List;

/**
 * 遥调数据2
 * Created by aawang on 2017/3/24.
 */
public class FragmentYaotiaoData2 extends Fragment implements ModbusResponseListner, Handler.Callback, View.OnClickListener {

    private EditText yaotiao_dyycddy;
    private EditText yaotiao_xb1mbz;
    private EditText yaotiao_xb2mbz;
    private EditText yaotiao_xb3mbz;
    private EditText yaotiao_xb4mbz;
    private EditText yaotiao_xb5mbz;
    private EditText yaotiao_xb6mbz;
    private EditText yaotiao_xb7mbz;
    private EditText yaotiao_xb8mbz;
    private EditText yaotiao_xb9mbz;
    private EditText yaotiao_xb10mbz;
    private EditText yaotiao_xb11mbz;
    private EditText yaotiao_xb12mbz;
    private EditText yaotiao_xb13mbz;
    private EditText yaotiao_xb1cs;
    private Button btn_data2commit;

    private ModbusResponseListner responseListner;
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
        View view = inflater.inflate(R.layout.fragment_yaotiao_data2, container, false);
        init(view);
        getData();
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

        yaotiao_dyycddy = (EditText)view.findViewById(R.id.yaotiao_dyycddy);
        yaotiao_xb1mbz = (EditText)view.findViewById(R.id.yaotiao_xb1mbz);
        yaotiao_xb2mbz = (EditText)view.findViewById(R.id.yaotiao_xb2mbz);
        yaotiao_xb3mbz = (EditText)view.findViewById(R.id.yaotiao_xb3mbz);
        yaotiao_xb4mbz = (EditText)view.findViewById(R.id.yaotiao_xb4mbz);
        yaotiao_xb5mbz = (EditText)view.findViewById(R.id.yaotiao_xb5mbz);
        yaotiao_xb6mbz = (EditText)view.findViewById(R.id.yaotiao_xb6mbz);
        yaotiao_xb7mbz = (EditText)view.findViewById(R.id.yaotiao_xb7mbz);
        yaotiao_xb8mbz = (EditText)view.findViewById(R.id.yaotiao_xb8mbz);
        yaotiao_xb9mbz = (EditText)view.findViewById(R.id.yaotiao_xb9mbz);
        yaotiao_xb10mbz = (EditText)view.findViewById(R.id.yaotiao_xb10mbz);
        yaotiao_xb11mbz = (EditText)view.findViewById(R.id.yaotiao_xb11mbz);
        yaotiao_xb12mbz = (EditText)view.findViewById(R.id.yaotiao_xb12mbz);
        yaotiao_xb13mbz = (EditText)view.findViewById(R.id.yaotiao_xb13mbz);
        yaotiao_xb1cs = (EditText)view.findViewById(R.id.yaotiao_xb1cs);
        textList.add(yaotiao_dyycddy);
        textList.add(yaotiao_xb1mbz);
        textList.add(yaotiao_xb2mbz);
        textList.add(yaotiao_xb3mbz);
        textList.add(yaotiao_xb4mbz);
        textList.add(yaotiao_xb5mbz);
        textList.add(yaotiao_xb6mbz);
        textList.add(yaotiao_xb7mbz);
        textList.add(yaotiao_xb8mbz);
        textList.add(yaotiao_xb9mbz);
        textList.add(yaotiao_xb10mbz);
        textList.add(yaotiao_xb11mbz);
        textList.add(yaotiao_xb12mbz);
        textList.add(yaotiao_xb13mbz);
        textList.add(yaotiao_xb1cs);

        btn_data2commit = (Button) view.findViewById(R.id.btn_data2commit);
        btn_data2commit.setOnClickListener(this);

        // 设置小数点位数
        yaotiao_dyycddy.setFilters(new InputFilter[]{new MoneyValueFilter().setDigits(1)});
        yaotiao_xb1mbz.setFilters(new InputFilter[]{new MoneyValueFilter().setDigits(1)});
        yaotiao_xb2mbz.setFilters(new InputFilter[]{new MoneyValueFilter().setDigits(1)});
        yaotiao_xb3mbz.setFilters(new InputFilter[]{new MoneyValueFilter().setDigits(1)});
        yaotiao_xb4mbz.setFilters(new InputFilter[]{new MoneyValueFilter().setDigits(1)});
        yaotiao_xb5mbz.setFilters(new InputFilter[]{new MoneyValueFilter().setDigits(1)});
        yaotiao_xb6mbz.setFilters(new InputFilter[]{new MoneyValueFilter().setDigits(1)});
        yaotiao_xb7mbz.setFilters(new InputFilter[]{new MoneyValueFilter().setDigits(1)});
        yaotiao_xb8mbz.setFilters(new InputFilter[]{new MoneyValueFilter().setDigits(1)});
        yaotiao_xb9mbz.setFilters(new InputFilter[]{new MoneyValueFilter().setDigits(1)});
        yaotiao_xb10mbz.setFilters(new InputFilter[]{new MoneyValueFilter().setDigits(1)});
        yaotiao_xb11mbz.setFilters(new InputFilter[]{new MoneyValueFilter().setDigits(1)});
        yaotiao_xb12mbz.setFilters(new InputFilter[]{new MoneyValueFilter().setDigits(1)});
        yaotiao_xb13mbz.setFilters(new InputFilter[]{new MoneyValueFilter().setDigits(1)});
        yaotiao_xb1cs.setFilters(new InputFilter[]{new MoneyValueFilter().setDigits(0)});
    }

    @Override
    public void onClick(View v){
        switch (v.getId()){
            case R.id.btn_data2commit:
                showSubmitDialog();
                break;
        }
    }

    private void getData(){
        if(null != loginingAnimation) {
            loginingAnimation.showLoading();
        }
        // 设置请求报文
        byte[] requestOriginalData = setRequestData();
        // 调用连接modbus函数
        ConnectModbus.connectServerWithTCPSocket(requestOriginalData, responseListner);
    }

    /**
     * 提交信息
     */
    private void submitData(){
        if(null != loginingAnimation) {
            loginingAnimation.showLoading();
        }
        // 调用连接modbus函数
        ConnectModbus.submitDataWithTCPSocket(setSubmitRequestData(), responseListner);
    }

    /**
     * 请求报文
     * @return
     */
    private byte[] setRequestData(){
        // 01 03 07 D0 00 08 81 44
        // 01设备号；03读；08B6是起始位2230的十六进制；001E是30的十六进制，相当于获取15个数
        // 创建一个byte类型的buffer字节数组，用于存放询问报文
        byte buffer1[] = new byte[6];
        buffer1[0] = 0x01;
        buffer1[1] = 0x03;
        buffer1[2] = 0x08;
        buffer1[3] = (byte)0xB6;
        buffer1[4] = 0x00;
        buffer1[5] = 0x1E;
        return buffer1;
    }

    /**
     * 提交请求报文
     * @return
     */
    private byte[] setSubmitRequestData(){
        // 01 03 07 D0 00 08 81 44
        // 01设备号；10写；08B6是起始位2200的十六进制；15个数，30个字(0x001E)，60个字节数(0x3C)
        // 创建一个byte类型的buffer字节数组，用于存放询问报文
        byte buffer1[] = new byte[67];
        buffer1[0] = 0x01;
        buffer1[1] = 0x10;
        buffer1[2] = 0x08;
        buffer1[3] = (byte)0xB6;
        buffer1[4] = 0x00;
        buffer1[5] = 0x1E;
        buffer1[6] = 0x3C;

        for (int i =0;i<textList.size();i++){
            byte[] bytes = ConnectModbus.dataToByteArray(textList.get(i));
            buffer1[4*i+7] = bytes[0];
            buffer1[4*i+8] = bytes[1];
            buffer1[4*i+9] = bytes[2];
            buffer1[4*i+10] = bytes[3];
        }
        return buffer1;
    }

    /**
     * 获取返回报文的回调
     * @param data
     */
    @Override
    public void getResponseData(byte[] data) {
        Message message = new Message();
        message.what = 1002;
        message.obj = data;
        handler.sendMessage(message);
    }

    @Override
    public void failedResponse() {
        Message message = new Message();
        message.what = 812;
        handler.sendMessageDelayed(message, 1000);
    }

    /**
     * 获取提交返回报文的回调
     * @param data
     */
    @Override
    public void getSubmitResponseData(byte[] data) {
        Message message = new Message();
        message.what = 1012;
        message.obj = data;
        handler.sendMessage(message);
    }

    @Override
    public void submitFailedResponse() {
        Message message = new Message();
        message.what = 8120;
        handler.sendMessage(message);
    }

    @Override
    public boolean handleMessage(Message msg) {
        switch (msg.what){
            case 1002:
                Log.d("dingshi",  "获取遥调2");
                List<String> dataList = new ArrayList<>();
                dataList = ConnectModbus.parsing_YaoTiaoData2((byte[])msg.obj);
                if (null != dataList && 0 < dataList.size()) {
                    for (int i = 0; i < textList.size(); i++) {
                        if (i < textList.size() && null != textList.get(i)) {
                            textList.get(i).setText(String.valueOf(dataList.get(i)));
                        }
                    }
                }
                if(null != loginingAnimation && loginingAnimation.isShowed()) {
                    loginingAnimation.dismissLoading();
                }
                break;
            case 1012:
                if(null != loginingAnimation && loginingAnimation.isShowed()) {
                    loginingAnimation.dismissLoading();
                }
                byte[] data = (byte[])msg.obj;
                Toast.makeText(getContext(), "提交成功", Toast.LENGTH_SHORT).show();
                getData();
                break;
            case 812:
                if(null != loginingAnimation && loginingAnimation.isShowed()) {
                    loginingAnimation.dismissLoading();
                }
                if(CommUtil.isNetworkConnected(getContext())) {
                    CommUtil.showToast(getContext(), "数据刷新失败");
                }
                break;
            case 8120:
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

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        isHidden = hidden;
        if(!hidden){
            getData();
        } else {
            if(null != handler) {
                handler.removeMessages(1002);
                handler.removeMessages(1012);
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
            handler.removeMessages(1002);
            handler.removeMessages(1012);
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
