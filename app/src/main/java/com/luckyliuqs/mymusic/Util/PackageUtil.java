package com.luckyliuqs.mymusic.Util;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.PackageManager;

import java.util.Iterator;
import java.util.List;

/**
 * Package工具类
 */
public class PackageUtil {

    /**
     * 根据传入的上下文获取应用版本名称
     * @param context
     * @return 如果获取到了版本名称则返回版本名称
     *         如果没有获取到版本名称则返回null
     */
    public static String getVersionName(Context context){
        //获取PackageManager对象
        PackageManager packageManager = context.getPackageManager();
        try {
            //返回版本名称
            return packageManager.getPackageInfo(context.getPackageName(),0).versionName;
        }catch (PackageManager.NameNotFoundException e){
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 根据传入的上下文获取应用版本号
     * @param context
     * @return 如果获取到了版本号则返回版本号
     *         如果没有获取到版本号则返回-1
     */
    public static int getVersionCode(Context context){
        //获取PackageManager对象
        PackageManager packageManager = context.getPackageManager();
        try {
            //返回版本号
            return packageManager.getPackageInfo(context.getPackageName(),0).versionCode;
        }catch (PackageManager.NameNotFoundException e){
            e.printStackTrace();
        }
        return -1;
    }

    /**
     * 根据pid获取当前进程的名称，一般就是当前APP的包名
     * @param context 上下文
     * @param pid 进程的id
     * @return 返回进程的名称
     */
    public static String getAppName(Context context, int pid){
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List list = activityManager.getRunningAppProcesses();
        Iterator i = list.iterator();
        while (i.hasNext())
        {
            ActivityManager.RunningAppProcessInfo info = (ActivityManager.RunningAppProcessInfo) (i.next());
            try
            {
                if (info.pid == pid)
                {
                    // 根据进程的信息获取当前进程的名字
                    return info.processName;
                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
        // 没有匹配的项，返回为null
        return null;
    }

}
