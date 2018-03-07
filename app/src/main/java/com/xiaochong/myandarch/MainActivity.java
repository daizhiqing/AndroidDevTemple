package com.xiaochong.myandarch;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.xiaochong.myandarch.base.BaseActivity;
import com.xiaochong.myandarch.databinding.ActivityMainBinding;
import com.xiaochong.ui_compont.prompt.PromptDialog;
import com.xiaochong.ui_compont.toasty.MyToast;

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
                new PromptDialog(this).showLoading("加载中``");
                break;
        }
    }
}
