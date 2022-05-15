package com.lee.tally.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.lee.tally.R;

import java.util.ArrayList;
import java.util.List;

public class CalendarAdapter extends BaseAdapter {
    private Context context;
    private List<String> monthList;

    private int year; // 当前年份
    private int selectedMonthPosition = -1; // 当前选中的年份

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
        monthList.clear();
        loadMonth(year);
        notifyDataSetChanged();
    }

    public int getSelectedMonthPosition() {
        return selectedMonthPosition;
    }

    public void setSelectedMonthPosition(int selectedMonthPosition) {
        this.selectedMonthPosition = selectedMonthPosition;
    }

    public CalendarAdapter(Context context, int year) {
        this.context = context;
        this.year = year;
        loadMonth(year);
    }

    private void loadMonth(int year) {
        // 初始化月份
        monthList = new ArrayList<>();
        for (int i = 0; i < 12; i++) {
            String title = "";
            if (i < 9) { // 0~8 -> 1~9
                title = year + "/0" + (i + 1);
            } else {
                title = year + "/" + (i + 1);
            }
            monthList.add(title);
        }
    }

    @Override
    public int getCount() {
        return monthList.size();
    }

    @Override
    public Object getItem(int position) {
        return monthList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = LayoutInflater.from(context).inflate(R.layout.item_dialogcalendar_gv, parent, false);
        TextView monthTv = convertView.findViewById(R.id.item_dialogcalendar_gv_tv);
        monthTv.setText(monthList.get(position));
        monthTv.setBackgroundResource(R.color.grey_f3f3f3);
        monthTv.setTextColor(Color.BLACK);
        // 更新当前位置
        if (position == selectedMonthPosition) {
            monthTv.setBackgroundResource(R.color.green_006400);
            monthTv.setTextColor(Color.WHITE);
        }
        return convertView;
    }
}
