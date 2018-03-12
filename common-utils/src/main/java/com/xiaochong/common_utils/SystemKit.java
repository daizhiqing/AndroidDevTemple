/*
 * Copyright (c) 2016, yindongdong@renwohua.com All Rights Reserved.
 */
package com.xiaochong.common_utils;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.ActivityManager;
import android.app.ActivityManager.MemoryInfo;
import android.app.KeyguardManager;
import android.bluetooth.BluetoothAdapter;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.Signature;
import android.media.AudioManager;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.provider.Settings;
import android.telephony.TelephonyManager;


import com.orhanobut.logger.Logger;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;

/**
 * 系统信息工具包
 *
 * @author 尹东东
 * @version 1.1
 * @date: 2016/11/8 下午3:56
 * @update 戴智青
 * @date now
 */
public final class SystemKit {


    /**
     * 获取手机唯一的ID号码
     * @param context
     * @return
     */
    public static String getOnlyID(Context context){
        String imei = getIMEI(context);
        String aid = getAndroidID(context);
        String macId = getMacAddress(context);

        String idStr = imei+aid+macId;
        idStr = md5Password(idStr);
        return idStr;
    }
    /**
     * 获取手机Android ID
     * @param context
     * @return
     */
    public static String getAndroidID(Context context){
        String m_szAndroidID = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);

        return m_szAndroidID != null ? m_szAndroidID : null;
    }
    /**
     *MD5加密
     * @param password
     * @return
     */
    public static String md5Password(String password) {

        try {
            MessageDigest digest = MessageDigest.getInstance("md5");
            byte[] result = digest.digest(password.getBytes());
            StringBuffer buffer = new StringBuffer();
            for (byte b : result) {
                int number = b & 0xff;//
                String str = Integer.toHexString(number);
                // System.out.println(str);
                if (str.length() == 1) {
                    buffer.append("0");
                }
                buffer.append(str);
            }
            return buffer.toString();
        } catch (NoSuchAlgorithmException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return "";
        }

    }

    /**
     * 获取手机系统SDK版本
     *
     * @return 如API 17 则返回 17
     */
    @TargetApi(Build.VERSION_CODES.DONUT)
    public static int getSDKVersion() {
        return Build.VERSION.SDK_INT;
    }

    /**
     * 获取系统版本
     *
     * @return 形如2.3.3
     */
    public static String getSystemVersion() {
        return Build.VERSION.RELEASE;
    }

    /**
     * 调用系统发送短信
     */
    public static void sendSMS(Context cxt, String smsBody) {
        Uri smsToUri = Uri.parse("smsto:");
        Intent intent = new Intent(Intent.ACTION_SENDTO, smsToUri);
        intent.putExtra("sms_body", smsBody);
        cxt.startActivity(intent);
    }

    /**
     * 拨打电话
     *
     * @param context
     * @param phoneNumber
     */
    public static void callTel(Context context, String phoneNumber) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        if (!phoneNumber.startsWith("tel:")) {
            phoneNumber = "tel:" + phoneNumber;
        }
        Uri data = Uri.parse(phoneNumber);
        intent.setData(data);
        context.startActivity(intent);
    }

    public static void clipboard(Context context, String text) {
        ClipboardManager cm = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        if (cm != null) {
            cm.setPrimaryClip(ClipData.newPlainText(null, text));
        }
    }

    /**
     * 判断手机是否处理睡眠
     */
    public static boolean isSleeping(Context context) {
        KeyguardManager kgMgr = (KeyguardManager) context
                .getSystemService(Context.KEYGUARD_SERVICE);
        boolean isSleeping = kgMgr.inKeyguardRestrictedInputMode();
        return isSleeping;
    }

    /**
     * 安装apk
     *
     * @param context
     * @param file
     */
    public static void installApk(Context context, File file) {
        Intent intent = new Intent();
        intent.setAction("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.setType("application/vnd.android.package-archive");
        intent.setData(Uri.fromFile(file));
        intent.setDataAndType(Uri.fromFile(file),
                "application/vnd.android.package-archive");
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    /**
     * 获取当前应用程序的版本号
     */
    public static String getAppVersionName(Context context) {
        String version = "0";
        try {
            version = context.getPackageManager().getPackageInfo(
                    context.getPackageName(), 0).versionName;
        } catch (NameNotFoundException e) {
            throw new RuntimeException(SystemKit.class.getName()
                    + "the application not found");
        }
        return version;
    }

    /**
     * 获取当前应用程序的版本号
     */
    public static int getAppVersionCode(Context context) {
        int version = 0;
        try {
            version = context.getPackageManager().getPackageInfo(
                    context.getPackageName(), 0).versionCode;
        } catch (NameNotFoundException e) {
            throw new RuntimeException(SystemKit.class.getName()
                    + "the application not found");
        }
        return version;
    }

    /**
     * 回到home，后台运行
     */
    public static void goHome(Context context) {
        Intent mHomeIntent = new Intent(Intent.ACTION_MAIN);
        mHomeIntent.addCategory(Intent.CATEGORY_HOME);
        mHomeIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
        context.startActivity(mHomeIntent);
    }

    /**
     * 获取应用签名
     *
     * @param context
     * @param pkgName
     */
    public static String getSign(Context context, String pkgName) {
        try {
            PackageInfo packageInfo = context.getPackageManager().getPackageInfo(
                    pkgName, PackageManager.GET_SIGNATURES);

            Signature[] signatures = packageInfo.signatures;
            Signature signature = signatures[0];

            return Md5CryptKit.crypt(signature.toCharsString().getBytes("utf-8"));
        } catch (NameNotFoundException e) {
            Logger.e(e.getMessage() , e);
        } catch (UnsupportedEncodingException e) {
            Logger.e(e.getMessage() ,e);
        }
        return null;
    }

    /**
     * 获取设备的可用内存大小
     *
     * @param cxt 应用上下文对象context
     * @return 当前内存大小
     */
    public static int getDeviceUsableMemory(Context cxt) {
        ActivityManager am = (ActivityManager) cxt
                .getSystemService(Context.ACTIVITY_SERVICE);
        MemoryInfo mi = new MemoryInfo();
        am.getMemoryInfo(mi);
        // 返回当前系统的可用内存
        return (int) (mi.availMem / (1024 * 1024));
    }

    /**
     * 获取设备型号
     *
     * @return 设备型号
     */
    public static String getModel() {
        String model = Build.MODEL;
        if (model != null) {
            model = model.trim().replaceAll("\\s*", "");
        } else {
            model = "";
        }
        return model;
    }

    /**
     * 获取制造商
     *
     * @return 制造商
     */
    public static String getVendor() {
        String model = Build.MANUFACTURER;
        if (model != null) {
            model = model.trim().replaceAll("\\s*", "");
        } else {
            model = "";
        }
        return model;
    }

    /**
     * 获取IMEI码
     *
     * @param context 上下文
     * @return IMEI码
     */
    public static String getIMEI(Context context) {
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        return tm != null ? tm.getDeviceId() : null;
    }

    /**
     * 获取手机IMSI号
     *
     * @param context
     * @return
     */
    public static String getIMSI(Context context) {
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        return tm != null ? tm.getSubscriberId() : null;
    }


    /**
     * 获取序列号
     *
     * @return 序列号  如果有则返回，没有返回null
     */
    public static String getSerialNumber(Context context) {
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        return tm != null ? tm.getSimSerialNumber() : null;
    }


    /**
     * 获取ip地址
     *
     * @param IPv4    是否获取ipv4地址
     * @param context 上下文
     * @return ip地址
     */
    public static String getIPAddress(boolean IPv4, Context context) {

        String ipAddress = getIpAddressByWifiInfo(context);
        if (ipAddress != null) {
            return ipAddress;
        }
        ipAddress = getIpAddressByNetworkInterface(IPv4);
        if (ipAddress != null) {
            return ipAddress;
        }
        return "";
    }

    /**
     * 获取蓝牙Mac地址
     *
     * @param context
     * @return Mac地址  如果系统禁止，则返回
     */
    public static String getBlueToothMacAddress(Context context) {
        try {
            BluetoothAdapter m_BluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
            String m_szBTMAC = m_BluetoothAdapter.getAddress();
            return m_szBTMAC;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "02:00:00:00:00:00";
    }


    /**
     * 获取网络MacAddress
     *
     * @param context 上下文
     * @return Mac地址
     */
    public static String getMacAddress(Context context) {
        String macAddress = getMacAddressByWifiInfo(context);
        if (!"02:00:00:00:00:00".equals(macAddress)) {
            return macAddress;
        }
        macAddress = getMacAddressByNetworkInterface();
        if (!"02:00:00:00:00:00".equals(macAddress)) {
            return macAddress;
        }
        return "请打开wifi";
    }


    /**
     * 调节系统音量
     *
     * @param context  上下文
     * @param flag     flag>0 增加音量 flag<0 降低音量
     * @param isShowUI true 显示系统调节音量界面  false不显示
     */
    public static void UpOrDownSystemAudio(Context context, int flag, boolean isShowUI) {
        setAudio(context, flag, AudioManager.STREAM_SYSTEM, isShowUI);
    }

    /**
     * 调节音乐音量
     *
     * @param context  上下文
     * @param flag     flag>0 增加音量 flag<0 降低音量
     * @param isShowUI true 显示音乐调节音量界面  false不显示
     */
    public static void UpOrDownMusicAudio(Context context, int flag, boolean isShowUI) {
        setAudio(context, flag, AudioManager.STREAM_MUSIC, isShowUI);
    }

    /**
     * 调节通话音量
     *
     * @param context  上下文
     * @param flag     flag>0 增加音量 flag<0 降低音量
     * @param isShowUI true 显示通话调节音量界面  false不显示
     */
    public static void UpOrDownCallAudio(Context context, int flag, boolean isShowUI) {
        setAudio(context, flag, AudioManager.STREAM_VOICE_CALL, isShowUI);
    }

    /**
     * 调节铃声音量
     *
     * @param context  上下文
     * @param flag     flag>0 增加音量 flag<0 降低音量
     * @param isShowUI true 显示铃声调节音量界面  false不显示
     */
    public static void UpOrDownRingAudio(Context context, int flag, boolean isShowUI) {
        setAudio(context, flag, AudioManager.STREAM_RING, isShowUI);
    }

    /**
     * 调节闹钟音量
     *
     * @param context  上下文
     * @param flag     flag>0 增加音量 flag<0 降低音量
     * @param isShowUI true 显示闹钟调节音量界面  false不显示
     */
    public static void UpOrDownAlarmMAudio(Context context, int flag, boolean isShowUI) {
        setAudio(context, flag, AudioManager.STREAM_ALARM, isShowUI);
    }

    /**
     * 设置系统音量大小
     *
     * @param context 上下文
     * @param volume  设置的音量大小
     */
    public static void setSystemAudio(Context context, int volume) {
        AudioManager mAudioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        mAudioManager.setStreamVolume(AudioManager.STREAM_SYSTEM, volume, AudioManager.FLAG_SHOW_UI);
    }


    /**
     * 设置音乐音量大小
     *
     * @param context 上下文
     * @param volume  设置的音量大小
     */
    public static void setMusicAudio(Context context, int volume) {
        AudioManager mAudioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, volume, AudioManager.FLAG_SHOW_UI);
    }

    /**
     * 设置通话音量大小
     *
     * @param context 上下文
     * @param volume  设置的音量大小
     */
    public static void setCallAudio(Context context, int volume) {
        AudioManager mAudioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        mAudioManager.setStreamVolume(AudioManager.STREAM_VOICE_CALL, volume, AudioManager.FLAG_SHOW_UI);
    }


    /**
     * 设置铃声音量大小
     *
     * @param context 上下文
     * @param volume  设置的音量大小
     */
    public static void setRingAudio(Context context, int volume) {
        AudioManager mAudioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        mAudioManager.setStreamVolume(AudioManager.STREAM_RING, volume, AudioManager.FLAG_SHOW_UI);
    }


    /**
     * 设置闹钟音量大小
     *
     * @param context 上下文
     * @param volume  设置的音量大小
     */
    public static void setAlarmAudio(Context context, int volume) {
        AudioManager mAudioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        mAudioManager.setStreamVolume(AudioManager.STREAM_ALARM, volume, AudioManager.FLAG_SHOW_UI);
    }


    //-----------------------------------不向外暴露的方法----------------------------------------------


    /**
     * 通过wifi获取ip地址
     *
     * @param context 上下文
     * @return ip地址
     */
    private static String getIpAddressByWifiInfo(Context context) {
        try {
            //获取wifi服务
            WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
            WifiInfo wifiInfo = wifiManager.getConnectionInfo();
            int ipAddress = wifiInfo.getIpAddress();
            if (ipAddress != 0) {
                String ip = intToIp(ipAddress);
                return ip;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 通过NetWork获取ip地址
     *
     * @param useIPv4 ipv4
     * @return ip地址
     */
    private static String getIpAddressByNetworkInterface(boolean useIPv4) {
        try {
            for (Enumeration<NetworkInterface> nis = NetworkInterface.getNetworkInterfaces(); nis.hasMoreElements(); ) {
                NetworkInterface ni = nis.nextElement();
                // 防止小米手机返回10.0.2.15
                if (!ni.isUp()) continue;
                for (Enumeration<InetAddress> addresses = ni.getInetAddresses(); addresses.hasMoreElements(); ) {
                    InetAddress inetAddress = addresses.nextElement();
                    if (!inetAddress.isLoopbackAddress()) {
                        String hostAddress = inetAddress.getHostAddress();
                        boolean isIPv4 = hostAddress.indexOf(':') < 0;
                        if (useIPv4) {
                            if (isIPv4) return hostAddress;
                        } else {
                            if (!isIPv4) {
                                int index = hostAddress.indexOf('%');
                                return index < 0 ? hostAddress.toUpperCase() : hostAddress.substring(0, index).toUpperCase();
                            }
                        }
                    }
                }
            }
        } catch (SocketException e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 获取设备MAC地址 通过Wifi
     *
     * @return MAC地址  如果系统禁止，则返回 02:00:00:00:00:00
     */
    @SuppressLint("HardwareIds")
    private static String getMacAddressByWifiInfo(Context context) {
        try {
            WifiManager wifi = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
            if (wifi != null) {
                WifiInfo info = wifi.getConnectionInfo();
                if (info != null) return info.getMacAddress();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "02:00:00:00:00:00";
    }

    /**
     * 获取设备MAC地址 通过NetWork
     *
     * @return MAC地址 如果系统禁止，则返回 02:00:00:00:00:00
     */
    private static String getMacAddressByNetworkInterface() {
        try {
            List<NetworkInterface> nis = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface ni : nis) {
                if (!ni.getName().equalsIgnoreCase("wlan0")) continue;
                byte[] macBytes = ni.getHardwareAddress();
                if (macBytes != null && macBytes.length > 0) {
                    StringBuilder res1 = new StringBuilder();
                    for (byte b : macBytes) {
                        res1.append(String.format("%02x:", b));
                    }
                    return res1.deleteCharAt(res1.length() - 1).toString();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "02:00:00:00:00:00";
    }


    /**
     * 格式化ip地址
     *
     * @param i ip地址
     * @return ip地址
     */
    private static String intToIp(int i) {
        return (i & 0xFF) + "." +
                ((i >> 8) & 0xFF) + "." +
                ((i >> 16) & 0xFF) + "." +
                (i >> 24 & 0xFF);
    }

    /**
     * 调节音量
     *
     * @param context  上下文
     * @param flag     flag>0 调高 flag<0 调低
     * @param type     调节音量类型
     * @param isShowUI 是否显示调节音量页面
     */
    private static void setAudio(Context context, int flag, int type, boolean isShowUI) {
        AudioManager mAudioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        //降低音量，调出音乐音量控制
        if (flag < 0) {
            mAudioManager.adjustStreamVolume(
                    type, AudioManager.ADJUST_LOWER,
                    AudioManager.FLAG_PLAY_SOUND | (isShowUI ? AudioManager.FLAG_SHOW_UI : 0));
        } else if (flag > 0) {     //增加音量，调出音乐音量控制
            mAudioManager.adjustStreamVolume(
                    type, AudioManager.ADJUST_RAISE,
                    AudioManager.FLAG_PLAY_SOUND | (isShowUI ? AudioManager.FLAG_SHOW_UI : 0));
        }
    }
}