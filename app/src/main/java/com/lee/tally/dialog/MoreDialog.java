package com.lee.tally.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.lee.tally.R;
import com.lee.tally.activity.AboutActivity;
import com.lee.tally.activity.HistoryActivity;
import com.lee.tally.activity.InfoActivity;
import com.lee.tally.activity.SettingActivity;

public class MoreDialog extends Dialog implements View.OnClickListener {
    private Button aboutBtn, settingBtn, historyBtn, infoBtn;
    private ImageView closeIv;

    public MoreDialog(@NonNull Context context) {
        super(context);
        setContentView(R.layout.dialog_more);
        setDialogStyle();

        bindView();

        setListener();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.dialog_more_btn_about:
                getContext().startActivity(new Intent(getContext(), AboutActivity.class));
                break;
            case R.id.dialog_more_btn_setting:
                getContext().startActivity(new Intent(getContext(), SettingActivity.class));
                break;
            case R.id.dialog_more_btn_history:
                getContext().startActivity(new Intent(getContext(), HistoryActivity.class));
                break;
            case R.id.dialog_more_btn_info:
                getContext().startActivity(new Intent(getContext(), InfoActivity.class));
                break;
            case R.id.dialog_more_iv_back:
                cancel();
                break;
        }
        cancel();
    }

    private void setListener() {
        aboutBtn.setOnClickListener(this);
        settingBtn.setOnClickListener(this);
        historyBtn.setOnClickListener(this);
        infoBtn.setOnClickListener(this);
        closeIv.setOnClickListener(this);
    }

    private void bindView() {
        aboutBtn = findViewById(R.id.dialog_more_btn_about);
        settingBtn = findViewById(R.id.dialog_more_btn_setting);
        historyBtn = findViewById(R.id.dialog_more_btn_history);
        infoBtn = findViewById(R.id.dialog_more_btn_info);
        closeIv = findViewById(R.id.dialog_more_iv_back);
    }

    /**
     * 设置窗口显示风格
     */
    private void setDialogStyle() {
        Window window = getWindow(); // 获取窗口对象
        WindowManager.LayoutParams attributes = window.getAttributes(); // 窗口对象参数
        Display display = window.getWindowManager().getDefaultDisplay(); // 屏幕宽度

        attributes.width = display.getWidth(); // 对话框窗口为屏幕窗口
        attributes.gravity = Gravity.BOTTOM; // 对话框从下至上显示

        window.setAttributes(attributes);
        window.setBackgroundDrawableResource(android.R.color.transparent);
    }
}
