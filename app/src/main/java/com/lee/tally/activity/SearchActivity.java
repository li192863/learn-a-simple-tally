package com.lee.tally.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.lee.tally.R;
import com.lee.tally.adapter.AccountAdapter;
import com.lee.tally.manager.AccountManager;
import com.lee.tally.model.Account;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity {
    private ImageView backIv, searchIv;
    private EditText searchEt;
    private ListView searchLv;
    private TextView emptyResultTv;

    private List<Account> accountList; // 账单列表
    private AccountAdapter adapter; // 账单列表适配器

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        // 绑定控件
        bindView();
        // 适配器
        accountList = new ArrayList<>();
        adapter = new AccountAdapter(this, accountList);
        searchLv.setAdapter(adapter);
        searchLv.setEmptyView(emptyResultTv); // 设置无数据时显示的控件
        // 键盘服务
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
        // 设置监听
        setTextChangeListener();
        setDeleteListener();
    }

    /**
     * 查找并绑定控件
     */
    private void bindView() {
        backIv = findViewById(R.id.search_iv_back);
        searchEt = findViewById(R.id.search_et);
        searchIv = findViewById(R.id.search_iv_search);

        searchLv = findViewById(R.id.search_lv);
        emptyResultTv = findViewById(R.id.search_tv_empty);
    }

    /**
     * 点击事件
     * @param view
     */
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.search_iv_back:
                finish();
                break;
            case R.id.search_iv_search: // 搜索按钮
                loadSearchResults();
                break;
        }
    }

    /**
     * 实时更新搜索结果
     */
    private void setTextChangeListener() {
        searchEt.requestFocus();
        // 监听
        searchEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                loadSearchResults();
            }
        });
    }

    /**
     * 长按删除
     */
    private void setDeleteListener() {
        searchLv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Account account = accountList.get(position);
                // 弹出是否删除对话框
                AlertDialog.Builder builder = new AlertDialog.Builder(SearchActivity.this);
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

    /**
     * 加载搜索结果
     */
    private void loadSearchResults() {
        String text = searchEt.getText().toString().trim();
        if (!TextUtils.isEmpty(text)) {
            List<Account> list = AccountManager.getAccountListByComment(text);
            accountList.clear();
            accountList.addAll(list);
            adapter.notifyDataSetChanged();
        }
    }
}