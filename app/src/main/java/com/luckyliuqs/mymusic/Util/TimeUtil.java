package com.luckyliuqs.mymusic.Util;


import org.joda.time.DateTime;

/**
 * 时间处理工具类
 */
public class TimeUtil {

    public static int parseInteger(String timeString){
        timeString = timeString.replace(":", ".");
        timeString = timeString.replace(".", "@");
        String[] timeData = timeString.split("@");
        if (timeData.length == 3){
            int m = Integer.parseInt(timeData[0]);
            int s = Integer.parseInt(timeData[1]);
            int ms = Integer.parseInt(timeData[2]);
            int currentTime = (m * 60 + s) * 1000 + ms;
            return currentTime;
        }else{
            return 0;
        }
    }


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

    /**
     * 将时间转换为 00:00格式
     * @param time
     * @return
     */
    public static String parseString(int time){
        time /= 1000;
        int minute = time / 60;
        int second = time % 60;
        minute %= 60;
        return String.format("%02d:%02d", new Object[]{Integer.valueOf(minute), Integer.valueOf(second)});
    }

    public static String dateTimeFormat(String date){
        try{
            DateTime dateTime = new DateTime(date);
            return dateTime.toString("yyyy-MM-dd HH:mm");
        }catch (Exception e){
            e.printStackTrace();
        }

        return "";
    }

}
