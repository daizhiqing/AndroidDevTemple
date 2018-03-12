/*
 * Copyright (c) 2016, yindongdong@renwohua.com All Rights Reserved.  
 */
package com.xiaochong.common_utils;

import android.text.TextUtils;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.regex.Pattern;

/**
 * @author 尹东东
 * @version 1.1
 * @date: 2016/11/8 下午3:56
 */
public class StringKit {
    private final static Pattern emailer = Pattern
            .compile("\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*");
    private final static Pattern phone = Pattern
            .compile("^1\\d{10}$");
    private final static Pattern http = Pattern
            .compile("^(http|https)\\:\\/\\/.+$");

    /**
     * 判断给定字符串是否空白串 空白串是指由空格、制表符、回车符、换行符组成的字符串 若输入字符串为null或空字符串，返回true
     */
    public static boolean isEmpty(CharSequence input) {
        if (input == null || "".equals(input))
            return true;

        for (int i = 0; i < input.length(); i++) {
            char c = input.charAt(i);
            if (c != ' ' && c != '\t' && c != '\r' && c != '\n') {
                return false;
            }
        }
        return true;
    }

    /**
     * 判断是不是一个合法的手机号码
     */
    public static boolean isPhone(CharSequence phoneNum) {
        if (isEmpty(phoneNum))
            return false;
        return phone.matcher(phoneNum).matches();
    }

    public static Boolean isLegitimatePassWord(String passWord) {
        //^$分别匹配字符串的开始和结束,(?=.*\d)表示字符串中有数字，(?=.*[a-z])(?=.*[A-Z])则分别表示字符串中有小写字母和大写字母 [a-zA-Z\d]{6,18}表示字母和数字有6到18位
//        String telRegex = "^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])[a-zA-Z\\d]{6,18}$";
        String telRegex = "^[a-zA-Z0-9]{6,18}$";
        if (TextUtils.isEmpty(passWord))
            return false;
        else
            return passWord.matches(telRegex);
    }

    /**
     * 验证身份证号是否符合规则
     *
     * @param text 身份证号
     * @return
     */
    public static boolean personIdValidation(String text) {
        String regx = "[0-9]{17}[xX]";
        String reg1 = "[0-9]{15}";
        String regex = "[0-9]{18}";
        return text.matches(regx) || text.matches(reg1) || text.matches(regex);
    }

    /**
     * 验证6位短信验证码
     */
    public static boolean isVerificationCode(String code) {
        if (TextUtils.isDigitsOnly(code) && code.length() == 6) {
            return true;
        }
        return false;
    }

    /**
     * 星号替换银行卡号
     *
     * @param number
     * @return
     */
    public static String hideMiddleCode(String number) {
        if (TextUtils.isEmpty(number)) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        char[] s = number.toCharArray();
        for (int i = 0; i < s.length; i++) {
            char c = s[i];
            if (i >= 4 && i <= (s.length - 5))
                sb.append('*');
            else
                sb.append(c);
        }
        return sb.toString();
    }

    /**
     * 星号替换手机号
     *
     * @param number
     * @return
     */
    public static String hideMiddlePhoneCode(String number) {
        if (TextUtils.isEmpty(number)) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        char[] s = number.toCharArray();
        for (int i = 0; i < s.length; i++) {
            char c = s[i];
            if (i >= 3 && i <= (s.length - 5))
                sb.append('*');
            else
                sb.append(c);
        }
        return sb.toString();
    }

    public static Boolean isBankNumber(String num) {
        if (TextUtils.isEmpty(num))
            return false;
        else
            return true;
    }

    public static String get2floatString(double loanMoney) {
        BigDecimal a = BigDecimal.valueOf(loanMoney).setScale(2, BigDecimal.ROUND_DOWN);
        String ft = a.toPlainString();
        return ft;
    }
    public static String get2floatString(String loanMoney) {
        BigDecimal a = BigDecimal.valueOf(Double.parseDouble(loanMoney)).setScale(2, BigDecimal.ROUND_DOWN);
        String ft = a.toPlainString();
        return ft;
    }

    public static boolean getBoolean(String val, boolean def) {
        try {
            return Boolean.parseBoolean(val);
        } catch (Exception e) {
        }
        return def;
    }

    public static int getInt(String val, int def) {
        try {
            return Integer.parseInt(val);
        } catch (Exception e) {
        }
        return def;
    }

    public static double getDouble(String val, double def) {
        try {
            return Double.parseDouble(val);
        } catch (Exception e) {
        }
        return def;
    }

    public static float getFloat(String val, float def) {
        try {
            return Float.parseFloat(val);
        } catch (Exception e) {
        }
        return def;
    }

    public static long getLong(String val, long def) {
        try {
            return Long.parseLong(val);
        } catch (Exception e) {
        }
        return def;
    }

    public static short getShort(String val, short def) {
        try {
            return Short.parseShort(val);
        } catch (Exception e) {
        }
        return def;
    }


    /**
     * Double 转string 去除科学记数法显示
     *
     * @param d
     * @return
     */
    public static String double2Str(Double d) {
        if (d == null) {
            return "";
        }
        NumberFormat nf = NumberFormat.getInstance();
        nf.setGroupingUsed(false);
        return (nf.format(d));
    }

    public static boolean moneyIsZero(String money){
        if (TextUtils.isEmpty(money))
            return true;

        try {
            if (Double.parseDouble(money) > 0){
                return false;
            }
        }catch (Exception e){
            return true;
        }
        return true;
    }
}
