package com.luckyliuqs.mymusic.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.luckyliuqs.mymusic.MainActivity;
import com.luckyliuqs.mymusic.R;

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
}
