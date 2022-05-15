package com.lee.tally.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.lee.tally.R;
import com.lee.tally.model.Account;

import java.text.SimpleDateFormat;
import java.util.List;

public class AccountAdapter extends BaseAdapter {
    private Context context;
    private List<Account> accountList;
    private LayoutInflater inflater;

    public AccountAdapter(Context context, List<Account> accountList) {
        this.context = context;
        this.accountList = accountList;

        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return accountList.size();
    }

    @Override
    public Object getItem(int position) {
        return accountList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_mainlv, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        Account account = accountList.get(position);
        holder.typeIv.setImageResource(account.getSelectImageId());
        holder.typeTv.setText(account.getTypeName());
        holder.commentTv.setText(account.getComment());
        holder.timeTv.setText(new SimpleDateFormat("yyyy年MM月dd日 HH:mm").format(account.getDate()));
        holder.moneyTv.setText("￥" + account.getMoney());
        return convertView;
    }

    class ViewHolder {
        ImageView typeIv;
        TextView typeTv, commentTv, timeTv, moneyTv;

        public ViewHolder(View view) {
            typeIv = view.findViewById(R.id.item_mainlv_iv);
            typeTv = view.findViewById(R.id.item_mainlv_tv_title);
            commentTv = view.findViewById(R.id.item_mainlv_tv_comment);
            timeTv = view.findViewById(R.id.item_mainlv_tv_time);
            moneyTv = view.findViewById(R.id.item_mainlv_tv_money);
        }
    }
}
