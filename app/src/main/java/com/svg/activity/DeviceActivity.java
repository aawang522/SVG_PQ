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

                        Intent intent = new Intent(DeviceActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        // 4G远距离连接 6000 9000
                        InetSocketAddress isa = new InetSocketAddress("218.2.153.198", 9000);
                        MyApp.socket.connect(isa, 10000);
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
    public void getSubmitResponseData(byte[] data) {

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
