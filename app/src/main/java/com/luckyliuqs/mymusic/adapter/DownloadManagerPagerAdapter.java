package com.luckyliuqs.mymusic.adapter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import com.luckyliuqs.mymusic.fragment.DownloadedFragment;
import com.luckyliuqs.mymusic.fragment.DownloadingFragment;

/**
 * DownloadManager Activity 的Adapter
 */
public class DownloadManagerPagerAdapter extends BaseFragmentPagerAdapter<Integer> {
    private static String[] titleNames = {"下载完成", "正在下载"};

    public DownloadManagerPagerAdapter(Context context, FragmentManager fm) {
        super(context, fm);
    }

    @Override
    public Fragment getItem(int position) {
        if (position == 0){
            //下载完成Fragment
            return DownloadedFragment.newInstance();
        }else{
            //正在下载Fragment
            return DownloadingFragment.newInstance();
        }
    }


    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return titleNames[position];
    }
}
