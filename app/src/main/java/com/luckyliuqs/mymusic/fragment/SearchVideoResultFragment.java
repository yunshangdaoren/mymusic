package com.luckyliuqs.mymusic.fragment;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.luckyliuqs.mymusic.R;
import com.luckyliuqs.mymusic.domain.event.OnSearchKeyChangedEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;


/**
 * 搜索结果Fragment页面-视频
 */
public class SearchVideoResultFragment  extends BaseCommonFragment {
    private RecyclerView recyclerView;

    public static SearchVideoResultFragment newInstance(){
        Bundle args = new Bundle();
        SearchVideoResultFragment fragment = new SearchVideoResultFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void initViews() {
        super.initViews();
        EventBus.getDefault().register(this);


    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onSearchKeyChangedEvent(OnSearchKeyChangedEvent event){
        fetchData(event.getContent());
    }

    private void fetchData(String content){

    }

    @Override
    protected void initDatas() {
        super.initDatas();
    }

    @Override
    public void initListener() {
        super.initListener();

    }

    @Override
    protected View getLayoutView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_search_result_video, null);
    }

    @Override
    public void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }
}
