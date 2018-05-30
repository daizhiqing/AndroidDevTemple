package com.xiaochong.myandarch;

import android.databinding.DataBindingUtil;
import android.view.View;

import com.xiaochong.myandarch.base.BaseActivity;
import com.xiaochong.myandarch.databinding.ActivityMainBinding;
import com.xiaochong.net.entity.Req;
import com.xiaochong.net.netutils.BaseObserver;
import com.xiaochong.net.netutils.HttpMethods;
import com.xiaochong.ui_compont.toasty.MyToast;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.Observable;


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
                HttpMethods.getInstance().setToken("dasdsadsadsadsads");
                Map<String , Object> params = new HashMap<>();
                params.put("channelId" ,7);
                params.put("pageNum" ,1);
                params.put("pageSize" ,10);

                Observable observable =  HttpMethods.getInstance().getHttpApi().getDoubanDatanew(params);
                HttpMethods.getInstance().toSubscribe(observable, new BaseObserver<Req>(mContext , true) {
                    @Override
                    public void onSuccess(Req dataEntity) {

                    }
                });

                break;

        }
    }
}
