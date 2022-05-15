package com.lee.tally.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.lee.tally.R;
import com.lee.tally.adapter.AccountAdapter;
import com.lee.tally.dialog.BudgetDialog;
import com.lee.tally.dialog.MoreDialog;
import com.lee.tally.manager.AccountManager;
import com.lee.tally.model.Account;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private ImageView searchIv;
    private ListView mainLv;
    private ImageButton editBtn, moreBtn;

    private TextView outcomeTv, incomeTv, budgetTv, todayTv;
    private ImageView hideIv;

    private int year, month, day; // 当前年 月 日
    private float outcomeToday, incomeToday, outcome, income; // 本日支出/收入 本月支出/收入
    private List<Account> accountList; // 账单列表
    private AccountAdapter adapter; // 账单列表适配器
    private View statisticView; // 统计布局
    private boolean isShown; // 统计布局数据是否显示
    private SharedPreferences preferences; // 配置信息

    /**
     * Activity创建时调用的方法
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // 查找控价
        bindView();
        // 适配器
        accountList = new ArrayList<>();
        adapter = new AccountAdapter(this, accountList);
        mainLv.setAdapter(adapter);
        // 初始化配置信息
        preferences = getSharedPreferences("budget", Context.MODE_PRIVATE);
        // 设置监听
        setListener();
    }

    /**
     * Activity获取焦点时调用的方法
     */
    @Override
    protected void onResume() {
        super.onResume();
        getDate();
        loadAccounts();
        loadStatistic();
    }

    /**
     * 点击事件
     * @param view
     */
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.main_btn_edit: // 记一笔按钮
                startActivity(new Intent(this, RecordActivity.class)); // 跳转页面
                break;
            case R.id.main_btn_more: // 更多按钮
                new MoreDialog(this).show();
                break;
            case R.id.main_iv_search: // 搜索按钮
                startActivity(new Intent(this, SearchActivity.class)); // 跳转页面
                break;
            case R.id.item_mainlv_top_tv_budget: // 预算按钮
                setBudget();
                break;
            case R.id.item_mainlv_top_iv_hide: // 隐藏/显示按钮
                toggleShow();
                break;
        }
        if (view == statisticView) { // 头布局被点击
            startActivity(new Intent(this, InfoActivity.class));
        }
    }

    /**
     * 设置监听器
     */
    private void setListener() {
        editBtn.setOnClickListener(this);
        moreBtn.setOnClickListener(this);
        searchIv.setOnClickListener(this);

        statisticView.setOnClickListener(this);
        budgetTv.setOnClickListener(this);
        hideIv.setOnClickListener(this);

        setDeleteListener(); // 设置ListView的事件监听器
    }

    /**
     * 获取当前时间
     */
    private void getDate() {
        // 获取当前日期
        Calendar calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
    }

    /**
     * 删除条目
     */
    private void setDeleteListener() {
        mainLv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 0) {
                    return false;
                }
                int position = i - 1;
                Account account = accountList.get(position);
                // 弹出是否删除对话框
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
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
                                loadStatistic(); // 更新统计数据
                            }
                        });
                builder.create().show();
                return false;
            }
        });
    }

    /**
     * 查找并绑定控件
     */
    private void bindView() {
        // 全局控件
        searchIv = findViewById(R.id.main_iv_search);
        mainLv = findViewById(R.id.main_lv);
        editBtn = findViewById(R.id.main_btn_edit);
        moreBtn = findViewById(R.id.main_btn_more);

        // 添加视图
        statisticView = getLayoutInflater().inflate(R.layout.item_mainlv_top, null);
        mainLv.addHeaderView(statisticView);

        // 统计控件
        outcomeTv = findViewById(R.id.item_mainlv_top_tv_out);
        incomeTv = findViewById(R.id.item_mainlv_top_tv_in);
        budgetTv = findViewById(R.id.item_mainlv_top_tv_budget);
        todayTv = findViewById(R.id.item_mainlv_top_tv_today);
        hideIv = findViewById(R.id.item_mainlv_top_iv_hide);
    }

    /**
     * 加载账单
     */
    private void loadAccounts() {
        List<Account> list = AccountManager.getAccountList();
        accountList.clear();
        accountList.addAll(list);
        adapter.notifyDataSetChanged();
    }

    /**
     * 更新统计数据
     */
    private void loadStatistic() {
        // 设置今日支出收入
        outcomeToday = AccountManager.getMoneyInOneDay(year, month, day, 0);
        incomeToday = AccountManager.getMoneyInOneDay(year, month, day, 1);
        String str = "今日支出 ￥" + outcomeToday + " 收入 ￥" + incomeToday;
        todayTv.setText(str);
        // 设置今月支出收入
        outcome = AccountManager.getMoneyInOneMonth(year, month, 0);
        income = AccountManager.getMoneyInOneMonth(year, month, 1);
        outcomeTv.setText("￥" + outcome);
        incomeTv.setText("￥" +income);
        // 设置预算金额
        float budget = preferences.getFloat("budget", 0);
        if (budget != 0) {
            budgetTv.setText("￥" + (budget - outcome));
        }
    }

    /**
     * 设置预算
     */
    private void setBudget() {
        BudgetDialog dialog = new BudgetDialog(this);
        dialog.show();
        // 监听dialog的确定按钮
        dialog.setOnConfirmListener(new BudgetDialog.OnConfirmListener() {
            @Override
            public void onConfirm() {
                String comment = dialog.getEditText().getText().toString();
                if (!TextUtils.isEmpty(comment)) {
                    float money = Float.parseFloat(comment);
                    if (money <= 0) {
                        Toast.makeText(getApplicationContext(), "预算金额必须大于0", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putFloat("budget", money);
                    editor.apply(); // 记录预算金额
                    float residuals = money - outcome;
                    budgetTv.setText("￥" + residuals);
                }
                dialog.cancel();
            }
        });
    }

    /**
     * 切换统计数据显示/隐藏
     */
    private void toggleShow() {
        if (isShown) { // 明文
            PasswordTransformationMethod method = PasswordTransformationMethod.getInstance();
            incomeTv.setTransformationMethod(method);
            outcomeTv.setTransformationMethod(method);
            budgetTv.setTransformationMethod(method);
            hideIv.setImageResource(R.mipmap.ih_hide);

            isShown = false;
        } else { // 密文
            HideReturnsTransformationMethod method = HideReturnsTransformationMethod.getInstance();
            incomeTv.setTransformationMethod(method);
            outcomeTv.setTransformationMethod(method);
            budgetTv.setTransformationMethod(method);
            hideIv.setImageResource(R.mipmap.ih_show);

            isShown = true;
        }
    }
}