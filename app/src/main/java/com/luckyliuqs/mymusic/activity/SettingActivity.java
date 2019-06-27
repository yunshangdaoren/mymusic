package com.luckyliuqs.mymusic.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.luckyliuqs.mymusic.AppContext;
import com.luckyliuqs.mymusic.R;
import com.luckyliuqs.mymusic.domain.event.LogoutSuccessEvent;

import org.greenrobot.eventbus.EventBus;

import butterknife.OnClick;

/**
 * 设置主页
 */
public class SettingActivity extends BaseTitleActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
    }

    @Override
    protected void initViews() {
        super.initViews();
        enableBackMenu();
    }

    /**
     * 退出登录
     */
    @OnClick(R.id.bt_logout)
    public void bt_logout(){
        //清除储存在本地的信息
        sp.logout();

        AppContext.logout();


        //发布退出登陆的信息，因为首页要根据登陆状态显示
        EventBus.getDefault().post(new LogoutSuccessEvent());
        startActivity(LoginActivity.class);
        finish();
    }


}
