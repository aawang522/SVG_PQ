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
import com.svg.utils.CommUtil;
import com.svg.utils.ModbusResponseListner;
import com.svg.utils.MoneyValueFilter;

import java.util.ArrayList;
import java.util.List;

/**
 * 遥调数据1
 * Created by aawang on 2017/3/24.
 */
public class FragmentYaotiaoData1 extends Fragment implements ModbusResponseListner, Handler.Callback, View.OnClickListener {

    private EditText yaotiao_eddl;
    private EditText yaotiao_zxxms;
    private EditText yaotiao_zlceddy;
    private EditText yaotiao_blts;
    private EditText yaotiao_ctbb;
    private EditText yaotiao_hebb;
    private EditText yaotiao_gybh1z;
    private EditText yaotiao_glbhz;
    private EditText yaotiao_glbhsj;
    private EditText yaotiao_glfzbh;
    private EditText yaotiao_scdlqx;
    private EditText yaotiao_scdlqxsj;
    private EditText yaotiao_jdbhdl;
    private EditText yaotiao_dygybhz;
    private EditText yaotiao_dyqybhz;
    private Button btn_data1commit;

    private ModbusResponseListner responseListner;
    private List<EditText> textList = new ArrayList<>();
    private Handler handler;
    private boolean isHidden = false;
    private boolean isPaused = false;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_yaotiao_data1, container, false);
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

        yaotiao_eddl = (EditText)view.findViewById(R.id.yaotiao_eddl);
        yaotiao_zxxms = (EditText)view.findViewById(R.id.yaotiao_zxxms);
        yaotiao_zlceddy = (EditText)view.findViewById(R.id.yaotiao_zlceddy);
        yaotiao_blts = (EditText)view.findViewById(R.id.yaotiao_blts);
        yaotiao_ctbb = (EditText)view.findViewById(R.id.yaotiao_ctbb);
        yaotiao_hebb = (EditText)view.findViewById(R.id.yaotiao_hebb);
        yaotiao_gybh1z = (EditText)view.findViewById(R.id.yaotiao_gybh1z);
        yaotiao_glbhz = (EditText)view.findViewById(R.id.yaotiao_glbhz);
        yaotiao_glbhsj = (EditText)view.findViewById(R.id.yaotiao_glbhsj);
        yaotiao_glfzbh = (EditText)view.findViewById(R.id.yaotiao_glfzbh);
        yaotiao_scdlqx = (EditText)view.findViewById(R.id.yaotiao_scdlqx);
        yaotiao_scdlqxsj = (EditText)view.findViewById(R.id.yaotiao_scdlqxsj);
        yaotiao_jdbhdl = (EditText)view.findViewById(R.id.yaotiao_jdbhdl);
        yaotiao_dygybhz = (EditText)view.findViewById(R.id.yaotiao_dygybhz);
        yaotiao_dyqybhz = (EditText)view.findViewById(R.id.yaotiao_dyqybhz);
        textList.add(yaotiao_eddl);
        textList.add(yaotiao_zxxms);
        textList.add(yaotiao_zlceddy);
        textList.add(yaotiao_blts);
        textList.add(yaotiao_ctbb);
        textList.add(yaotiao_hebb);
        textList.add(yaotiao_gybh1z);
        textList.add(yaotiao_glbhz);
        textList.add(yaotiao_glbhsj);
        textList.add(yaotiao_glfzbh);
        textList.add(yaotiao_scdlqx);
        textList.add(yaotiao_scdlqxsj);
        textList.add(yaotiao_jdbhdl);
        textList.add(yaotiao_dygybhz);
        textList.add(yaotiao_dyqybhz);

        btn_data1commit = (Button) view.findViewById(R.id.btn_data1commit);
        btn_data1commit.setOnClickListener(this);

        // 设置输入时小数点位数
        yaotiao_eddl.setFilters(new InputFilter[] {new MoneyValueFilter().setDigits(1)});
        yaotiao_zxxms.setFilters(new InputFilter[]{new MoneyValueFilter().setDigits(0)});
        yaotiao_zlceddy.setFilters(new InputFilter[]{new MoneyValueFilter().setDigits(0)});
        yaotiao_blts.setFilters(new InputFilter[]{new MoneyValueFilter().setDigits(0)});
        yaotiao_ctbb.setFilters(new InputFilter[]{new MoneyValueFilter().setDigits(0)});
        yaotiao_hebb.setFilters(new InputFilter[]{new MoneyValueFilter().setDigits(0)});
        yaotiao_gybh1z.setFilters(new InputFilter[]{new MoneyValueFilter().setDigits(1)});
        yaotiao_glbhz.setFilters(new InputFilter[]{new MoneyValueFilter().setDigits(1)});
        yaotiao_glbhsj.setFilters(new InputFilter[]{new MoneyValueFilter().setDigits(0)});
        yaotiao_glfzbh.setFilters(new InputFilter[]{new MoneyValueFilter().setDigits(1)});
        yaotiao_scdlqx.setFilters(new InputFilter[]{new MoneyValueFilter().setDigits(1)});
        yaotiao_scdlqxsj.setFilters(new InputFilter[]{new MoneyValueFilter().setDigits(0)});
        yaotiao_jdbhdl.setFilters(new InputFilter[]{new MoneyValueFilter().setDigits(1)});
        yaotiao_dygybhz.setFilters(new InputFilter[]{new MoneyValueFilter().setDigits(1)});
        yaotiao_dyqybhz.setFilters(new InputFilter[]{new MoneyValueFilter().setDigits(1)});
    }

    @Override
    public void onClick(View v){
        switch (v.getId()){
            case R.id.btn_data1commit:
                showSubmitDialog();
                break;
        }
    }

    private void getData(){
        // 设置请求报文
        byte[] requestOriginalData = setRequestData();
        // 调用连接modbus函数
        ConnectModbus.connectServerWithTCPSocket(requestOriginalData, responseListner);
    }

    /**
     * 提交信息
     */
    private void submitData(){
        // 调用连接modbus函数
        ConnectModbus.submitDataWithTCPSocket(setSubmitRequestData(), responseListner);
    }

    /**
     * 请求报文
     * @return
     */
    private byte[] setRequestData(){
        // 01 03 07 D0 00 08 81 44
        // 01设备号；03读；0898是起始位2200的十六进制；001E是30的十六进制，相当于获取15个数
        // 创建一个byte类型的buffer字节数组，用于存放询问报文
        byte buffer1[] = new byte[6];
        buffer1[0] = 0x01;
        buffer1[1] = 0x03;
        buffer1[2] = 0x08;
        buffer1[3] = (byte)0x98;
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
        // 01设备号；10写；0898是起始位2200的十六进制；15个数，30个字(0x001E)，60个字节数(0x3C)
        // 创建一个byte类型的buffer字节数组，用于存放询问报文
        byte buffer1[] = new byte[67];
        buffer1[0] = 0x01;
        buffer1[1] = 0x10;
        buffer1[2] = 0x08;
        buffer1[3] = (byte)0x98;
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
        message.what = 1001;
        message.obj = data;
        handler.sendMessage(message);
    }

    /**
     * 获取提交返回报文的回调
     * @param data
     */
    @Override
    public void getSubmitResponseData(byte[] data) {
        Message message = new Message();
        message.what = 1011;
        message.obj = data;
        handler.sendMessage(message);
    }

    @Override
    public boolean handleMessage(Message msg) {
        switch (msg.what){
            case 1001:
                Log.d("dingshi",  "获取遥调1");
                List<String> dataList = new ArrayList<>();
                dataList = ConnectModbus.parsing_YaoTiaoData1((byte[])msg.obj);
                if (null != dataList && 0 < dataList.size()) {
                    for (int i = 0; i < textList.size(); i++) {
                        if (null != textList.get(i)) {
                            textList.get(i).setText(String.valueOf(dataList.get(i)));
                        }
                    }
                }

                break;
            case 1011:
                byte[] data = (byte[])msg.obj;
                Toast.makeText(getContext(), "提交成功", Toast.LENGTH_SHORT).show();
                getData();
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
                handler.removeMessages(1001);
                handler.removeMessages(1011);
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
            handler.removeMessages(1001);
            handler.removeMessages(1011);
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
