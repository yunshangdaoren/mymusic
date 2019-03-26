package com.luckyliuqs.mymusic.Util;

import android.content.Context;
import android.content.pm.PackageManager;

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


}
