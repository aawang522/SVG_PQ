package com.svg.utils;

import java.util.Map;

/**
 * Created by Administrator on 2018/3/22.
 */

public interface HistoryResponseListner {
    /**
     * modbus请求后返回的报文数据
     * @param map
     */
    public void getResponseData(Map<String, byte[]> map);

}
