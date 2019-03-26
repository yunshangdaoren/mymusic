package com.luckyliuqs.mymusic.activity;

import com.luckyliuqs.mymusic.Util.SharedPreferencesUtil;

/**
 *
 */
public class BaseCommonActivity extends BaseActivity{

    protected SharedPreferencesUtil sp;

    @Override
    protected void initViews() {
        super.initViews();

        sp = SharedPreferencesUtil.getInstance(getApplicationContext());

    }
}
