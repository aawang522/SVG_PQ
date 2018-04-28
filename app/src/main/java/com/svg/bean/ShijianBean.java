package com.svg.bean;

/** 事件实体类
 * Created by Administrator on 2018/4/4.
 */

public class ShijianBean {
    String date;
    String time;
    String title;
    String value;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public ShijianBean(String date, String time, String title, String value) {
        this.date = date;
        this.time = time;
        this.title = title;
        this.value = value;
    }
}
