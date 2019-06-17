package com.luckyliuqs.mymusic.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.luckyliuqs.mymusic.R;

public class RecentPlayActivity extends BaseTitleActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recent_play);
    }

    @Override
    protected void initViews() {
        super.initViews();
        enableBackMenu();

    }
}
