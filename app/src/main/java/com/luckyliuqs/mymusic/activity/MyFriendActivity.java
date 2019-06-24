package com.luckyliuqs.mymusic.activity;

import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.luckyliuqs.mymusic.R;

import net.lucode.hackware.magicindicator.MagicIndicator;

/**
 * 我的好友Activity页面
 */
public class MyFriendActivity extends BaseTitleActivity {
    private MagicIndicator tabs;

    private ViewPager viewPager;
    private MyFriendAdapter myFriendAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_friend);


    }
}
