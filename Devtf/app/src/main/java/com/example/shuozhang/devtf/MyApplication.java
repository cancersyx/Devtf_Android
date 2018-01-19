package com.example.shuozhang.devtf;

import android.app.Application;

/**
 * Created by EWorld
 * 2018/1/9 00:25
 *
 * @Description
 * @E-mail 852333743@qq.com
 */

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        //必须初始化数据库模块先
        DatabaseHelper.init(this);
    }
}
