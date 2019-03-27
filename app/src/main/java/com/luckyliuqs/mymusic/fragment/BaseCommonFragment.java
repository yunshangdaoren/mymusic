package com.luckyliuqs.mymusic.fragment;


import com.luckyliuqs.mymusic.Util.SharedPreferencesUtil;

public abstract class BaseCommonFragment extends BaseFragment{
    /**
     * 定义SharedPreferencesUtil对象
     */
    protected SharedPreferencesUtil sp;

    @Override
    protected void initViews() {
        super.initViews();
        //获取到SharePreferencesUtil实例对象
        sp = SharedPreferencesUtil.getInstance(getActivity().getApplicationContext());
    }
}
