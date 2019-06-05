package com.luckyliuqs.mymusic.activity;

import com.luckyliuqs.mymusic.Util.OrmUtil;
import com.luckyliuqs.mymusic.Util.ServiceUtil;
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

    /**
     * 数据库连接工具类
     */
    protected OrmUtil ormUtil;


    @Override
    protected void initViews() {
        super.initViews();
        //注解
        ButterKnife.bind(this);
        //获取到SharePreferencesUtil实例对象
        sp = SharedPreferencesUtil.getInstance(getApplicationContext());
        ormUtil = OrmUtil.getInstance(getApplicationContext());

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!ServiceUtil.isBackgroundRunning(getApplicationContext())){
            //如果当前程序在前台，则尝试隐藏桌面歌词

        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (ServiceUtil.isBackgroundRunning(getApplicationContext())){
            //如果当前程序在后台，则显示桌面歌词

        }
    }
}
