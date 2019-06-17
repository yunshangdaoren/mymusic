package com.luckyliuqs.mymusic.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.luckyliuqs.mymusic.R;
import com.luckyliuqs.mymusic.domain.Song;


/**
 * 点击歌曲更多信息图标后弹出的Dialog Fragment
 */
public class SongMoreDialogFragment extends BottomSheetDialogFragment implements View.OnClickListener {
    private OnMoreListener onMoreListener;

    private Song song;

    private TextView tv_song_more_title;
    private TextView tv_song_more_comment_count;
    private TextView tv_song_more_album;
    private TextView tv_song_more_artist;

    private LinearLayout ll_song_more_next_play;
    private LinearLayout ll_song_more_collection;
    private LinearLayout ll_song_more_download;
    private LinearLayout ll_song_more_comment;
    private LinearLayout ll_song_more_delete;

    private boolean isShowDelete;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_dialog_song_more, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        tv_song_more_title = view.findViewById(R.id.tv_song_more_title);
        tv_song_more_comment_count = view.findViewById(R.id.tv_song_more_comment_count);
        tv_song_more_album = view.findViewById(R.id.tv_song_more_album);
        tv_song_more_artist = view.findViewById(R.id.tv_song_more_artist);
        ll_song_more_next_play = view.findViewById(R.id.ll_song_more_next_play);
        ll_song_more_collection = view.findViewById(R.id.ll_song_more_collection);
        ll_song_more_download = view.findViewById(R.id.ll_song_more_download);
        ll_song_more_comment = view.findViewById(R.id.ll_song_more_comment);
        ll_song_more_delete = view.findViewById(R.id.ll_song_more_delete);

        if (isShowDelete){
            //如果歌单是用户自己创建的歌单，则显示歌曲删除选项
            ll_song_more_delete.setVisibility(View.VISIBLE);
        }

        initData();
        initListener();
    }

    private void initData(){
        //设置歌曲名称
        tv_song_more_title.setText(getContext().getResources().getString(R.string.song_detail, song.getTitle()));
        //设置歌曲评论数量
        tv_song_more_comment_count.setText(getContext().getResources().getString(R.string.comment_count, song.getComments_count()));
        //设置歌曲专辑
        tv_song_more_album.setText(getContext().getResources().getString(R.string.album, song.getAlbum_title()));
        //设置歌曲歌手
        tv_song_more_artist.setText(getContext().getResources().getString(R.string.artist, song.getArtist_name()));
    }

    private void initListener(){
        ll_song_more_next_play.setOnClickListener(this);
        ll_song_more_collection.setOnClickListener(this);
        ll_song_more_download.setOnClickListener(this);
        ll_song_more_delete.setOnClickListener(this);
    }

    public static void show(FragmentManager fragmentManager, Song song, OnMoreListener onMoreListener){
        SongMoreDialogFragment.show(fragmentManager, song, false, onMoreListener);
    }

    public static void show(FragmentManager fragmentManager, Song song, boolean isShowDelete, OnMoreListener onMoreListener){
        SongMoreDialogFragment songMoreDialogFragment = new SongMoreDialogFragment();
        //赋值
        songMoreDialogFragment.setSong(song);
        songMoreDialogFragment.setShowDelete(isShowDelete);
        songMoreDialogFragment.setOnMoreListener(onMoreListener);
        //show
        songMoreDialogFragment.show(fragmentManager, "SongMoreDialogFragment");
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.ll_song_more_next_play:  //下一首播放
                //关闭dialog fragment
                this.dismiss();
                break;
            case R.id.ll_song_more_collection:   //收藏
                this.dismiss();
                if (onMoreListener != null){
                    onMoreListener.onCollectionClick(song);
                }
                break;
            case R.id.ll_song_more_download:   //下载
                this.dismiss();
                if (onMoreListener != null){
                    onMoreListener.onDownloadClick(song);
                }
                break;
            case R.id.ll_song_more_delete:    //删除
                this.dismiss();
                if (onMoreListener != null){
                    onMoreListener.onDeleteClick(song);
                }
                break;
        }
    }

    private void setShowDelete(boolean isShowDelete){
        this.isShowDelete = isShowDelete;
    }

    private void setSong(Song song){
        this.song = song;
    }

    /**
     * set listener
     * @param onMoreListener
     */
    private void setOnMoreListener(OnMoreListener onMoreListener){
        this.onMoreListener = onMoreListener;
    }

    public interface OnMoreListener{
        /**
         * 收藏歌曲到歌单中点击监听事件
         * @param song
         */
        void onCollectionClick(Song song);

        /**
         * 下载歌曲点击监听事件
         * @param song
         */
        void onDownloadClick(Song song);

        /**
         * 删除歌曲点击监听事件
         * @param song
         */
        void onDeleteClick(Song song);
    }
}
