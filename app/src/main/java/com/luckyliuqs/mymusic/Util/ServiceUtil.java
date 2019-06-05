package com.luckyliuqs.mymusic.Util;

import android.app.ActivityManager;
import android.content.Context;

import com.luckyliuqs.mymusic.service.MusicPlayerService;

import java.util.List;

/**
 * Service工具类
 */
public class ServiceUtil {

    /**
     * 判断MusiclPlayerService是否开启了
     * @param context
     * @return
     */
    public static boolean isServiceRunning(Context context){
        boolean isRunning = false;
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> serviceList = activityManager.getRunningServices(Integer.MAX_VALUE);

        if (serviceList == null || serviceList.size() == 0){
            return false;
        }

        for(int i = 0; i < serviceList.size(); i++){
            if (serviceList.get(i).service.getClassName().equals(MusicPlayerService.class.getName())){
                isRunning = true;
                break;
            }
        }
        return isRunning;
    }

    public static boolean isBackgroundRunning(Context context){
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> appProcessInfos = activityManager.getRunningAppProcesses();

        for (ActivityManager.RunningAppProcessInfo appProcessInfo : appProcessInfos){
            if (appProcessInfo.processName.equals(context.getPackageName())){
                if (appProcessInfo.importance != ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND){
                    return true;
                }else{
                    return false;
                }
            }
        }

        return false;
    }

}
