package com.luckyliuqs.mymusic.adapter;

import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.view.View;

import com.luckyliuqs.mymusic.R;
import com.luckyliuqs.mymusic.domain.Song;
import com.luckyliuqs.mymusic.fragment.SongMoreDialogFragment;
import com.luckyliuqs.mymusic.manager.PlayListManager;

/**
 * 歌曲信息Adapter
 */
public class SongAdapter extends BaseQuickRecyclerViewAdapter<Song>{
    private final PlayListManager playListManager;
    private final FragmentManager fragmentManager;
    private OnSongListener onSongListener;


    /**
     * 是否是用户自己创建的歌单
     */
    private boolean isMySheet;

    public SongAdapter(Context context, int layoutId, FragmentManager fragmentManager, PlayListManager playListManager) {
        super(context, layoutId);
        this.playListManager = playListManager;
        this.fragmentManager = fragmentManager;
    }

    @Override
    protected void bindData(ViewHolder holder, int position, final Song data) {
        //设置歌曲序号
        holder.setText(R.id.tv_item_song_list_position, String.valueOf(position + 1));
        //设置歌曲名称
        holder.setText(R.id.tv_item_song_list_title, data.getTitle());
        //设置歌曲信息：歌手-歌曲
        holder.setText(R.id.tv_item_song_list_info, data.getArtist_name() + " - " + data.getAlbum_title());

        //设置歌曲文字颜色
        if (data.equals(playListManager.getPlayData())){
            //如果是当前播放的歌曲，则文字设置为红色
            holder.setTextColorsRes(R.id.tv_item_song_list_title, R.color.main_color);
        }else{
            //如果为非当前播放歌曲，则文字设置为黑色
            holder.setTextColorsRes(R.id.tv_item_song_list_title, R.color.text);
        }

        //歌曲更多信息图标点击监听事件
        holder.setOnClickListener(R.id.iv_item_song_list_more, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //如果歌单是用户自己创建的歌单，则显示删除歌曲选项
                SongMoreDialogFragment.show(fragmentManager, data, isMySheet, new SongMoreDialogFragment.OnMoreListener() {
                    @Override
                    public void onCollectionClick(Song song) {

                    }

                    @Override
                    public void onDownloadClick(Song song) {

                    }

                    @Override
                    public void onDeleteClick(Song song) {

                    }
                });

            }
        });
    }

    private void collectionSong(Song song){
        if (onSongListener != null){
            onSongListener.onCollectionClick(song);
        }
    }

    public void setIsMySheet(boolean isMySheet){
        this.isMySheet = isMySheet;
    }

    public void setOnSongListener(OnSongListener onSongListener){
        this.onSongListener = onSongListener;
    }

    /**
     * 歌曲操作监听接口
     */
    public interface OnSongListener{
        /**
         * 收藏歌曲
         * @param song
         */
        void onCollectionClick(Song song);

        /**
         * 下载歌曲
         * @param song
         */
        void onDownloadClick(Song song);

        /**
         * 从歌单中删除歌曲
         * @param song
         */
        void onDeleteClick(Song song);
    }
}





























