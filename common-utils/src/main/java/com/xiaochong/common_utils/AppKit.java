/*
 * Copyright (c) 2016, yindongdong@renwohua.com All Rights Reserved.  
 */
package com.xiaochong.common_utils;

import android.app.Activity;
import android.app.Application;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.pm.ServiceInfo;


import com.orhanobut.logger.Logger;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map;

/**
 * @author 尹东东
 * @version 1.1
 * @date: 2016/11/9 下午1:05
 */
public class AppKit {
    private static final String RELEASE_SIGN = "18da6734b7f2f5f77b9c73ffdaf81c86";
    private static String sSign = null;
    private static boolean sIsRelease = false;
    public static final Application me;

    static {
        Application app = null;
        try {
            app = (Application) Class.forName("android.app.AppGlobals").getMethod("getInitialApplication").invoke(null);
            if (app == null)
                throw new IllegalStateException("Static initialization of Applications must be on main thread.");
        } catch (final Exception e) {
            Logger.e("Failed to get current application from AppGlobals." + e.getMessage());
            try {
                app = (Application) Class.forName("android.app.ActivityThread").getMethod("currentApplication").invoke(null);
            } catch (final Exception ex) {
                Logger.e("Failed to get current application from ActivityThread." + e.getMessage());
            }
        } finally {
            me = app;
        }
    }

    public static String getMetaData(Activity activity, String key) {
        String val = "";
        try {
            ActivityInfo info = activity.getPackageManager()
                    .getActivityInfo(activity.getComponentName(),
                            PackageManager.GET_META_DATA);
            val = info.metaData.getString(key);
        } catch (PackageManager.NameNotFoundException e) {
            Logger.e(e.getMessage() ,e);
        }
        return val;
    }

    public static String getMetaData(Application application, String key) {
        String val = "";
        try {
            ApplicationInfo appInfo = application.getPackageManager()
                    .getApplicationInfo(application.getPackageName(),
                            PackageManager.GET_META_DATA);
            val = appInfo.metaData.getString(key);
        } catch (PackageManager.NameNotFoundException e) {
            Logger.e(e,e.getMessage());
        }
        return val;
    }

    public static String getMetaData(Context pkg, Class<? extends Service> cls, String key) {
        String val = "";
        try {
            ComponentName cn = new ComponentName(pkg, cls);
            ServiceInfo info = pkg.getPackageManager()
                    .getServiceInfo(cn, PackageManager.GET_META_DATA);
            val = info.metaData.getString(key);
        } catch (PackageManager.NameNotFoundException e) {
            Logger.e(e.getMessage() ,e);
        }
        return val;
    }

    /**
     * 是否是一个合法的intent，即intent是否能调起一个activity
     *
     * @param intent , 需要检验的intent
     */
    public static boolean isAvailableIntent(Intent intent) {
        PackageManager packageManager = me.getPackageManager();
        List<ResolveInfo> resolveInfo = packageManager
                .queryIntentActivities(intent, PackageManager.GET_RESOLVED_FILTER);
        return resolveInfo != null && !resolveInfo.isEmpty();
    }

    /**
     * 是否正式版本
     *
     * @return
     */
    public static boolean isReleased() {
        if (StringKit.isEmpty(sSign)) {
            sSign = SystemKit.getSign(me.getApplicationContext(), me.getPackageName());
            Logger.e("sign : " + sSign);
            sIsRelease = RELEASE_SIGN.equals(sSign);
        }
        return sIsRelease;
    }

    public static Activity getCurrentActivity() {
        try {
            Class activityThreadClass = Class.forName("android.app.ActivityThread");
            Object activityThread = activityThreadClass.getMethod("currentActivityThread").invoke(
                    null);
            Field activitiesField = activityThreadClass.getDeclaredField("mActivities");
            activitiesField.setAccessible(true);
            Map activities = (Map) activitiesField.get(activityThread);
            for (Object activityRecord : activities.values()) {
                Class activityRecordClass = activityRecord.getClass();
                Field pausedField = activityRecordClass.getDeclaredField("paused");
                pausedField.setAccessible(true);
                if (!pausedField.getBoolean(activityRecord)) {
                    Field activityField = activityRecordClass.getDeclaredField("activity");
                    activityField.setAccessible(true);
                    Activity activity = (Activity) activityField.get(activityRecord);
                    return activity;
                }
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
