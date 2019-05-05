package com.luckyliuqs.mymusic.Util;

import android.content.Context;

/**
 * dp与px转换工具类
 */
public class DensityUtil {

    /**
     * 根据手机的分辨率将dp单位转换为px单位
     * @param context
     * @param dpValue
     * @return int
     */
    public static int dipToPx(Context context, float dpValue){
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int)(dpValue * scale + 0.5f);
    }

    /**
     * 根据手机的分辨率将px单位转换为dp单位
     * @param context
     * @param pxValue
     * @return int
     */
    public static int pxToDip(Context context, float pxValue){
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int)(pxValue / scale + 0.5f);
    }

}
