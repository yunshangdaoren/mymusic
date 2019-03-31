package com.luckyliuqs.mymusic.Util;

import android.content.Context;
import android.widget.Toast;

/**
 * Toast提示信息工具类：单例模式思想
 */
public class ToastUtil {
    /**
     * 全局唯一的Toast实例
     */
    private static Toast mToast = null;

    /**
     * 发送短时间的提示信息，根据传入的字符串信息
     * @param context
     * @param message
     */
    public static void showSortToast(Context context, String message){
        showToast(context, message, Toast.LENGTH_SHORT);
    }

    /**
     * 发送短时间的提示信息，根据传入的资源ID
     * @param context
     * @param resID
     */
    public static void showSortToast(Context context, int resID){
        showToast(context, context.getResources().getString(resID), Toast.LENGTH_SHORT);
    }

    /**
     * 展示提示信息
     * @param context
     * @param message
     * @param time
     */
    public static void showToast(Context context, String message, int time){
        //单例模式
        if(mToast != null){
        }else{
            //如果Toast实例为null，则创建实例
            mToast = Toast.makeText(context.getApplicationContext(),message,time);
        }
        mToast.setText(message);
        mToast.show();
    }
}
