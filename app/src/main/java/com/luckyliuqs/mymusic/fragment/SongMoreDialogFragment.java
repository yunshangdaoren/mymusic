package com.luckyliuqs.mymusic.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v4.app.FragmentManager;
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

    private TextView tv_song;
    private TextView tv_comment_count;
    private TextView tv_album;
    private TextView tv_artist;

    private LinearLayout ll_next_play;
    private LinearLayout ll_collection;
    private LinearLayout ll_download;
    private LinearLayout ll_comment;
    private LinearLayout ll_delete;

    private boolean isShowDelete;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_dialog_song_more, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        tv_song = view.findViewById(R.id.tv_song);
        tv_comment_count = view.findViewById(R.id.tv_comment_count);
        tv_album = view.findViewById(R.id.tv_album);
        tv_artist = view.findViewById(R.id.tv_artist);
        ll_next_play = view.findViewById(R.id.ll_next_play);
        ll_collection = view.findViewById(R.id.ll_collection);
        ll_download = view.findViewById(R.id.ll_download);
        ll_comment = view.findViewById(R.id.ll_comment);
        ll_delete = view.findViewById(R.id.ll_delete);

        if (isShowDelete){
            //如果歌单是用户自己创建的歌单，则显示歌曲删除选项
            ll_delete.setVisibility(View.VISIBLE);
        }

        initData();
        initListener();
    }

    private void initData(){
        //设置歌曲名称
        tv_song.setText(getContext().getResources().getString(R.string.song_detail, song.getTitle()));
        //设置歌曲评论数量
        tv_comment_count.setText(getContext().getResources().getString(R.string.comment_count, song.getComments_count()));
        //设置歌曲专辑
        tv_album.setText(getContext().getResources().getString(R.string.album, song.getAlbum_title()));
        //设置歌曲歌手
        tv_artist.setText(getContext().getResources().getString(R.string.artist, song.getArtist_name()));
    }

    private void initListener(){
        ll_next_play.setOnClickListener(this);
        ll_collection.setOnClickListener(this);
        ll_download.setOnClickListener(this);
        ll_delete.setOnClickListener(this);
    }

    public static void show(FragmentManager fragmentManager, Song song, OnMoreListener onMoreListener){
        SongMoreDialogFragment.show(fragmentManager, song, false, onMoreListener);
    }

    public static void show(FragmentManager fragmentManager, Song song, boolean isShowDelete, OnMoreListener onMoreListener){
        SongMoreDialogFragment songMoreDialogFragment = new SongMoreDialogFragment();
        songMoreDialogFragment.setSong(song);
        songMoreDialogFragment.setShowDelete(isShowDelete);
        songMoreDialogFragment.show(fragmentManager, "SortDialogFragment");
    }

    private void setShowDelete(boolean isShowDelete){
        this.isShowDelete = isShowDelete;
    }

    private void setSong(Song song){
        this.song = song;
    }

    @Override
    public void onClick(View v) {

    }



    public interface OnMoreListener{
        /**
         * 收藏到歌单点击监听事件
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
