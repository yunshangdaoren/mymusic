package com.luckyliuqs.mymusic.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.luckyliuqs.mymusic.AppContext;
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

//    @SuppressLint("HandlerLeak")
//    private Handler handler = new Handler(){
//        @SuppressWarnings("unused")
//        @Override
//        public void handleMessage(Message msg) {
//            switch (msg.what){
//
//            }
//        }
//
//    };

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

    /**
     * 登录成功
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void loginSuccessEvent(LoginSuccessEvent event) {
        //连接融云服务器
        ((AppContext)getApplication()).imConnect();
        finish();
    }

    @Override
    protected void onDestroy() {
        //解除
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }
}
