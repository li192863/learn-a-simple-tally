package com.lee.tally.activity;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.google.android.material.tabs.TabLayout;
import com.lee.tally.R;
import com.lee.tally.adapter.RecordPagerAdapter;
import com.lee.tally.fragment.RecordFragment;
import com.lee.tally.manager.AccountManager;
import com.lee.tally.model.Account;

import java.util.ArrayList;
import java.util.List;

public class RecordActivity extends AppCompatActivity {
    private TabLayout recordTab;
    private ViewPager recordVp;

    private List<Fragment> fragmentList; // viewPager显示的Fragment
    private RecordPagerAdapter adapter; // 适配器
    private Account account; // 需要插入的记录(类型名称 类型图片 备注 金额 时间 类型=0)
    private boolean isIncomeTypeSelect; // 收入类型是否被点击
    private boolean isOutcomeTypeSelect; // 支出类型是否被点击

    /**
     * Activity创建时调用的方法
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);
        // 记录账户
        account = new Account();
        // 查找控价
        bindView();
        // 适配器
        loadFragment();
        // 处理页面滑动
        setPageChangeListener();
        // 键盘服务
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
    }

    /**
     * 点击事件
     * @param view
     */
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.record_iv_back: // 关闭按钮
                finish();
                break;
            case R.id.record_iv_save: // 保存按钮
                saveAccount();
                finish();
        }
    }

    private void saveAccount() {
        if (account.getMoney() <= 0) {
            finish();
        } else {
            AccountManager.insertAccount(account);
        }
    }

    public Account getAccount() {
        return account;
    }

    public void setIncomeTypeSelect(boolean incomeTypeSelect) {
        isIncomeTypeSelect = incomeTypeSelect;
    }

    public void setOutcomeTypeSelect(boolean outcomeTypeSelect) {
        isOutcomeTypeSelect = outcomeTypeSelect;
    }

    /**
     * 设置适配器
     */
    private void loadFragment() {
        fragmentList = new ArrayList<>(); // 初始化ViewPager页面集合
        RecordFragment outcomeFragment = new RecordFragment(0);
        RecordFragment incomeFragment = new RecordFragment(1);
        // 添加至集合
        fragmentList.add(outcomeFragment);
        fragmentList.add(incomeFragment);
        // 适配器
        adapter = new RecordPagerAdapter(getSupportFragmentManager(), fragmentList); // 创建适配器
        recordVp.setAdapter(adapter); // 设置适配器
        recordTab.setupWithViewPager(recordVp); // 关联TabLayout与ViewPager
    }

    /**
     * 查找并绑定控件
     */
    private void bindView() {
        recordTab = findViewById(R.id.record_tabs);
        recordVp = findViewById(R.id.record_vp);
    }

    /**
     * 处理页面滑动
     */
    private void setPageChangeListener() {
        // 初始化记录
        account.setTypeName("其他"); // 记录类型名称
        account.setSelectImageId(R.mipmap.ic_qita_fs); // 记录类型图片
        account.setKind(0); // 记录类型
        // 监听
        recordVp.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onPageSelected(int position) {
                if (position == 0 && !isOutcomeTypeSelect) {
                    account.setTypeName("其他"); // 记录类型名称
                    account.setSelectImageId(R.mipmap.ic_qita_fs); // 记录类型图片
                    account.setKind(0); // 记录类型
                } else if (position == 1 && !isIncomeTypeSelect){
                    account.setTypeName("其他"); // 记录类型名称
                    account.setSelectImageId(R.mipmap.in_qita_fs); // 记录类型图片
                    account.setKind(1); // 记录类型
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }
}