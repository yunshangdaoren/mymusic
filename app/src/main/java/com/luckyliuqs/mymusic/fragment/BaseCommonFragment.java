package com.luckyliuqs.mymusic.fragment;


import com.luckyliuqs.mymusic.Util.SharedPreferencesUtil;

public abstract class BaseCommonFragment extends BaseFragment{
    protected SharedPreferencesUtil sp;

    @Override
    protected void initViews() {
        super.initViews();
        sp = SharedPreferencesUtil.getInstance(getActivity().getApplicationContext());

    }
}
