package com.svg.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.luoshihai.xxdialog.DialogViewHolder;
import com.luoshihai.xxdialog.XXDialog;
import com.svg.ConnectModbus;
import com.svg.R;
import com.svg.common.MyApp;
import com.svg.utils.CommUtil;
import com.svg.utils.LoginingAnimation;
import com.svg.utils.ModbusResponseListner;

import java.util.ArrayList;
import java.util.List;

/**
 * 控制Fragment
 * Created by aawang on 2017/3/24.
 */
public class FragmentKongzhi extends Fragment implements ModbusResponseListner, Handler.Callback, View.OnClickListener{

    private CheckBox checkZZQD;
    private CheckBox checkZZTJ;
    private CheckBox checkZZFW;
    private Button btn_kongzhi;
    private ModbusResponseListner responseListner;
    private Handler handler;
    private LoginingAnimation loginingAnimation;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_kongzhi, container, false);
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
        loginingAnimation = new LoginingAnimation(getContext());

        checkZZQD = (CheckBox)view.findViewById(R.id.checkZZQD);
        checkZZTJ = (CheckBox)view.findViewById(R.id.checkZZTJ);
        checkZZFW = (CheckBox)view.findViewById(R.id.checkZZFW);
        btn_kongzhi = (Button) view.findViewById(R.id.btn_kongzhi);
        btn_kongzhi.setOnClickListener(this);
        checkZZQD.setOnClickListener(this);
        checkZZTJ.setOnClickListener(this);
        checkZZFW.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.checkZZQD:
            case R.id.checkZZTJ:
            case R.id.checkZZFW:
                setOneChecked((CheckBox) v.findViewById(v.getId()));
                break;
            case R.id.btn_kongzhi:
                showSubmitDialog();
                break;
        }
    }

    public void setOneChecked(CheckBox checkBox){
        // 先将所有按钮的背景都清除为白底黑字
        checkZZQD.setChecked(false);
        checkZZTJ.setChecked(false);
        checkZZFW.setChecked(false);

        // 然后将制定的TextView改变样式
        checkBox.setChecked(true);
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
     * 提交请求报文
     * @return
     */
    private byte[] setSubmitRequestData(){
        // 01 03 07 D0 00 08 81 44
        // 01设备号；10写；0898是起始位2200的十六进制；15个数，30个字(0x001E)，60个字节数(0x3C)
        // 创建一个byte类型的buffer字节数组，用于存放询问报文
        byte buffer1[] = new byte[9];
        buffer1[0] = 0x01;
        buffer1[1] = 0x10;
        buffer1[2] = 0x09;
        buffer1[3] = (byte)0x1E;
        buffer1[4] = 0x00;
        buffer1[5] = 0x01;
        buffer1[6] = 0x02;

        // 启动
        if(checkZZQD.isChecked()){
            buffer1[7] = 0x00;
            buffer1[8] = (byte) 0xAA;
        }
        // 停机
        else if (checkZZTJ.isChecked()){
            buffer1[7] = (byte) 0x55;
            buffer1[8] = 0x00;
        }
        // 复位
        else if (checkZZFW.isChecked()){
            buffer1[7] = (byte) 0xCC;
            buffer1[8] = 0x00;
        }
        return buffer1;
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

    }

    /**
     * 获取提交返回报文的回调
     * @param data
     */
    @Override
    public void getSubmitResponseData(byte[] data) {
        Message message = new Message();
        message.what = 207;
        message.obj = data;
        handler.sendMessage(message);
    }

    @Override
    public void submitFailedResponse() {
        Message message = new Message();
        message.what = 807;
        handler.sendMessage(message);
    }

    @Override
    public boolean handleMessage(Message msg) {
        switch (msg.what){
            case 207:
                if(null != loginingAnimation && loginingAnimation.isShowed()) {
                    loginingAnimation.dismissLoading();
                }
                byte[] data = (byte[])msg.obj;
                Toast.makeText(getContext(), "提交成功", Toast.LENGTH_SHORT).show();
                break;
            case 807:
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
        if(!hidden){
        }
    }

    @Override
    public void onResume() {
        super.onResume();
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
