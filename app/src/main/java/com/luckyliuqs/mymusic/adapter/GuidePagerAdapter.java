package com.luckyliuqs.mymusic.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import com.luckyliuqs.mymusic.fragment.GuideFragment;

/**
 * 引导页面Adapter类
 */
public class GuidePagerAdapter extends BaseFragmentPagerAdapter<Integer> {
    public GuidePagerAdapter(Context context, FragmentManager fm) {
        super(context, fm);
    }

    /**
     * 通过指定的资源创建一个Fragment并返回
     * @param position
     * @return Fragment
     */
    @Override
    public Fragment getItem(int position) {
        return GuideFragment.newInstance(getData(position));
    }
}
