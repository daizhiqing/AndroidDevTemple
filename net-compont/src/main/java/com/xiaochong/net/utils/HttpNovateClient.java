package com.xiaochong.net.utils;

import android.content.Context;
import android.text.TextUtils;

import com.orhanobut.logger.Logger;
import com.tamic.novate.Novate;
import com.tamic.novate.callback.RxResultCallback;
import com.tamic.novate.callback.RxStringCallback;

import java.util.HashMap;
import java.util.Map;


/**
 * 作者：daizhiqing on 2018/3/7 17:48
 * 邮箱：daizhiqing8@163.com
 * 描述：
 */

public class HttpNovateClient {

    private static String baseUrl;
    private static String version = "0.0.1"; //默认版本号
    private static int timeout = 30; //默认超时时间

    private HttpNovateClient(){}

    /**
     * 初始化网络框架
     * @param baseUrl
     * @param version
     * @param timeout
     */
    public static void init(String baseUrl ,String version , int timeout){

        HttpNovateClient.baseUrl = baseUrl;
        HttpNovateClient.version = version;
        HttpNovateClient.timeout = timeout;

        Logger.i("初始化网络请求路径：%s",baseUrl);
    }

    static Novate novate;

    public static Novate getInstance(Context context){
        if(TextUtils.isEmpty(baseUrl)){
            Logger.e("baseUrl 为空~~");
            return null;
        }
        if(novate == null){
            novate = new Novate.Builder(context)
                    .connectTimeout(timeout)
                    .readTimeout(timeout)
                    .baseUrl(baseUrl)
                    .addHeader(commonHeaders())
                    .addLog(true)
                    .build();
        }
        return novate;
    }


    public static Novate getNewNovate(Context context , String url){
        if(TextUtils.isEmpty(url)){
            Logger.e("getNovate url 为空~~");
            return null;
        }
        Novate  n = new Novate.Builder(context)
                    .connectTimeout(timeout)
                    .readTimeout(timeout)
                    .baseUrl(url)
                    .addHeader(commonHeaders())
                    .addLog(true)
                    .build();
        return n;
    }

    private static Map<String , String> commonHeaders(){
        Map<String, String> headers = new HashMap<>();
        headers.put("platform", "android");
        headers.put("version", version);
        return headers;
    }

}
