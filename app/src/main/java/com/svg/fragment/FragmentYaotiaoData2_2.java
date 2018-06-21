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
import android.widget.EditText;
import android.widget.Toast;

import com.luoshihai.xxdialog.DialogViewHolder;
import com.luoshihai.xxdialog.XXDialog;
import com.svg.ConnectModbus;
import com.svg.R;
import com.svg.utils.CommUtil;
import com.svg.utils.LoginingAnimation;
import com.svg.utils.ModbusResponseListner;

import java.util.ArrayList;
import java.util.List;

/**
 * 遥调数据2
 * Created by aawang on 2017/3/24.
 */
public class FragmentYaotiaoData2_2 extends Fragment implements ModbusResponseListner, Handler.Callback, View.OnClickListener {
    private EditText yaotiao_xb1sn;
    private EditText yaotiao_xb2sn;
    private EditText yaotiao_xb3sn;
    private EditText yaotiao_xb4sn;
    private EditText yaotiao_xb5sn;
    private EditText yaotiao_xb6sn;
    private EditText yaotiao_xb7sn;
    private EditText yaotiao_xb8sn;
    private EditText yaotiao_xb9sn;
    private EditText yaotiao_xb10sn;
    private EditText yaotiao_xb11sn;
    private EditText yaotiao_xb12sn;
    private EditText yaotiao_xb13sn;
    private Button btn_data22commit;

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
        View view = inflater.inflate(R.layout.fragment_yaotiao_data2_2, container, false);
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

        yaotiao_xb1sn = (EditText)view.findViewById(R.id.yaotiao2_xb1sn);
        yaotiao_xb2sn = (EditText)view.findViewById(R.id.yaotiao2_xb2sn);
        yaotiao_xb3sn = (EditText)view.findViewById(R.id.yaotiao2_xb3sn);
        yaotiao_xb4sn = (EditText)view.findViewById(R.id.yaotiao2_xb4sn);
        yaotiao_xb5sn = (EditText)view.findViewById(R.id.yaotiao2_xb5sn);
        yaotiao_xb6sn = (EditText)view.findViewById(R.id.yaotiao2_xb6sn);
        yaotiao_xb7sn = (EditText)view.findViewById(R.id.yaotiao2_xb7sn);
        yaotiao_xb8sn = (EditText)view.findViewById(R.id.yaotiao2_xb8sn);
        yaotiao_xb9sn = (EditText)view.findViewById(R.id.yaotiao2_xb9sn);
        yaotiao_xb10sn = (EditText)view.findViewById(R.id.yaotiao2_xb10sn);
        yaotiao_xb11sn = (EditText)view.findViewById(R.id.yaotiao2_xb11sn);
        yaotiao_xb12sn = (EditText)view.findViewById(R.id.yaotiao2_xb12sn);
        yaotiao_xb13sn = (EditText)view.findViewById(R.id.yaotiao2_xb13sn);
        textList.add(yaotiao_xb1sn);
        textList.add(yaotiao_xb2sn);
        textList.add(yaotiao_xb3sn);
        textList.add(yaotiao_xb4sn);
        textList.add(yaotiao_xb5sn);
        textList.add(yaotiao_xb6sn);
        textList.add(yaotiao_xb7sn);
        textList.add(yaotiao_xb8sn);
        textList.add(yaotiao_xb9sn);
        textList.add(yaotiao_xb10sn);
        textList.add(yaotiao_xb11sn);
        textList.add(yaotiao_xb12sn);
        textList.add(yaotiao_xb13sn);

        btn_data22commit = (Button) view.findViewById(R.id.btn_data22commit);
        btn_data22commit.setOnClickListener(this);
    }

    @Override
    public void onClick(View v){
        switch (v.getId()){
            case R.id.btn_data22commit:
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
        // 01设备号；03读；08EC是起始位2290的十六进制；001A是26的十六进制，相当于获取13个数
        // 创建一个byte类型的buffer字节数组，用于存放询问报文
        byte buffer1[] = new byte[6];
        buffer1[0] = 0x01;
        buffer1[1] = 0x03;
        buffer1[2] = 0x08;
        buffer1[3] = (byte)0xEC;
        buffer1[4] = 0x00;
        buffer1[5] = 0x1A;
        return buffer1;
    }

    /**
     * 提交请求报文
     * @return
     */
    private byte[] setSubmitRequestData(){
        // 01 03 07 D0 00 08 81 44
        // 01设备号；10写；08B6是起始位2200的十六进制；13个数，26个字(0x001A)，52个字节数(0x34)
        // 创建一个byte类型的buffer字节数组，用于存放询问报文
        byte buffer1[] = new byte[67];
        buffer1[0] = 0x01;
        buffer1[1] = 0x10;
        buffer1[2] = 0x08;
        buffer1[3] = (byte)0xEC;
        buffer1[4] = 0x00;
        buffer1[5] = 0x1A;
        buffer1[6] = 0x34;

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
        message.what = 1202;
        message.obj = data;
        handler.sendMessage(message);
    }

    @Override
    public void failedResponse() {
        Message message = new Message();
        message.what = 814;
        handler.sendMessageDelayed(message, 1000);
    }

    /**
     * 获取提交返回报文的回调
     * @param data
     */
    @Override
    public void getSubmitResponseData(byte[] data) {
        Message message = new Message();
        message.what = 1212;
        message.obj = data;
        handler.sendMessage(message);
    }

    @Override
    public void submitFailedResponse() {
        Message message = new Message();
        message.what = 8140;
        handler.sendMessage(message);
    }

    @Override
    public boolean handleMessage(Message msg) {
        switch (msg.what){
            case 1202:
                List<String> dataList = new ArrayList<>();
                dataList = parsing_YaoTiaoData2((byte[])msg.obj);
                if(null != dataList && 0 < dataList.size()) {
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
            case 1212:
                if(null != loginingAnimation && loginingAnimation.isShowed()) {
                    loginingAnimation.dismissLoading();
                }
                byte[] data = (byte[])msg.obj;
                Toast.makeText(getContext(), "提交成功", Toast.LENGTH_SHORT).show();
                getData();
                break;
            case 814:
                if(null != loginingAnimation && loginingAnimation.isShowed()) {
                    loginingAnimation.dismissLoading();
                }
                if(CommUtil.isNetworkConnected(getContext())) {
                    CommUtil.showToast(getContext(), "数据刷新失败");
                }
                break;
            case 8140:
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
                handler.removeMessages(1202);
                handler.removeMessages(1212);
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
            handler.removeMessages(1202);
            handler.removeMessages(1212);
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

    /**
     * 数据2——前面都是32位浮点数，最后一个是32位无符号数
     * @return
     */
    public static List<String> parsing_YaoTiaoData2(byte[] buffer1){
        if(ConnectModbus.checkReturnCRC(buffer1)) {
            List<String> dataList = new ArrayList<>();
            int dataLength = buffer1[2] / 4;
            // 根据数据的个数，一一展示在textview中
            for (int i = 0; i < dataLength; i++) {
                short[] shorts = new short[4];
                shorts[0] = (short) (0x00FF & buffer1[4 * i + 3]);
                shorts[1] = (short) (0x00FF & buffer1[4 * i + 3 + 1]);
                shorts[2] = (short) (0x00FF & buffer1[4 * i + 3 + 2]);
                shorts[3] = (short) (0x00FF & buffer1[4 * i + 3 + 3]);

                // 32位无符号数
                long a = shorts[0] *0x1000000 +shorts[1]*0x10000  + shorts[2]*0x100 +shorts[3];
                dataList.add(String.valueOf(a));
            }
            return dataList;
        }
        return null;
    }
}
