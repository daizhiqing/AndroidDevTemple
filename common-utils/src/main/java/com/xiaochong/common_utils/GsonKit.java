/*
 * Copyright (c) 2016, yindongdong@renwohua.com All Rights Reserved.  
 */
package com.xiaochong.common_utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * @author 戴智青
 * @version 1.1
 * @date: 2016/11/10 上午11:33
 */
public class GsonKit {
    private static Gson GSON = null;

    static {
        GsonBuilder builder = new GsonBuilder();
        GSON = builder.create();
    }

    public static Gson getGSON() {
        return GSON;
    }

    /**
     * 对象实体转化为json
     */
    public static String toJson(Object object) {
        return GSON.toJson(object);
    }

    /**
     * json 对象转化为 JAVA 实例
     */
    public static <T> T fromJson(String json, Class<T> clazz) {
        return GSON.fromJson(json, clazz);
    }

    /**
     *  json对象转化成
     * @param json
     * @param typeOfT
     * @param <T>
     * @return
     */
    public static <T> T fromJson(String json, Type typeOfT) {
        return GSON.fromJson(json, typeOfT);
    }

    /**
     * 判断某个类型是否是另一个类型的子类
     * @param clazz , 待判断的类型
     * @param superClazz , 目标类型
     * */
    public static boolean isSubObjectOf(Object clazz, Class superClazz) {
        do {
            if (clazz == superClazz) return true;

            if (clazz instanceof ParameterizedType) {
                Type type = ((ParameterizedType) clazz).getRawType();
                if (type == superClazz) {
                    return true;
                } else {
                    clazz = type.getClass();
                }
            } else if (clazz instanceof Class){
                Class target = (Class) clazz;
                Class[] interfaces = target.getInterfaces();
                for (Class item : interfaces) {
                    if (item == superClazz) {
                        return true;
                    }
                }
                clazz = target.getSuperclass();
            }

        } while (clazz != Object.class);
        return false;
    }
}
