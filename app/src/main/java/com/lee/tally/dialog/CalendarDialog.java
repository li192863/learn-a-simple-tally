package com.lee.tally.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.lee.tally.R;
import com.lee.tally.adapter.CalendarAdapter;
import com.lee.tally.manager.AccountManager;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class CalendarDialog extends Dialog implements View.OnClickListener {
    private ImageView closeIv;
    private LinearLayout yearLl; // 显示年份
    private GridView monthGv; // 显示月份

    private List<TextView> yearTvList; // 年份视图列表
    private List<Integer> yearList; // 年份列表
    private int selectedYearPosition = -1, selectedMonthPosition = -1; // 当前正在被点击的年份 月份
    private CalendarAdapter adapter; // 日历适配器(显示月份)
    private OnRefreshListener onRefreshListener; // 选中月份时的监听器(回调函数)

    public void setOnRefreshListener(OnRefreshListener onRefreshListener) {
        this.onRefreshListener = onRefreshListener;
    }

    public CalendarDialog(@NonNull Context context, int dialogYearPosition, int dialogMonthPosition) {
        super(context);
        this.selectedYearPosition = dialogYearPosition;
        this.selectedMonthPosition = dialogMonthPosition;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_calendar);
        setDialogStyle();
        // 查找控价
        bindView();
        // 设置监听
        closeIv.setOnClickListener(this);
        // 添加年份
        loadYear();
        // 添加月份
        loadMonth();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.dialog_calendar_iv:
                cancel();
                break;
        }
    }

    private void bindView() {
        closeIv = findViewById(R.id.dialog_calendar_iv);
        monthGv = findViewById(R.id.dialog_calendar_gv);
        yearLl = findViewById(R.id.dialog_calendar_ll);
    }

    /**
     * 加载年份
     */
    private void loadYear() {
        yearTvList = new ArrayList<>();
        yearList = AccountManager.getYearList();
        // 若无记录则添加
        if (yearList.size() == 0) {
            int year = Calendar.getInstance().get(Calendar.YEAR);
            yearList.add(year);
        }
        // 遍历年份 添加年份视图
        for (int year: yearList) {
            View view = getLayoutInflater().inflate(R.layout.item_dialogcalendar_hsv, null);
            yearLl.addView(view); // 将view添加到布局当中

            TextView yearTv = view.findViewById(R.id.item_dialogcalendar_hsv_tv);
            yearTv.setText(year + "");
            yearTvList.add(yearTv);
        }
        // 更新当前位置
        if (selectedYearPosition == -1) {
            selectedYearPosition = yearTvList.size() - 1; // 若还未选年份则设置当前年份为最近的一年
        }
        // 选中当前年份
        selectYear(selectedYearPosition); // 设置年份样式
        // 设置监听器
        setYearListener();
    }

    /**
     * 改变指定位置的背景和文字颜色
     * @param selectPosition
     */
    private void selectYear(int selectPosition) {
        for (TextView view : yearTvList) {
            view.setBackgroundResource(R.drawable.dialog_btn_bg);
            view.setTextColor(Color.BLACK);
        }
        TextView selectedView = yearTvList.get(selectPosition);
        selectedView.setBackgroundResource(R.drawable.record_btn_bg);
        selectedView.setTextColor(Color.WHITE);
    }

    /**
     * 设置年份TextView监听器
     */
    private void setYearListener() {
        for (int i = 0; i < yearTvList.size(); i++) {
            TextView yearTv = yearTvList.get(i);
            int position = i;
            yearTv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    selectYear(position);  // 设置年份样式
                    selectedYearPosition = position; // 更新当前选中年份位置

                    Integer year = yearList.get(selectedYearPosition); // 获取年份
                    adapter.setYear(year); // 更新月份(GridView中月份会发生改变)
                }
            });
        }
    }

    /**
     * 加载月份
     */
    private void loadMonth() {
        int year = yearList.get(selectedYearPosition); // 当前选中的年
        adapter = new CalendarAdapter(getContext(), year);
        // 更新当前位置
        if (selectedMonthPosition == -1) {
            int month = Calendar.getInstance().get(Calendar.MONTH);
            adapter.setSelectedMonthPosition(month); // 若还未选月份则设置当前月份为当前月
        } else {
            adapter.setSelectedMonthPosition(selectedMonthPosition); // 若已选择月份则传入当前已选择的月份
        }
        monthGv.setAdapter(adapter);
        // 设置监听器
        setMonthListener();
    }

    /**
     * 设置月份TextView监听器
     */
    private void setMonthListener() {
        monthGv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                adapter.setSelectedMonthPosition(position);
                adapter.notifyDataSetChanged();
                // 获取到被选中的年份和月份
                int year = adapter.getYear(); // 当前年份
                int month = adapter.getSelectedMonthPosition(); // 当前月份 0~11
                onRefreshListener.onRefresh(selectedYearPosition, year, month);
                cancel();
            }
        });
    }


    /**
     * 设置窗口显示风格
     */
    private void setDialogStyle() {
        Window window = getWindow(); // 获取窗口对象
        WindowManager.LayoutParams attributes = window.getAttributes(); // 窗口对象参数
        Display display = window.getWindowManager().getDefaultDisplay(); // 屏幕宽度

        attributes.width = display.getWidth(); // 对话框窗口为屏幕窗口
        attributes.gravity = Gravity.TOP; // 对话框从上至下显示

        window.setAttributes(attributes);
        window.setBackgroundDrawableResource(android.R.color.transparent);
    }

    public interface OnRefreshListener {
        void onRefresh(int selectedYearPosition, int year, int month);
    }
}
