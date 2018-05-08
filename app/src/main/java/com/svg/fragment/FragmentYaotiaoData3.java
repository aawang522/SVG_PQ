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
import com.svg.common.MyApp;
import com.svg.utils.CommUtil;
import com.svg.utils.ModbusResponseListner;

import java.util.ArrayList;
import java.util.List;

/**
 * 遥调数据3
 * Created by aawang on 2017/3/24.
 */
public class FragmentYaotiaoData3 extends Fragment implements ModbusResponseListner, Handler.Callback, View.OnClickListener {

    private EditText yaotiao_xb2cs;
    private EditText yaotiao_xb3cs;
    private EditText yaotiao_xb4cs;
    private EditText yaotiao_xb5cs;
    private EditText yaotiao_xb6cs;
    private EditText yaotiao_xb7cs;
    private EditText yaotiao_xb8cs;
    private EditText yaotiao_xb9cs;
    private EditText yaotiao_xb10cs;
    private EditText yaotiao_xb11cs;
    private EditText yaotiao_xb12cs;
    private EditText yaotiao_xb13cs;
    private EditText yaotiao_xb1sn;
    private EditText yaotiao_xb2sn;
    private EditText yaotiao_xb3sn;
    private Button btn_data3commit;

    private ModbusResponseListner responseListner;
    private List<EditText> textList = new ArrayList<>();
    private Handler handler;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_yaotiao_data3, container, false);
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

        yaotiao_xb2cs = (EditText)view.findViewById(R.id.yaotiao_xb2cs);
        yaotiao_xb3cs = (EditText)view.findViewById(R.id.yaotiao_xb3cs);
        yaotiao_xb4cs = (EditText)view.findViewById(R.id.yaotiao_xb4cs);
        yaotiao_xb5cs = (EditText)view.findViewById(R.id.yaotiao_xb5cs);
        yaotiao_xb6cs = (EditText)view.findViewById(R.id.yaotiao_xb6cs);
        yaotiao_xb7cs = (EditText)view.findViewById(R.id.yaotiao_xb7cs);
        yaotiao_xb8cs = (EditText)view.findViewById(R.id.yaotiao_xb8cs);
        yaotiao_xb9cs = (EditText)view.findViewById(R.id.yaotiao_xb9cs);
        yaotiao_xb10cs = (EditText)view.findViewById(R.id.yaotiao_xb10cs);
        yaotiao_xb11cs = (EditText)view.findViewById(R.id.yaotiao_xb11cs);
        yaotiao_xb12cs = (EditText)view.findViewById(R.id.yaotiao_xb12cs);
        yaotiao_xb13cs = (EditText)view.findViewById(R.id.yaotiao_xb13cs);
        yaotiao_xb1sn = (EditText)view.findViewById(R.id.yaotiao_xb1sn);
        yaotiao_xb2sn = (EditText)view.findViewById(R.id.yaotiao_xb2sn);
        yaotiao_xb3sn = (EditText)view.findViewById(R.id.yaotiao_xb3sn);
        textList.add(yaotiao_xb2cs);
        textList.add(yaotiao_xb3cs);
        textList.add(yaotiao_xb4cs);
        textList.add(yaotiao_xb5cs);
        textList.add(yaotiao_xb6cs);
        textList.add(yaotiao_xb7cs);
        textList.add(yaotiao_xb8cs);
        textList.add(yaotiao_xb9cs);
        textList.add(yaotiao_xb10cs);
        textList.add(yaotiao_xb11cs);
        textList.add(yaotiao_xb12cs);
        textList.add(yaotiao_xb13cs);
        textList.add(yaotiao_xb1sn);
        textList.add(yaotiao_xb2sn);
        textList.add(yaotiao_xb3sn);

        btn_data3commit = (Button) view.findViewById(R.id.btn_data3commit);
        btn_data3commit.setOnClickListener(this);
    }

    @Override
    public void onClick(View v){
        switch (v.getId()){
            case R.id.btn_data3commit:
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
        // 01设备号；03读；08D4是起始位2260的十六进制；001E是30的十六进制，相当于获取15个数
        // 创建一个byte类型的buffer字节数组，用于存放询问报文
        byte buffer1[] = new byte[6];
        buffer1[0] = 0x01;
        buffer1[1] = 0x03;
        buffer1[2] = 0x08;
        buffer1[3] = (byte)0xD4;
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
        buffer1[3] = (byte)0xD4;
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
        message.what = 1003;
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
        message.what = 1013;
        message.obj = data;
        handler.sendMessage(message);
    }

    @Override
    public boolean handleMessage(Message msg) {
        switch (msg.what){
            case 1003:
                List<Long> dataList = new ArrayList<>();
                dataList = ConnectModbus.parsing32Wufuhao_YaoTiaoData3((byte[])msg.obj);
                for (int i = 0; i<textList.size();i++){
                    if(null != textList.get(i)) {
                        textList.get(i).setText(String.valueOf(dataList.get(i)));
                    }
                }
                break;
            case 1013:
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
        if(!hidden){
            getData();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        getData();
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
