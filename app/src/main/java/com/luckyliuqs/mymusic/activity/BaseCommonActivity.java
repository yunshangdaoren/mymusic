package com.luckyliuqs.mymusic.activity;

import com.luckyliuqs.mymusic.Util.SharedPreferencesUtil;

import butterknife.ButterKnife;

/**
 *
 */
public class BaseCommonActivity extends BaseActivity{
    /**
     * 定义SharedPreferencesUtil对象
     */
    protected SharedPreferencesUtil sp;

    @Override
    protected void initViews() {
        super.initViews();
        //注解
        ButterKnife.bind(this);
        //获取到SharePreferencesUtil实例对象
        sp = SharedPreferencesUtil.getInstance(getApplicationContext());

    }
}
