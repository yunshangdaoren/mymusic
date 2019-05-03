package com.luckyliuqs.mymusic.adapter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import com.luckyliuqs.mymusic.fragment.FMFragment;
import com.luckyliuqs.mymusic.fragment.FeedFragment;
import com.luckyliuqs.mymusic.fragment.RecommendFragment;

/**
 * 音乐界面Adapter类
 */
public class MusicUIAdapter extends BaseFragmentAdapter<Integer>{
    private static String[] titleNames = {"推荐", "朋友", "电台"};

    public MusicUIAdapter(Context context, FragmentManager fm) {
        super(context, fm);
    }

    @Override
    public Fragment getItem(int position) {
        if (position == 0){
            return RecommendFragment.newInstance();
        }else if(position == 1){
            return FeedFragment.newInstance();
        }else{
            return FMFragment.newInstance();
        }
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return titleNames[position];
    }
}
