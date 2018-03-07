package com.xiaochong.myandarch;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.orhanobut.logger.Logger;
import com.tamic.novate.Throwable;
import com.tamic.novate.callback.RxResultCallback;
import com.xiaochong.myandarch.base.BaseActivity;
import com.xiaochong.myandarch.databinding.ActivityMainBinding;
import com.xiaochong.net.utils.HttpNovateClient;
import com.xiaochong.ui_compont.prompt.PromptDialog;
import com.xiaochong.ui_compont.toasty.MyToast;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends BaseActivity {

    ActivityMainBinding activityMainBinding;


    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initData() {
        initToolbar(true, true, true).setMyTitle("主页").setMoreTitle("更多");

    }

    @Override
    protected void initBinding(View view) {
        activityMainBinding = DataBindingUtil.bind(view);
    }

    public void onViewClick(View view) {

        switch (view.getId()){
            case R.id.btn1:
                MyToast.success(activityMainBinding.btn2+"ssssssssssssssssss"+activityMainBinding.btn1);
                break;
            case R.id.btn2:
                Map<String , Object> params = new HashMap<>();
                params.put("channelId" ,7);
                params.put("pageNum" ,1);
                params.put("pageSize" ,10);
                HttpNovateClient.getInstance(mContext).rxPost("/coins/getCoinIndex.open",params ,
                        new RxResultCallback<Req>() {
                            @Override
                            public void onNext(Object tag, int code, String message, Req response) {
                                Logger.i("tag=%s",tag);
                                Logger.i("code=%s",code);
                                Logger.i("message=%s",message);
                                Logger.i("response=%s",response);
                            }

                            @Override
                            public void onError(Object tag, Throwable e) {
                            }
                            @Override
                            public void onCancel(Object tag, Throwable e) {
                            }
                            @Override
                            public void onStart(Object tag) {
                                super.onStart(tag);
                                showLoading();
                            }
                            @Override
                            public void onCompleted(Object tag) {
                                super.onCompleted(tag);
                                hideLoading();
                            }

                        });                break;
        }
    }
}
