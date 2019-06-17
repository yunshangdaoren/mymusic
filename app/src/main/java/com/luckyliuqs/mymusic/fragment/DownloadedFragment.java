package com.luckyliuqs.mymusic.fragment;

import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.luckyliuqs.mymusic.R;
import com.luckyliuqs.mymusic.activity.MusicPlayerActivity;
import com.luckyliuqs.mymusic.adapter.BaseRecyclerViewAdapter;
import com.luckyliuqs.mymusic.adapter.DownloadedAdapter;
import com.luckyliuqs.mymusic.adapter.DownloadingAdapter;
import com.luckyliuqs.mymusic.domain.Song;
import com.luckyliuqs.mymusic.domain.event.DownloadStatusChanged;
import com.luckyliuqs.mymusic.manager.PlayListManager;
import com.luckyliuqs.mymusic.service.MusicPlayerService;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import cn.woblog.android.downloader.DownloadService;
import cn.woblog.android.downloader.callback.DownloadManager;
import cn.woblog.android.downloader.domain.DownloadInfo;

/**
 * 下载管理主页：下载完成Fragment页面
 */
public class DownloadedFragment extends BaseCommonFragment implements View.OnClickListener{
    RecyclerView recyclerView;
    private DownloadManager downloadManager;
    private DownloadedAdapter downloadedAdapter;

    /**
     * 歌曲播放列表manager
     */
    private PlayListManager playListManager;

    /**
     * 播放全部歌曲
     */
    private LinearLayout ll_play_all_container;

    public static DownloadedFragment newInstance(){
        Bundle args = new Bundle();
        DownloadedFragment fragment = new DownloadedFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected View getLayoutView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_downloaded, container, false);
    }

    @Override
    protected void initViews() {
        super.initViews();
        ll_play_all_container = findViewById(R.id.ll_local_music_play_all_container);

        recyclerView = findViewById(R.id.rv_downloaded);
        recyclerView.setHasFixedSize(true);

        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getActivity(), RecyclerView.VERTICAL);
        recyclerView.addItemDecoration(dividerItemDecoration);
    }

    @Override
    protected void initDatas() {
        super.initDatas();
        //注册EventBus
        EventBus.getDefault().register(this);

        downloadManager = DownloadService.getDownloadManager(getActivity().getApplicationContext());
        playListManager = MusicPlayerService.getPlayListManager(getActivity().getApplicationContext());

        downloadedAdapter = new DownloadedAdapter(getMainActivity(), orm, getChildFragmentManager(),  downloadManager,playListManager);
        downloadedAdapter.setOnItemClickListener(new BaseRecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseRecyclerViewAdapter.ViewHolder holder, int position) {
                //从已下载歌曲页面从指定位置开始播放歌曲
                play(position);
            }
        });

        recyclerView.setAdapter(downloadedAdapter);

        //刷新已下载歌曲页面的歌曲数据
        fetchData();
    }

    /**
     * 从已下载歌曲页面从指定位置开始播放歌曲
     * @param position
     */
    private void play(int position){
        //获取已下载歌曲页面所有歌曲
        List<DownloadInfo> downloadInfos = downloadedAdapter.getDatas();
        //储存所有待播放歌曲列表
        ArrayList<Song> songs = new ArrayList<>();

        //根据下载的信息查询出对应的歌曲，然后播放
        for (DownloadInfo downloadInfo : downloadInfos){
            //将歌曲添加到待播放歌曲列表中
            songs.add(orm.findSongById(downloadInfo.getId()));
        }
        //获取该position位置的歌曲
        Song song = songs.get(position);
        //设置用于歌曲播放列表数据
        playListManager.setPlayList(songs);
        //播放该position位置的歌曲
        playListManager.play(song);
        downloadedAdapter.notifyDataSetChanged();
        //跳转到音乐播放页面
        startActivity(MusicPlayerActivity.class);
    }

    /**
     * 刷新已下载歌曲页面的歌曲数据
     */
    private void fetchData(){
        //设置所有已经下载完成的歌曲数据
        downloadedAdapter.setData(downloadManager.findAllDownloaded());
    }

    @Override
    public void initListener() {
        super.initListener();
        ll_play_all_container.setOnClickListener(this);
    }

    /**
     * 监听下载状态事件
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void downloadStatusChanged(DownloadStatusChanged event){
        //歌曲下载完成后，就刷新已下载歌曲页面歌曲数据
        fetchData();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ll_local_music_play_all_container:
                //从第一首歌曲开始播放
                play(0);
                break;
        }
    }

    @Override
    public void onDestroy() {
        //取消注册EventBus
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }
}
