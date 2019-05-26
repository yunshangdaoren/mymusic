package com.luckyliuqs.mymusic.activity;

import android.media.MediaPlayer;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.luckyliuqs.mymusic.R;
import com.luckyliuqs.mymusic.domain.Song;
import com.luckyliuqs.mymusic.listener.OnMusicPlayerListener;
import com.luckyliuqs.mymusic.listener.PlayListListener;
import com.luckyliuqs.mymusic.manager.MusicPlayerManager;
import com.luckyliuqs.mymusic.manager.PlayListManager;

import java.util.List;

public class BaseMusicPlayerActivity extends BaseTitleActivity implements OnMusicPlayerListener, PlayListManager, View.OnClickListener {
    private LinearLayout ll_play_small_container;
    protected ImageView iv_icon_small_controller;
    protected ImageView iv_play_small_controller;
    protected ImageView iv_play_list_small_controller;
    protected ImageView iv_next_small_controller;
    protected TextView tv_title_small_controller;
    //protected LyricSingleLineView tv_info_small_controller;
    protected ProgressBar pb_progress_small;

    protected PlayListManager playListManager;
    protected MusicPlayerManager musicPlayerManager;
    //private PlayListDialogFragment playListDialog;


    @Override
    protected void initViews() {
        super.initViews();


    }

    @Override
    public void onClick(View view) {

    }

    @Override
    public void onPrepared(MediaPlayer mediaPlayer, Song data) {

    }

    @Override
    public void onProgress(long progress, long total) {

    }

    @Override
    public void onPaused(Song data) {

    }

    @Override
    public void onPlaying(Song data) {

    }

    @Override
    public void onCompletion(MediaPlayer mediaPlayer) {

    }

    @Override
    public void onError(MediaPlayer mediaPlayer, int what, int extra) {

    }

    @Override
    public List<Song> getPlayList() {
        return null;
    }

    @Override
    public void setPlayList(List<Song> songList) {

    }

    @Override
    public void play(Song song) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void delete(Song song) {

    }

    @Override
    public Song getPlayData() {
        return null;
    }

    @Override
    public Song next() {
        return null;
    }

    @Override
    public Song previous() {
        return null;
    }

    @Override
    public int getLoopModel() {
        return 0;
    }

    @Override
    public int changeLoopModel() {
        return 0;
    }

    @Override
    public void addPlayListListener(PlayListListener playListListener) {

    }

    @Override
    public void removePlayListListener(PlayListListener playListListener) {

    }

    @Override
    public void destroy() {

    }

    @Override
    public void nextPlay(Song song) {

    }
}
