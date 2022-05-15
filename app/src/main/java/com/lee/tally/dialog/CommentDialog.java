package com.lee.tally.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;

import com.lee.tally.R;


public class CommentDialog extends Dialog implements View.OnClickListener {
    private EditText editText;
    private Button cancelButton, confirmButton;

    private OnConfirmListener onConfirmListener;

    public CommentDialog(@NonNull Context context) {
        super(context);
    }

    public void setOnConfirmListener(OnConfirmListener onConfirmListener) {
        this.onConfirmListener = onConfirmListener;
    }

    public EditText getEditText() {
        return editText;
    }

    public void setEditText(EditText editText) {
        this.editText = editText;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_comment); // 对话框显示布局

        editText = findViewById(R.id.dialog_comment_et);
        cancelButton = findViewById(R.id.dialog_comment_btn_cancel);
        confirmButton = findViewById(R.id.dialog_comment_btn_confirm);

        this.setDialogStyle(); // 设置显示风格

        cancelButton.setOnClickListener(this);
        confirmButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.dialog_comment_btn_cancel:
                cancel();
                break;
            case R.id.dialog_comment_btn_confirm:
                if (onConfirmListener != null) {
                    onConfirmListener.onConfirm();
                }
                break;
        }
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

        Handler handler = new Handler() {
            @Override
            public void handleMessage(@NonNull Message msg) {
                InputMethodManager manager = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                manager.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
            }
        };
        handler.sendEmptyMessageDelayed(1, 100);
    }


    /**
     * 点击确定监听器接口
     */
    public interface OnConfirmListener {
        void onConfirm();
    }
}
