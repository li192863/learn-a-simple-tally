package com.lee.tally.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.lee.tally.R;
import com.lee.tally.model.ChartItem;
import com.lee.tally.util.FloatFormatUtil;

import java.util.List;

public class ChartItemAdapter extends BaseAdapter {
    private Context context;
    private List<ChartItem> chartItemList;
    private LayoutInflater inflater;

    public ChartItemAdapter(Context context, List<ChartItem> chartItemList) {
        this.context = context;
        this.chartItemList = chartItemList;

        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return chartItemList.size();
    }

    @Override
    public Object getItem(int position) {
        return chartItemList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // 尝试复用
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_infofrag_lv, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        // 获取显示内容
        ChartItem chartItem = chartItemList.get(position);
        holder.typeIv.setImageResource(chartItem.getSelectImageId());
        holder.typeTv.setText(chartItem.getTypeName());
        holder.percentageTv.setText(FloatFormatUtil.getPercentage(chartItem.getPercentage()));
        holder.moneyTv.setText("￥" + chartItem.getMoney());
        return convertView;
    }

    class ViewHolder {
        ImageView typeIv;
        TextView typeTv, percentageTv, moneyTv;

        public ViewHolder(View view) {
            typeIv = view.findViewById(R.id.item_infofrag_iv);
            typeTv = view.findViewById(R.id.item_infofrag_tv_type);
            percentageTv = view.findViewById(R.id.item_infofrag_tv_percentage);
            moneyTv = view.findViewById(R.id.item_infofrag_tv_money);
        }
    }
}
