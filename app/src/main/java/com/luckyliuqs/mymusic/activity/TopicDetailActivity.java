package com.luckyliuqs.mymusic.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.jdsjlzx.recyclerview.LRecyclerViewAdapter;
import com.luckyliuqs.mymusic.R;
import com.luckyliuqs.mymusic.adapter.FeedAdapter;
import com.luckyliuqs.mymusic.adapter.TopicAdapter;
import com.luckyliuqs.mymusic.domain.response.ListResponse;

/**
 * 话题详情Activity页面
 */
public class TopicDetailActivity extends BaseTitleActivity {
    private String title;
    private String id;
    private LRecyclerViewAdapter lRecyclerViewAdapter;
    private RecyclerView recyclerView;
    private TextView tv_title;
    private TextView tv_count;
    private TextView tv_description;
    private ListResponse.Meta meta;
    private ImageView iv_icon;
    private FeedAdapter feedAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topic_detail);
    }
}
