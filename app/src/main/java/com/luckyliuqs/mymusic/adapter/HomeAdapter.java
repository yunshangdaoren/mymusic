package com.luckyliuqs.mymusic.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import com.luckyliuqs.mymusic.fragment.MusicFragment;
import com.luckyliuqs.mymusic.fragment.MeFragment;
import com.luckyliuqs.mymusic.fragment.VideoFragment;

/**
 * 主界面Adapter类
 */
public class HomeAdapter extends BaseFragmentAdapter<Integer>{
    public HomeAdapter(Context context, FragmentManager fm) {
        super(context, fm);
    }

    @Override
    public Fragment getItem(int position) {
        if(position == 0){
            //返回我的Fragment页面
            return MeFragment.newInstance();
        }else if(position == 1){
            //返回音乐Fragment页面
            return MusicFragment.newInstance();
        }else{
            //返回视频Fragment页面
            return VideoFragment.newInstance();
        }
    }
}
