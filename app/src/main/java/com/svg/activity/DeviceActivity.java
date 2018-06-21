package com.svg.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.svg.ConnectModbus;
import com.svg.R;
import com.svg.common.MyApp;
import com.svg.utils.ModbusResponseListner;
import com.svg.utils.SPUtils;
import com.svg.utils.SysCode;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.text.DecimalFormat;

/**
 * 填写设备ID进行绑定
 * Created by Administrator on 2017/3/21.
 */

public class DeviceActivity extends AppCompatActivity implements View.OnClickListener,
        ModbusResponseListner, Handler.Callback{

    EditText deviceID;
    TextView btnConfirm;
    private ModbusResponseListner responseListner;
    private Handler handler;

    static int ConnectCount = 0;
    static boolean isConnected = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device);
        initView();
    }

    private void initView(){
        deviceID = (EditText) findViewById(R.id.deviceID);
        btnConfirm = (TextView) findViewById(R.id.btnConfirm);
        btnConfirm.setOnClickListener(this);

        responseListner = this;
        handler = new Handler(this);

        linkService();

//        byte[] buffer1 = new byte[4];
//        buffer1[0] = 0x44;
//        buffer1[1] = (byte) 0x9A;
//        buffer1[2] = 0x52;
//        buffer1[3] = 0x2B;
//        // 将10进制转换成16进制
//        StringBuilder data1Str = ConnectModbus.bytesToHexFun3(buffer1);
//        // 将16进制单精度浮点型转换为10进制浮点型，这就是计算出来的数据
//        Float data1 = ConnectModbus.parseHex2Float(data1Str.toString());

//        DecimalFormat df = new DecimalFormat("###.00000");
//        deviceID.setText(df.format(Float.valueOf(String.valueOf("1234.5678"))));

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnConfirm:
                if(!TextUtils.isEmpty(deviceID.getText().toString().trim())) {
                    if (isConnected) {
                        try {
                            ConnectModbus.writeIntoDevice(responseListner, deviceID.getText().toString().trim());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                } else {
                    Toast.makeText(DeviceActivity.this, "请先输入设备号", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    private void linkService(){
        if(null == MyApp.socket || MyApp.socket.isClosed()) {
            connectSocket(true);
        }
    }

    private void connectSocket(final boolean byWifi){
        new Thread(){
            public void run() {
                try {
                    ConnectCount ++;
                    MyApp.socket = new Socket();
                    // 创建一个Socket对象，并指定服务端的IP及端口号
                    if(byWifi) {
                        // wifi热点近距离连接
                        InetSocketAddress isa = new InetSocketAddress("192.168.1.1", 8887);

                        MyApp.socket.connect(isa, 10000);
                        // 设置 socket 读取数据流的超时时间
                        MyApp.socket.setSoTimeout(5000);
                        // 发送数据包，默认为 false，即客户端发送数据采用 Nagle 算法；
                        // 但是对于实时交互性高的程序，建议其改为 true，即关闭 Nagle 算法，客户端每发送一次数据，无论数据包大小都会将这些数据发送出去
                        MyApp.socket.setTcpNoDelay(true);
                        // 设置客户端 socket 关闭时，close() 方法起作用时延迟 30 秒关闭，如果 30 秒内尽量将未发送的数据包发送出去
                        MyApp.socket.setSoLinger(true, 30);
                        // 设置输出流的发送缓冲区大小，默认是4KB，即4096字节
                        MyApp.socket.setSendBufferSize(4096);
                        // 设置输入流的接收缓冲区大小，默认是4KB，即4096字节
                        MyApp.socket.setReceiveBufferSize(4096);
                        // 作用：每隔一段时间检查服务器是否处于活动状态，如果服务器端长时间没响应，自动关闭客户端socket
                        // 防止服务器端无效时，客户端长时间处于连接状态
                        MyApp.socket.setKeepAlive(true);

                        SPUtils.put(DeviceActivity.this, SysCode.APP_SOCKET, MyApp.socket);
                        Intent intent = new Intent(DeviceActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        // 4G远距离连接 6000 9000
                        InetSocketAddress isa = new InetSocketAddress("218.2.153.198", 9000);

                        MyApp.socket.connect(isa, 10000);
                        // 设置 socket 读取数据流的超时时间
                        MyApp.socket.setSoTimeout(5000);
                        // 发送数据包，默认为 false，即客户端发送数据采用 Nagle 算法；
                        // 但是对于实时交互性高的程序，建议其改为 true，即关闭 Nagle 算法，客户端每发送一次数据，无论数据包大小都会将这些数据发送出去
                        MyApp.socket.setTcpNoDelay(true);
                        // 设置客户端 socket 关闭时，close() 方法起作用时延迟 30 秒关闭，如果 30 秒内尽量将未发送的数据包发送出去
                        MyApp.socket.setSoLinger(true, 30);
                        // 设置输出流的发送缓冲区大小，默认是4KB，即4096字节
                        MyApp.socket.setSendBufferSize(4096);
                        // 设置输入流的接收缓冲区大小，默认是4KB，即4096字节
                        MyApp.socket.setReceiveBufferSize(4096);
                        // 作用：每隔一段时间检查服务器是否处于活动状态，如果服务器端长时间没响应，自动关闭客户端socket
                        // 防止服务器端无效时，客户端长时间处于连接状态
                        MyApp.socket.setKeepAlive(true);

                        SPUtils.put(DeviceActivity.this, SysCode.APP_SOCKET, MyApp.socket);
                    }
                    isConnected = true;
                    ConnectCount = 0;
                } catch (UnknownHostException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    Log.d("ConnectModbus", "设备连接失败");
                    e.printStackTrace();
                    isConnected = false;
                    if(3 >= ConnectCount) {
                        connectSocket(false);
                    } else {
                        Intent intent = new Intent(DeviceActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }
            }
        }.start();
    }

    @Override
    public void getResponseData(byte[] data) {
        Message message = new Message();
        message.what = 100;
        message.obj = data;
        handler.sendMessage(message);
    }

    @Override
    public void failedResponse() {

    }

    @Override
    public void getSubmitResponseData(byte[] data) {

    }

    @Override
    public void submitFailedResponse() {

    }

    @Override
    public boolean handleMessage(Message msg) {
        switch (msg.what){
            case 100:
                byte[] bytes = (byte[]) msg.obj;
                if(0x90 == (0x00FF & bytes[1])){
                    Toast.makeText(DeviceActivity.this, "设备ID错误，请重新输入", Toast.LENGTH_SHORT).show();
                } else {
                    SPUtils.put(DeviceActivity.this, SysCode.DEVICE_ID, deviceID.getText().toString().trim());
                    Intent intent = new Intent(DeviceActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
                break;
        }
        return false;
    }
}
