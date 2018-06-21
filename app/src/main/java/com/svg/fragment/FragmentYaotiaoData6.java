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
import android.widget.CompoundButton;
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
 * 遥调数据6
 * Created by aawang on 2017/3/24.
 */
public class FragmentYaotiaoData6 extends Fragment implements ModbusResponseListner, Handler.Callback, View.OnClickListener {

    private CheckBox yaotiao_gybh1;
    private CheckBox yaotiao_gybh2;
    private CheckBox yaotiao_qybh1;
    private CheckBox yaotiao_qybh2;
    private CheckBox yaotiao_dyfxbh;
    private CheckBox yaotiao_dyqxbh;
    private CheckBox yaotiao_dybphbh;
    private CheckBox yaotiao_glbh;
    private CheckBox yaotiao_jddlbh1;
    private CheckBox yaotiao_zlgybh;
    private CheckBox yaotiao_zlqybh;
    private CheckBox yaotiao_dyqdbh;
    private CheckBox yaotiao_cdcsbh;
    private Button btn_data6commit;

    private ModbusResponseListner responseListner;
    private List<CheckBox> textList = new ArrayList<>();
    private Handler handler;
    // 提交报文的数据
    private byte[] submitBytes = new byte[15];
    // 标志位，false表示是在初始化展示数据，此时不用对checkbox进行监听
    private boolean flag = false;
    private boolean isHidden = false;
    private boolean isPaused = false;
    private LoginingAnimation loginingAnimation;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_yaotiao_data6, container, false);
        init(view);
        setCheckListener();
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

        yaotiao_gybh1 = (CheckBox)view.findViewById(R.id.yaotiao_gybh1);
        yaotiao_gybh2 = (CheckBox)view.findViewById(R.id.yaotiao_gybh2);
        yaotiao_qybh1 = (CheckBox)view.findViewById(R.id.yaotiao_qybh1);
        yaotiao_qybh2 = (CheckBox)view.findViewById(R.id.yaotiao_qybh2);
        yaotiao_dyfxbh = (CheckBox)view.findViewById(R.id.yaotiao_dyfxbh);
        yaotiao_dyqxbh = (CheckBox)view.findViewById(R.id.yaotiao_dyqxbh);
        yaotiao_dybphbh = (CheckBox)view.findViewById(R.id.yaotiao_dybphbh);
        yaotiao_glbh = (CheckBox)view.findViewById(R.id.yaotiao_glbh);
        yaotiao_jddlbh1 = (CheckBox)view.findViewById(R.id.yaotiao_jddlbh1);
        yaotiao_zlgybh = (CheckBox)view.findViewById(R.id.yaotiao_zlgybh);
        yaotiao_zlqybh = (CheckBox)view.findViewById(R.id.yaotiao_zlqybh);
        yaotiao_dyqdbh = (CheckBox)view.findViewById(R.id.yaotiao_dyqdbh);
        yaotiao_cdcsbh = (CheckBox)view.findViewById(R.id.yaotiao_cdcsbh);
        btn_data6commit = (Button) view.findViewById(R.id.btn_data6commit);
        textList.add(yaotiao_gybh1);
        textList.add(yaotiao_gybh2);
        textList.add(yaotiao_qybh1);
        textList.add(yaotiao_qybh2);
        textList.add(yaotiao_dyfxbh);
        textList.add(yaotiao_dyqxbh);
        textList.add(yaotiao_dybphbh);
        textList.add(yaotiao_glbh);
        textList.add(yaotiao_jddlbh1);
        textList.add(yaotiao_zlgybh);
        textList.add(yaotiao_zlqybh);
        textList.add(yaotiao_dyqdbh);
        textList.add(yaotiao_cdcsbh);
        btn_data6commit.setOnClickListener(this);
    }

    private void setCheckListener(){
        yaotiao_gybh1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(flag) {
                    submitBytes = ConnectModbus.assemblyWei_YaoTiaoData6(submitBytes, 0);
                }
            }
        });
        yaotiao_gybh2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(flag) {
                    submitBytes = ConnectModbus.assemblyWei_YaoTiaoData6(submitBytes, 1);
                }
            }
        });
        yaotiao_qybh1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(flag) {
                    submitBytes = ConnectModbus.assemblyWei_YaoTiaoData6(submitBytes, 2);
                }
            }
        });
        yaotiao_qybh2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(flag) {
                    submitBytes = ConnectModbus.assemblyWei_YaoTiaoData6(submitBytes, 3);
                }
            }
        });
        yaotiao_dyfxbh.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(flag) {
                    submitBytes = ConnectModbus.assemblyWei_YaoTiaoData6(submitBytes, 4);
                }
            }
        });
        yaotiao_dyqxbh.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(flag) {
                    submitBytes = ConnectModbus.assemblyWei_YaoTiaoData6(submitBytes, 5);
                }
            }
        });
        yaotiao_dybphbh.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(flag) {
                    submitBytes = ConnectModbus.assemblyWei_YaoTiaoData6(submitBytes, 6);
                }
            }
        });

        yaotiao_glbh.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(flag) {
                    submitBytes = ConnectModbus.assemblyWei_YaoTiaoData6(submitBytes, 7);
                }
            }
        });
        yaotiao_jddlbh1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(flag) {
                    submitBytes = ConnectModbus.assemblyWei_YaoTiaoData6(submitBytes, 8);
                }
            }
        });
        yaotiao_zlgybh.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(flag) {
                    submitBytes = ConnectModbus.assemblyWei_YaoTiaoData6(submitBytes, 9);
                }
            }
        });
        yaotiao_zlqybh.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(flag) {
                    submitBytes = ConnectModbus.assemblyWei_YaoTiaoData6(submitBytes, 10);
                }
            }
        });
        yaotiao_dyqdbh.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(flag) {
                    submitBytes = ConnectModbus.assemblyWei_YaoTiaoData6(submitBytes, 11);
                }
            }
        });
        yaotiao_cdcsbh.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(flag) {
                    submitBytes = ConnectModbus.assemblyWei_YaoTiaoData6(submitBytes, 12);
                }
            }
        });
    }

    @Override
    public void onClick(View v){
        switch (v.getId()){
            case R.id.btn_data6commit:
                showSubmitDialog();
                break;
        }
    }

    /**
     * 获取信息
     */
    private void getData(){
        if(null != loginingAnimation) {
            loginingAnimation.showLoading();
        }
        btn_data6commit.setEnabled(false);
        flag = false;
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
        ConnectModbus.submitDataWithTCPSocket(submitBytes, responseListner);
    }

    /**
     * 请求报文
     * @return
     */
    private byte[] setRequestData(){
        // 01 03 07 D0 00 08 81 44
        // 01设备号；03读；0914是起始位2324的十六进制；0003是3的十六进制，相当于获取3个数(这里不是双字节)
        // 创建一个byte类型的buffer字节数组，用于存放询问报文
        byte buffer1[] = new byte[6];
        buffer1[0] = 0x01;
        buffer1[1] = 0x03;
        buffer1[2] = 0x09;
        buffer1[3] = 0x14;
        buffer1[4] = 0x00;
        buffer1[5] = 0x03;
        return buffer1;
    }

    /**
     * 提交请求报文
     * @return
     */
    private byte[] setSubmitRequestData(byte[] data){
        // 01 03 07 D0 00 08 81 44
        // 01设备号；10写；0914是起始位2324的十六进制；3个寄存器，字的长度是6，十六进制是0006；字节长度是12，十六进制是000C
        // 创建一个byte类型的buffer字节数组，用于存放询问报文
        byte buffer1[] = new byte[13];
        buffer1[0] = 0x01;
        buffer1[1] = 0x10;
        buffer1[2] = 0x09;
        buffer1[3] = 0x14;
        buffer1[4] = 0x00;
        buffer1[5] = 0x03;
        buffer1[6] = 0x06;
        buffer1[7] = data[3];
        buffer1[8] = data[4];
        buffer1[9] = data[5];
        buffer1[10] = data[6];
        buffer1[11] = data[7];
        buffer1[12] = data[8];
        return buffer1;
    }

    /**
     * 获取返回报文的回调
     * @param data
     */
    @Override
    public void getResponseData(byte[] data) {
        submitBytes = setSubmitRequestData(data);

        Message message = new Message();
        message.what = 1006;
        message.obj = data;
        handler.sendMessage(message);
    }

    @Override
    public void failedResponse() {
        Message message = new Message();
        message.what = 818;
        handler.sendMessageDelayed(message, 1000);
    }

    /**
     * 获取提交返回报文的回调
     * @param data
     */
    @Override
    public void getSubmitResponseData(byte[] data) {
        Message message = new Message();
        message.what = 1016;
        message.obj = data;
        handler.sendMessage(message);
    }

    @Override
    public void submitFailedResponse() {
        Message message = new Message();
        message.what = 8180;
        handler.sendMessage(message);
    }

    @Override
    public boolean handleMessage(Message msg) {
        switch (msg.what){
            case 1006:
                List<Boolean> dataList = new ArrayList<>();
                dataList = ConnectModbus.parsingWei_YaoTiaoData6((byte[])msg.obj);
                if (null != dataList && 0 < dataList.size()) {
                    for (int i = 0; i < textList.size(); i++) {
                        textList.get(i).setChecked(dataList.get(i));
                    }
                }
                btn_data6commit.setEnabled(true);
                flag = true;
                if(null != loginingAnimation && loginingAnimation.isShowed()) {
                    loginingAnimation.dismissLoading();
                }
                break;
            case 1016:
                if(null != loginingAnimation && loginingAnimation.isShowed()) {
                    loginingAnimation.dismissLoading();
                }
                byte[] data = (byte[])msg.obj;
                Toast.makeText(getContext(), "提交成功", Toast.LENGTH_SHORT).show();
                getData();
                break;
            case 818:
                if(null != loginingAnimation && loginingAnimation.isShowed()) {
                    loginingAnimation.dismissLoading();
                }
                if(CommUtil.isNetworkConnected(getContext())) {
                    CommUtil.showToast(getContext(), "数据刷新失败");
                }
                break;
            case 8180:
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
                handler.removeMessages(1006);
                handler.removeMessages(1016);
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
            handler.removeMessages(1006);
            handler.removeMessages(1016);
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
