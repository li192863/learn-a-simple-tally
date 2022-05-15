package com.lee.tally.manager;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.lee.tally.model.Type;

import java.util.ArrayList;
import java.util.List;

public class DBManager {
    private static SQLiteDatabase db;

    public static void initDB(Context context) {
        DBOpenHelper helper = new DBOpenHelper(context); // 得到打开帮助类对象
        db = helper.getWritableDatabase(); // 得到数据库对象
    }

    /**
     * 获取数据库
     * @return
     */
    public static SQLiteDatabase getDatabase() {
        return db;
    }

    /**
     * 读取条目数据库当中数据写入内存集合
     * @param kind
     * @return
     */
    public static List<Type> getTypeList(int kind) {
        List<Type> list = new ArrayList<>();
        // 读取tb_type表
        String sql = "SELECT * FROM tb_type WHERE kind = ?";
        Cursor cursor = db.rawQuery(sql, new String[]{kind + ""});
        while (cursor.moveToNext()) {
            Type type = new Type();
            type.setId(cursor.getInt(cursor.getColumnIndexOrThrow("id")));
            type.setName(cursor.getString(cursor.getColumnIndexOrThrow("name")));
            type.setImageId(cursor.getInt(cursor.getColumnIndexOrThrow("imageId")));
            type.setSelectImageId(cursor.getInt(cursor.getColumnIndexOrThrow("selectImageId")));
            type.setKind(cursor.getInt(cursor.getColumnIndexOrThrow("kind")));

            list.add(type);
        }
        return list;
    }
}
