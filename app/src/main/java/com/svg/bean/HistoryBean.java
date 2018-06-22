package com.svg.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/4/27.
 */

public class HistoryBean {
    private int year = 0;
    private int month = 0;
    private int day = 0;
    private int hour = 0;
    private int minute = 0;
    private List<Float> datas = new ArrayList<>();

    public HistoryBean() {
    }

    public HistoryBean(int year, int month, int day, int hour, int minute, List<Float> datas) {
        this.year = year;
        this.month = month;
        this.day = day;
        this.hour = hour;
        this.minute = minute;
        this.datas = datas;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public int getMinute() {
        return minute;
    }

    public void setMinute(int minute) {
        this.minute = minute;
    }

    public List<Float> getDatas() {
        return datas;
    }

    public void setDatas(List<Float> datas) {
        this.datas = datas;
    }
}
