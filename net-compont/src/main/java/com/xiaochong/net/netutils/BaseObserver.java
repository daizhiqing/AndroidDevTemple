package com.xiaochong.net.netutils;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.CallSuper;
import android.util.Log;
import android.widget.Toast;

import com.orhanobut.logger.Logger;
import com.xiaochong.net.HttpResponse;
import com.xiaochong.ui_compont.prompt.PromptDialog;

import java.io.IOException;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.net.UnknownServiceException;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import retrofit2.HttpException;

/**
 * Base Observer 的封装处理,对Rxjava 不熟悉
 *
 * Created by daizhiqing on 2017/4/14.
 */
public abstract class BaseObserver<T > implements Observer<HttpResponse<T>> {
    private final int RESPONSE_CODE_OK = 200;      //自定义的业务逻辑，成功返回积极数据
    private final int RESPONSE_CODE_FAILED = 500; //返回数据失败

    //是否需要显示Http 请求的进度，默认的是需要，但是Service 和 预取数据不需要
    private boolean showProgress = false;
    private Context mContext;

    private Disposable disposable;   //不处理吧

    PromptDialog promptDialog;

    /**
     * 根据具体的Api 业务逻辑去重写 onSuccess 方法！
     *
     * @param t
     */
    public abstract void onSuccess(T t);

    /**
     * @param mContext
     * @param showProgress 默认需要显示进程，不要的话请传 false
     */
    public BaseObserver(Activity mContext, boolean showProgress) {
        this.showProgress = showProgress;
        this.mContext = mContext;
        if (showProgress) {
            promptDialog = new PromptDialog(mContext);
            promptDialog.showLoading("");
        }
    }

    @Override
    public final void onSubscribe(Disposable d) {
        this.disposable = d;
    }

    @Override
    public final void onNext(HttpResponse<T> response) {
        Logger.e(">>>>>>>>>>>>>>>>>>>>>>>>"+response.toString());
        if ("000000".equals(response.getCode() )) {
            onSuccess(response.getData());
        } else {
            onFailure(response.getCode(), response.getMsg());
        }

    }

    @Override
    public final void onError(Throwable t) {
        if(promptDialog!= null)promptDialog.dismiss();
        int code = 0;
        String errorMessage = "未知错误";
        if (t instanceof HttpException) {
            HttpException httpException = (HttpException) t;
            Logger.e("meg==="+httpException.response().body());
            code = httpException.code();
            errorMessage = httpException.getMessage();
        } else if (t instanceof SocketTimeoutException) {  //VPN open
            code = RESPONSE_CODE_FAILED;
            errorMessage = "服务器响应超时";
        } else if (t instanceof ConnectException) {
            code = RESPONSE_CODE_FAILED;
            errorMessage = "网络连接异常，请检查网络";
        } else if (t instanceof RuntimeException) {
            code = RESPONSE_CODE_FAILED;
            errorMessage = "运行时错误";
        } else if (t instanceof UnknownHostException) {
            code = RESPONSE_CODE_FAILED;
            errorMessage = "无法解析主机，请检查网络连接";
        } else if (t instanceof UnknownServiceException) {
            code = RESPONSE_CODE_FAILED;
            errorMessage = "未知的服务器错误";
        } else if (t instanceof IOException) {  //飞行模式等
            code = RESPONSE_CODE_FAILED;
            errorMessage = "没有网络，请检查网络连接";
        }

        /**
         * 严重的错误弹出dialog，一般的错误就只要Toast
         */
        disposeEorCode(errorMessage, code);
    }


    /**
     * 简单的把Dialog 处理掉
     */
    @Override
    public void onComplete() {
        if(promptDialog!= null)promptDialog.dismiss();
    }

    /**
     * Default error dispose!
     * 一般的就是 AlertDialog 或 SnackBar
     *
     * @param code
     * @param message
     */
    @CallSuper  //if overwrite,you should let it run.
    public void onFailure(String  code, String message) {

    }


    /**
     * 对通用问题的统一拦截处理
     *
     * @param code
     */
    private final void disposeEorCode(String message, int code) {
        switch (code) {
            case 101:
            case 112:
            case 123:
            case 401:
                //退回到登录页面

                break;
        }
        if (mContext != null && !((Activity) mContext).isFinishing()) {
            Toast.makeText(mContext, message + " - " + code, Toast.LENGTH_SHORT).show();
        }
    }

}
