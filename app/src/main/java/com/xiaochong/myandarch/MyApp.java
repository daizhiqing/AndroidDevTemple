package com.xiaochong.myandarch;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;
import com.xiaochong.net.netutils.HttpMethods;
import com.xiaochong.ui_compont.toasty.MyToast;

/**
 * 作者：daizhiqing on 2018/3/7 19:34
 * 邮箱：daizhiqing8@163.com
 * 描述：
 */

public class MyApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Logger.addLogAdapter(new AndroidLogAdapter());
        MyToast.init(this , true , false);
        HttpMethods.init(this, getPackageName());
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }
}
