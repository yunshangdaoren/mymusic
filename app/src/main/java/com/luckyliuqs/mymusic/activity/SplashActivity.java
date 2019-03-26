package com.luckyliuqs.mymusic.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.WindowManager;

import com.luckyliuqs.mymusic.MainActivity;
import com.luckyliuqs.mymusic.R;
import com.luckyliuqs.mymusic.Util.PackageUtil;

public class SplashActivity extends BaseCommonActivity {
    private static final String TAG = "TAG";
    //延迟时间
    private static final long DEFAULT_DELAY_TIME = 3000;
    //跳转到引导页标志
    private static final int MSG_GUIDE = 100;
    //跳转到首页标志
    private static final int MSG_HOME = 110;

    //创建Handler
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            next();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        //去除状态栏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    /**
     * 初始化数据
     */
    @Override
    protected void initDatas() {
        super.initDatas();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //延迟指定时间后发送需要展示引导页的信息
                handler.sendEmptyMessage(MSG_GUIDE);
            }
        },DEFAULT_DELAY_TIME);
    }

    /**
     * 根据当前版本号判断是否需要引导页面
     * @return boolean
     */
    private boolean isShowGuide(){
        return sp.getBoolean(String.valueOf(PackageUtil.getVersionCode(getApplicationContext())),true);
    }

    private void next(){
        if(isShowGuide()){
            //进入引导页面
            startActivityAfterFinishThis(GuideActivity.class);
        }else if(!sp.isLoginInfoEmpty()){
            //进入首页
            startActivityAfterFinishThis(MainActivity.class);
        }else{
            //进入登录注册页面
            startActivityAfterFinishThis(LoginActivity.class);
        }
    }
}
