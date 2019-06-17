package com.luckyliuqs.mymusic.adapter;

import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.View;

import com.luckyliuqs.mymusic.R;
import com.luckyliuqs.mymusic.Util.ToastUtil;
import com.luckyliuqs.mymusic.domain.Song;
import com.luckyliuqs.mymusic.fragment.SongMoreDialogFragment;
import com.luckyliuqs.mymusic.manager.PlayListManager;

import cn.woblog.android.downloader.callback.DownloadManager;
import cn.woblog.android.downloader.domain.DownloadInfo;

/**
 * 歌曲信息Adapter
 */
public class SongAdapter extends BaseQuickRecyclerViewAdapter<Song>{
    private final PlayListManager playListManager;
    private final FragmentManager fragmentManager;
    private OnSongListener onSongListener;
    private DownloadManager downloadManager;


    /**
     * 是否是用户自己创建的歌单
     */
    private boolean isMySheet;

    public SongAdapter(Context context, int layoutId, FragmentManager fragmentManager, PlayListManager playListManager, DownloadManager downloadManager) {
        super(context, layoutId);
        this.playListManager = playListManager;
        this.fragmentManager = fragmentManager;
        this.downloadManager = downloadManager;
    }

    @Override
    protected void bindData(ViewHolder holder, int position, final Song song) {
        //设置歌曲序号
        holder.setText(R.id.tv_item_song_position, String.valueOf(position + 1));
        //设置歌曲名称
        holder.setText(R.id.tv_item_song_title, song.getTitle());
        //设置歌曲信息：歌手-歌曲
        holder.setText(R.id.tv_item_song_info, song.getArtist_name() + " - " + song.getAlbum_title());

        //设置当前播放歌曲文字显示颜色
        if (song.equals(playListManager.getPlayData())){
            //如果是当前播放的歌曲，则文字设置为红色
            holder.setTextColorsRes(R.id.tv_item_song_title, R.color.main_color);
        }else{
            //如果为非当前播放歌曲，则文字设置为黑色
            holder.setTextColorsRes(R.id.tv_item_song_title, R.color.text);
        }

        //判断是否下载了，从而是否显示已下载图标
        DownloadInfo downloadInfo = downloadManager.getDownloadById(song.getId());
        if (downloadInfo != null && downloadInfo.getStatus() == DownloadInfo.STATUS_COMPLETED){
            //如果该歌曲已经下载，显示已下载图标
            holder.setVisibility(R.id.iv_item_song_downloaded, View.VISIBLE);
        }else{
            //如果该歌曲还未下载，隐藏已下载图标
            holder.setVisibility(R.id.iv_item_song_downloaded, View.GONE);
        }

        //歌曲更多信息图标点击监听事件
        holder.setOnClickListener(R.id.iv_item_song_more, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //如果歌单是用户自己创建的歌单，则显示删除歌曲选项
                SongMoreDialogFragment.show(fragmentManager, song, isMySheet, new SongMoreDialogFragment.OnMoreListener() {
                    /**
                     * 收藏该歌曲到指定的歌单中
                     * @param song
                     */
                    @Override
                    public void onCollectionClick(Song song) {
                        collectionSong(song);
                    }

                    /**
                     * 下载歌曲
                     * @param song
                     */
                    @Override
                    public void onDownloadClick(Song song) {
                        downloadSong(song);
                    }

                    /**
                     * 删除歌曲
                     * @param song
                     */
                    @Override
                    public void onDeleteClick(Song song) {
                        removeData(song);
                        //回调接口，从服务端删除
                        if (onSongListener != null){
                            onSongListener.onDeleteClick(song);
                        }
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

    private void downloadSong(Song song){
        if (onSongListener != null){
            onSongListener.onDownloadClick(song);
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
         * 收藏歌曲到指定歌单中
         * @param song
         */
        void onCollectionClick(Song song);

        /**
         * 下载歌曲
         * @param song
         */
        void onDownloadClick(Song song);

        /**
         * 删除歌曲
         * @param song
         */
        void onDeleteClick(Song song);
    }
}





























