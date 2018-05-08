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
import com.svg.utils.SysCode;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/4/24.
 */

public class SetLineChart {

    /**
     * @param data1
     * @param data2
     */
    public static void setLineData(final Context context, final LineChartInViewPager lineChart,
                                   byte[] data1, byte[] data2, String danwei, String type, byte length) {
        HistoryBean historyBean1 = new HistoryBean();
        HistoryBean historyBean2 = new HistoryBean();
        List<Float> dataList1 = new ArrayList<>();
        List<Float> dataList2 = new ArrayList<>();
        historyBean1 = ConnectModbus.from32_Lishi(data1, type, length);
        historyBean2 = ConnectModbus.from32_Lishi(data2, type, length);
        if(null != historyBean1 && null != historyBean2) {
            int currentYear = historyBean1.getYear();
            int currentMonth = historyBean1.getMonth();
            int currentDay = historyBean1.getDay();
            int currentHour = historyBean1.getHour();
            int currentMinute = historyBean1.getMinute();
            dataList1 = historyBean1.getDatas();
            dataList2 = historyBean2.getDatas();
            if (null != dataList1 && 0 < dataList1.size()) {
                ArrayList realList = new ArrayList<>();
                for (int i = 0; i < dataList1.size(); i++) {
                    double month = 0;
                    if(SysCode.HISTYORY_YEAR.equals(type)) {
                        month = currentMonth + i;
                    } else if(SysCode.HISTYORY_MONTH.equals(type)){
                        month = currentDay + i;
                    } else {
                        month = currentHour + i;
                    }
                    RealListEntity realListEntity = new RealListEntity();
                    realListEntity.setAmount(String.valueOf(dataList1.get(i)));
                    realListEntity.setMonth(String.valueOf(month));
                    realListEntity.setYear("补偿前");
                    realList.add(realListEntity);
                }

                if (null != dataList2 && 0 < dataList2.size()) {
                    ArrayList yoyList = new ArrayList<>();
                    for (int i = 0; i < dataList2.size(); i++) {
                        double month2 = 0;
                        if(SysCode.HISTYORY_YEAR.equals(type)) {
                            month2 = currentMonth + i;
                        } else if(SysCode.HISTYORY_MONTH.equals(type)){
                            month2 = currentDay + i;
                        } else {
                            month2 = currentHour + i;
                        }
                        YoyListEntity yoyListEntity = new YoyListEntity();
                        yoyListEntity.setAmount(String.valueOf(dataList2.get(i)));
                        yoyListEntity.setMonth(String.valueOf(month2));
                        yoyListEntity.setYear("补偿后");
                        yoyList.add(yoyListEntity);
                    }
                    SetLineChart.setLineChart(context, lineChart, yoyList, realList, danwei, type);
                }
            }
        }
    }

    /**
     * @param data1
     * @param data2
     */
    public static void setLineDataABC(final Context context, final LineChartInViewPager lineChart,
                                   byte[] data1, byte[] data2, byte[] data3, String danwei, String type, byte length) {
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
            int currentYear = historyBean1.getYear();
            int currentMonth = historyBean1.getMonth();
            int currentDay = historyBean1.getDay();
            int currentHour = historyBean1.getHour();
            int currentMinute = historyBean1.getMinute();
            dataList1 = historyBean1.getDatas();
            dataList2 = historyBean2.getDatas();
            dataList3 = historyBean3.getDatas();
            if (null != dataList1 && 0 < dataList1.size()) {
                for (int i = 0; i < dataList1.size(); i++) {
                    double month = 0;
                    if(SysCode.HISTYORY_YEAR.equals(type)) {
                        month = currentMonth + i;
                    } else if(SysCode.HISTYORY_MONTH.equals(type)){
                        month = currentDay + i;
                    } else {
                        month = currentHour + i;
                    }
                    YoyListEntity realListEntity = new YoyListEntity();
                    realListEntity.setAmount(String.valueOf(dataList1.get(i)));
                    realListEntity.setMonth(String.valueOf(month));
                    realListEntity.setYear("A相");
                    AXList.add(realListEntity);
                }

                if (null != dataList2 && 0 < dataList2.size()) {
                    for (int i = 0; i < dataList2.size(); i++) {
                        double month2 = 0;
                        if(SysCode.HISTYORY_YEAR.equals(type)) {
                            month2 = currentMonth + i;
                        } else if(SysCode.HISTYORY_MONTH.equals(type)){
                            month2 = currentDay + i;
                        } else {
                            month2 = currentHour + i;
                        }
                        YoyListEntity yoyListEntity = new YoyListEntity();
                        yoyListEntity.setAmount(String.valueOf(dataList2.get(i)));
                        yoyListEntity.setMonth(String.valueOf(month2));
                        yoyListEntity.setYear("B相");
                        BXList.add(yoyListEntity);
                    }
//                    SetLineChart.setLineChart(context, lineChart, yoyList, realList, danwei, type);
                }

                if (null != dataList3 && 0 < dataList3.size()) {
                    for (int i = 0; i < dataList2.size(); i++) {
                        double month3 = 0;
                        if(SysCode.HISTYORY_YEAR.equals(type)) {
                            month3 = currentMonth + i;
                        } else if(SysCode.HISTYORY_MONTH.equals(type)){
                            month3 = currentDay + i;
                        } else {
                            month3 = currentHour + i;
                        }
                        YoyListEntity yoyListEntity = new YoyListEntity();
                        yoyListEntity.setAmount(String.valueOf(dataList3.get(i)));
                        yoyListEntity.setMonth(String.valueOf(month3));
                        yoyListEntity.setYear("C相");
                        CXList.add(yoyListEntity);
                    }
                    setLineChartABC(context, lineChart, AXList, BXList, CXList, danwei, type);
                }
            }
        }
    }

    public static void setLineChart(final Context context, final LineChartInViewPager lineChart, final List<YoyListEntity> yoyList,
                                    final List<RealListEntity> realList, final String danwei, String type) {
        ArrayList values1 = new ArrayList<>();
        ArrayList values2 = new ArrayList<>();
        for (int i = 0; i < yoyList.size(); i++) {
            YoyListEntity yoyListEntity = yoyList.get(i);
            String amount = yoyListEntity.getAmount();
            if (amount != null) {
                float f = 0;
                try {
                    f = Float.parseFloat(amount);
                } catch (Exception e) {
                    e.printStackTrace();
                    f = 0;
                }
                Entry entry = new Entry(Float.valueOf(yoyListEntity.getMonth()), f);
                values1.add(entry);
            }
        }

        for (int i = 0; i < realList.size(); i++) {
            RealListEntity realListEntity = realList.get(i);
            String amount = realListEntity.getAmount();
            if (amount != null) {
                float f = 0;
                try {
                    f = Float.parseFloat(amount);
                } catch (Exception e) {
                    e.printStackTrace();
                    f = 0;
                }
                Entry entry = new Entry(Float.valueOf(realListEntity.getMonth()), f);
                values2.add(entry);
            }
        }


        Drawable[] drawables = {
                ContextCompat.getDrawable(context, R.drawable.chart_thisyear_blue),
                ContextCompat.getDrawable(context, R.drawable.chart_callserice_call_casecount)
        };
        int[] callDurationColors = {Color.parseColor("#45A2FF"), Color.parseColor("#5fd1cc")};
        String thisYear = "";
        if (realList.size() > 0) {
            thisYear = realList.get(0).getYear();
        }

        String lastYear = "";
        if (yoyList.size() > 0) {
            lastYear = yoyList.get(0).getYear();
        }
        String[] labels = new String[]{thisYear, lastYear};
        updateLinehart(context, yoyList, realList, lineChart, callDurationColors, drawables, "", values1, values2, labels, danwei, type);
    }

    public static void setLineChartABC(final Context context, final LineChartInViewPager lineChart,
                                       final List<YoyListEntity> AXList,
                                       final List<YoyListEntity> BXList,
                                       final List<YoyListEntity> CXList,
                                       final String danwei, String type) {
        ArrayList values1 = new ArrayList<>();
        ArrayList values2 = new ArrayList<>();
        ArrayList values3 = new ArrayList<>();
        for (int i = 0; i < AXList.size(); i++) {
            YoyListEntity yoyListEntity = AXList.get(i);
            String amount = yoyListEntity.getAmount();
            if (amount != null) {
                float f = 0;
                try {
                    f = Float.parseFloat(amount);
                } catch (Exception e) {
                    e.printStackTrace();
                    f = 0;
                }
                Entry entry = new Entry(Float.valueOf(yoyListEntity.getMonth()), f);
                values1.add(entry);
            }
        }

        for (int i = 0; i < BXList.size(); i++) {
            YoyListEntity realListEntity = BXList.get(i);
            String amount = realListEntity.getAmount();
            if (amount != null) {
                float f = 0;
                try {
                    f = Float.parseFloat(amount);
                } catch (Exception e) {
                    e.printStackTrace();
                    f = 0;
                }
                Entry entry = new Entry(Float.valueOf(realListEntity.getMonth()), f);
                values2.add(entry);
            }
        }

        for (int i = 0; i < CXList.size(); i++) {
            YoyListEntity realListEntity = CXList.get(i);
            String amount = realListEntity.getAmount();
            if (amount != null) {
                float f = 0;
                try {
                    f = Float.parseFloat(amount);
                } catch (Exception e) {
                    e.printStackTrace();
                    f = 0;
                }
                Entry entry = new Entry(Float.valueOf(realListEntity.getMonth()), f);
                values3.add(entry);
            }
        }


        Drawable[] drawables = {
                ContextCompat.getDrawable(context, R.drawable.chart_thisyear_yellow),
                ContextCompat.getDrawable(context, R.drawable.chart_callserice_call_casecount),
                ContextCompat.getDrawable(context, R.drawable.chart_thisyear_red)
        };
        int[] callDurationColors = {Color.parseColor("#FFFF00"), Color.parseColor("#20B2AA"), Color.parseColor("#F08080")};
        String thisYear = "";
        if (AXList.size() > 0) {
            thisYear = AXList.get(0).getYear();
        }

        String lastYear = "";
        if (BXList.size() > 0) {
            lastYear = BXList.get(0).getYear();
        }

        String CX = "";
        if (CXList.size() > 0) {
            CX = CXList.get(0).getYear();
        }
        String[] labels = new String[]{thisYear, lastYear, CX};
        updateLinehartABC(context, AXList, BXList, CXList, lineChart, callDurationColors, drawables, "",
                values1, values2, values3, labels, danwei, type);
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
    private static void updateLinehart(final Context context, final List<YoyListEntity> yoyList, final List<RealListEntity> realList, LineChart lineChart, int[] colors, Drawable[] drawables,
                                       final String unit, List<Entry> values2, List<Entry> values1, final String[] labels, final String danwei, final String type) {
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
                                if (value > 31.0f) {
                                    return mFormat.format(value - 31.0f) + "日";
                                } else {
                                    return monthStr + "日";
                                }
                            } else {
                                return "";
                            }
                        } else {
                            if (value % 4 == 0) {
                                if (value/4 > 24.0f) {
                                    return mFormat.format(value/4 - 24.0f) + "时";
                                } else {
                                    return mFormat.format(value/4) + "时";
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
                if (index > yoyList.size() && index > realList.size()) {
                    return;
                }
                String textTemp = "";

                if (index <= yoyList.size()) {
                    if (!TextUtils.isEmpty(textTemp)) {
                    }
                    textTemp += yoyList.get(index - 1).getYear() + "." + index + "  " + mFormat.format(Float.parseFloat(yoyList.get(index - 1).getAmount())) + unit;
                }

                if (index <= realList.size()) {
                    textTemp += "\n";
                    textTemp += realList.get(index - 1).getYear() + "." + index + "  " + mFormat.format(Float.parseFloat(realList.get(index - 1).getAmount())) + unit;
                }
                markerView.getTvContent().setText(textTemp);
            }
        });
        lineChartEntity.setMarkView(markerView);
        lineChart.getData().setDrawValues(false);
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
                                          final String[] labels, final String danwei, final String type) {
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
                                if (value > 31.0f) {
                                    return mFormat.format(value - 31.0f) + "日";
                                } else {
                                    return monthStr + "日";
                                }
                            } else {
                                return "";
                            }
                        } else {
                            if (value % 4 == 0) {
                                if (value/4 > 24.0f) {
                                    return mFormat.format(value/4 - 24.0f) + "时";
                                } else {
                                    return mFormat.format(value/4) + "时";
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
                if (index > AXList.size() && index > BXList.size() && index > CXList.size()) {
                    return;
                }
                String textTemp = "";

                if (index <= AXList.size()) {
                    if (!TextUtils.isEmpty(textTemp)) {
                    }
                    textTemp += AXList.get(index - 1).getYear() + "." + index + "  " + mFormat.format(Float.parseFloat(AXList.get(index - 1).getAmount())) + unit;
                }

                if (index <= BXList.size()) {
                    textTemp += "\n";
                    textTemp += BXList.get(index - 1).getYear() + "." + index + "  " + mFormat.format(Float.parseFloat(BXList.get(index - 1).getAmount())) + unit;
                }
                if (index <= CXList.size()) {
                    textTemp += "\n";
                    textTemp += CXList.get(index - 1).getYear() + "." + index + "  " + mFormat.format(Float.parseFloat(CXList.get(index - 1).getAmount())) + unit;
                }
                markerView.getTvContent().setText(textTemp);
            }
        });
        lineChartEntity.setMarkView(markerView);
        lineChart.getData().setDrawValues(false);
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
}
