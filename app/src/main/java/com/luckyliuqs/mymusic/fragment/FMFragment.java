package com.luckyliuqs.mymusic.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.luckyliuqs.mymusic.R;

/**
 * 我的推荐主页：电台Fragment页面
 */
public class FMFragment extends BaseCommonFragment{

    public static FMFragment newInstance() {
        
        Bundle args = new Bundle();

        FMFragment fragment = new FMFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected View getLayoutView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_fm,null);
    }
}
