package com.luckyliuqs.mymusic.manager.impl;

import android.content.Context;

import com.luckyliuqs.mymusic.api.Api;
import com.luckyliuqs.mymusic.domain.User;
import com.luckyliuqs.mymusic.domain.response.DetailResponse;
import com.luckyliuqs.mymusic.manager.UserManager;
import com.luckyliuqs.mymusic.reactivex.AbsObserver;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class UserManagerImpl implements UserManager {
    private static UserManager manager;
    private final Context context;
    private Map<String, User> userCaches = new HashMap<>();

    public UserManagerImpl(Context context) {
        this.context=context.getApplicationContext();
    }

    public static synchronized UserManager getInstance(Context context) {
        if (manager == null) {
            manager = new UserManagerImpl(context);
        }
        return manager;
    }

    /**
     * 获取用户信息
     * @param userId
     * @param onUserListener
     */
    @Override
    public void getUser(final String userId, final OnUserListener onUserListener) {
        final User user = userCaches.get(userId);
        if (user != null){
            //如果本地有用户信息
            onUserListener.onUser(user);
            return;
        }

        //本地没有用户信息，则去从服务器上获取
        Api.getInstance().userDetail(userId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new AbsObserver<DetailResponse<User>>() {
                    @Override
                    public void onNext(DetailResponse<User> userDetailResponse) {
                        super.onNext(userDetailResponse);
                        if (userDetailResponse != null && userDetailResponse.getData() != null) {
                            onUserListener.onUser(userDetailResponse.getData());
                            userCaches.put(userId, userDetailResponse.getData());
                        }
                    }
                });
    }
}
