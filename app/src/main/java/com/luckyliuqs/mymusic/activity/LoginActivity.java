package com.luckyliuqs.mymusic.activity;

import android.os.Bundle;

import com.luckyliuqs.mymusic.MainActivity;
import com.luckyliuqs.mymusic.R;
import com.luckyliuqs.mymusic.domain.event.LoginSuccessEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.OnClick;

/**
 * 登录页面
 */
public class LoginActivity extends BaseCommonActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    @Override
    protected void initDatas() {
        super.initDatas();
        //监听到注册成功消息
        EventBus.getDefault().register(this);
    }

    /**
     * 监听注册成功的消息方法
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void loginSuccess(LoginSuccessEvent event){
        //关闭当前登录界面
        finish();
    }


    /**
     * 登录按钮点击事件
     */
    @OnClick(R.id.bt_login)
    public void bt_login(){
        startActivity(LoginPhoneActivity.class);
    }

    /**
     * 注册按钮点击事件
     */
    @OnClick(R.id.bt_register)
    public void bt_register(){
        startActivity(RegisterActivity.class);
    }

    /**
     * 游客登录点击事件
     */
    @OnClick(R.id.tv_toursit_login)
    public void tv_toursit_login(){
        startActivity(MainActivity.class);
    }

    /**
     * QQ登录点击事件
     */
    @OnClick(R.id.iv_login_qq)
    public void iv_login_qq(){
        startActivity(MainActivity.class);
    }


    @Override
    protected void onDestroy() {
        //解除
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }
}
