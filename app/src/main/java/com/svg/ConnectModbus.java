package com.svg;

import android.content.Context;
import android.support.v4.util.ArrayMap;
import android.util.Log;
import android.widget.EditText;
import android.widget.HorizontalScrollView;

import com.svg.bean.HistoryBean;
import com.svg.bean.ShijianBean;
import com.svg.common.MyApp;
import com.svg.utils.HistoryResponseListner;
import com.svg.utils.ModbusResponseListner;
import com.svg.utils.SPUtils;
import com.svg.utils.SysCode;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigInteger;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2018/3/22.
 */

public class ConnectModbus {
    static int ConnectCount = 0;
    static boolean isConnected = false;
    public static Socket connetSocket;
    public static void connectSocket(final boolean byWifi){
        new Thread(){
            public void run() {
                try {
                    ConnectCount ++;
                    connetSocket = new Socket();
                    // 创建一个Socket对象，并指定服务端的IP及端口号
                    if(byWifi) {
                        // wifi热点近距离连接
                        InetSocketAddress isa = new InetSocketAddress("192.168.1.1", 8887);
                        connetSocket.connect(isa, 10000);
                    } else {
                        // 4G远距离连接 6000 9000
                        InetSocketAddress isa = new InetSocketAddress("218.2.153.198", 9000);
                        connetSocket.connect(isa, 10000);
                        // 连接之后必须先发送写入设备ID命令
//                        writeIntoDevice(connetSocket);
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

    /**
     * 远程连接需要向设备写入数据
     * @throws IOException
     */
    public static void writeIntoDevice(final ModbusResponseListner responseListner, final String deviceID) throws IOException{
        new Thread(){
            public void run() {
                try {
                    byte buffer[] = new byte[1024];
                    if (null != MyApp.socket && !MyApp.socket.isClosed()) {
                        // 获取Socket的OutputStream对象用于发送数据。
                        OutputStream outputStream = MyApp.socket.getOutputStream();
                        // 命令：011007CE0002040001E24040D3
                        byte buffer1[] = new byte[11];
                        buffer1[0] = 0x01;
                        buffer1[1] = 0x10;
                        buffer1[2] = 0x07;
                        buffer1[3] = (byte) 0xCE;
                        buffer1[4] = 0x00;
                        buffer1[5] = 0x02;
                        buffer1[6] = 0x04;

                        // 写入设备ID号123456
                        byte[] deviceByte = intToByteArray(Integer.valueOf(deviceID));
//            buffer1[7] = 0x00;
//            buffer1[8] = 0x01;
//            buffer1[9] = (byte)0xE2;
//            buffer1[10] = 0x40;
                        buffer1[7] = deviceByte[0];
                        buffer1[8] = deviceByte[1];
                        buffer1[9] = deviceByte[2];
                        buffer1[10] = deviceByte[3];

                        byte[] requestData = setSubmitRequestData(buffer1);
                        outputStream.write(requestData);
                        // 发送读取的数据到服务端
                        outputStream.flush();

                        // 创建一个InputStream用户读取要发送的文件。
                        InputStream inputStream = MyApp.socket.getInputStream();
                        inputStream.read(buffer);

                        responseListner.getResponseData(buffer);
                    }
                } catch (IOException e) {
                    Log.d("ConnectModbus", "设备连接失败");
                    e.printStackTrace();
                    isConnected = false;
                    if(3 >= ConnectCount) {
                        connectSocket(false);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    /**
     * 连接modbus
     */
    public static void connectServerWithTCPSocket(final byte[] requestOriginaldata,
                                                  final ModbusResponseListner responseListner) {
        new Thread(){
            public void run() {
                try {
                    if(null != MyApp.socket && !MyApp.socket.isClosed()) {
                        // 获取Socket的OutputStream对象用于发送数据。
                        OutputStream outputStream = MyApp.socket.getOutputStream();
                        // 把数据写入到OuputStream对象中
                        byte[] requestData = setRequestData(requestOriginaldata);
                        outputStream.write(requestData);
                        // 发送读取的数据到服务端
                        outputStream.flush();

                        // 创建一个InputStream用户读取要发送的文件。
                        InputStream inputStream = MyApp.socket.getInputStream();
                        byte buffer[] = new byte[4 * 1024];
                        inputStream.read(buffer);

                        // 因为获取到的是十进制，这里需要转换成16进制
                        // 01 03 10 45 7a 28 0a  45 a0 39 5e  3e 45 f1 8c 00 00 00 00 21 db
                        responseListner.getResponseData(buffer);
                    }
                } catch (UnknownHostException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    Log.d("ConnectModbus", "设备没有连接");
                    e.printStackTrace();
                    isConnected = false;
                    if(3 >= ConnectCount) {
                        connectSocket(true);
                        if (isConnected) {
                            connectServerWithTCPSocket(requestOriginaldata, responseListner);
                        }
                    }
                }
            }
        }.start();
    }

    /**
     * 历史曲线获取补偿前和后的数据
     */
    public static void connectServerGetHistory(final byte[] requestOriginaldata1,final byte[] requestOriginaldata2,
                                                  final HistoryResponseListner responseListner) {
        new Thread(){
            public void run() {
                try {
                    if(null != MyApp.socket && !MyApp.socket.isClosed()) {
                        // 获取Socket的OutputStream对象用于发送数据。
                        OutputStream outputStream = MyApp.socket.getOutputStream();
                        // 补偿前
                        byte[] requestData1 = setRequestData(requestOriginaldata1);
                        outputStream.write(requestData1);
                        // 发送读取的数据到服务端
                        outputStream.flush();
                        // 创建一个InputStream用户读取要发送的文件。
                        InputStream inputStream = MyApp.socket.getInputStream();
                        byte buffer[] = new byte[1 * 1024];
                        inputStream.read(buffer);

                        // 补偿后
                        byte[] requestData2 = setRequestData(requestOriginaldata2);
                        outputStream.write(requestData2);
                        // 发送读取的数据到服务端
                        outputStream.flush();
                        // 创建一个InputStream用户读取要发送的文件。
                        byte buffer2[] = new byte[1 * 1024];
                        inputStream.read(buffer2);

                        // 因为获取到的是十进制，这里需要转换成16进制
                        // 01 03 10 45 7a 28 0a  45 a0 39 5e  3e 45 f1 8c 00 00 00 00 21 db
                        Map<String, byte[]> map = new ArrayMap<>();
                        map.put("data1", buffer);
                        map.put("data2", buffer2);
                        responseListner.getResponseData(map);
                    }
                } catch (UnknownHostException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    Log.d("ConnectModbus", "设备没有连接");
                    e.printStackTrace();
                    isConnected = false;
                    if(3 >= ConnectCount) {
                        connectSocket(true);
                        if (isConnected) {
                            connectServerGetHistory(requestOriginaldata1, requestOriginaldata2, responseListner);
                        }
                    }
                }
            }
        }.start();
    }

    /**
     * 历史曲线获取ABC的数据
     */
    public static void connectServerGetHistoryABC(final Socket socket, final byte[] requestOriginaldata1,
                                                  final byte[] requestOriginaldata2, final byte[] requestOriginaldata3,
                                               final HistoryResponseListner responseListner) {
        new Thread(){
            public void run() {
                try {
                    if(null != MyApp.socket && !MyApp.socket.isClosed()) {
                        // 获取Socket的OutputStream对象用于发送数据。
                        OutputStream outputStream = MyApp.socket.getOutputStream();
                        // A
                        byte[] requestData1 = setSubmitRequestData(requestOriginaldata1);
                        outputStream.write(requestData1);
                        // 发送读取的数据到服务端
                        outputStream.flush();
                        // 创建一个InputStream用户读取要发送的文件。
                        InputStream inputStream = MyApp.socket.getInputStream();
                        byte buffer[] = new byte[1 * 1024];
                        inputStream.read(buffer);

                        // B
                        byte[] requestData2 = setSubmitRequestData(requestOriginaldata2);
                        outputStream.write(requestData2);
                        // 发送读取的数据到服务端
                        outputStream.flush();
                        // 创建一个InputStream用户读取要发送的文件。
                        byte buffer2[] = new byte[1 * 1024];
                        inputStream.read(buffer2);

                        // C
                        byte[] requestData3 = setRequestData(requestOriginaldata3);
                        outputStream.write(requestData3);
                        // 发送读取的数据到服务端
                        outputStream.flush();
                        // 创建一个InputStream用户读取要发送的文件。
                        byte buffer3[] = new byte[1 * 1024];
                        inputStream.read(buffer3);

                        // 因为获取到的是十进制，这里需要转换成16进制
                        // 01 03 10 45 7a 28 0a  45 a0 39 5e  3e 45 f1 8c 00 00 00 00 21 db
                        Map<String, byte[]> map = new ArrayMap<>();
                        map.put("data1", buffer);
                        map.put("data2", buffer2);
                        map.put("data3", buffer3);
                        responseListner.getResponseData(map);
                    }
                } catch (UnknownHostException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    Log.d("ConnectModbus", "设备没有连接");
                    e.printStackTrace();
                    isConnected = false;
                    if(3 >= ConnectCount) {
                        connectSocket(true);
                        if (isConnected) {
                            connectServerGetHistory(requestOriginaldata1, requestOriginaldata2, responseListner);
                        }
                    }
                }
            }
        }.start();
    }

    /**
     * 提交报文
     */
    public static void submitDataWithTCPSocket(final byte[] requestOriginaldata,
                                                  final ModbusResponseListner responseListner) {
        new Thread(){
            public void run() {
                try {
                    if(null != MyApp.socket && !MyApp.socket.isClosed()) {
                        // 获取Socket的OutputStream对象用于发送数据。
                        OutputStream outputStream = MyApp.socket.getOutputStream();
                        // 把数据写入到OuputStream对象中
                        byte[] requestData = setSubmitRequestData(requestOriginaldata);
                        outputStream.write(requestData);
                        // 发送读取的数据到服务端
                        outputStream.flush();

                        // 创建一个InputStream用户读取要发送的文件。
                        InputStream inputStream = MyApp.socket.getInputStream();
                        byte buffer[] = new byte[4 * 1024];
                        inputStream.read(buffer);

                        // 因为获取到的是十进制，这里需要转换成16进制
                        // 01 03 10 45 7a 28 0a  45 a0 39 5e  3e 45 f1 8c 00 00 00 00 21 db
                        responseListner.getSubmitResponseData(buffer);
                    }
                } catch (UnknownHostException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    Log.d("ConnectModbus", "设备没有连接");
                    e.printStackTrace();

                    isConnected = false;
                    if(3 >= ConnectCount) {
                        connectSocket(true);
                        if (isConnected) {
                            submitDataWithTCPSocket(requestOriginaldata, responseListner);
                        }
                    }
                }
            }
        }.start();
    }

    /**
     * 提交报文
     */
    public static void submitDataWithThree(final byte[] requestOriginaldata,final byte[] requestOriginaldata1,
                                           final byte[] requestOriginaldata2,
                                               final ModbusResponseListner responseListner) {
        new Thread(){
            public void run() {
                try {
                    if(null != MyApp.socket && !MyApp.socket.isClosed()) {
                        // 获取Socket的OutputStream对象用于发送数据。
                        OutputStream outputStream = MyApp.socket.getOutputStream();
                        // 把数据写入到OuputStream对象中
                        byte[] requestData = setSubmitRequestData(requestOriginaldata);
                        outputStream.write(requestData);
                        // 发送读取的数据到服务端
                        outputStream.flush();

                        // 创建一个InputStream用户读取要发送的文件。
                        InputStream inputStream = MyApp.socket.getInputStream();
                        byte buffer[] = new byte[4 * 1024];
                        inputStream.read(buffer);

                        // 因为获取到的是十进制，这里需要转换成16进制
                        // 01 03 10 45 7a 28 0a  45 a0 39 5e  3e 45 f1 8c 00 00 00 00 21 db
                        responseListner.getSubmitResponseData(buffer);
                    }
                } catch (UnknownHostException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    Log.d("ConnectModbus", "设备没有连接");
                    e.printStackTrace();

                    isConnected = false;
                    if(3 >= ConnectCount) {
                        connectSocket(true);
                        if (isConnected) {
                            submitDataWithTCPSocket(requestOriginaldata, responseListner);
                        }
                    }
                }
            }
        }.start();
    }

    /**
     * 拼接crc，得出完整的请求报文
     */
    private static byte[] setRequestData(byte[] originalData) {
        // 先根据前面的报文计算出crc
        String crcStr = getCRC(originalData);
        // 把16进制string类型的crc转为byte[]
        byte[] byte2 = hexStringToBytes(crcStr);
        // 然后定义另一个byte[]来整合请求报文和crc
        byte buffer2[] = new byte[8];
        for(int i =0;i<originalData.length;i++){
            buffer2[i] = originalData[i];
        }
        // 将crc放在报文末尾，注意低字节在前，高字节在后
        buffer2[6] = byte2[1];
        // 最后成功的buffer2[]: 1 3 7 -48 0 8 68 -127
        buffer2[7] = byte2[0];
        return buffer2;
    }

    /**
     * 拼接crc，得出完整的提交请求报文
     */
    private static byte[] setSubmitRequestData(byte[] originalData) {
        // 先根据前面的报文计算出crc
        String crcStr = getCRC(originalData);
        // 把16进制string类型的crc转为byte[]
        byte[] byte2 = hexStringToBytes(crcStr);
        // 然后定义另一个byte[]来整合请求报文和crc
        byte buffer2[] = new byte[originalData.length+2];
        for(int i =0;i<originalData.length;i++){
            buffer2[i] = originalData[i];
        }
        // 将crc放在报文末尾，注意低字节在前，高字节在后
        buffer2[originalData.length] = byte2[1];
        // 最后成功的buffer2[]: 1 3 7 -48 0 8 68 -127
        buffer2[originalData.length+1] = byte2[0];
        return buffer2;
    }

    /**
     * 计算CRC16校验码
     * @param bytes 字节数组
     * @return {@link String} 校验码
     * @since 1.0
     */
    private static String getCRC(byte[] bytes) {
        int len = bytes.length;
        //预置 1 个 16 位的寄存器为十六进制FFFF, 称此寄存器为 CRC寄存器。
        int crc = 0xFFFF;
        int i, j;
        for (i = 0; i < len; i++) {
            //把第一个 8 位二进制数据 与 16 位的 CRC寄存器的低 8 位相异或, 把结果放于 CRC寄存器
            crc = ((crc & 0xFF00) | (crc & 0x00FF) ^ (bytes[i] & 0xFF));
            for (j = 0; j < 8; j++) {
                //把 CRC 寄存器的内容右移一位( 朝低位)用 0 填补最高位, 并检查右移后的移出位
                if ((crc & 0x0001) > 0) {
                    //如果移出位为 1, CRC寄存器与多项式A001进行异或
                    crc = crc >> 1;
                    crc = crc ^ 0xA001;
                } else
                    //如果移出位为 0,再次右移一位
                    crc = crc >> 1;
            }
        }
        // 转换成16进制的string
        String crcStr = Integer.toHexString(crc);
        if ("0".equals(crcStr) || null == crcStr || "".equals(crcStr)){
            crcStr = "0000";
        }
        if(1 == crcStr.length()){
            crcStr = "000"+crcStr;
        } else if(2 == crcStr.length()){
            crcStr = "00"+crcStr;
        } else if(3 == crcStr.length()){
            crcStr = "0"+crcStr;
        }
        return crcStr;
    }

    /**
     * 解析获取的数据
     */
    public static boolean checkReturnCRC(byte[] buffer1){
        //  && 0<buffer1[2]
        if(5<buffer1.length) {
            // buffer1的第2个字节就是返回数据的长度，加上5就是整体返回报文的长度
            short[] shorts = new short[1];
            shorts[0] = (short) (0x00FF & buffer1[2]);
            int reactLength = shorts[0] + 5;
            // 为了计算crc，先将buffer1除去crc的长度赋给buffer2
            byte buffer2[] = new byte[reactLength - 2];
            // 先将buffer1的前面赋值给buffer2，便于计算crc
            for (int i = 0; i < reactLength - 2; i++) {
                buffer2[i] = buffer1[i];
            }

            // 首先crc校验
            // 先根据前面的报文计算出crc
            String crcStr = getCRC(buffer2);
            // 如果crc计算出来是3位，比如a56，则在前面补0
            // 然后将计算出来的crc放在byte[]中
            byte[] byte2 = hexStringToBytes(crcStr);
            // 对比crc是否正确，高字节对比前一个，低字节对比后一个，crc正确，则开始解析数据
            if (buffer1[reactLength - 2] == byte2[1] && buffer1[reactLength - 1] == byte2[0]) {
                return true;
            }
        }
        return false;
    }

    /**
     * 验证历史数据的CRC
     */
    public static int checkHistoryCRC(byte[] buffer1){
        if(5<buffer1.length) {
            // buffer1的第2个字节就是返回数据的长度，加上5就是整体返回报文的长度
//            short[] shorts = new short[1];
//            shorts[0] = (short) (0x00FF & buffer1[2]);
            int reactLength = returnActualLength(buffer1);
            // 为了计算crc，先将buffer1除去crc的长度赋给buffer2
            byte buffer2[] = new byte[reactLength - 2];
            // 先将buffer1的前面赋值给buffer2，便于计算crc
            for (int i = 0; i < reactLength - 2; i++) {
                buffer2[i] = buffer1[i];
            }

            // 首先crc校验
            // 先根据前面的报文计算出crc
            String crcStr = getCRC(buffer2);
            // 如果crc计算出来是3位，比如a56，则在前面补0
            // 然后将计算出来的crc放在byte[]中
            byte[] byte2 = hexStringToBytes(crcStr);
            // 对比crc是否正确，高字节对比前一个，低字节对比后一个，crc正确，则开始解析数据
            if (buffer1[reactLength - 2] == byte2[1] && buffer1[reactLength - 1] == byte2[0]) {
                return reactLength;
            }
        }
        return 0;
    }

    public static int returnActualLength(byte[] data) {
        int i = data.length - 1;
        for (; i >= 0; i--) {
            if (data[i] != '\0')
                break;
        }
        return i+1;
    }

    /**
     * 32位浮点数解析
     * @return
     */
    public static List<Float> from32Fudian(byte[] buffer1){
        if(checkReturnCRC(buffer1)) {
            List<Float> dataList = new ArrayList<>();
            // 返回数据的长度
            short[] shorts = new short[1];
            shorts[0] = (short) (0x00FF & buffer1[2]);
            int dataLength = shorts[0] / 4;
            // 根据数据的个数，一一展示在textview中
            for (int i = 0; i < dataLength; i++) {
                byte[] data1Byte = new byte[4];
                data1Byte[0] = buffer1[4 * i + 3];
                data1Byte[1] = buffer1[4 * i + 3 + 1];
                data1Byte[2] = buffer1[4 * i + 3 + 2];
                data1Byte[3] = buffer1[4 * i + 3 + 3];

                // 将10进制转换成16进制
                StringBuilder data1Str = bytesToHexFun3(data1Byte);
                // 将16进制单精度浮点型转换为10进制浮点型，这就是计算出来的数据
                Float data1 = parseHex2Float(data1Str.toString());
                dataList.add(data1);
            }
            return dataList;
        }
        return null;
    }

    /**
     * 数据1——第1、3、8、11是32位无符号数，其余都是浮点数
     * @return
     */
    public static List<String> parsing_YaoTiaoData1(byte[] buffer1){
        if(checkReturnCRC(buffer1)) {
            List<String> dataList = new ArrayList<>();
            int dataLength = buffer1[2] / 4;
            // 根据数据的个数，一一展示在textview中
            for (int i = 0; i < dataLength; i++) {
                short[] shorts = new short[4];
                shorts[0] = (short) (0x00FF & buffer1[4 * i + 3]);
                shorts[1] = (short) (0x00FF & buffer1[4 * i + 3 + 1]);
                shorts[2] = (short) (0x00FF & buffer1[4 * i + 3 + 2]);
                shorts[3] = (short) (0x00FF & buffer1[4 * i + 3 + 3]);

                // 第1、3、8、11是32位无符号数
                if(i == 1 || i == 3 || i == 8 || i ==11) {
                    long a = shorts[0] *0x1000000 +shorts[1]*0x10000  + shorts[2]*0x100 +shorts[3];
                    dataList.add(String.valueOf(a));
                }
                // 否则就是32位浮点数
                else {
                    // 将10进制转换成16进制
                    StringBuilder data1Str = bytesToHexFun3(shorts);
                    // 将16进制单精度浮点型转换为10进制浮点型，这就是计算出来的数据
                    Float data1 = parseHex2Float(data1Str.toString());
                    dataList.add(String.valueOf(data1));
                }
            }
            return dataList;
        }
        return null;
    }

    /**
     * 利用 {@link java.nio.ByteBuffer}实现byte[]转long
     * @param input
     * @param offset
     * @param littleEndian 输入数组是否小端模式
     * @return
     */
    public static long bytesToLong(byte[] input, int offset, boolean littleEndian) {
//        if(offset <0 || offset+8>input.length)
//            throw new IllegalArgumentException(String.format("less than 8 bytes from index %d  is insufficient for long",offset));
        ByteBuffer buffer = ByteBuffer.wrap(input,offset,8);
        if(littleEndian){
            // ByteBuffer.order(ByteOrder) 方法指定字节序,即大小端模式(BIG_ENDIAN/LITTLE_ENDIAN)
            // ByteBuffer 默认为大端(BIG_ENDIAN)模式
            buffer.order(ByteOrder.LITTLE_ENDIAN);
        }
        return buffer.getLong();
    }

    /**
     * 数据2——前面都是32位浮点数，最后一个是32位无符号数
     * @return
     */
    public static List<String> parsing_YaoTiaoData2(byte[] buffer1){
        if(checkReturnCRC(buffer1)) {
            List<String> dataList = new ArrayList<>();
            int dataLength = buffer1[2] / 4;
            // 根据数据的个数，一一展示在textview中
            for (int i = 0; i < dataLength; i++) {
                short[] shorts = new short[4];
                shorts[0] = (short) (0x00FF & buffer1[4 * i + 3]);
                shorts[1] = (short) (0x00FF & buffer1[4 * i + 3 + 1]);
                shorts[2] = (short) (0x00FF & buffer1[4 * i + 3 + 2]);
                shorts[3] = (short) (0x00FF & buffer1[4 * i + 3 + 3]);

                // 如果是最后一位，则是32位无符号数
                if(i == dataLength-1) {
                    long a = shorts[0] *0x1000000 +shorts[1]*0x10000  + shorts[2]*0x100 +shorts[3];
                    dataList.add(String.valueOf(a));
                }
                // 否则就是32位浮点数
                else {
                    // 将10进制转换成16进制
                    StringBuilder data1Str = bytesToHexFun3(shorts);
                    // 将16进制单精度浮点型转换为10进制浮点型，这就是计算出来的数据
                    Float data1 = parseHex2Float(data1Str.toString());
                    dataList.add(String.valueOf(data1));
                }
            }
            return dataList;
        }
        return null;
    }

    /**
     * 数据3——32位无符号数解析
     * @return
     */
    public static List<Long> parsing32Wufuhao_YaoTiaoData3(byte[] buffer1){
        if(checkReturnCRC(buffer1)) {
            List<Long> dataList = new ArrayList<>();
            // 返回数据的长度
            int dataLength = buffer1[2] / 4;
            // 根据数据的个数，一一展示在textview中
            for (int i = 0; i < dataLength; i++) {
                short[] shorts = new short[4];
                shorts[0] = (short) (0x00FF & buffer1[4 * i + 3]);
                shorts[1] = (short) (0x00FF & buffer1[4 * i + 3 + 1]);
                shorts[2] = (short) (0x00FF & buffer1[4 * i + 3 + 2]);
                shorts[3] = (short) (0x00FF & buffer1[4 * i + 3 + 3]);

                long a = shorts[0] *0x1000000 +shorts[1]*0x10000  + shorts[2]*0x100 +shorts[3];
                dataList.add(a);
            }
            return dataList;
        }
        return null;
    }


    /**
     * 数据4——第10、13、14位是32位浮点数，其他都是32位无符号数
     * @return
     */
    public static List<String> parsing_YaoTiaoData4(byte[] buffer1){
        if(checkReturnCRC(buffer1)) {
            List<String> dataList = new ArrayList<>();
            int dataLength = buffer1[2] / 4;
            // 根据数据的个数，一一展示在textview中
            for (int i = 0; i < dataLength; i++) {
                short[] shorts = new short[4];
                shorts[0] = (short) (0x00FF & buffer1[4 * i + 3]);
                shorts[1] = (short) (0x00FF & buffer1[4 * i + 3 + 1]);
                shorts[2] = (short) (0x00FF & buffer1[4 * i + 3 + 2]);
                shorts[3] = (short) (0x00FF & buffer1[4 * i + 3 + 3]);

                // 如果是第10位、第13位和第14位，则是32位浮点数
                if(i == 10 || i == 13 || i == 14) {
                    // 将10进制转换成16进制
                    StringBuilder data1Str = bytesToHexFun3(shorts);
                    // 将16进制单精度浮点型转换为10进制浮点型，这就是计算出来的数据
                    Float data1 = parseHex2Float(data1Str.toString());
                    dataList.add(String.valueOf(data1));
                }
                // 否则就是32位无符号数
                else {
                    long a = shorts[0] *0x1000000 +shorts[1]*0x10000  + shorts[2]*0x100 +shorts[3];
                    dataList.add(String.valueOf(a));
                }
            }
            return dataList;
        }
        return null;
    }

    /**
     * 数据5——前四个是32位浮点数，第二个四个是32位无符号，接下来6个是数据6的状态位（不显示），最后的7个是数据5后面的16位无符号数
     * @return
     */
    public static List<String> parsing_YaoTiaoData5(byte[] buffer1){
        if(checkReturnCRC(buffer1)) {
            List<String> dataList = new ArrayList<>();

            // 无功指令电流：前四个是32位浮点数
            byte[] data1 = new byte[4];
            data1[0] = buffer1[3];
            data1[1] = buffer1[4];
            data1[2] = buffer1[5];
            data1[3] = buffer1[6];
            // 将10进制转换成16进制
            StringBuilder data1Str = bytesToHexFun3(data1);
            // 将16进制单精度浮点型转换为10进制浮点型，这就是计算出来的数据
            Float dataFloat = parseHex2Float(data1Str.toString());
            dataList.add(String.valueOf(dataFloat));

            // 频谱读写控制：第二个四个是32位无符号
            byte[] data2 = new byte[4];
            data2[3] = buffer1[7];
            data2[2] = buffer1[8];
            data2[1] = buffer1[9];
            data2[0] = buffer1[10];
            long a = data2[3] *0x1000000 +data2[2]*0x10000  + data2[1]*0x100 +data2[0];
            dataList.add(String.valueOf(a));

            // 返回数据的后面的长度，7个16位无符号数
            int dataLength = (buffer1[2] - 14)/2;
            // 根据数据的个数，一一展示在textview中
            for (int i = 0; i < dataLength; i++) {
                byte[] data1Byte = new byte[2];
                // 从第17位开始计算
                data1Byte[1] = buffer1[2 * i + 17];
                data1Byte[0] = buffer1[2 * i + 17 + 1];
                int b = data1Byte[1]*0x10 +data1Byte[0];
                dataList.add(String.valueOf(b));
            }
            return dataList;
        }
        return null;
    }

    /**
     * 数据6解析——位解析
     * @param byteData
     * @return
     */
    public static List<Boolean> parsingWei_YaoTiaoData6(byte[] byteData){
        if(checkReturnCRC(byteData)) {
            byte a = 0x00, b = 0x00, c = 0x00, d = 0x00, e = 0x00, f = 0x00;
            b = (byteData)[3];
            a = (byteData)[4];
            d = (byteData)[5];
            c = (byteData)[6];
            f = (byteData)[7];
            e = (byteData)[8];

            boolean gybh1 = ((a & 0x01) == 0x01);
            boolean gybh2 = (((a >> 1) & 0x01) == 0x01);
            boolean qybh1 = (((a >> 3) & 0x01) == 0x01);
            boolean qybh2 = (((a >> 4) & 0x01) == 0x01);
            boolean dyfxbh = (((a >> 5) & 0x01) == 0x01);
            boolean dyqxbh = (((a >> 6) & 0x01) == 0x01);
            boolean dybphbh = (((a >> 7) & 0x01) == 0x01);
            boolean glbh = (((b >> 3) & 0x01) == 0x01);
            boolean jddlbh1 = (((b >> 7) & 0x01) == 0x01);
            boolean zlcdygybh = (((c >> 3) & 0x01) == 0x01);
            boolean zlcdyqybh = (((c >> 4) & 0x01) == 0x01);
            boolean dyqdbh = (((c >> 5) & 0x01) == 0x01);
            boolean cdcsgzbh = ((e & 0x01) == 0x01);
            List<Boolean> data = new ArrayList<>();
            data.add(gybh1);
            data.add(gybh2);
            data.add(qybh1);
            data.add(qybh2);
            data.add(dyfxbh);
            data.add(dyqxbh);
            data.add(dybphbh);
            data.add(glbh);
            data.add(jddlbh1);
            data.add(zlcdygybh);
            data.add(zlcdyqybh);
            data.add(dyqdbh);
            data.add(cdcsgzbh);
            return data;
        }
        return null;
    }

    /**
     * 数据6组装——位解析
     * @param byteData
     * @return
     */
    public static byte[] assemblyWei_YaoTiaoData6(byte[] byteData, int position){
        byte[] newByte = new byte[6];
        byte a = 0x00, b = 0x00, c = 0x00, d = 0x00, e = 0x00, f = 0x00;
        b = (byteData)[7];
        a = (byteData)[8];
        d = (byteData)[9];
        c = (byteData)[10];
        f = (byteData)[11];
        e = (byteData)[12];

        switch (position){
            // a的0 第一个按钮，过压保护1
            case 0:
                a = (byte)(a ^ 0x01);
                break;
            // a的1 第二个按钮，过压保护2
            case 1:
                a = (byte)(a ^ 0x02);
                break;
            // a的3 第三个按钮，欠压保护1
            case 2:
                a = (byte)(a ^ 0x08);
                break;
            // a的4 第四个按钮，欠压保护2
            case 3:
                a = (byte)(a ^ 0x10);
                break;
            // a的5 第五个按钮，电压反序保护
            case 4:
                a = (byte)(a ^ 0x20);
                break;
            // a的6 第六个按钮，电压缺相保护
            case 5:
                a = (byte)(a ^ 0x40);
                break;
            // a的7 第七个按钮，电压不平衡保护
            case 6:
                a = (byte)(a ^ 0x80);
                break;
            // b的3 第八个按钮，过流保护
            case 7:
                b = (byte)(b ^ 0x08);
                break;
            // b的7 第九个按钮，接地电流保护1
            case 8:
                b = (byte)(b ^ 0x80);
                break;
            // c的3 第十个按钮，直流过压保护
            case 9:
                c = (byte)(c ^ 0x08);
                break;
            // c的4 第十一个按钮，直流欠压保护
            case 10:
                c = (byte)(c ^ 0x10);
                break;
            // c的5 第十二个按钮，单元驱动保护
            case 11:
                c = (byte)(c ^ 0x20);
                break;
            // e的0 第十三个按钮，充电超时保护
            case 12:
                e = (byte)(e ^ 0x01);
                break;
        }
        byteData[7] = b;
        byteData[8] = a;
        byteData[9] = d;
        byteData[10] = c;
        byteData[11] = f;
        byteData[12] = e;
        return byteData;
    }

    /**
     * 状态数据1解析——位解析
     * @param byteData
     * @return
     */
    public static List<Boolean> parsingWei_ZhuangtaiData1(byte[] byteData){
        if(checkReturnCRC(byteData)) {
            byte a = 0x00, b = 0x00;
            b = (byteData)[3];
            a = (byteData)[4];

            boolean cszt = ((a & 0x01) == 0x01);
            boolean ycdzt = (((a >> 1) & 0x01) == 0x01);
            boolean hzzt = (((a >> 2) & 0x01) == 0x01);
            boolean jxzt = (((a >> 3) & 0x01) == 0x01);
            boolean yxzt = (((a >> 4) & 0x01) == 0x01);
            boolean gzzt = (((a >> 5) & 0x01) == 0x01);
            List<Boolean> data = new ArrayList<>();
            data.add(cszt);
            data.add(ycdzt);
            data.add(hzzt);
            data.add(jxzt);
            data.add(yxzt);
            data.add(gzzt);
            return data;
        }
        return null;
    }

    /**
     * 状态数据2解析——位解析
     * @param byteData
     * @return
     */
    public static List<Boolean> parsingWei_ZhuangtaiData2(byte[] byteData){
        if(checkReturnCRC(byteData)) {
            byte a = 0x00, b = 0x00;
            b = (byteData)[3];
            a = (byteData)[4];

            boolean fbdrq1a = ((a & 0x01) == 0x01);
            boolean fbdrq1b = (((a >> 1) & 0x01) == 0x01);
            boolean fbdrq1c = (((a >> 2) & 0x01) == 0x01);
            boolean fbdrq2a = (((a >> 3) & 0x01) == 0x01);
            boolean fbdrq2b = (((a >> 4) & 0x01) == 0x01);
            boolean fbdrq2c = (((a >> 5) & 0x01) == 0x01);
            boolean fbdrq3a = (((a >> 6) & 0x01) == 0x01);
            boolean fbdrq3b = (((a >> 7) & 0x01) == 0x01);
            boolean fbdrq3c = ((b & 0x01) == 0x01);
            boolean gbdrq1 = (((b >> 1) & 0x01) == 0x01);
            boolean gbdrq2 = (((b >> 2) & 0x01) == 0x01);
            boolean gbdrq3 = (((b >> 3) & 0x01) == 0x01);
            List<Boolean> data = new ArrayList<>();
            data.add(fbdrq1a);
            data.add(fbdrq1b);
            data.add(fbdrq1c);
            data.add(fbdrq2a);
            data.add(fbdrq2b);
            data.add(fbdrq2c);
            data.add(fbdrq3a);
            data.add(fbdrq3b);
            data.add(fbdrq3c);
            data.add(gbdrq1);
            data.add(gbdrq2);
            data.add(gbdrq3);
            return data;
        }
        return null;
    }

    /**
     * 事件解析——16位和位解析
     * @param byteData
     * @return
     */
    public static List<ShijianBean> parsingWei_Shijian(byte[] byteData){
        if(checkReturnCRC(byteData)) {
            List<ShijianBean> shijianBeanList = new ArrayList<>();
            // 每24个代表一个事件
            short[] shorts = new short[1];
            shorts[0] = (short) (0x00FF & byteData[2]);
            int reactLength = shorts[0]/24;
            for(int j =0; j< reactLength;j++) {
                // 先解析前面6个十六位无符号数，作为时间
                // 年
                short[] data1Byte = new short[2];
                data1Byte[1] = (short) (0x00FF & byteData[24 * j + 3]);
                data1Byte[0] = (short) (0x00FF & byteData[24 * j + 4]);
                int a = data1Byte[1] * 0x100 + data1Byte[0];
                // 月
                short[] data2Byte = new short[2];
                data2Byte[1] = (short) (0x00FF & byteData[24 * j + 5]);
                data2Byte[0] = (short) (0x00FF & byteData[24 * j + 6]);
                int b = data2Byte[1] * 0x100 + data2Byte[0];
                // 日
                short[] data3Byte = new short[2];
                data3Byte[1] = (short) (0x00FF & byteData[24 * j + 7]);
                data3Byte[0] = (short) (0x00FF & byteData[24 * j + 8]);
                int c = data3Byte[1] * 0x100 + data3Byte[0];
                // 时
                short[] data4Byte = new short[2];
                data4Byte[1] = (short) (0x00FF & byteData[24 * j + 9]);
                data4Byte[0] = (short) (0x00FF & byteData[24 * j + 10]);
                int d = data4Byte[1] * 0x100 + data4Byte[0];
                // 分
                short[] data5Byte = new short[2];
                data5Byte[1] = (short) (0x00FF & byteData[24 * j + 11]);
                data5Byte[0] = (short) (0x00FF & byteData[24 * j + 12]);
                int e = data5Byte[1] * 0x100 + data5Byte[0];
                // 秒
                short[] data6Byte = new short[2];
                data6Byte[1] = (short) (0x00FF & byteData[24 * j + 13]);
                data6Byte[0] = (short) (0x00FF & byteData[24 * j + 14]);
                int f = data6Byte[1] * 0x100 + data6Byte[0];

                String date = a+"/"+b+"/"+c;
                String time = d+":"+e+":"+f;

                // 接下来按位解析事件的内容
                byte a1 = 0x00, b1 = 0x00, c1 = 0x00, d1 = 0x00, e1 = 0x00, f1 = 0x00, g1 = 0x00, h1 = 0x00;
                b1 = (byteData)[24*j+15];
                a1 = (byteData)[24*j+16];
                d1 = (byteData)[24*j+17];
                c1 = (byteData)[24*j+18];
                f1 = (byteData)[24*j+19];
                e1 = (byteData)[24*j+20];
                h1 = (byteData)[24*j+21];
                g1 = (byteData)[24*j+22];

                // 7206(D)   &0x03或者&0b11都可以直接得到与本身相同的值，以此来判断
                if((a1 >> 1 & 0b11) == 0x00 && (a1 & 0x01) == 0x01){
                    ShijianBean shijianBean = new ShijianBean(date, time, "过压保护", "动作");
                    shijianBeanList.add(shijianBean);
                } else if ((a1 >> 1 & 0x01) == 0x01 && (a1 & 0x01) == 0x00){
                    ShijianBean shijianBean = new ShijianBean(date, time, "过压保护", "复归");
                    shijianBeanList.add(shijianBean);
                }
                if((a1 >> 3 & 0x01) == 0x00 && (a1 >> 2 & 0x01) == 0x01){
                    ShijianBean shijianBean = new ShijianBean(date, time, "过压保护2", "动作");
                    shijianBeanList.add(shijianBean);
                } else if ((a1 >> 3 & 0x01) == 0x01 && (a1 >> 2 & 0x01) == 0x00){
                    ShijianBean shijianBean = new ShijianBean(date, time, "过压保护2", "复归");
                    shijianBeanList.add(shijianBean);
                }
                if((a1 >> 5 & 0x01) == 0x00 && (a1 >> 4 & 0x01) == 0x01){
                    ShijianBean shijianBean = new ShijianBean(date, time, "电压峰值保护", "动作");
                    shijianBeanList.add(shijianBean);
                } else if ((a1 >> 5 & 0x01) == 0x01 && (a1 >> 4 & 0x01) == 0x00){
                    ShijianBean shijianBean = new ShijianBean(date, time, "电压峰值保护", "复归");
                    shijianBeanList.add(shijianBean);
                }
                if((a1 >> 7 & 0x01) == 0x00 && (a1 >> 6 & 0x01) == 0x01){
                    ShijianBean shijianBean = new ShijianBean(date, time, "欠压保护1", "动作");
                    shijianBeanList.add(shijianBean);
                } else if ((a1 >> 7 & 0x01) == 0x01 && (a1 >> 6 & 0x01) == 0x00){
                    ShijianBean shijianBean = new ShijianBean(date, time, "欠压保护1", "复归");
                    shijianBeanList.add(shijianBean);
                }
                if((b1 >> 1 & 0x01) == 0x00 && (b1 & 0x01) == 0x01){
                    ShijianBean shijianBean = new ShijianBean(date, time, "欠压保护2", "动作");
                    shijianBeanList.add(shijianBean);
                } else if ((b1 >> 1 & 0x01) == 0x01 && (b1 & 0x01) == 0x00){
                    ShijianBean shijianBean = new ShijianBean(date, time, "欠压保护2", "复归");
                    shijianBeanList.add(shijianBean);
                }
                if((b1 >> 3 & 0x01) == 0x00 && (b1 >> 2 & 0x01) == 0x01){
                    ShijianBean shijianBean = new ShijianBean(date, time, "电压反序保护", "动作");
                    shijianBeanList.add(shijianBean);
                } else if ((b1 >> 3 & 0x01) == 0x01 && (b1 >> 2 & 0x01) == 0x00){
                    ShijianBean shijianBean = new ShijianBean(date, time, "电压反序保护", "复归");
                    shijianBeanList.add(shijianBean);
                }
                if((b1 >> 5 & 0x01) == 0x00 && (b1 >> 4 & 0x01) == 0x01){
                    ShijianBean shijianBean = new ShijianBean(date, time, "电压缺相保护", "动作");
                    shijianBeanList.add(shijianBean);
                } else if ((b1 >> 5 & 0x01) == 0x01 && (b1 >> 4 & 0x01) == 0x00){
                    ShijianBean shijianBean = new ShijianBean(date, time, "电压缺相保护", "复归");
                    shijianBeanList.add(shijianBean);
                }
                if((b1 >> 7 & 0x01) == 0x00 && (b1 >> 6 & 0x01) == 0x01){
                    ShijianBean shijianBean = new ShijianBean(date, time, "电压不平衡保护", "动作");
                    shijianBeanList.add(shijianBean);
                } else if ((b1 >> 7 & 0x01) == 0x01 && (b1 >> 6 & 0x01) == 0x00){
                    ShijianBean shijianBean = new ShijianBean(date, time, "电压不平衡保护", "复归");
                    shijianBeanList.add(shijianBean);
                }
                // 7207(D)
                if((c1 >> 1 & 0x01) == 0x00 && (c1 & 0x01) == 0x01){
                    ShijianBean shijianBean = new ShijianBean(date, time, "过流保护", "动作");
                    shijianBeanList.add(shijianBean);
                } else if ((c1 >> 1 & 0x01) == 0x01 && (c1 & 0x01) == 0x00){
                    ShijianBean shijianBean = new ShijianBean(date, time, "过流保护", "复归");
                    shijianBeanList.add(shijianBean);
                }
                if((c1 >> 3 & 0x01) == 0x00 && (c1 >> 2 & 0x01) == 0x01){
                    ShijianBean shijianBean = new ShijianBean(date, time, "过流峰值保护", "动作");
                    shijianBeanList.add(shijianBean);
                } else if ((c1 >> 3 & 0x01) == 0x01 && (c1 >> 2 & 0x01) == 0x00){
                    ShijianBean shijianBean = new ShijianBean(date, time, "过流峰值保护", "复归");
                    shijianBeanList.add(shijianBean);
                }
                if((c1 >> 5 & 0x01) == 0x00 && (c1 >> 4 & 0x01) == 0x01){
                    ShijianBean shijianBean = new ShijianBean(date, time, "接地电流保护", "动作");
                    shijianBeanList.add(shijianBean);
                } else if ((c1 >> 5 & 0x01) == 0x01 && (c1 >> 4 & 0x01) == 0x00){
                    ShijianBean shijianBean = new ShijianBean(date, time, "接地电流保护", "复归");
                    shijianBeanList.add(shijianBean);
                }
                if((c1 >> 7 & 0x01) == 0x00 && (c1 >> 6 & 0x01) == 0x01){
                    ShijianBean shijianBean = new ShijianBean(date, time, "直流侧电压过压保护", "动作");
                    shijianBeanList.add(shijianBean);
                } else if ((c1 >> 7 & 0x01) == 0x01 && (c1 >> 6 & 0x01) == 0x00){
                    ShijianBean shijianBean = new ShijianBean(date, time, "直流侧电压过压保护", "复归");
                    shijianBeanList.add(shijianBean);
                }
                if((d1 >> 1 & 0x01) == 0x00 && (d1 & 0x01) == 0x01){
                    ShijianBean shijianBean = new ShijianBean(date, time, "直流侧电压欠压保护", "动作");
                    shijianBeanList.add(shijianBean);
                } else if ((d1 >> 1 & 0x01) == 0x01 && (d1 & 0x01) == 0x00){
                    ShijianBean shijianBean = new ShijianBean(date, time, "直流侧电压欠压保护", "复归");
                    shijianBeanList.add(shijianBean);
                }
                if((d1 >> 3 & 0x01) == 0x00 && (d1 >> 2 & 0x01) == 0x01){
                    ShijianBean shijianBean = new ShijianBean(date, time, "单元驱动保护", "动作");
                    shijianBeanList.add(shijianBean);
                } else if ((d1 >> 3 & 0x01) == 0x01 && (d1 >> 2 & 0x01) == 0x00){
                    ShijianBean shijianBean = new ShijianBean(date, time, "单元驱动保护", "复归");
                    shijianBeanList.add(shijianBean);
                }
                if((d1 >> 5 & 0x01) == 0x00 && (d1 >> 4 & 0x01) == 0x01){
                    ShijianBean shijianBean = new ShijianBean(date, time, "单元超温保护", "动作");
                    shijianBeanList.add(shijianBean);
                } else if ((d1 >> 5 & 0x01) == 0x01 && (d1 >> 4 & 0x01) == 0x00){
                    ShijianBean shijianBean = new ShijianBean(date, time, "单元超温保护", "复归");
                    shijianBeanList.add(shijianBean);
                }
                if((d1 >> 7 & 0x01) == 0x00 && (d1 >> 6 & 0x01) == 0x01){
                    ShijianBean shijianBean = new ShijianBean(date, time, "充电超时故障保护", "动作");
                    shijianBeanList.add(shijianBean);
                } else if ((d1 >> 7 & 0x01) == 0x01 && (d1 >> 6 & 0x01) == 0x00){
                    ShijianBean shijianBean = new ShijianBean(date, time, "充电超时故障保护", "复归");
                    shijianBeanList.add(shijianBean);
                }
                // 7208(D)
                if((e1 >> 1 & 0x01) == 0x00 && (e1 & 0x01) == 0x01){
                    ShijianBean shijianBean = new ShijianBean(date, time, "ROM参数校验保护", "动作");
                    shijianBeanList.add(shijianBean);
                } else if ((e1 >> 1 & 0x01) == 0x01 && (e1 & 0x01) == 0x00){
                    ShijianBean shijianBean = new ShijianBean(date, time, "ROM参数校验保护", "复归");
                    shijianBeanList.add(shijianBean);
                }
                if((e1 >> 3 & 0x01) == 0x00 && (e1 >> 2 & 0x01) == 0x01){
                    ShijianBean shijianBean = new ShijianBean(date, time, "过压报警", "动作");
                    shijianBeanList.add(shijianBean);
                } else if ((e1 >> 3 & 0x01) == 0x01 && (e1 >> 2 & 0x01) == 0x00){
                    ShijianBean shijianBean = new ShijianBean(date, time, "过压报警", "复归");
                    shijianBeanList.add(shijianBean);
                }
                if((e1 >> 5 & 0x01) == 0x00 && (e1 >> 4 & 0x01) == 0x01){
                    ShijianBean shijianBean = new ShijianBean(date, time, "过流报警", "动作");
                    shijianBeanList.add(shijianBean);
                } else if ((e1 >> 5 & 0x01) == 0x01 && (e1 >> 4 & 0x01) == 0x00){
                    ShijianBean shijianBean = new ShijianBean(date, time, "过流报警", "复归");
                    shijianBeanList.add(shijianBean);
                }
                if((e1 >> 7 & 0x01) == 0x00 && (e1 >> 6 & 0x01) == 0x01){
                    ShijianBean shijianBean = new ShijianBean(date, time, "限幅报警", "动作");
                    shijianBeanList.add(shijianBean);
                } else if ((e1 >> 7 & 0x01) == 0x01 && (e1 >> 6 & 0x01) == 0x00){
                    ShijianBean shijianBean = new ShijianBean(date, time, "限幅报警", "复归");
                    shijianBeanList.add(shijianBean);
                }
                if((f1 >> 1 & 0x01) == 0x00 && (f1 & 0x01) == 0x01){
                    ShijianBean shijianBean = new ShijianBean(date, time, "1号IGBT故障", "动作");
                    shijianBeanList.add(shijianBean);
                } else if ((f1 >> 1 & 0x01) == 0x01 && (f1 & 0x01) == 0x00){
                    ShijianBean shijianBean = new ShijianBean(date, time, "1号IGBT故障", "复归");
                    shijianBeanList.add(shijianBean);
                }
                if((f1 >> 3 & 0x01) == 0x00 && (f1 >> 2 & 0x01) == 0x01){
                    ShijianBean shijianBean = new ShijianBean(date, time, "2号IGBT故障", "动作");
                    shijianBeanList.add(shijianBean);
                } else if ((f1 >> 3 & 0x01) == 0x01 && (f1 >> 2 & 0x01) == 0x00){
                    ShijianBean shijianBean = new ShijianBean(date, time, "2号IGBT故障", "复归");
                    shijianBeanList.add(shijianBean);
                }
                if((f1 >> 5 & 0x01) == 0x00 && (f1 >> 4 & 0x01) == 0x01){
                    ShijianBean shijianBean = new ShijianBean(date, time, "3号IGBT故障", "动作");
                    shijianBeanList.add(shijianBean);
                } else if ((f1 >> 5 & 0x01) == 0x01 && (f1 >> 4 & 0x01) == 0x00){
                    ShijianBean shijianBean = new ShijianBean(date, time, "3号IGBT故障", "复归");
                    shijianBeanList.add(shijianBean);
                }
                if((f1 >> 7 & 0x01) == 0x00 && (f1 >> 6 & 0x01) == 0x01){
                    ShijianBean shijianBean = new ShijianBean(date, time, "4号IGBT故障", "动作");
                    shijianBeanList.add(shijianBean);
                } else if ((f1 >> 7 & 0x01) == 0x01 && (f1 >> 6 & 0x01) == 0x00){
                    ShijianBean shijianBean = new ShijianBean(date, time, "4号IGBT故障", "复归");
                    shijianBeanList.add(shijianBean);
                }
                // 7209(D)
                if((g1 >> 1 & 0x01) == 0x00 && (g1 & 0x01) == 0x01){
                    ShijianBean shijianBean = new ShijianBean(date, time, "5号IGBT故障", "动作");
                    shijianBeanList.add(shijianBean);
                } else if ((g1 >> 1 & 0x01) == 0x01 && (g1 & 0x01) == 0x00){
                    ShijianBean shijianBean = new ShijianBean(date, time, "5号IGBT故障", "复归");
                    shijianBeanList.add(shijianBean);
                }
                if((g1 >> 3 & 0x01) == 0x00 && (g1 >> 2 & 0x01) == 0x01){
                    ShijianBean shijianBean = new ShijianBean(date, time, "6号IGBT故障", "动作");
                    shijianBeanList.add(shijianBean);
                } else if ((g1 >> 3 & 0x01) == 0x01 && (g1 >> 2 & 0x01) == 0x00){
                    ShijianBean shijianBean = new ShijianBean(date, time, "6号IGBT故障", "复归");
                    shijianBeanList.add(shijianBean);
                }
            }
            return shijianBeanList;
        }
        return null;
    }

    /**
     * 历史数据解析
     * @return
     */
    public static HistoryBean from32_Lishi(byte[] buffer1, String type, byte length){
        // 得到的数据：2018  3  50.012512  51.012512  52...  53...  54... .... 62.012512
        int reactLength = checkHistoryCRC(buffer1);
        if(0 != reactLength) {
            HistoryBean historyBean = new HistoryBean();
            List<Float> dataList = new ArrayList<>();
            // 返回数据的长度
            int dataLength = (reactLength-5) / 4;
            if(SysCode.HISTYORY_YEAR.equals(type)){
                // 年
                short[] data1Byte = new short[4];
                data1Byte[3] = (short) (0x00FF & buffer1[3]);
                data1Byte[2] = (short) (0x00FF & buffer1[4]);
                data1Byte[1] = (short) (0x00FF & buffer1[5]);
                data1Byte[0] = (short) (0x00FF & buffer1[6]);
                int year = data1Byte[3] *0x1000000 +data1Byte[2]*0x10000  + data1Byte[1]*0x100 +data1Byte[0];
                historyBean.setYear(year);
                // 月
                short[] data2Byte = new short[4];
                data2Byte[3] = (short) (0x00FF & buffer1[7]);
                data2Byte[2] = (short) (0x00FF & buffer1[8]);
                data2Byte[1] = (short) (0x00FF & buffer1[9]);
                data2Byte[0] = (short) (0x00FF & buffer1[10]);
                int month = data2Byte[3] *0x1000000 +data2Byte[2]*0x10000  + data2Byte[1]*0x100 +data2Byte[0];
                historyBean.setMonth(month);
                for (int i = 0; i < dataLength-2; i++) {
                    byte[] dataByte = new byte[4];
                    dataByte[0] = buffer1[4 * i + 11];
                    dataByte[1] = buffer1[4 * i + 11 + 1];
                    dataByte[2] = buffer1[4 * i + 11 + 2];
                    dataByte[3] = buffer1[4 * i + 11 + 3];

                    // 将10进制转换成16进制
                    StringBuilder dataStr = bytesToHexFun3(dataByte);
                    // 将16进制单精度浮点型转换为10进制浮点型，这就是计算出来的数据
                    Float data = parseHex2Float(dataStr.toString());
                    dataList.add(data);
                }
                historyBean.setDatas(dataList);
            } else if (SysCode.HISTYORY_MONTH.equals(type)){
                // 年
                short[] data1Byte = new short[4];
                data1Byte[3] = (short) (0x00FF & buffer1[3]);
                data1Byte[2] = (short) (0x00FF & buffer1[4]);
                data1Byte[1] = (short) (0x00FF & buffer1[5]);
                data1Byte[0] = (short) (0x00FF & buffer1[6]);
                int year = data1Byte[3] *0x1000000 +data1Byte[2]*0x10000  + data1Byte[1]*0x100 +data1Byte[0];
                historyBean.setYear(year);
                // 月
                short[] data2Byte = new short[4];
                data2Byte[3] = (short) (0x00FF & buffer1[7]);
                data2Byte[2] = (short) (0x00FF & buffer1[8]);
                data2Byte[1] = (short) (0x00FF & buffer1[9]);
                data2Byte[0] = (short) (0x00FF & buffer1[10]);
                int month = data2Byte[3] *0x1000000 +data2Byte[2]*0x10000  + data2Byte[1]*0x100 +data2Byte[0];
                historyBean.setMonth(month);
                // 日
                short[] data3Byte = new short[4];
                data3Byte[3] = (short) (0x00FF & buffer1[11]);
                data3Byte[2] = (short) (0x00FF & buffer1[12]);
                data3Byte[1] = (short) (0x00FF & buffer1[13]);
                data3Byte[0] = (short) (0x00FF & buffer1[14]);
                int day = data3Byte[3] *0x1000000 +data3Byte[2]*0x10000  + data3Byte[1]*0x100 +data3Byte[0];
                historyBean.setDay(day);
                for (int i = 0; i < dataLength-3; i++) {
                    byte[] dataByte = new byte[4];
                    dataByte[0] = buffer1[4 * i + 15];
                    dataByte[1] = buffer1[4 * i + 15 + 1];
                    dataByte[2] = buffer1[4 * i + 15 + 2];
                    dataByte[3] = buffer1[4 * i + 15 + 3];

                    // 将10进制转换成16进制
                    StringBuilder dataStr = bytesToHexFun3(dataByte);
                    // 将16进制单精度浮点型转换为10进制浮点型，这就是计算出来的数据
                    Float data = parseHex2Float(dataStr.toString());
                    dataList.add(data);
                }
                historyBean.setDatas(dataList);
            }  else if (SysCode.HISTYORY_DAY.equals(type)){
                // 年
                short[] data1Byte = new short[4];
                data1Byte[3] = (short) (0x00FF & buffer1[3]);
                data1Byte[2] = (short) (0x00FF & buffer1[4]);
                data1Byte[1] = (short) (0x00FF & buffer1[5]);
                data1Byte[0] = (short) (0x00FF & buffer1[6]);
                int year = data1Byte[3] *0x1000000 +data1Byte[2]*0x10000  + data1Byte[1]*0x100 +data1Byte[0];
                historyBean.setYear(year);
                // 月
                short[] data2Byte = new short[4];
                data2Byte[3] = (short) (0x00FF & buffer1[7]);
                data2Byte[2] = (short) (0x00FF & buffer1[8]);
                data2Byte[1] = (short) (0x00FF & buffer1[9]);
                data2Byte[0] = (short) (0x00FF & buffer1[10]);
                int month = data2Byte[3] *0x1000000 +data2Byte[2]*0x10000  + data2Byte[1]*0x100 +data2Byte[0];
                historyBean.setMonth(month);
                // 日
                short[] data3Byte = new short[4];
                data3Byte[3] = (short) (0x00FF & buffer1[11]);
                data3Byte[2] = (short) (0x00FF & buffer1[12]);
                data3Byte[1] = (short) (0x00FF & buffer1[13]);
                data3Byte[0] = (short) (0x00FF & buffer1[14]);
                int day = data3Byte[3] *0x1000000 +data3Byte[2]*0x10000  + data3Byte[1]*0x100 +data3Byte[0];
                historyBean.setDay(day);
                // 时
                short[] data4Byte = new short[4];
                data4Byte[3] = (short) (0x00FF & buffer1[15]);
                data4Byte[2] = (short) (0x00FF & buffer1[16]);
                data4Byte[1] = (short) (0x00FF & buffer1[17]);
                data4Byte[0] = (short) (0x00FF & buffer1[18]);
                int hour = data4Byte[3] *0x1000000 +data4Byte[2]*0x10000  + data4Byte[1]*0x100 +data4Byte[0];
                historyBean.setHour(hour);
                // 日
                short[] data5Byte = new short[4];
                data5Byte[3] = (short) (0x00FF & buffer1[19]);
                data5Byte[2] = (short) (0x00FF & buffer1[20]);
                data5Byte[1] = (short) (0x00FF & buffer1[21]);
                data5Byte[0] = (short) (0x00FF & buffer1[22]);
                int minute = data5Byte[3] *0x1000000 +data5Byte[2]*0x10000  + data5Byte[1]*0x100 +data5Byte[0];
                historyBean.setMinute(minute);
                for (int i = 0; i < dataLength-5; i++) {
                    byte[] dataByte = new byte[4];
                    dataByte[0] = buffer1[4 * i + 23];
                    dataByte[1] = buffer1[4 * i + 24 + 1];
                    dataByte[2] = buffer1[4 * i + 25 + 2];
                    dataByte[3] = buffer1[4 * i + 26 + 3];

                    // 将10进制转换成16进制
                    StringBuilder dataStr = bytesToHexFun3(dataByte);
                    // 将16进制单精度浮点型转换为10进制浮点型，这就是计算出来的数据
                    Float data = parseHex2Float(dataStr.toString());
                    dataList.add(data);
                }
                historyBean.setDatas(dataList);
            }
            return historyBean;
        }
        return null;
    }

    /**
     * 16位无符号数解析
     * @return
     */
    public static List<Integer> from16Wufuhao(byte[] buffer1){
        if(checkReturnCRC(buffer1)) {
            List<Integer> dataList = new ArrayList<>();
            // 返回数据的长度
            int dataLength = buffer1[2] / 2;
            // 根据数据的个数，一一展示在textview中
            for (int i = 0; i < dataLength; i++) {
                byte[] data1Byte = new byte[2];
                data1Byte[1] = buffer1[2 * i + 3];
                data1Byte[0] = buffer1[2 * i + 3 + 1];

                int a = data1Byte[1]*0x100 +data1Byte[0];
                dataList.add(a);
            }
            return dataList;
        }
        return null;
    }

    /**16进制的string放入byte[]中
     * Convert hex string to byte[]
     * @param hexString the hex string
     * @return byte[]
     */
    private static byte[] hexStringToBytes(String hexString) {
        if (hexString == null || hexString.equals("")) {
            return null;
        }
        hexString = hexString.toUpperCase();
        int length = hexString.length() / 2;
        char[] hexChars = hexString.toCharArray();
        byte[] d = new byte[length];
        for (int i = 0; i < length; i++) {
            int pos = i * 2;
            d[i] = (byte) (charToByte(hexChars[pos]) << 4 | charToByte(hexChars[pos + 1]));
        }
        return d;
    }

    /**
     * char转换为byte
     * Convert char to byte
     * @param c char
     * @return byte
     */
    private static byte charToByte(char c) {
        return (byte) "0123456789ABCDEF".indexOf(c);
    }

    /**
     * 将10进制转换成16进制
     * @param bytes
     * @return
     */
    public static StringBuilder bytesToHexFun3(byte[] bytes) {
        StringBuilder buf = new StringBuilder(bytes.length * 2);
        for(byte b : bytes) { // 使用String的format方法进行转换
            buf.append(String.format("%02x", new Integer(b & 0xff)));
        }
        return buf;
    }

    /**
     * 将10进制转换成16进制
     * @param bytes
     * @return
     */
    public static StringBuilder bytesToHexFun3(short[] bytes) {
        StringBuilder buf = new StringBuilder(bytes.length * 2);
        for(short b : bytes) { // 使用String的format方法进行转换
            buf.append(String.format("%02x", new Integer(b & 0xff)));
        }
        return buf;
    }

    /**
     * 将10进制转换成16进制
     * @param bytes
     * @return
     */
    private static List<String> bytesToList(byte[] bytes) {
//        StringBuilder buf = new StringBuilder(bytes.length * 2);
        List<String> list = new ArrayList<>();
        for(byte b : bytes) { // 使用String的format方法进行转换
            list.add(String.format("%02x", new Integer(b & 0xff)));
        }
        return list;
    }

    /**
     * 将16进制单精度浮点型转换为10进制浮点型，计算出最后的数据
     * @return float
     * @since 1.0
     */
    public static Float parseHex2Float(String hexStr) {
        BigInteger bigInteger = new BigInteger(hexStr, 16);
        return Float.intBitsToFloat(bigInteger.intValue());
    }

    /**
     * 将16进制单精度浮点型转换为10进制浮点型，计算出最后的数据
     * @return float
     * @since 1.0
     */
    private static Float parseHex2Integer(String hexStr) {
        return Float.intBitsToFloat(Integer.parseInt(hexStr,16));

    }

    /**
     * 字符串转换成十六进制字符串
     * @param  str 待转换的ASCII字符串
     * @return String 每个Byte之间空格分隔，如: [61 6C 6B]
     */
    public static String str2HexStr(String str)
    {

        char[] chars = "0123456789ABCDEF".toCharArray();
        StringBuilder sb = new StringBuilder("");
        byte[] bs = str.getBytes();
        int bit;


        for (int i = 0; i < bs.length; i++)
        {
            bit = (bs[i] & 0x0f0) >> 4;
            sb.append(chars[bit]);
            bit = bs[i] & 0x0f;
            sb.append(chars[bit]);
            sb.append(' ');
        }
        return sb.toString().trim();
    }

    /**
     * float32位浮点数转字节组
     * @param f
     * @return
     */
    public static  byte[] floatToByte(Float f){
        byte[] b = new byte[4];
        float f1 = Math.abs(f);
            int i = Float.floatToIntBits(f1);
            b[3] = (byte) (i % 0x100);
            b[2] = (byte) (i / 0x100 % 0x100);
            b[1] = (byte) (i / 0x10000 % 0x100);
            b[0] = (byte) (i / 0x1000000);
        if (f < 0){
            b[0] ^= 0x80;
        }
        return b;
    }

    /**
     * 将32位整数转换成长度为4的byte数组
     *
     * @param s
     *            int
     * @return byte[]
     * */
    public static byte[] intToByteArray(int s) {
        byte[] targets = new byte[4];
        for (int i = 0; i < 4; i++) {
            int offset = (targets.length - 1 - i) * 8;
            targets[i] = (byte) ((s >>> offset) & 0xff);
        }
        return targets;
    }

    /**
     * 根据edittext的tag来区分
     * @param editText
     * @return
     */
    public static byte[] dataToByteArray(EditText editText){
        byte[] data = new byte[4];
        String str = editText.getText().toString();
        if(null == str || "".equals(str)){
            str = "0";
        }
        try {
            if ("fudian".equals(editText.getTag().toString())) {
                data = floatToByte(Float.valueOf(str));
            } else if ("wufuhao".equals(editText.getTag().toString())) {
                data = intToByteArray(Integer.valueOf(str));
            }
        }catch (NumberFormatException e){
            e.printStackTrace();
        }
        return data;
    }

    /**
     * float32位无符号转字节组
     * @param f
     * @return
     */
    public static  byte[] floatToWufuhaoByte(int f){
        String string = Integer.toHexString(f);
        byte[] data = new byte[4];
        byte[] data1 = hexStringToBytes(string);
        if(null != data1 && 0 < data1.length) {
            if (1 == data1.length) {
                data[3] = data1[0];
            } else if (2 == data1.length) {
                data[3] = data1[0];
                data[2] = data1[1];
            } else if (3 == data1.length) {
                data[3] = data1[0];
                data[2] = data1[1];
                data[1] = data1[2];
            } else if(4 == data1.length){
                data[3] = data1[0];
                data[2] = data1[1];
                data[1] = data1[2];
                data[0] = data1[3];
            }
        }
        return data;
    }
}




