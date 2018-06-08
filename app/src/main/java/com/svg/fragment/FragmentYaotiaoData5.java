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
 * 遥调数据5
 * Created by aawang on 2017/3/24.
 */
public class FragmentYaotiaoData5 extends Fragment implements ModbusResponseListner, Handler.Callback, View.OnClickListener {
    private EditText yaotiao_wgzldl;
    private EditText yaotiao_ppdxkz;
    private EditText yaotiao_svgrl;
    private EditText yaotiao_1gbrl;
    private EditText yaotiao_2gbrl;
    private EditText yaotiao_3gbrl;
    private EditText yaotiao_4dxfbrl;
    private EditText yaotiao_5dxfbrl;
    private EditText yaotiao_6dxfbrl;
    private Button btn_data5commit;

    private ModbusResponseListner responseListner;
    private List<EditText> textList = new ArrayList<>();
    private Handler handler;
    private static byte[] data6Info = new byte[6];

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_yaotiao_data5, container, false);
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

        yaotiao_wgzldl = (EditText)view.findViewById(R.id.yaotiao_wgzldl);
        yaotiao_ppdxkz = (EditText)view.findViewById(R.id.yaotiao_ppdxkz);
        yaotiao_svgrl = (EditText)view.findViewById(R.id.yaotiao_svgrl);
        yaotiao_1gbrl = (EditText)view.findViewById(R.id.yaotiao_1gbrl);
        yaotiao_2gbrl = (EditText)view.findViewById(R.id.yaotiao_2gbrl);
        yaotiao_3gbrl = (EditText)view.findViewById(R.id.yaotiao_3gbrl);
        yaotiao_4dxfbrl = (EditText)view.findViewById(R.id.yaotiao_4dxfbrl);
        yaotiao_5dxfbrl = (EditText)view.findViewById(R.id.yaotiao_5dxfbrl);
        yaotiao_6dxfbrl = (EditText)view.findViewById(R.id.yaotiao_6dxfbrl);
        textList.add(yaotiao_wgzldl);
        textList.add(yaotiao_ppdxkz);
        textList.add(yaotiao_svgrl);
        textList.add(yaotiao_1gbrl);
        textList.add(yaotiao_2gbrl);
        textList.add(yaotiao_3gbrl);
        textList.add(yaotiao_4dxfbrl);
        textList.add(yaotiao_5dxfbrl);
        textList.add(yaotiao_6dxfbrl);

        btn_data5commit = (Button) view.findViewById(R.id.btn_data5commit);
        btn_data5commit.setOnClickListener(this);

    }

    @Override
    public void onClick(View v){
        switch (v.getId()){
            case R.id.btn_data5commit:
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
        // 01设备号；03读；0910是起始位2320的十六进制；000E是14的十六进制，相当于获取14个数(这里不是双字节)
        // 创建一个byte类型的buffer字节数组，用于存放询问报文
        byte buffer1[] = new byte[6];
        buffer1[0] = 0x01;
        buffer1[1] = 0x03;
        buffer1[2] = 0x09;
        buffer1[3] = 0x10;
        buffer1[4] = 0x00;
        buffer1[5] = 0x0E;
        return buffer1;
    }

    /**
     * 提交请求报文
     * @return
     */
    private byte[] setSubmitRequestData(){
        // 01 03 07 D0 00 08 81 44
        // 01设备号；10写；0898是起始位2200的十六进制；14个数，14个字(0x000E)，28个字节数(0x3C)
        // 创建一个byte类型的buffer字节数组，用于存放询问报文
        byte buffer1[] = new byte[35];
        buffer1[0] = 0x01;
        buffer1[1] = 0x10;
        buffer1[2] = 0x09;
        buffer1[3] = 0x10;
        buffer1[4] = 0x00;
        buffer1[5] = 0x0E;
        buffer1[6] = 0x1C;

        // 前两个是32位的，无功指令流
        byte[] bytes1 = ConnectModbus.dataToByteArray(yaotiao_wgzldl);
        buffer1[7] = bytes1[0];
        buffer1[8] = bytes1[1];
        buffer1[9] = bytes1[2];
        buffer1[10] = bytes1[3];
        // 频谱读写控制
        byte[] bytes2 = ConnectModbus.dataToByteArray(yaotiao_ppdxkz);
        buffer1[11] = bytes2[0];
        buffer1[12] = bytes2[1];
        buffer1[13] = bytes2[2];
        buffer1[14] = bytes2[3];

        // 中间是数据6的内容，不变
        for(int j = 0; j<data6Info.length;j++){
            buffer1[j+15] = data6Info[j];
        }

        for (int i =2;i<textList.size();i++){
            byte[] bytes = ConnectModbus.dataToByteArray(textList.get(i));
            buffer1[2*(i-2)+21] = bytes[2];
            buffer1[2*(i-2)+22] = bytes[3];
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
        message.what = 1005;
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
        message.what = 1015;
        message.obj = data;
        handler.sendMessage(message);
    }

    @Override
    public boolean handleMessage(Message msg) {
        switch (msg.what){
            case 1005:
                List<String> dataList = new ArrayList<>();
                dataList = parsing_YaoTiaoData5((byte[])msg.obj);
                if (null != dataList && 0 < dataList.size()) {
                    int length = 0;
                    if (dataList.size() < textList.size()) {
                        length = dataList.size();
                    } else {
                        length = textList.size();
                    }
                    for (int i = 0; i < length; i++) {
                        if (null != textList.get(i)) {
                            textList.get(i).setText(dataList.get(i));
                        }
                    }
                }
                break;
            case 1015:
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
     * 数据5——前四个是32位浮点数，第二个四个是32位无符号，接下来6个是数据6的状态位（不显示），最后的7个是数据5后面的16位无符号数
     * @return
     */
    public static List<String> parsing_YaoTiaoData5(byte[] buffer1){
        if(ConnectModbus.checkReturnCRC(buffer1)) {
            List<String> dataList = new ArrayList<>();

            // 无功指令电流：前四个是32位浮点数
            byte[] data1 = new byte[4];
            data1[0] = buffer1[3];
            data1[1] = buffer1[4];
            data1[2] = buffer1[5];
            data1[3] = buffer1[6];
            // 将10进制转换成16进制
            StringBuilder data1Str = ConnectModbus.bytesToHexFun3(data1);
            // 将16进制单精度浮点型转换为10进制浮点型，这就是计算出来的数据
            Float dataFloat = ConnectModbus.parseHex2Float(data1Str.toString());
            dataList.add(String.valueOf(dataFloat));

            // 频谱读写控制：第二个四个是32位有符号
            short[] shorts = new short[4];
            shorts[0] = (short) (0x00FF & buffer1[7]);
            shorts[1] = (short) (0x00FF & buffer1[8]);
            shorts[2] = (short) (0x00FF & buffer1[9]);
            shorts[3] = (short) (0x00FF & buffer1[10]);
            long a = shorts[0] *0x1000000 +shorts[1]*0x10000  + shorts[2]*0x100 +shorts[3];
            dataList.add(String.valueOf(a));

            // 中间是数据6的数据，暂时放在data6Info中，为后续的提交备份着
            for(int j = 11;j<17;j++){
                data6Info[j-11] = buffer1[j];
            }

            // 返回数据的后面的长度，7个16位无符号数
            int dataLength = (buffer1[2] - 14)/2;
            // 根据数据的个数，一一展示在textview中
            for (int i = 0; i < dataLength; i++) {
                short[] shorts2 = new short[2];
                // 从第17位开始计算
                shorts2[0] = buffer1[2 * i + 17];
                shorts2[1] = buffer1[2 * i + 17 + 1];
                int b = shorts2[0]*0x100 +shorts2[1];
                dataList.add(String.valueOf(b));
            }
            return dataList;
        }
        return null;
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
