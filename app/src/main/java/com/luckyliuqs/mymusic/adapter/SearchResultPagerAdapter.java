package com.luckyliuqs.mymusic.adapter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;

import com.luckyliuqs.mymusic.fragment.SearchAlbumResultFragment;
import com.luckyliuqs.mymusic.fragment.SearchRadioResultFragment;
import com.luckyliuqs.mymusic.fragment.SearchSingerResultFragment;
import com.luckyliuqs.mymusic.fragment.SearchSongListResultFragment;
import com.luckyliuqs.mymusic.fragment.SearchSongResultFragment;
import com.luckyliuqs.mymusic.fragment.SearchUserResultFragment;
import com.luckyliuqs.mymusic.fragment.SearchVideoResultFragment;

/**
 * 搜索结果页面Adapter
 */
public class SearchResultPagerAdapter extends BaseFragmentPagerAdapter<Integer>{
    private static String[] titleNames = {"单曲", "视频", "歌手", "专辑", "歌单", "主播电台", "用户"};

    public SearchResultPagerAdapter(Context context, FragmentManager fm) {
        super(context, fm);
    }

    @Override
    public Fragment getItem(int position) {
        if (position == 0){
            Log.i("SearchResultPager", "position: " + position);
            //搜索结果Fragment页面-单曲
            return SearchSongResultFragment.newInstance();
        }else if (position == 1){
            Log.i("SearchResultPager", "position: " + position);
            //搜索结果Fragment页面-视频
            return SearchVideoResultFragment.newInstance();
        }else if (position == 2){
            Log.i("SearchResultPager", "position: " + position);
            //搜索结果Fragment页面-歌手
            return SearchSingerResultFragment.newInstance();
        }else if (position == 3){
            Log.i("SearchResultPager", "position: " + position);
            //搜索结果Fragment页面-专辑
            return SearchAlbumResultFragment.newInstance();
        }else if (position == 4){
            Log.i("SearchResultPager", "position: " + position);
            //搜索结果Fragment页面-歌单
            return SearchSongListResultFragment.newInstance();
        }else if (position == 5){
            Log.i("SearchResultPager", "position: " + position);
            //搜索结果Fragment页面-主播电台
            return SearchRadioResultFragment.newInstance();
        }else{
            Log.i("SearchResultPager", "position: " + position);
            //搜索结果Fragment页面-用户
            return SearchUserResultFragment.newInstance();
        }

    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return titleNames[position];
    }
}
