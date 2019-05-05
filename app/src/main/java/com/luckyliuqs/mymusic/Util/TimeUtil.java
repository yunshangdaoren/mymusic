package com.luckyliuqs.mymusic.Util;

/**
 * 时间处理工具类
 */
public class TimeUtil {


    /**
     * 用于将毫秒格式化为分:秒，例如：150:11
     * @param time
     * @return
     */
    public static String formatMSTime(int time){
        if (time == 0){
            return "00:00";
        }
        time /= 1000;
        int i = time / 60;
        return String.format("%02d", i) + ":" + String.format("%02d", time - (i * 60));
    }

}
