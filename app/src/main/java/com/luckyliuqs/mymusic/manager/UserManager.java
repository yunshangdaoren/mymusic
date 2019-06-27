package com.luckyliuqs.mymusic.manager;

import com.luckyliuqs.mymusic.domain.User;

/**
 * 用户管理类
 */
public interface UserManager {
    void getUser(String userId, OnUserListener onUserListener);

    interface OnUserListener{
        void onUser(User user);
    }
}
