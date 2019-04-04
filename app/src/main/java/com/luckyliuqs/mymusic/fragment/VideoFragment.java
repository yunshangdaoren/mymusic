package com.luckyliuqs.mymusic.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.luckyliuqs.mymusic.R;

/**
 * 视频Fragment页面
 */
public class VideoFragment extends BaseCommonFragment{

    public static VideoFragment newInstance() {
        
        Bundle args = new Bundle();

        VideoFragment fragment = new VideoFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected View getLayoutView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_video,null);
    }
}
