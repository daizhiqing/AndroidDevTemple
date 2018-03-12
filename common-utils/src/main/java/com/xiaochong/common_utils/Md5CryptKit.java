/*
 * Copyright (c) 2017, yindongdong@renwohua.com All Rights Reserved.  
 */
package com.xiaochong.common_utils;

import java.security.MessageDigest;

/**
 * @author 尹东东
 * @version 1.1
 * @date: 2017/2/8 上午6:21
 */
public class Md5CryptKit {
    public static String crypt(byte[] data) {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            messageDigest.reset();
            messageDigest.update(data);

            byte[] byteArray = messageDigest.digest();
            StringBuilder buffer = new StringBuilder();

            for (byte item : byteArray) {
                if (Integer.toHexString(0xFF & item).length() == 1) {
                    buffer.append("0").append(Integer.toHexString(0xFF & item));
                } else {
                    buffer.append(Integer.toHexString(0xFF & item));
                }
            }

            return buffer.toString();
        } catch (Exception e) {
            //D.e(e.toString());
        }
        return null;
    }
}
