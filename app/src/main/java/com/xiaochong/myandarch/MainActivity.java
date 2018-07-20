package com.xiaochong.myandarch;

import android.databinding.DataBindingUtil;
import android.os.Environment;
import android.view.View;

import com.xiaochong.myandarch.base.BaseActivity;
import com.xiaochong.myandarch.databinding.ActivityMainBinding;
import com.xiaochong.net.entity.Req;
import com.xiaochong.net.netutils.BaseObserver;
import com.xiaochong.net.netutils.HttpMethods;

import org.bitcoinj.core.NetworkParameters;
import org.bitcoinj.kits.WalletAppKit;
import org.bitcoinj.params.MainNetParams;
import org.bitcoinj.params.TestNet3Params;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import io.reactivex.Observable;


public class MainActivity extends BaseActivity {

    ActivityMainBinding activityMainBinding;

    private File walletDir; //Context.getCacheDir();

    private NetworkParameters parameters;
    private WalletAppKit walletAppKit;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initData() {
        initToolbar(true, true, true).setMyTitle("Bitcoin钱包");
        walletDir = Environment.getExternalStorageDirectory();
        parameters = TestNet3Params.get();
        activityMainBinding.tvNetwork.setText("测试网络");
    }

    @Override
    protected void initBinding(View view) {
        activityMainBinding = DataBindingUtil.bind(view);
    }

    public void onViewClick(View view) {

        switch (view.getId()){
            case R.id.btn_switch:
                if(parameters instanceof  MainNetParams){ //BTC主网
                    parameters = TestNet3Params.get();
                    activityMainBinding.tvNetwork.setText("BTC测试网络");
                }else if(parameters instanceof  TestNet3Params){//BTC测试网络
                    parameters = MainNetParams.get();
                    activityMainBinding.tvNetwork.setText("BTC主网");
                }
            case R.id.btn1:


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
