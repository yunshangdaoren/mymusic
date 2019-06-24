package com.luckyliuqs.mymusic.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import com.luckyliuqs.mymusic.fragment.FindFragment;
import com.luckyliuqs.mymusic.fragment.MeFragment;
import com.luckyliuqs.mymusic.fragment.VideoFragment;

/**
 * 主界面Adapter类
 */
public class MainActivityPagerAdapter extends BaseFragmentPagerAdapter<Integer> {
    public MainActivityPagerAdapter(Context context, FragmentManager fm) {
        super(context, fm);
    }

    @Override
    public Fragment getItem(int position) {
        if(position == 0){
            //返回我的音乐Fragment页面
            return MeFragment.newInstance();
        }else if(position == 1){
            //返回发现Fragment页面
            return FindFragment.newInstance();
        }else{
            //返回视频Fragment页面
            return VideoFragment.newInstance();
        }
    }
}
