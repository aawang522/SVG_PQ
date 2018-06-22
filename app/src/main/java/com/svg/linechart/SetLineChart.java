package com.svg.linechart;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;

import com.github.mikephil.chart_3_0_1v.charts.LineChart;
import com.github.mikephil.chart_3_0_1v.components.AxisBase;
import com.github.mikephil.chart_3_0_1v.components.Legend;
import com.github.mikephil.chart_3_0_1v.data.Entry;
import com.github.mikephil.chart_3_0_1v.data.LineDataSet;
import com.github.mikephil.chart_3_0_1v.formatter.IAxisValueFormatter;
import com.github.mikephil.chart_3_0_1v.formatter.IValueFormatter;
import com.github.mikephil.chart_3_0_1v.utils.ViewPortHandler;
import com.svg.ConnectModbus;
import com.svg.R;
import com.svg.bean.HistoryBean;
import com.svg.utils.LoginingAnimation;
import com.svg.utils.SysCode;

import java.text.DecimalFormat;
import java.text.Format;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/4/24.
 */

public class SetLineChart {
    static int currentYear = 0;
    static int currentMonth = 0;
    static int currentDay = 0;
    static int currentHour = 0;
    static int currentMonthDays = 0;
    static int lastMonthDays = 0;
    /**
     * @param data1
     * @param data2
     */
    public static void setLineData(final Context context, final LineChartInViewPager lineChart,
                                   byte[] data1, byte[] data2, String danwei, String type, byte length,
                                   LoginingAnimation loginingAnimation) {
        HistoryBean historyBean1 = new HistoryBean();
        HistoryBean historyBean2 = new HistoryBean();
        List<Float> dataList1 = new ArrayList<>();
        List<Float> dataList2 = new ArrayList<>();
        historyBean1 = ConnectModbus.from32_Lishi(data1, type, length);
        historyBean2 = ConnectModbus.from32_Lishi(data2, type, length);
        if(null != historyBean1 && null != historyBean2) {
            currentYear = historyBean1.getYear();
            currentMonth = historyBean1.getMonth();
            currentDay = historyBean1.getDay();
            currentHour = historyBean1.getHour();
            // 计算当月天数和上一个月天数
            setCurrentData();
            dataList1 = historyBean1.getDatas();
            dataList2 = historyBean2.getDatas();
            if (null != dataList1 && 0 < dataList1.size()) {
                ArrayList realList = new ArrayList<>();
                for (int i = 0; i < dataList1.size(); i++) {
                    YoyListEntity realListEntity = new YoyListEntity();
                    realListEntity.setAmount(String.valueOf(dataList1.get(i)));
                    realListEntity.setMonth(String.valueOf(i));
                    realListEntity.setYear("补偿前");
                    realList.add(realListEntity);
                }

                if (null != dataList2 && 0 < dataList2.size()) {
                    ArrayList yoyList = new ArrayList<>();
                    for (int i = 0; i < dataList2.size(); i++) {
                        YoyListEntity yoyListEntity = new YoyListEntity();
                        yoyListEntity.setAmount(String.valueOf(dataList2.get(i)));
                        yoyListEntity.setMonth(String.valueOf(i));
                        yoyListEntity.setYear("补偿后");
                        yoyList.add(yoyListEntity);
                    }
                    if(null != yoyList && null != realList) {
                        SetLineChart.setLineChart(context, lineChart, yoyList, realList, danwei, type, loginingAnimation);
                    }
                }
            }
        }
    }

    /**
     * @param data1
     * @param data2
     */
    public static void setLineDataABC(final Context context, final LineChartInViewPager lineChart,
                                   byte[] data1, byte[] data2, byte[] data3, String danwei, String type, byte length,
                                              LoginingAnimation loginingAnimation) {
        HistoryBean historyBean1 = new HistoryBean();
        HistoryBean historyBean2 = new HistoryBean();
        HistoryBean historyBean3 = new HistoryBean();
        List<Float> dataList1 = new ArrayList<>();
        List<Float> dataList2 = new ArrayList<>();
        List<Float> dataList3 = new ArrayList<>();
        ArrayList AXList = new ArrayList<>();
        ArrayList BXList = new ArrayList<>();
        ArrayList CXList = new ArrayList<>();
        historyBean1 = ConnectModbus.from32_Lishi(data1, type, length);
        historyBean2 = ConnectModbus.from32_Lishi(data2, type, length);
        historyBean3 = ConnectModbus.from32_Lishi(data3, type, length);
        if(null != historyBean1 && null != historyBean2 && null != historyBean3) {
            currentYear = historyBean1.getYear();
            currentMonth = historyBean1.getMonth();
            currentDay = historyBean1.getDay();
            currentHour = historyBean1.getHour();
            // 计算当月天数和上一个月天数
            setCurrentData();
            dataList1 = historyBean1.getDatas();
            dataList2 = historyBean2.getDatas();
            dataList3 = historyBean3.getDatas();
            if (null != dataList1 && 0 < dataList1.size()) {
                for (int i = 0; i < dataList1.size(); i++) {
                    YoyListEntity realListEntity = new YoyListEntity();
                    realListEntity.setAmount(String.valueOf(dataList1.get(i)));
                    realListEntity.setMonth(String.valueOf(i));
                    realListEntity.setYear("A相");
                    AXList.add(realListEntity);
                }

                if (null != dataList2 && 0 < dataList2.size()) {
                    for (int i = 0; i < dataList2.size(); i++) {
                        YoyListEntity yoyListEntity = new YoyListEntity();
                        yoyListEntity.setAmount(String.valueOf(dataList2.get(i)));
                        yoyListEntity.setMonth(String.valueOf(i));
                        yoyListEntity.setYear("B相");
                        BXList.add(yoyListEntity);
                    }
                }

                if (null != dataList3 && 0 < dataList3.size()) {
                    for (int i = 0; i < dataList3.size(); i++) {
                        YoyListEntity yoyListEntity = new YoyListEntity();
                        yoyListEntity.setAmount(String.valueOf(dataList3.get(i)));
                        yoyListEntity.setMonth(String.valueOf(i));
                        yoyListEntity.setYear("C相");
                        CXList.add(yoyListEntity);
                    }
                    if(null != AXList && null != BXList && null != CXList) {
                        setLineChartABC(context, lineChart, AXList, BXList, CXList, danwei, type, loginingAnimation);
                    }
                }
            }
        }
    }

    public static void setLineChart(final Context context, final LineChartInViewPager lineChart,
                                    final List<YoyListEntity> yoyList,
                                    final List<YoyListEntity> realList, final String danwei, String type,
                                    LoginingAnimation loginingAnimation) {
        ArrayList values1 = new ArrayList<>();
        ArrayList values2 = new ArrayList<>();
        if(null != yoyList && 0 < yoyList.size()) {
            for (int i = 0; i < yoyList.size(); i++) {
                YoyListEntity yoyListEntity = yoyList.get(i);
                if(null != yoyListEntity) {
                    String amount = yoyListEntity.getAmount();
                    String month = yoyListEntity.getMonth();
                    if (null != amount && null != month) {
                        try {
                            float f = 0;
                            float m = 0 ;
                            f = Float.parseFloat(amount);
                            m = Float.valueOf(month);
                            Entry entry = new Entry(m, f);
                            values1.add(entry);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }

        if(null != realList && 0 < realList.size()) {
            for (int i = 0; i < realList.size(); i++) {
                YoyListEntity realListEntity = realList.get(i);
                if(null != realListEntity) {
                    String amount = realListEntity.getAmount();
                    String month = realListEntity.getMonth();
                    if (null != amount && null != month) {
                        try {
                            float f = 0;
                            float m = 0 ;
                            f = Float.parseFloat(amount);
                            m = Float.valueOf(month);
                            Entry entry = new Entry(m, f);
                            values2.add(entry);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }

        Drawable[] drawables = {
                ContextCompat.getDrawable(context, R.drawable.chart_thisyear_blue),
                ContextCompat.getDrawable(context, R.drawable.chart_callserice_call_casecount)
        };
        int[] callDurationColors = {Color.parseColor("#45A2FF"), Color.parseColor("#5fd1cc")};
        String thisYear = "";
        if (null != realList && 0 < realList.size() && null != realList.get(0)) {
            thisYear = realList.get(0).getYear();
        }

        String lastYear = "";
        if (null != yoyList && 0 < yoyList.size() && null != yoyList.get(0)) {
            lastYear = yoyList.get(0).getYear();
        }
        String[] labels = new String[]{thisYear, lastYear};
        updateLinehart(context, yoyList, realList, lineChart, callDurationColors, drawables, "",
                values1, values2, labels, danwei, type, loginingAnimation);
    }

    public static void setLineChartABC(final Context context, final LineChartInViewPager lineChart,
                                       final List<YoyListEntity> AXList,
                                       final List<YoyListEntity> BXList,
                                       final List<YoyListEntity> CXList,
                                       final String danwei, String type,
                                       LoginingAnimation loginingAnimation) {
        ArrayList values1 = new ArrayList<>();
        ArrayList values2 = new ArrayList<>();
        ArrayList values3 = new ArrayList<>();
        if(null != AXList && 0 < AXList.size()) {
            for (int i = 0; i < AXList.size(); i++) {
                YoyListEntity yoyListEntity = AXList.get(i);
                if(null != yoyListEntity) {
                    String amount = yoyListEntity.getAmount();
                    String month = yoyListEntity.getMonth();
                    if (null != amount && null != month) {
                        try {
                            float f = 0;
                            float m = 0 ;
                            f = Float.parseFloat(amount);
                            m = Float.valueOf(month);
                            Entry entry = new Entry(m, f);
                            values1.add(entry);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }

        if(null != BXList && 0 < BXList.size()) {
            for (int i = 0; i < BXList.size(); i++) {
                YoyListEntity realListEntity = BXList.get(i);
                if(null != realListEntity) {
                    String amount = realListEntity.getAmount();
                    String month = realListEntity.getMonth();
                    if (null != amount && null != month) {
                        try {
                            float f = 0;
                            float m = 0 ;
                            f = Float.parseFloat(amount);
                            m = Float.valueOf(month);
                            Entry entry = new Entry(m, f);
                            values2.add(entry);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }

        if(null != CXList && 0 < CXList.size()) {
            for (int i = 0; i < CXList.size(); i++) {
                YoyListEntity realListEntity = CXList.get(i);
                if(null != realListEntity) {
                    String amount = realListEntity.getAmount();
                    String month = realListEntity.getMonth();
                    if (null != amount && null != month) {
                        try {
                            float f = 0;
                            float m = 0 ;
                            f = Float.parseFloat(amount);
                            m = Float.valueOf(month);
                            Entry entry = new Entry(m, f);
                            values3.add(entry);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }

        Drawable[] drawables = {
                ContextCompat.getDrawable(context, R.drawable.chart_thisyear_yellow),
                ContextCompat.getDrawable(context, R.drawable.chart_callserice_call_casecount),
                ContextCompat.getDrawable(context, R.drawable.chart_thisyear_red)
        };
        int[] callDurationColors = {Color.parseColor("#FFFF00"), Color.parseColor("#20B2AA"), Color.parseColor("#F08080")};
        String thisYear = "";
        if (null != AXList && 0 < AXList.size() && null != AXList.get(0)) {
            thisYear = AXList.get(0).getYear();
        }

        String lastYear = "";
        if (null != BXList && 0 < BXList.size() && null != BXList.get(0)) {
            lastYear = BXList.get(0).getYear();
        }

        String CX = "";
        if (null != CXList && 0 < CXList.size() && null != CXList.get(0)) {
            CX = CXList.get(0).getYear();
        }
        String[] labels = new String[]{thisYear, lastYear, CX};
        updateLinehartABC(context, AXList, BXList, CXList, lineChart, callDurationColors, drawables, "",
                values1, values2, values3, labels, danwei, type, loginingAnimation);
    }

    /**
     * 双平滑曲线传入数据，添加markview，添加实体类单位
     *
     * @param yoyList
     * @param realList
     * @param lineChart
     * @param colors
     * @param drawables
     * @param unit
     * @param values2
     * @param values1
     * @param labels
     */
    private static void updateLinehart(final Context context, final List<YoyListEntity> yoyList,
                                       final List<YoyListEntity> realList, LineChart lineChart,
                                       int[] colors, Drawable[] drawables,
                                       final String unit, List<Entry> values2, List<Entry> values1,
                                       final String[] labels, final String danwei, final String type,
                                       LoginingAnimation loginingAnimation) {
        final DecimalFormat mFormat = new DecimalFormat("#,###.##");
        List<Entry>[] entries = new ArrayList[2];
        entries[0] = values1;
        entries[1] = values2;
        LineChartEntity lineChartEntity = new LineChartEntity(lineChart, entries, labels, colors, Color.parseColor("#999999"), 12f);
        lineChartEntity.drawCircle(true);
        lineChart.setScaleMinima(1.0f, 1.0f);
        toggleFilled(lineChartEntity, drawables, colors);

        /**
         * 这里切换平滑曲线或者折现图
         */
//        lineChartEntity.setLineMode(LineDataSet.Mode.CUBIC_BEZIER);
        lineChartEntity.setLineMode(LineDataSet.Mode.HORIZONTAL_BEZIER);
        lineChartEntity.initLegend(Legend.LegendForm.CIRCLE, 12f, Color.parseColor("#999999"));
        lineChartEntity.updateLegendOrientation(Legend.LegendVerticalAlignment.TOP, Legend.LegendHorizontalAlignment.RIGHT, Legend.LegendOrientation.HORIZONTAL);
        lineChartEntity.setAxisFormatter(
                new IAxisValueFormatter() {
                    @Override
                    public String getFormattedValue(float value, AxisBase axis) {
                        if(SysCode.HISTYORY_YEAR.equals(type)) {
                            value = currentMonth + value;
                        } else if(SysCode.HISTYORY_MONTH.equals(type)){
                            if(31 == currentMonthDays && 31 == currentDay) {
                                value = value + 1;
                            } else {
                                value = currentDay + value + (lastMonthDays - 30);
                            }
                        } else {
                            value = currentHour + value/4;
                        }
                        String monthStr = mFormat.format(value);
                        if (SysCode.HISTYORY_YEAR.equals(type)) {
                            if (value % 2 == 0) {
                                if (value > 12.0f) {
                                    return mFormat.format(value - 12.0f) + "月";
                                } else {
                                    return monthStr + "月";
                                }
                            } else {
                                return "";
                            }
                        } else if(SysCode.HISTYORY_MONTH.equals(type)) {
                            if (value % 2 == 0) {
                                if(31 == currentMonthDays && 31 == currentDay){
                                    return monthStr + "日";
                                }
                                else if (value > lastMonthDays) {
                                    return mFormat.format(value - lastMonthDays) + "日";
                                } else {
                                    if(-1 >= value){
                                        return "30日";
                                    } else if(0 == value){
                                        return "31日";
                                    }
                                    return monthStr + "日";
                                }
                            } else {
                                return "";
                            }
                        } else {
                            if (value % 2 == 0) {
                                if (value > 24.0f) {
                                    return mFormat.format(value - 24.0f) + "时";
                                } else {
                                    return mFormat.format(value) + "时";
                                }
                            } else {
                                return "";
                            }
                        }
                    }
                },
                new IAxisValueFormatter() {
                    @Override
                    public String getFormattedValue(float value, AxisBase axis) {
                        // unit
                        return mFormat.format(value) + danwei;
                    }
                });

        lineChartEntity.setDataValueFormatter(new IValueFormatter() {
            @Override
            public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
                return mFormat.format(value) + unit;
            }
        });

        final NewMarkerView markerView = new NewMarkerView(context, R.layout.custom_marker_view_layout);
        markerView.setCallBack(new NewMarkerView.CallBack() {
            @Override
            public void onCallBack(float x, String value) {
                int index = (int) (x);
                if (index < 0) {
                    return;
                }
                if (index >= yoyList.size() && index >= realList.size()) {
                    return;
                }
                // 点的信息展示
                StringBuffer textTemp = setPointData(index, type, danwei, mFormat, realList, yoyList, null);
                markerView.getTvContent().setText(textTemp.toString());
            }
        });
        lineChartEntity.setMarkView(markerView);
        lineChart.getData().setDrawValues(false);
        if(null != loginingAnimation && loginingAnimation.isShowed()){
            loginingAnimation.dismissLoading();
        }
    }

    /**
     * 双平滑曲线传入数据，添加markview，添加实体类单位
     *
     * @param lineChart
     * @param colors
     * @param drawables
     * @param unit
     * @param values2
     * @param values1
     * @param labels
     */
    private static void updateLinehartABC(final Context context,
                                          final List<YoyListEntity> AXList,
                                          final List<YoyListEntity> BXList,
                                          final List<YoyListEntity> CXList,
                                          LineChart lineChart, int[] colors, Drawable[] drawables,
                                          final String unit,
                                          List<Entry> values1, List<Entry> values2, List<Entry> values3,
                                          final String[] labels, final String danwei, final String type,
                                          LoginingAnimation loginingAnimation) {
        final DecimalFormat mFormat = new DecimalFormat("#,###.##");
        List<Entry>[] entries = new ArrayList[3];
        entries[0] = values1;
        entries[1] = values2;
        entries[2] = values3;
        LineChartEntity lineChartEntity = new LineChartEntity(lineChart, entries, labels, colors, Color.parseColor("#999999"), 12f);
        lineChartEntity.drawCircle(true);
        lineChart.setScaleMinima(1.0f, 1.0f);
        toggleFilled(lineChartEntity, drawables, colors);

        /**
         * 这里切换平滑曲线或者折现图
         */
//        lineChartEntity.setLineMode(LineDataSet.Mode.CUBIC_BEZIER);
        lineChartEntity.setLineMode(LineDataSet.Mode.HORIZONTAL_BEZIER);
        lineChartEntity.initLegend(Legend.LegendForm.CIRCLE, 12f, Color.parseColor("#999999"));
        lineChartEntity.updateLegendOrientation(Legend.LegendVerticalAlignment.TOP, Legend.LegendHorizontalAlignment.RIGHT, Legend.LegendOrientation.HORIZONTAL);
        lineChartEntity.setAxisFormatter(
                new IAxisValueFormatter() {
                    @Override
                    public String getFormattedValue(float value, AxisBase axis) {
                        if(SysCode.HISTYORY_YEAR.equals(type)) {
                            value = currentMonth + value;
                        } else if(SysCode.HISTYORY_MONTH.equals(type)){
                            if(31 == currentMonthDays && 31 == currentDay) {
                                value = value + 1;
                            } else {
                                value = currentDay + value + (lastMonthDays - 30);
                            }
                        } else {
                            value = currentHour + value/4;
                        }
                        String monthStr = mFormat.format(value);
                        if (SysCode.HISTYORY_YEAR.equals(type)) {
                            if (value % 2 == 0) {
                                if (value > 12.0f) {
                                    return mFormat.format(value - 12.0f) + "月";
                                } else {
                                    return monthStr + "月";
                                }
                            } else {
                                return "";
                            }
                        } else if(SysCode.HISTYORY_MONTH.equals(type)) {
                            if (value % 2 == 0) {
                                if(31 == currentMonthDays && 31 == currentDay){
                                    return monthStr + "日";
                                }
                                else if (value > lastMonthDays) {
                                    return mFormat.format(value - lastMonthDays) + "日";
                                } else {
                                    if(-1 >= value){
                                        return "30日";
                                    } else if(0 == value){
                                        return "31日";
                                    }
                                    return monthStr + "日";
                                }
                            } else {
                                return "";
                            }
                        } else {
                            if (value % 2 == 0) {
                                if (value > 24.0f) {
                                    return mFormat.format(value - 24.0f) + "时";
                                } else {
                                    return mFormat.format(value) + "时";
                                }
                            } else {
                                return "";
                            }
                        }
                    }
                },
                new IAxisValueFormatter() {
                    @Override
                    public String getFormattedValue(float value, AxisBase axis) {
                        // unit
                        return mFormat.format(value) + danwei;
                    }
                });

        lineChartEntity.setDataValueFormatter(new IValueFormatter() {
            @Override
            public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
                return mFormat.format(value) + unit;
            }
        });

        final NewMarkerView markerView = new NewMarkerView(context, R.layout.custom_marker_view_layout);
        markerView.setCallBack(new NewMarkerView.CallBack() {
            @Override
            public void onCallBack(float x, String value) {
                int index = (int) (x);
                if (index < 0) {
                    return;
                }
                if (index >= AXList.size() && index >= BXList.size() && index >= CXList.size()) {
                    return;
                }

                // 点的信息展示
                StringBuffer textTemp = setPointData(index, type, danwei, mFormat, AXList, BXList, CXList);
                markerView.getTvContent().setText(textTemp);
            }
        });
        lineChartEntity.setMarkView(markerView);
        lineChart.getData().setDrawValues(false);
        if(null != loginingAnimation && loginingAnimation.isShowed()){
            loginingAnimation.dismissLoading();
        }
    }

    /**
     * 双平滑曲线添加线下的阴影
     *
     * @param lineChartEntity
     * @param drawables
     * @param colors
     */
    private static void toggleFilled(LineChartEntity lineChartEntity, Drawable[] drawables, int[] colors) {
        if (android.os.Build.VERSION.SDK_INT >= 18) {
            lineChartEntity.toggleFilled(drawables, null, true);
        } else {
            lineChartEntity.toggleFilled(null, colors, true);
        }
    }

    /**
     * 计算当月天数和上个月天数
     */
    private static void setCurrentData(){
        if(1 == currentMonth || 3 == currentMonth || 5 == currentMonth || 7 == currentMonth ||
                8 == currentMonth || 10 == currentMonth || 12 == currentMonth) {
            currentMonthDays = 31;
        } else if (2 == currentMonth){
            if(currentYear % 4 == 0 && currentYear % 100 != 0 || currentYear % 400 == 0) {
                currentMonthDays = 29;
            } else {
                currentMonthDays = 28;
            }
        } else {
            currentMonthDays = 30;
        }
        if(1 == currentMonth-1 || 3 == currentMonth-1 || 5 == currentMonth-1 || 7 == currentMonth-1 ||
                8 == currentMonth-1 || 10 == currentMonth-1 || 0 == currentMonth) {
            lastMonthDays = 31;
        } else if (2 == currentMonth-1){
            if(currentYear % 4 == 0 && currentYear % 100 != 0 || currentYear % 400 == 0) {
                lastMonthDays = 29;
            } else {
                lastMonthDays = 28;
            }
        } else {
            lastMonthDays = 30;
        }
    }

    /**
     * 点的信息展示
     * @param index
     * @param type
     * @param danwei
     * @param mFormat
     * @param AList
     * @param BList
     * @param CList
     * @return
     */
    private static StringBuffer setPointData(int index, String type, String danwei, Format mFormat,
                                             List<YoyListEntity> AList, List<YoyListEntity> BList, List<YoyListEntity> CList){
        int time = 0;
        if(SysCode.HISTYORY_YEAR.equals(type)) {
            time = currentMonth + index;
        } else if(SysCode.HISTYORY_MONTH.equals(type)){
            if(31 == currentMonthDays && 31 == currentDay) {
                time = index + 1;
            } else {
                time = currentDay + index + (lastMonthDays - 30);
            }
        } else {
            time = currentHour + index/4;
        }

        String Aname = "";
        String Adata = "";
        if (null != AList && index < AList.size()) {
            if (null != AList.get(index)) {
                try {
                    Aname = AList.get(index).getYear();
                    Adata = mFormat.format(Float.parseFloat(AList.get(index).getAmount()));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        String Bname = "";
        String Bdata = "";
        if (null != BList && index < BList.size()) {
            if (null != BList.get(index)) {
                try {
                    Bname = BList.get(index).getYear();
                    Bdata = mFormat.format(Float.parseFloat(BList.get(index).getAmount()));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        String Cname = "";
        String Cdata = "";
        if (null != CList && index < CList.size()) {
            if (null != CList.get(index)) {
                try {
                    Cname = CList.get(index).getYear();
                    Cdata = mFormat.format(Float.parseFloat(CList.get(index).getAmount()));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        StringBuffer textTemp = new StringBuffer();
        if (SysCode.HISTYORY_YEAR.equals(type)) {
            if (null != AList && index < AList.size()) {
                textTemp.append(Aname).append(":");
                if (12.0f >= time) {
                    textTemp.append(currentYear-1).append("年").append(time).append("月  ");
                } else {
                    textTemp.append(currentYear).append("年").append(mFormat.format((time-12.0f))).append("月  ");
                }
                textTemp.append(Adata).append(danwei);
            }

            if (null != BList && index < BList.size()) {
                textTemp.append("\n");
                textTemp.append(Bname).append(":");
                if (12.0f >= time) {
                    textTemp.append(currentYear-1).append("年").append(time).append("月  ");
                } else {
                    textTemp.append(currentYear).append("年").append(mFormat.format((time-12.0f))).append("月  ");
                }
                textTemp.append(Bdata).append(danwei);
            }

            if (null != CList && index < CList.size()) {
                textTemp.append("\n");
                textTemp.append(Cname).append(":");
                if (12.0f >= time) {
                    textTemp.append(currentYear-1).append("年").append(time).append("月  ");
                } else {
                    textTemp.append(currentYear).append("年").append(mFormat.format((time-12.0f))).append("月  ");
                }
                textTemp.append(Cdata).append(danwei);
            }
        } else if(SysCode.HISTYORY_MONTH.equals(type)) {
            if (null != AList && index < AList.size()) {
                textTemp.append(Aname).append(":");
                if(31 == currentMonthDays && 31 == currentDay) {
                    textTemp.append(currentMonth).append("月").append(mFormat.format(time)).append("日  ");
                } else if (lastMonthDays >= time && time > 0) {
                    if(1 < currentMonth) {
                        textTemp.append(currentMonth - 1).append("月").append(time).append("日  ");
                    } else {
                        textTemp.append("12月").append(time).append("日  ");
                    }
                } else if (0 >= time){
                    textTemp.append(currentMonth-2).append("月").append(mFormat.format(time+31)).append("日  ");
                } else{
                    textTemp.append(currentMonth).append("月").append(mFormat.format(time-lastMonthDays)).append("日  ");
                }
                textTemp.append(Adata).append(danwei);
            }

            if (null != BList && index < BList.size()) {
                textTemp.append("\n");
                textTemp.append(Bname).append(":");
                if(31 == currentMonthDays && 31 == currentDay) {
                    textTemp.append(currentMonth).append("月").append(mFormat.format(time)).append("日  ");
                }
                else if (lastMonthDays >= time && time > 0) {
                    if(1 < currentMonth) {
                        textTemp.append(currentMonth - 1).append("月").append(time).append("日  ");
                    } else {
                        textTemp.append("12月").append(time).append("日  ");
                    }
                } else if (0 >= time){
                    textTemp.append(currentMonth-2).append("月").append(mFormat.format(time+31)).append("日  ");
                } else {
                    textTemp.append(currentMonth).append("月").append(mFormat.format((time-lastMonthDays))).append("日  ");
                }
                textTemp.append(Bdata).append(danwei);
            }

            if (null != CList && index < CList.size()) {
                textTemp.append("\n");
                textTemp.append(Cname).append(":");
                if(31 == currentMonthDays && 31 == currentDay) {
                    textTemp.append(currentMonth).append("月").append(mFormat.format(time)).append("日  ");
                }
                else if (lastMonthDays >= time && time > 0) {
                    if(1 < currentMonth) {
                        textTemp.append(currentMonth - 1).append("月").append(time).append("日  ");
                    } else {
                        textTemp.append("12月").append(time).append("日  ");
                    }
                } else if (0 >= time){
                    textTemp.append(currentMonth-2).append("月").append(mFormat.format(time+31)).append("日  ");
                } else {
                    textTemp.append(currentMonth).append("月").append(mFormat.format((time-lastMonthDays))).append("日  ");
                }
                textTemp.append(Cdata).append(danwei);
            }
        } else {
            if (null != AList && index < AList.size()) {
                textTemp.append(Aname).append(":");
                if (24.0f >= time) {
                    textTemp.append("昨日").append(time).append("时 ");
                } else {
                    textTemp.append("今日").append(mFormat.format((time-24.0f))).append("时 ");
                }
                textTemp.append(Adata).append(danwei);
            }

            if (null != BList && index < BList.size()) {
                textTemp.append("\n");
                textTemp.append(Bname).append(":");
                if (24.0f >= time) {
                    textTemp.append("昨日").append(time).append("时 ");
                } else {
                    textTemp.append("今日").append(mFormat.format((time-24.0f))).append("时 ");
                }
                textTemp.append(Bdata).append(danwei);
            }

            if (null != CList && index < CList.size()) {
                textTemp.append("\n");
                textTemp.append(Cname).append(":");
                if (24.0f >= time) {
                    textTemp.append("昨日").append(time).append("时 ");
                } else {
                    textTemp.append("今日").append(mFormat.format((time-24.0f))).append("时 ");
                }
                textTemp.append(Cdata).append(danwei);
            }
        }
        return textTemp;
    }
}
