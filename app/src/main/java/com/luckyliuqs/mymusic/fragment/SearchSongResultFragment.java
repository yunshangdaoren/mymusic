package com.luckyliuqs.mymusic.fragment;

import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.luckyliuqs.mymusic.R;
import com.luckyliuqs.mymusic.Util.DataUtil;
import com.luckyliuqs.mymusic.activity.MusicPlayerActivity;
import com.luckyliuqs.mymusic.adapter.BaseRecyclerViewAdapter;
import com.luckyliuqs.mymusic.adapter.SongAdapter;
import com.luckyliuqs.mymusic.api.Api;
import com.luckyliuqs.mymusic.domain.Song;
import com.luckyliuqs.mymusic.domain.event.OnSearchKeyChangedEvent;
import com.luckyliuqs.mymusic.domain.response.ListResponse;
import com.luckyliuqs.mymusic.manager.PlayListManager;
import com.luckyliuqs.mymusic.reactivex.HttpListener;
import com.luckyliuqs.mymusic.service.MusicPlayerService;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import cn.woblog.android.downloader.DownloadService;
import cn.woblog.android.downloader.callback.DownloadManager;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * 搜索结果Fragment页面-歌手
 */
public class SearchSongResultFragment extends BaseCommonFragment implements View.OnClickListener, SongAdapter.OnSongListener{
    private RecyclerView recyclerView;
    private SongAdapter songAdapter;
    private DownloadManager downloadManager;
    private PlayListManager playListManager;

    public static SearchSongResultFragment newInstance(){
        Bundle args = new Bundle();
        SearchSongResultFragment fragment = new SearchSongResultFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void initViews() {
        super.initViews();
        EventBus.getDefault().register(this);

        recyclerView = findViewById(R.id.rv_select_song_list);
        recyclerView.setHasFixedSize(true);

        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getActivity(), RecyclerView.VERTICAL);
        recyclerView.addItemDecoration(dividerItemDecoration);

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onSearchKeyChangedEvent(OnSearchKeyChangedEvent event){
        fetchData(event.getContent());
    }

    private void fetchData(String content){
        Api.getInstance().searchSong(content).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new HttpListener<ListResponse<Song>>(getMainActivity()) {
                    @Override
                    public void onSucceeded(final ListResponse<Song> data) {
                        super.onSucceeded(data);
                        songAdapter.setData(DataUtil.fill(data.getData()));
                    }
                });
    }

    @Override
    protected void initDatas() {
        super.initDatas();
        downloadManager = DownloadService.getDownloadManager(getActivity().getApplicationContext());
        playListManager = MusicPlayerService.getPlayListManager(getActivity().getApplicationContext());

        songAdapter = new SongAdapter(getActivity(), R.layout.item_song_detail, getChildFragmentManager(), playListManager, downloadManager);
        songAdapter.setOnItemClickListener(new BaseRecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseRecyclerViewAdapter.ViewHolder holder, int position) {
                play(position);
            }
        });

        songAdapter.setOnSongListener(this);

        recyclerView.setAdapter(songAdapter);
    }

    private void play(int position){
        Song song = songAdapter.getData(position);
        playListManager.setPlayList(songAdapter.getDatas());
        playListManager.play(song);
        songAdapter.notifyDataSetChanged();
        startActivity(MusicPlayerActivity.class);
    }

    @Override
    public void initListener() {
        super.initListener();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ll_day_container:

                break;
        }

    }

    @Override
    public void onCollectionClick(Song song) {

    }

    @Override
    public void onDownloadClick(Song song) {

    }

    @Override
    public void onDeleteClick(Song song) {

    }

    @Override
    protected View getLayoutView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_select_song_list, null);
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
