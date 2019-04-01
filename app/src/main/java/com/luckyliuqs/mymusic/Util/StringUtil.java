package com.luckyliuqs.mymusic.Util;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * 字符串工具类
 */
public class StringUtil {

    private static final String PHONE_REGEX = "^(13[0-9]|14[579]|15[0-3,5-9]|16[6]|17[0135678]|18[0-9]|19[89])\\d{8}$";

    public static String number2Scale(double value){
        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(2, RoundingMode.HALF_UP);
        return bd.toString();
    }

    /**
     * 正则表达式：判断是否是手机号
     * @param value
     * @return boolean
     */
    public static boolean isPhone(String value){
        return value.matches(PHONE_REGEX);
    }

    /**
     * 判断密码是否大于6位数
     * @param value
     * @return
     */
    public static boolean isPassword(String value){
        return value.length() >= 6;
    }

    /**
     * 统计数据格式化，以万为单位
     * @param count
     * @return
     */
    public static String formatCount(long count){
        if(count >= 10000){
            count = count/10000;
            return String.format("%d万",count);
        }
        return String.valueOf(count);
    }

    /**
     * 判断字符数是否为数字
     * @param str
     * @return
     */
    public static boolean isNumber(String str){
        if(str == null){
            return false;
        }
        int length = str.length();
        for(int i = 0; i < length; i++){
            //判断字母是否为数字
            if(Character.isDigit(str.charAt(i)) == false){
                return false;
            }
        }
        return true;
    }



}
