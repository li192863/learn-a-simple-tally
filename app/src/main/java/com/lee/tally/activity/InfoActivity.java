package com.lee.tally.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.lee.tally.R;
import com.lee.tally.adapter.InfoPagerAdapter;
import com.lee.tally.dialog.CalendarDialog;
import com.lee.tally.fragment.InfoFragment;
import com.lee.tally.manager.AccountManager;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class InfoActivity extends AppCompatActivity {
    private Button outcomeBtn, incomeBtn;
    private TextView dateTv, outcomeTv, incomeTv;
    private ViewPager infoVp;

    private int year, month, day; // 当前年 月 日
    private int dialogYearPosition = -1, dialogMonthPosition = -1; // 对话框年份位置 月份位置
    private List<Fragment> fragmentList;
    private InfoPagerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);
        bindView();
        loadDate();
        loadStatistic(year, month);
        loadFragment();
        setChartItemListener();
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.info_iv_back:
                finish();
                break;
            case R.id.info_iv_calendar:
                loadCalendarDialog();
                break;
            case R.id.info_btn_outcome:
                loadButton(0);
                infoVp.setCurrentItem(0);
                break;
            case R.id.info_btn_income:
                loadButton(1);
                infoVp.setCurrentItem(1);
                break;
        }
    }

    private void bindView() {
        outcomeBtn = findViewById(R.id.info_btn_outcome);
        incomeBtn = findViewById(R.id.info_btn_income);
        dateTv = findViewById(R.id.info_tv_date);
        outcomeTv = findViewById(R.id.info_tv_outcome);
        incomeTv = findViewById(R.id.info_tv_income);
        infoVp = findViewById(R.id.info_vp);
    }

    /**
     * 根据点击时间加载按钮 支出0 收入1
     * @param kind
     */
    private void loadButton(int kind) {
        if (kind == 0) {
            outcomeBtn.setBackgroundResource(R.drawable.record_btn_bg);
            outcomeBtn.setTextColor(Color.WHITE);
            incomeBtn.setBackgroundResource(R.drawable.dialog_btn_bg);
            incomeBtn.setTextColor(Color.BLACK);
        } else {
            incomeBtn.setBackgroundResource(R.drawable.record_btn_bg);
            incomeBtn.setTextColor(Color.WHITE);
            outcomeBtn.setBackgroundResource(R.drawable.dialog_btn_bg);
            outcomeBtn.setTextColor(Color.BLACK);
        }
    }

    /**
     * 初始化时间
     */
    private void loadDate() {
        Calendar calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
    }

    /**
     * 加载统计数据
     * @param year
     * @param month
     */
    private void loadStatistic(int year, int month) {
        float outcomeMoney = AccountManager.getMoneyInOneMonth(year, month, 0);
        float incomeMoney = AccountManager.getMoneyInOneMonth(year, month, 1);
        int outcomeCount = AccountManager.getCountInOneMonth(year, month, 0);
        int incomeCount = AccountManager.getCountInOneMonth(year, month, 1);
        dateTv.setText(year + "年" + (month + 1) + "月账单");
        outcomeTv.setText("共" + outcomeCount + "笔支出，共￥" + outcomeMoney + "元。");
        incomeTv.setText("共" + incomeCount + "笔收入，共￥" + incomeMoney + "元。");
    }

    /**
     * 加载日历对话框
     */
    private void loadCalendarDialog() {
        CalendarDialog dialog = new CalendarDialog(this, dialogYearPosition, dialogMonthPosition);
        dialog.show();
        dialog.setOnRefreshListener(new CalendarDialog.OnRefreshListener() {
            @Override
            public void onRefresh(int selectedYearPosition, int year, int month) {
                dialogYearPosition = selectedYearPosition;
                dialogMonthPosition = month;
                ((InfoFragment) fragmentList.get(0)).setDate(year, month);
                ((InfoFragment) fragmentList.get(1)).setDate(year, month);
                loadStatistic(year, month);
            }
        });
    }

    /**
     * 加载Fragment
     */
    private void loadFragment() {
        fragmentList = new ArrayList<>(); // 初始化ViewPager页面集合
        InfoFragment outcomeFragment = new InfoFragment(0);
        InfoFragment incomeFragment = new InfoFragment(1);
        // 添加数据
        Bundle bundle = new Bundle();
        bundle.putInt("year", year);
        bundle.putInt("month", month);
        outcomeFragment.setArguments(bundle);
        incomeFragment.setArguments(bundle);
        // 添加至集合
        fragmentList.add(outcomeFragment);
        fragmentList.add(incomeFragment);
        // 适配器
        adapter = new InfoPagerAdapter(getSupportFragmentManager(), fragmentList);
        infoVp.setAdapter(adapter);
    }


    /**
     * 设置条目的监听器
     */
    private void setChartItemListener() {
        infoVp.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                loadButton(position);
            }
        });
    }
}