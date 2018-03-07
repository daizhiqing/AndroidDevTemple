package com.xiaochong.myandarch;

import android.app.Application;

import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;
import com.xiaochong.net.utils.HttpNovateClient;
import com.xiaochong.ui_compont.toasty.MyToast;

/**
 * 作者：daizhiqing on 2018/3/7 19:34
 * 邮箱：daizhiqing8@163.com
 * 描述：
 */

public class MyApp extends Application {

    public static final String BASE_URL = "https://api.walian.cn";
    public static final int REQ_TIMEOUT = 60;



    @Override
    public void onCreate() {
        super.onCreate();
        Logger.addLogAdapter(new AndroidLogAdapter());
        MyToast.init(this , true , false);
        HttpNovateClient.init( BASE_URL , BuildConfig.VERSION_NAME , REQ_TIMEOUT);
    }
}
