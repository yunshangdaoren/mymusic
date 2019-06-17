package com.luckyliuqs.mymusic.adapter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import com.luckyliuqs.mymusic.fragment.AboutUserFragment;
import com.luckyliuqs.mymusic.fragment.FeedFragment;
import com.luckyliuqs.mymusic.fragment.UserDetailMusicFragment;

/**
 * 用户详情主页Adapter类
 */
public class UserDetailAdapter extends BaseFragmentAdapter<Integer>{
    private static String[] titleNames = {"音乐", "动态", "关于我"};
    private String userId;

    public void setUserId(String userId){
        this.userId = userId;
    }

    public UserDetailAdapter(Context context, FragmentManager fm) {
        super(context, fm);
    }

    @Override
    public Fragment getItem(int position) {
        if (position == 0){
            //音乐
            return UserDetailMusicFragment.newInstance();
        }else if (position == 1){
            //动态
            return FeedFragment.newInstance();
        }else{
            //关于我
            return AboutUserFragment.newInstance();
        }
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return titleNames[position];
    }
}
