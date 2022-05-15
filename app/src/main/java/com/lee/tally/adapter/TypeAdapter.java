package com.lee.tally.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.lee.tally.R;
import com.lee.tally.model.Type;

import java.util.List;

public class TypeAdapter extends BaseAdapter {
    private Context context;
    private List<Type> typeList;
    private int selectPosition;

    public TypeAdapter(Context context, List<Type> typeList) {
        this.context = context;
        this.typeList = typeList;
    }

    public int getSelectPosition() {
        return selectPosition;
    }

    public void setSelectPosition(int selectPosition) {
        this.selectPosition = selectPosition;
    }

    @Override
    public int getCount() {
        return typeList.size();
    }

    @Override
    public Object getItem(int i) {
        return typeList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        /* 此适配器不考虑复用问题 因为所有item都显示在界面上 不会因为滑动就消失 故没有剩余的convertView */
        view = LayoutInflater.from(context).inflate(R.layout.item_recordfrag_gv, viewGroup, false); // Inflate a new view hierarchy from the specified XML node.
        // 布局控件
        ImageView iv = view.findViewById(R.id.item_recordfrag_iv); // 图标
        TextView tv = view.findViewById(R.id.item_recordfrag_tv); // 文字
        // 获取数据
        Type type = typeList.get(i); // 获取指定位置数据源
        // 设置文字以及图标
        tv.setText(type.getName()); // 设置对应文字
        if (selectPosition != i) { // 若未被选中使用灰白图片
            iv.setImageResource(type.getImageId());
        } else { // 若被选中使用彩色图片
            iv.setImageResource(type.getSelectImageId());
        } // 设置对应图标
        return view;
    }
}
