package com.lee.tally.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.lee.tally.R;
import com.lee.tally.adapter.AccountAdapter;
import com.lee.tally.dialog.CalendarDialog;
import com.lee.tally.manager.AccountManager;
import com.lee.tally.model.Account;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class HistoryActivity extends AppCompatActivity implements View.OnClickListener {
    private ListView historyLv;
    private TextView dateTv;

    private int year, month, day; // 选择年 月 日
    private List<Account> accountList; // 账单列表
    private AccountAdapter adapter; // 账单列表适配器
    private int dialogYearPosition = -1, dialogMonthPosition = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        // 查找控价
        bindView();
        // 适配器
        accountList = new ArrayList<>();
        adapter = new AccountAdapter(this, accountList);
        historyLv.setAdapter(adapter);
        // 加载
        loadDate();
        loadAccountList(year, month);
        setDeleteListener();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.history_iv_back:
                finish();
                break;
            case R.id.history_iv_calendar:
                loadCalendarDialog();
                break;
        }
    }

    /**
     * 查找并绑定控件
     */
    private void bindView() {
        historyLv = findViewById(R.id.history_lv);
        dateTv = findViewById(R.id.history_tv_date);
    }

    /**
     * 加载日期
     */
    private void loadDate() {
        Calendar calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        dateTv.setText(year + "年" + (month + 1) + "日");
    }

    /**
     * 加载用户列表
     */
    private void loadAccountList(int year, int month) {
        List<Account> list = AccountManager.getAccountListInOneMonth(year, month);
        accountList.clear();
        accountList.addAll(list);
        adapter.notifyDataSetChanged();
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
                dateTv.setText(year + "年" + (month + 1) + "月");
                loadAccountList(year, month);
                dialogYearPosition = selectedYearPosition;
                dialogMonthPosition = month;
            }
        });
    }

    /**
     * 长按删除
     */
    private void setDeleteListener() {
        historyLv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Account account = accountList.get(position);
                // 弹出是否删除对话框
                AlertDialog.Builder builder = new AlertDialog.Builder(HistoryActivity.this);
                builder.setTitle("提示信息")
                        .setMessage("长按将删除此条记录, 是否继续?")
                        .setNegativeButton("取消", null)
                        .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // 删除记录
                                AccountManager.deleteAccount(account.getId());
                                accountList.remove(account);
                                adapter.notifyDataSetChanged(); // 提示适配器更新数据
                            }
                        });
                builder.create().show();
                return false;
            }
        });
    }
}