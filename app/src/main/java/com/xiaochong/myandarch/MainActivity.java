package com.xiaochong.myandarch;

import android.databinding.DataBindingUtil;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ScrollView;

import com.orhanobut.logger.Logger;
import com.xiaochong.myandarch.base.BaseActivity;
import com.xiaochong.myandarch.databinding.ActivityMainBinding;
import com.xiaochong.net.entity.Req;
import com.xiaochong.net.netutils.BaseObserver;
import com.xiaochong.net.netutils.HttpMethods;
import com.xiaochong.ui_compont.toasty.MyToast;

import org.bitcoinj.core.Address;
import org.bitcoinj.core.Coin;
import org.bitcoinj.core.ECKey;
import org.bitcoinj.core.InsufficientMoneyException;
import org.bitcoinj.core.NetworkParameters;
import org.bitcoinj.core.Peer;
import org.bitcoinj.core.Transaction;
import org.bitcoinj.core.listeners.DownloadProgressTracker;
import org.bitcoinj.kits.WalletAppKit;
import org.bitcoinj.params.MainNetParams;
import org.bitcoinj.params.TestNet3Params;
import org.bitcoinj.utils.BriefLogFormatter;
import org.bitcoinj.utils.Threading;
import org.bitcoinj.wallet.SendRequest;
import org.bitcoinj.wallet.Wallet;

import java.io.File;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executor;

import io.reactivex.Observable;


public class MainActivity extends BaseActivity {

    ActivityMainBinding activityMainBinding;

    private File walletDir; //Context.getCacheDir();

    public static final String WALLET_NAME = "DZQ_BTC_WALLET";

    private NetworkParameters parameters;
    private WalletAppKit walletAppKit;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initData() {
        initToolbar(true, true, true).setMyTitle("Bitcoin钱包");


        //设置默认滚动到底部
        activityMainBinding.scrollView.post(new Runnable() {

            @Override
            public void run() {
                // TODO Auto-generated method stub
                activityMainBinding.scrollView.fullScroll(ScrollView.FOCUS_DOWN);
            }
        });
        Handler handler = new Handler();

        Executor runInUIThread = new Executor() {
            @Override public void execute(Runnable runnable) {
                handler.post(runnable);
            }
        };

        Threading.USER_THREAD = runInUIThread;


        BriefLogFormatter.init();

        walletDir = getCacheDir();
        parameters = MainNetParams.get();
        activityMainBinding.tvNetwork.setText("BTC网络");


        initWallet();

    }

    private void initWallet() {
        if(walletAppKit != null && walletAppKit.isRunning()){
            walletAppKit.stopAsync();
            walletAppKit = null;
        }
        walletAppKit = new WalletAppKit(parameters, walletDir, WALLET_NAME) {
            @Override
            protected void onSetupCompleted() {
                if (wallet().getImportedKeys().size() < 1) wallet().importKey(new ECKey());
                wallet().allowSpendingUnconfirmedTransactions();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        activityMainBinding.tvWalletDir.setText(vWalletFile.getAbsolutePath());
                        activityMainBinding.tvShowData.setText("本地文件："+vWalletFile.getAbsolutePath());
                    }
                });
                setupWalletListeners(wallet());

                Logger.i("myLogs  My address = " + wallet().freshReceiveAddress());
            }
        };

        walletAppKit.setDownloadListener(new DownloadProgressTracker() {

            @Override
            public void onChainDownloadStarted(Peer peer, int blocksLeft) {
                super.onChainDownloadStarted(peer, blocksLeft);
                String msg = "\r\n"+new Date()+"开始同步区块： " ;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        activityMainBinding.tvShowData.setText( activityMainBinding.tvShowData.getText()+msg);
                    }
                });
            }

            @Override
            protected void progress(double pct, int blocksSoFar, Date date) {
                super.progress(pct, blocksSoFar, date);
                int percentage = (int) pct;
                String msg = "\r\n"+new Date()+"同步区块： " + percentage+"%";
                Logger.i(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>"+msg);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        activityMainBinding.tvShowData.setText( activityMainBinding.tvShowData.getText()+msg);
                    }
                });
            }

            @Override
            protected void doneDownload() {
                super.doneDownload();

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        String msg = "\r\n"+new Date()+"同步区块完成： " ;
                        activityMainBinding.tvShowData.setText( activityMainBinding.tvShowData.getText()+msg);

                        refresh();
                    }
                });

            }
        });
        walletAppKit.setBlockingStartup(false);
        walletAppKit.startAsync();

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
                    initWallet();
                }else if(parameters instanceof  TestNet3Params){//BTC测试网络
                    parameters = MainNetParams.get();
                    activityMainBinding.tvNetwork.setText("BTC主网");
                    initWallet();
                }
                break;
            case R.id.btn2:
//                HttpMethods.getInstance().setToken("dasdsadsadsadsads");
//                Map<String , Object> params = new HashMap<>();
//                params.put("channelId" ,7);
//                params.put("pageNum" ,1);
//                params.put("pageSize" ,10);
//
//                Observable observable =  HttpMethods.getInstance().getHttpApi().getDoubanDatanew(params);
//                HttpMethods.getInstance().toSubscribe(observable, new BaseObserver<Req>(mContext , true) {
//                    @Override
//                    public void onSuccess(Req dataEntity) {
//
//                    }
//                });
                send();
                break;

        }
    }

    public void refresh() {
        String myAddress = walletAppKit.wallet().freshReceiveAddress().toBase58();

        activityMainBinding.tvBalance.setText(walletAppKit.wallet().getBalance().toFriendlyString());
        activityMainBinding.tvWalletAddress.setText(myAddress);

    }

    /**
     * 初始化钱包监听
     * @param wallet
     */
    private void setupWalletListeners(Wallet wallet) {
        wallet.addCoinsReceivedEventListener((wallet1, tx, prevBalance, newBalance) -> {

            Coin value = tx.getValueSentToMe(wallet1);

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    String msg = new Date()+"收到了tx " + value.toFriendlyString() + ": " + tx+"\r\n" +
                            "交易将在确认后转发"+"\r\n";

                    activityMainBinding.tvBalance.setText(wallet.getBalance().toFriendlyString());

                    if(tx.getPurpose() == Transaction.Purpose.UNKNOWN){
                        msg +=  new Date()+"接收 " + newBalance.minus(prevBalance).toFriendlyString()+"\r\n";
                    }
                    activityMainBinding.tvShowData.setText( activityMainBinding.tvShowData.getText()+msg);
                }
            });

        });

        wallet.addCoinsSentEventListener((wallet12, tx, prevBalance, newBalance) -> {

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    activityMainBinding.tvBalance.setText(wallet.getBalance().toFriendlyString());
                    activityMainBinding.etAmount.setText("");
//            view.displayRecipientAddress(null);
//            view.showToastMessage("Sent " + prevBalance.minus(newBalance).minus(tx.getFee()).toFriendlyString());
                    String msg = "\r\n"+new Date()+"发送 " + prevBalance.minus(newBalance).minus(tx.getFee()).toFriendlyString();

                    activityMainBinding.tvShowData.setText( activityMainBinding.tvShowData.getText()+msg);
                }
            });

        });
    }

    public void send() {
        String recipientAddress =  activityMainBinding.etRecipientAddress.getText().toString();
        String amount = activityMainBinding.etAmount.getText().toString();
        if(TextUtils.isEmpty(recipientAddress) ) {
            MyToast.warn("输入接收地址");
            return;
        }
        if(TextUtils.isEmpty(amount) | Double.parseDouble(amount) <= 0) {
            MyToast.warn("金额不正确");
            return;
        }
        if(walletAppKit.wallet().getBalance().isLessThan(Coin.parseCoin(amount))) {
            MyToast.warn("余额不足");
            return;
        }
        SendRequest request = SendRequest.to(Address.fromBase58(parameters, recipientAddress), Coin.parseCoin(amount));
        try {
            walletAppKit.wallet().completeTx(request);
            walletAppKit.wallet().commitTx(request.tx);
            walletAppKit.peerGroup().broadcastTransaction(request.tx).broadcast();
        } catch (InsufficientMoneyException e) {
            e.printStackTrace();
            MyToast.error(e.getMessage());
        }

    }
}
