package com.lee.tally.manager;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.lee.tally.model.BarDatum;
import com.lee.tally.model.ChartItem;
import com.lee.tally.util.DateTimeUtil;
import com.lee.tally.util.FloatFormatUtil;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ChartManager {
    private static SQLiteDatabase db;

    static {
        db = DBManager.getDatabase();
    }

    /**
     * 获取某月支出/收入的数据
     * @param year
     * @param month
     * @param kind
     * @return
     */
    public static List<ChartItem> getChartItemListInOneMonth(int year, int month, int kind) {
        Date startDate = DateTimeUtil.getThisMonthFirstDate(new Date(year - 1900, month, 1));
        Date endDate = DateTimeUtil.getNextMonthFirstDate(new Date(year - 1900, month, 1));
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String start = sdf.format(startDate);
        String end = sdf.format(endDate);

        List<ChartItem> chartItemList = new ArrayList<>();
        float totalMoney = AccountManager.getMoneyInOneMonth(year, month, kind);
        String sql = "SELECT typeName, selectImageId, sum(money) AS typeMoney FROM tb_account WHERE date >= ? AND date < ? AND kind = ? GROUP BY typeName ORDER BY typeMoney DESC";
        Cursor cursor = db.rawQuery(sql, new String[]{start, end, kind + ""});
        while (cursor.moveToNext()) {
            ChartItem chartItem = new ChartItem();
            chartItem.setTypeName(cursor.getString(cursor.getColumnIndexOrThrow("typeName")));
            chartItem.setSelectImageId(cursor.getInt(cursor.getColumnIndexOrThrow("selectImageId")));
            chartItem.setMoney(cursor.getFloat(cursor.getColumnIndexOrThrow("typeMoney")));
            chartItem.setPercentage(FloatFormatUtil.reformatFloat(chartItem.getMoney() / totalMoney));
            chartItemList.add(chartItem);
        }
        return chartItemList;
    }

    /**
     * 获取某月支出最大金额
     * @param year
     * @param month
     * @param kind
     * @return
     */
    public static float getMaxMoneyInOneMonth(int year, int month, int kind) {
        Date startDate = DateTimeUtil.getThisMonthFirstDate(new Date(year - 1900, month, 1));
        Date endDate = DateTimeUtil.getNextMonthFirstDate(new Date(year - 1900, month, 1));
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String start = sdf.format(startDate);
        String end = sdf.format(endDate);
        String sql = "SELECT SUM(money) AS total, substr(date, 9, 2) AS day FROM tb_account WHERE date >= ? AND date < ? AND kind = ? GROUP BY day ORDER BY total DESC";
        Cursor cursor = db.rawQuery(sql, new String[]{start, end, kind + ""});
        if (cursor.moveToNext()) {
            return cursor.getInt(cursor.getColumnIndexOrThrow("total"));
        }
        return 0F;
    }

    /**
     * 根据指定月份获取每日收入
     * @param year
     * @param month
     * @param kind
     * @return
     */
    public static List<BarDatum> getBarDataInOneMonth(int year, int month, int kind) {
        Date startDate = DateTimeUtil.getThisMonthFirstDate(new Date(year - 1900, month, 1));
        Date endDate = DateTimeUtil.getNextMonthFirstDate(new Date(year - 1900, month, 1));
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String start = sdf.format(startDate);
        String end = sdf.format(endDate);

        List<BarDatum> barData = new ArrayList<>();
        String sql = "SELECT SUM(money) AS total, substr(date, 9, 2) AS day FROM tb_account WHERE date >= ? AND date < ? AND kind = ? GROUP BY day";
        Cursor cursor = db.rawQuery(sql, new String[]{start, end, kind + ""});
        while (cursor.moveToNext()) {
            BarDatum barDatum = new BarDatum();
            barDatum.setDay(cursor.getInt(cursor.getColumnIndexOrThrow("day")));
            barDatum.setMoney(cursor.getInt(cursor.getColumnIndexOrThrow("total")));
            barData.add(barDatum);
        }
        return barData;
    }
}
