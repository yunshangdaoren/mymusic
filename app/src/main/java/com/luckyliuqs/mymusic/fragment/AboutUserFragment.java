package com.luckyliuqs.mymusic.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.luckyliuqs.mymusic.R;

/**
 * 用户资料主页：关于我Fragment页面
 */
public class AboutUserFragment extends BaseCommonFragment{

    public static AboutUserFragment newInstance(){
        Bundle args = new Bundle();
        AboutUserFragment fragment = new AboutUserFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected View getLayoutView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_about_me, null);
    }
}