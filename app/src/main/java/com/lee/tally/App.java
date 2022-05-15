package com.lee.tally;

import android.app.Application;

import com.lee.tally.manager.DBManager;

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        DBManager.initDB(getApplicationContext()); // 初始化数据库
    }
}
