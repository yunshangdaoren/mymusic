package com.luckyliuqs.mymusic.Util;

import android.app.Activity;
import android.widget.ImageView;
import android.widget.TextView;

import com.luckyliuqs.mymusic.R;
import com.luckyliuqs.mymusic.domain.User;

public class UserUtil {

    /**
     * 显示已登陆状态下用户界面
     * @param activity
     * @param user
     * @param iv_user_avatar
     * @param tv_user_nickname
     * @param tv_user_description
     */
    public static void showUser(Activity activity, User user, ImageView iv_user_avatar, TextView tv_user_nickname, TextView tv_user_description) {
        if (user == null) {
            ImageUtil.showCircle(activity, iv_user_avatar, R.drawable.default_avatar);
        } else {
            ImageUtil.showCircle(activity, iv_user_avatar, user.getAvatar());
            tv_user_nickname.setText(user.getNickname());
            tv_user_description.setText(user.getDescription());

        }

    }

    /**
     * 显示未登陆状态下用户界面
     * @param activity
     * @param iv_user_avatar
     * @param tv_user_nickname
     * @param tv_user_description
     */
    public static void showNotLoginUser(Activity activity,  ImageView iv_user_avatar, TextView tv_user_nickname, TextView tv_user_description) {
        ImageUtil.showCircle(activity, iv_user_avatar, R.drawable.default_avatar);
        tv_user_nickname.setText("请先登陆");
        tv_user_description.setText("");

    }
}
