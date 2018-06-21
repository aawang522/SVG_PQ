package com.svg.utils;

/**
 * Created by Administrator on 2018/3/22.
 */

public interface ModbusResponseListner {
    /**
     * modbus请求后返回的报文数据
     * @param data
     */
    public void getResponseData(byte[] data);

    /**
     * modbus请求失败
     */
    public void failedResponse();

    /**
     * modbus请求后返回的报文数据
     * @param data
     */
    public void getSubmitResponseData(byte[] data);

    /**
     * modbus提交失败
     */
    public void submitFailedResponse();
}
