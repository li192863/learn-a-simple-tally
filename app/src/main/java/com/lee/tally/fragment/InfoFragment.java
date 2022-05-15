package com.lee.tally.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.utils.ViewPortHandler;
import com.lee.tally.R;
import com.lee.tally.adapter.ChartItemAdapter;
import com.lee.tally.manager.ChartManager;
import com.lee.tally.model.BarDatum;
import com.lee.tally.model.ChartItem;
import com.lee.tally.util.DateTimeUtil;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class InfoFragment extends Fragment {
    private ListView infoLv;
    private View headerView;
    private BarChart barChart;
    private TextView emptyChartItemTv;

    private int kind;
    private int year, month;
    private List<ChartItem> chartItemList;
    private ChartItemAdapter adapter;

    public InfoFragment(int kind) {
        this.kind = kind;
    }

    public void setDate(int year, int month) {
        this.year = year;
        this.month = month;
        loadChartItem(year, month);
        barChart.clear();
        barChart.invalidate();
        setXAxisStyle(year, month);
        setYAxisStyle(year, month);

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // 渲染视图
        View view = inflater.inflate(R.layout.fragment_info, container, false);
        // 获取各个布局对象
        infoLv = view.findViewById(R.id.frag_info_lv);
        // 获取数据
        Bundle arguments = getArguments();
        year = arguments.getInt("year");
        month = arguments.getInt("month");
        // 加载视图
        chartItemList = new ArrayList<>();
        adapter = new ChartItemAdapter(getContext(), chartItemList);
        infoLv.setAdapter(adapter);
        // 添加柱状图
        loadChart(year, month);
        return view;
    }

    /**
     * Activity获取焦点时调用的方法
     */
    @Override
    public void onResume() {
        super.onResume();
        getDate();
        loadChartItem(year, month);
    }

    /**
     * 加载柱状图
     */
    private void loadChart(int year, int month) {
        // 绑定布局
        headerView = getLayoutInflater().inflate(R.layout.item_infofrag_top, null);
        infoLv.addHeaderView(headerView);
        barChart = headerView.findViewById(R.id.item_infofrag_top_chart);
        emptyChartItemTv = headerView.findViewById(R.id.item_infofrag_top_tv);
        // 设置柱状图样式
        setChartStyle(year, month);

    }

    /**
     * 设置柱状图样式
     */
    private void setChartStyle(int year, int month) {
        // 初始化
        barChart.getDescription().setEnabled(false);
        barChart.getLegend().setEnabled(false);
        barChart.setExtraOffsets(20, 20, 20, 20);
        // x轴
        setXAxisStyle(year, month);
        // y轴
        setYAxisStyle(year, month);
    }

    private void setYAxisStyle(int year, int month) {
        int maxMoneyInOneMonth = Math.round(ChartManager.getMaxMoneyInOneMonth(year, month, kind));
        YAxis yAxis_right = barChart.getAxisRight();
        yAxis_right.setAxisMinimum(0F);
        yAxis_right.setAxisMaximum(maxMoneyInOneMonth);
        yAxis_right.setEnabled(false);
        YAxis yAxis_left = barChart.getAxisLeft();
        yAxis_left.setAxisMinimum(0F);
        yAxis_left.setAxisMaximum(maxMoneyInOneMonth);
        yAxis_left.setEnabled(false);
        // 数据
        List<IBarDataSet> sets = new ArrayList<>();
        List<BarDatum> barData = ChartManager.getBarDataInOneMonth(year, month, kind);
        // 若无数据
        if (barData.size() == 0) {
            barChart.setVisibility(View.GONE);
            emptyChartItemTv.setVisibility(View.VISIBLE);
            return;
        }
        // 若有数据
        barChart.setVisibility(View.VISIBLE);
        emptyChartItemTv.setVisibility(View.GONE);
        // 初始化每天数据
        List<BarEntry> entries = new ArrayList<>(31);
        for (int i = 0; i < DateTimeUtil.getMaxDayOfMonth(year, month); i++) {
            entries.add(new BarEntry(i, 0.0F));
        }
        for (int i = 0; i < barData.size(); i++) {
            BarDatum barDatum = barData.get(i);
            entries.get(barDatum.getDay() - 1).setY(barDatum.getMoney());
        }
        BarDataSet barDataSet = new BarDataSet(entries, "");
        barDataSet.setValueTextColor(Color.BLACK);
        barDataSet.setValueTextSize(15f);
        barDataSet.setColor(kind == 0 ? Color.RED: Color.GREEN);
        barDataSet.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                int val = Math.round(value);
                if (value == 0) {
                    return "";
                }
                return value + "";
            }
        });
        sets.add(barDataSet);
        BarData data = new BarData(sets);
        data.setBarWidth(0.2f);
        barChart.setData(data);
    }

    private void setXAxisStyle(int year, int month) {
        XAxis xAxis = barChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM); // 轴的位置为底部
        xAxis.setDrawGridLines(true); // 显示绘制该轴
        xAxis.setLabelCount(31, true); // 共31个标签
        xAxis.setTextSize(12F); // 文字大小
        xAxis.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                int val = Math.round(value);
                if (val == 0) {
                    return (month + 1) + "-01";
                }
                if (val == 14) {
                    return (month + 1) + "-15";
                }
                int maxDayOfMonth = DateTimeUtil.getMaxDayOfMonth(year, month);
                if (val == maxDayOfMonth - 1) {
                    return (month + 1) + "-" + maxDayOfMonth;
                }
                return "";
            }
        });
        xAxis.setYOffset(15); // 设置标签对x轴偏移量
    }

    /**
     * 获取当前时间
     */
    private void getDate() {
        // 获取当前日期
        Calendar calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
    }

    private void loadChartItem(int year, int month) {
        List<ChartItem> list = ChartManager.getChartItemListInOneMonth(year, month, kind);
        chartItemList.clear();
        chartItemList.addAll(list);
        adapter.notifyDataSetChanged();
    }
}