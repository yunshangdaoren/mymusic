package com.luckyliuqs.mymusic.activity;

import android.media.MediaPlayer;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.luckyliuqs.mymusic.R;
import com.luckyliuqs.mymusic.Util.ImageUtil;
import com.luckyliuqs.mymusic.adapter.BaseRecyclerViewAdapter;
import com.luckyliuqs.mymusic.adapter.PlayListAdapter;
import com.luckyliuqs.mymusic.domain.Lyric;
import com.luckyliuqs.mymusic.domain.Song;
import com.luckyliuqs.mymusic.fragment.PlayListDialogFragment;
import com.luckyliuqs.mymusic.listener.OnMusicPlayerListener;
import com.luckyliuqs.mymusic.listener.PlayListListener;
import com.luckyliuqs.mymusic.manager.MusicPlayerManager;
import com.luckyliuqs.mymusic.manager.PlayListManager;
import com.luckyliuqs.mymusic.parser.LyricsParser;
import com.luckyliuqs.mymusic.service.MusicPlayerService;
import com.luckyliuqs.mymusic.view.LyricSingleLineView;

import java.util.List;

/**
 *
 */
public class BaseMusicPlayerActivity extends BaseTitleActivity implements OnMusicPlayerListener, PlayListListener, View.OnClickListener{
    /**
     * 全局歌曲播放栏
     */
    private LinearLayout ll_play_small_container_container;

    /**
     * 歌曲封面
     */
    protected ImageView iv_banner_play_small_controller;

    /**
     * 暂停或播放
     */
    protected ImageView iv_control_play_small_controller;

    /**
     * 下一首
     */
    protected ImageView iv_next_play_small_controller;

    /**
     * 歌曲播放列表
     */
    protected ImageView iv_play_list_play_small_controller;

    /**
     * 歌曲名称
     */
    protected TextView tv_title_play_small_controller;

    /**
     * 自定义一行歌词
     */
    protected LyricSingleLineView tv_single_line_lyric_play_small_controller;

    /**
     * 歌曲播放进度条
     */
    protected ProgressBar progress_bar_play_small_controller;

    protected PlayListManager playListManager;
    protected MusicPlayerManager musicPlayerManager;
    private PlayListDialogFragment playListDialog;


    @Override
    protected void initViews() {
        super.initViews();

        ll_play_small_container_container = findViewById(R.id.ll_play_small_container_container);
        iv_banner_play_small_controller = findViewById(R.id.iv_banner_play_small_controller);
        iv_control_play_small_controller = findViewById(R.id.iv_control_play_small_controller);
        iv_next_play_small_controller = findViewById(R.id.iv_next_play_small_controller);
        iv_play_list_play_small_controller = findViewById(R.id.iv_play_list_play_small_controller);
        tv_title_play_small_controller = findViewById(R.id.tv_title_play_small_controller);
        progress_bar_play_small_controller = findViewById(R.id.progress_bar_play_small_controller);
        tv_single_line_lyric_play_small_controller = findViewById(R.id.tv_single_line_lyric_play_small_controller);

    }

    @Override
    protected void initDatas() {
        super.initDatas();
        playListManager = MusicPlayerService.getPlayListManager(getApplicationContext());
        musicPlayerManager = MusicPlayerService.getMusicPlayerManager(getApplicationContext());
    }

    @Override
    protected void onResume() {
        super.onResume();
        //写在这里是因为：可以在播放界面将播放列表清空后，这里全局歌曲播放栏需要隐藏
        if (playListManager.getPlayList() != null && playListManager.getPlayList().size() > 0){
            //显示全局歌曲播放栏
            ll_play_small_container_container.setVisibility(View.VISIBLE);
            Song currentSong = playListManager.getPlayData();
            if (currentSong != null){
                setFirstData(currentSong);
            }
        }else{
            //隐藏全局歌曲播放栏
            ll_play_small_container_container.setVisibility(View.GONE);
        }

        //注册监听器
        musicPlayerManager.addOnMusicPlayerListener(this);
        playListManager.addPlayListListener(this);
    }

    public void setFirstData(Song song){
        //设置进度条最大进度值
        progress_bar_play_small_controller.setMax((int) song.getDuration());
        //设置进度条当前进度值
        progress_bar_play_small_controller.setProgress(sp.getLastSongProgress());
        //将歌曲封面图片圆角化
        ImageUtil.show(getActivity(), iv_banner_play_small_controller, song.getBanner());
        //设置歌曲名称
        tv_title_play_small_controller.setText(song.getTitle());
    }

    @Override
    protected void initListener() {
        super.initListener();
        ll_play_small_container_container.setOnClickListener(this);
        iv_control_play_small_controller.setOnClickListener(this);
        iv_next_play_small_controller.setOnClickListener(this);
        iv_play_list_play_small_controller.setOnClickListener(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        musicPlayerManager.removeOnMusicPlayerListener(this);
        playListManager.removePlayListListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.ll_play_small_container_container:   //全局歌曲播放栏
                startActivity(MusicPlayerActivity.class);
                break;
            case R.id.iv_control_play_small_controller:    //暂停或播放
                playOrPause();
                break;
            case R.id.iv_next_play_small_controller:     //下一首
                Song nextSong = playListManager.next();
                playListManager.play(nextSong);
                break;
            case R.id.iv_play_list_play_small_controller:   //播放列表
                showPlayListDialogFragment();
                break;
        }
    }

    private void playOrPause(){
        if (musicPlayerManager.isPlaying()){
            pause();
        }else{
            play();
        }
    }

    /**
     * 歌曲开始播放
     */
    private void play(){
        playListManager.resume();
    }

    /**
     * 歌曲暂停播放
     */
    private void pause(){
        playListManager.pause();
    }

    /**
     * 展示歌曲播放列表Dialog Fragment
     */
    private void showPlayListDialogFragment(){
        playListDialog = new PlayListDialogFragment();
        playListDialog.setCurrentSong(playListManager.getPlayData());
        playListDialog.setData(playListManager.getPlayList());

        playListDialog.setOnItemClickListener(new BaseRecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseRecyclerViewAdapter.ViewHolder holder, int position) {
                playListDialog.dismiss();
                playListManager.play(playListManager.getPlayList().get(position));
                playListDialog.setCurrentSong(playListManager.getPlayData());
                playListDialog.notifyDataSetChanged();
            }
        });

        playListDialog.setOnRemoveClickListener(new PlayListAdapter.OnRemoveClickListener() {
            @Override
            public void onRemoveClick(int position) {
                Song currentSong = playListManager.getPlayList().get(position);
                playListManager.delete(currentSong);
                playListDialog.removeData(position);
                currentSong = playListManager.getPlayData();
                if (currentSong == null){
                    playListManager.destroy();
                    finish();
                }else{
                    playListDialog.setCurrentSong(currentSong);
                }

            }
        });

        playListDialog.show(getSupportFragmentManager(), "dialog");
    }

    @Override
    public void onPrepared(MediaPlayer mediaPlayer, Song song) {
        setFirstData(song);
    }

    @Override
    public void onProgress(long progress, long total) {
        progress_bar_play_small_controller.setProgress((int) progress);
        tv_single_line_lyric_play_small_controller.show(progress);
    }

    @Override
    public void onPaused(Song data) {
        //通过setSelected,将图片切换到暂停状态下的图片
        iv_control_play_small_controller.setSelected(false);
    }

    @Override
    public void onPlaying(Song data) {
        //通过setSelected,将图片切换到播放状态下的图片
        iv_control_play_small_controller.setSelected(true);
    }

    @Override
    public void onCompletion(MediaPlayer mediaPlayer) {

    }

    @Override
    public void onError(MediaPlayer mediaPlayer, int what, int extra) {

    }


    @Override
    public void onDataReady(Song song) {
        setLyric(song.getLyric());
    }

    /**
     * 解析歌词，并显示一行歌词
     * @param lyric
     */
    private void setLyric(Lyric lyric){
        LyricsParser parser = LyricsParser.parse(lyric.getStyle(), lyric.getContent());
        parser.parse();
        if (parser.getConvertedLyric() != null){
            tv_single_line_lyric_play_small_controller.setData(parser.getConvertedLyric());
        }
    }


}
