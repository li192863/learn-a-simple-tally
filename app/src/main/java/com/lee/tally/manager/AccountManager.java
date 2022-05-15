package com.lee.tally.manager;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.lee.tally.model.Account;
import com.lee.tally.util.DateTimeUtil;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class AccountManager {
    private static SQLiteDatabase db;

    static {
        db = DBManager.getDatabase();
    }

    /**
     * 向账目表中插入数据
     * @param account
     */
    public static void insertAccount(Account account) {
        ContentValues values = new ContentValues();
        values.put("typeName", account.getTypeName());
        values.put("selectImageId", account.getSelectImageId());
        values.put("comment", account.getComment());
        values.put("money", account.getMoney());
        values.put("date", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(account.getDate()));
        values.put("kind", account.getKind());
        db.insert("tb_account", null, values);
        Log.i("lee", "insertAccount ok!" + account);
    }

    /**
     * 获取账目列表
     * @return
     */
    public static List<Account> getAccountList() {
        List<Account> accountList = new ArrayList<>();
        String sql = "SELECT * FROM tb_account ORDER BY date DESC";
        Cursor cursor = db.rawQuery(sql, new String[] {});
        while (cursor.moveToNext()) {
            Account account = new Account();
            account.setId(cursor.getInt(cursor.getColumnIndexOrThrow("id")));
            account.setTypeName(cursor.getString(cursor.getColumnIndexOrThrow("typeName")));
            account.setSelectImageId(cursor.getInt(cursor.getColumnIndexOrThrow("selectImageId")));
            account.setComment(cursor.getString(cursor.getColumnIndexOrThrow("comment")));
            account.setMoney(cursor.getFloat(cursor.getColumnIndexOrThrow("money")));
            try {
                account.setDate(new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(cursor.getString(cursor.getColumnIndexOrThrow("date"))));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            account.setKind(cursor.getInt(cursor.getColumnIndexOrThrow("kind")));
            accountList.add(account);
        }
        return accountList;
    }

    /**
     * 获取某天收入支出金额
     * @param year
     * @param month
     * @param day
     * @param kind
     * @return
     */
    public static float getMoneyInOneDay(int year, int month, int day, int kind) {
        Date date = new Date(year - 1900, month, day);
        String dateString = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
        float total = 0.0f;
        String sql = "SELECT sum(money) FROM tb_account WHERE date >= ? AND kind = ?";
        Cursor cursor = db.rawQuery(sql, new String[]{dateString, kind + ""});
        if (cursor.moveToFirst()) {
            total += cursor.getFloat(cursor.getColumnIndexOrThrow("sum(money)"));
        }
        return total;
    }

    /**
     * 获取某月收入支出金额
     * @param year
     * @param month
     * @param kind
     * @return
     */
    public static float getMoneyInOneMonth(int year, int month, int kind) {
        Date startDate = DateTimeUtil.getThisMonthFirstDate(new Date(year - 1900, month, 1));
        Date endDate = DateTimeUtil.getNextMonthFirstDate(new Date(year - 1900, month, 1));
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String start = sdf.format(startDate);
        String end = sdf.format(endDate);

        float total = 0.0f;
        String sql = "SELECT sum(money) FROM tb_account WHERE date >= ? AND date < ? AND kind = ?";
        Cursor cursor = db.rawQuery(sql, new String[]{start, end, kind + ""});
        if (cursor.moveToFirst()) {
            total += cursor.getFloat(cursor.getColumnIndexOrThrow("sum(money)"));
        }
        return total;
    }

    /**
     * 获取某月收入支出条目数
     * @param year
     * @param month
     * @param kind
     * @return
     */
    public static int getCountInOneMonth(int year, int month, int kind) {
        Date startDate = DateTimeUtil.getThisMonthFirstDate(new Date(year - 1900, month, 1));
        Date endDate = DateTimeUtil.getNextMonthFirstDate(new Date(year - 1900, month, 1));
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String start = sdf.format(startDate);
        String end = sdf.format(endDate);

        int total = 0;
        String sql = "SELECT count(money) FROM tb_account WHERE date >= ? AND date < ? AND kind = ?";
        Cursor cursor = db.rawQuery(sql, new String[]{start, end, kind + ""});
        if (cursor.moveToFirst()) {
            total += cursor.getFloat(cursor.getColumnIndexOrThrow("count(money)"));
        }
        return total;
    }



    /**
     * 指定id删除账目
     * @param id
     * @return
     */
    public static int deleteAccount(int id) {
        int i = db.delete("tb_account", "id = ?", new String[]{id + ""});
        return i;
    }

    /**
     * 根据评论搜索账目
     * @param text
     * @return
     */
    public static List<Account> getAccountListByComment(String text) {
        List<Account> accountList = new ArrayList<>();
        String sql = "SELECT * FROM tb_account WHERE comment LIKE ?";
        Cursor cursor = db.rawQuery(sql, new String[]{"%" + text + "%"});
        while (cursor.moveToNext()) {
            Account account = new Account();
            account.setId(cursor.getInt(cursor.getColumnIndexOrThrow("id")));
            account.setTypeName(cursor.getString(cursor.getColumnIndexOrThrow("typeName")));
            account.setSelectImageId(cursor.getInt(cursor.getColumnIndexOrThrow("selectImageId")));
            account.setComment(cursor.getString(cursor.getColumnIndexOrThrow("comment")));
            account.setMoney(cursor.getFloat(cursor.getColumnIndexOrThrow("money")));
            try {
                account.setDate(new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(cursor.getString(cursor.getColumnIndexOrThrow("date"))));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            account.setKind(cursor.getInt(cursor.getColumnIndexOrThrow("kind")));
            accountList.add(account);
        }
        return accountList;
    }

    /**
     * 获取某月收入支出账目
     * @param year
     * @param month
     * @return
     */
    public static List<Account> getAccountListInOneMonth(int year, int month) {
        Date startDate = DateTimeUtil.getThisMonthFirstDate(new Date(year - 1900, month, 1));
        Date endDate = DateTimeUtil.getNextMonthFirstDate(new Date(year - 1900, month, 1));
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String start = sdf.format(startDate);
        String end = sdf.format(endDate);

        List<Account> accountList = new ArrayList<>();
        String sql = "SELECT * FROM tb_account WHERE date >= ? AND date < ? ORDER BY date DESC";
        Cursor cursor = db.rawQuery(sql, new String[]{start, end});
        while (cursor.moveToNext()) {
            Account account = new Account();
            account.setId(cursor.getInt(cursor.getColumnIndexOrThrow("id")));
            account.setTypeName(cursor.getString(cursor.getColumnIndexOrThrow("typeName")));
            account.setSelectImageId(cursor.getInt(cursor.getColumnIndexOrThrow("selectImageId")));
            account.setComment(cursor.getString(cursor.getColumnIndexOrThrow("comment")));
            account.setMoney(cursor.getFloat(cursor.getColumnIndexOrThrow("money")));
            try {
                account.setDate(new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(cursor.getString(cursor.getColumnIndexOrThrow("date"))));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            account.setKind(cursor.getInt(cursor.getColumnIndexOrThrow("kind")));
            accountList.add(account);
        }
        return accountList;
    }

    /**
     * 查询所有账目的年份信息
     * @return
     */
    public static List<Integer> getYearList() {
        List<Integer> yearList = new ArrayList<>();
        String sql = "SELECT DISTINCT substr(date, 1, 4) FROM tb_account ORDER BY date ASC";
        Cursor cursor = db.rawQuery(sql, new String[]{});
        while (cursor.moveToNext()) {
            int year = cursor.getInt(cursor.getColumnIndexOrThrow("substr(date, 1, 4)"));
            yearList.add(year);
        }
        return yearList;
    }

    /**
     * 删除表中所有数据
     */
    public static void deleteAllAccounts() {
        String sql = "DELETE FROM tb_account";
        db.execSQL(sql);
    }
}
