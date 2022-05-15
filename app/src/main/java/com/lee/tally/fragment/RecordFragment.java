package com.lee.tally.fragment;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.lee.tally.R;
import com.lee.tally.activity.RecordActivity;
import com.lee.tally.adapter.TypeAdapter;
import com.lee.tally.dialog.CommentDialog;
import com.lee.tally.manager.DBManager;
import com.lee.tally.model.Type;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 支出页面
 */
public class RecordFragment extends Fragment {
    private EditText moneyEt; // 金额
    private ImageView typeIv; // 类型图片
    private TextView typeTv, commentTv, dateTv, timeTv; // 类型名称 备注 日期 时间
    private GridView typeGv; // 条目

    private RecordActivity activity; // 获取RecordActivity
    private int kind; // 支出0 收入1

    public RecordFragment(int kind) {
        this.kind = kind;
    }

    /**
     * 创建Fragment时调用 只调用一次
     * @param savedInstanceState
     */
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = (RecordActivity) this.getActivity();
    }

    /**
     * 每次创建该fragment对应的视图调用
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // 渲染视图
        View view = inflater.inflate(R.layout.fragment_record, container, false); // The root View of the inflated hierarchy.
        // 获取各个布局对象
        moneyEt = view.findViewById(R.id.frag_record_et_money); // 金额
        typeIv = view.findViewById(R.id.frag_record_iv); // 类型图片
        typeTv = view.findViewById(R.id.frag_record_tv_type); // 类型名称
        commentTv = view.findViewById(R.id.frag_record_tv_comment); // 备注
        dateTv = view.findViewById(R.id.frag_record_tv_date); // 日期
        timeTv = view.findViewById(R.id.frag_record_tv_time); // 时间
        typeGv = view.findViewById(R.id.frag_record_gv); // 条目(GridView)
//        // 初始化记录
//        activity.getAccount().setTypeName("其他"); // 记录类型名称
//        activity.getAccount().setSelectImageId(R.mipmap.ic_qita_fs); // 记录类型图片
//        activity.getAccount().setKind(0); // 记录类型
        // 加载视图
        loadKeyboard(); // 键盘 金额
        loadType(); // 条目
        loadDateTime(); // 日期 时间
        loadComment(); // 备注
        return view;
    }

    /**
     * 加载键盘并监听
     */
    private void loadKeyboard() {
        // 加载
        moneyEt.requestFocus();
        // 监听
        moneyEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                // 获取输入钱数
                String moneyStr = moneyEt.getText().toString();
                Float money = 0.0f;
                if (TextUtils.isEmpty(moneyStr)) {
                    activity.getAccount().setMoney(money); // 记录金额
                    return;
                }
                activity.getAccount().setMoney(Float.parseFloat(moneyStr)); // 记录金额
            }
        });
    }

    /**
     * 加载条目(GridView)并监听
     */
    private void loadType() {
        // 加载
        List<Type> typeList = new ArrayList<>(); // 条目中需要的类型对象
        List<Type> outList = DBManager.getTypeList(kind); // 获取支出数据
        typeList.addAll(outList); // 添加至条目

        TypeAdapter adapter = new TypeAdapter(getContext(), typeList); // 条目需要的适配器
        typeGv.setAdapter(adapter); // 关联适配器
        adapter.notifyDataSetChanged(); // 提示绘制发生变化
        typeIv.setImageResource(R.mipmap.ic_qita_fs);
        // 监听
        typeGv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                adapter.setSelectPosition(i);
                adapter.notifyDataSetChanged(); // 提示绘制发生变化
                Type type = typeList.get(i); // 获取当前选择对象

                String typeName = type.getName();
                typeTv.setText(typeName); // 显示类型名称
                activity.getAccount().setTypeName(typeName); // 记录类型名称

                int selectImageId = type.getSelectImageId();
                typeIv.setImageResource(selectImageId); // 显示类型图片
                activity.getAccount().setSelectImageId(selectImageId); // 记录类型图片

                activity.getAccount().setKind(kind); // 记录类型
                if (kind == 0) {
                    activity.setOutcomeTypeSelect(true);
                } else {
                    activity.setIncomeTypeSelect(true);
                }
            }
        });
    }

    /**
     * 加载时间
     */
    private void loadDateTime() {
        // 加载
        Date date = new Date();
        dateTv.setText(new SimpleDateFormat("yyyy年MM月dd日").format(date)); // 显示日期
        timeTv.setText(new SimpleDateFormat("HH:mm").format(date)); // 显示时间
        activity.getAccount().setDate(date); // 记录时间
        // 监听
        dateTv.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                DatePickerDialog dialog = new DatePickerDialog(getContext());
                dialog.getDatePicker().setMaxDate(System.currentTimeMillis()); // 设置最大日期
                dialog.show();
                dialog.setOnDateSetListener(new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        dateTv.setText(year + "年" + (month + 1) + "月" + dayOfMonth + "日"); // 显示日期
                        activity.getAccount().setDatePart(year - 1900, month, dayOfMonth);
                    }
                });
            }
        });
        timeTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog dialog = new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        timeTv.setText(hourOfDay + ":" + minute);
                        activity.getAccount().setTimePart(hourOfDay, minute, 0);
                    }
                }, date.getHours(), date.getMinutes(), true);
                dialog.show();
            }
        });

    }

    /**
     * 加载备注
     */
    private void loadComment() {
        // 监听
        commentTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CommentDialog dialog = new CommentDialog(getContext());
                dialog.show();
                dialog.getEditText().setText(commentTv.getText());
                // 监听dialog的确定按钮
                dialog.setOnConfirmListener(new CommentDialog.OnConfirmListener() {
                    @Override
                    public void onConfirm() {
                        String comment = dialog.getEditText().getText().toString();
                        if (!TextUtils.isEmpty(comment)) {
                            commentTv.setText(comment); // 显示备注信息
                            activity.getAccount().setComment(comment); // 记录备注信息
                        }
                        dialog.cancel();
                    }
                });
            }
        });
    }
}