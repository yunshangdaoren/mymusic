package com.luckyliuqs.mymusic.adapter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import com.luckyliuqs.mymusic.fragment.FMFragment;
import com.luckyliuqs.mymusic.fragment.FeedFragment;
import com.luckyliuqs.mymusic.fragment.RecommendFragment;

/**
 * 发现界面Adapter类
 */
public class FindFragmentAdapter extends BaseFragmentAdapter<Integer>{
    private static String[] titleNames = {"推荐", "朋友", "电台"};

    public FindFragmentAdapter(Context context, FragmentManager fm) {
        super(context, fm);
    }

    @Override
    public Fragment getItem(int position) {
        if (position == 0){
            //推荐页面
            return RecommendFragment.newInstance();
        }else if(position == 1){
            //朋友动态页面
            return FeedFragment.newInstance();
        }else{
            //FM电台页面
            return FMFragment.newInstance();
        }
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return titleNames[position];
    }
}
