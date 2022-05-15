package com.lee.tally.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.lee.tally.R;
import com.lee.tally.manager.AccountManager;

public class SettingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
    }


    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.setting_iv_back:
                finish();
                break;
            case R.id.setting_tv_clear:
                // 弹出是否删除对话框
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("提示信息")
                        .setMessage("即将清空所有记录, 是否继续?")
                        .setNegativeButton("取消", null)
                        .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // 删除记录
                                AccountManager.deleteAllAccounts();
                                Toast.makeText(SettingActivity.this, "已清空所有记录!", Toast.LENGTH_SHORT).show();
                            }
                        });
                builder.create().show();
                break;
        }
    }
}