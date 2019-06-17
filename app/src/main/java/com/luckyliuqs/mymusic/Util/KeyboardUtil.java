package com.luckyliuqs.mymusic.Util;

import android.app.Activity;
import android.content.Context;
import android.view.inputmethod.InputMethodManager;

/**
 * 手机输入键盘工具类
 */
public class KeyboardUtil {
    /**
     * 关闭键盘
     * @param activity
     */
    public static void hideKeyboard(Activity activity){
        if (activity.getCurrentFocus() != null){
            ((InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE))
                    .hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

}
