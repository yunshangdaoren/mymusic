package com.luckyliuqs.mymusic.adapter;

import android.content.Context;
import android.view.View;

import com.luckyliuqs.mymusic.R;
import com.luckyliuqs.mymusic.domain.Song;

/**
 * 歌曲播放列表Adapter
 */
public class PlayListAdapter extends BaseQuickRecyclerViewAdapter<Song>{
    private OnRemoveClickListener onRemoveClickListener;
    /**
     * 当前歌曲
     */
    private Song currentSong;


    public PlayListAdapter(Context context, int layoutId) {
        super(context, layoutId);
    }

    @Override
    protected void bindData(ViewHolder holder, final int position, Song song) {
        holder.setText(R.id.tv_title, String.format("%s - %s", song.getTitle(), song.getArtist_name()));
        if (song.equals(currentSong)) {
            holder.setTextColorsRes(R.id.tv_title, R.color.main_color);
        }else{
            holder.setTextColorsRes(R.id.tv_title, R.color.text);
        }

        //歌曲播放列表歌曲点击删除事件
        holder.setOnClickListener(R.id.iv_remove, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onRemoveClickListener != null){
                    onRemoveClickListener.onRemoveClick(position);
                }
            }
        });

    }

    public void setOnRemoveClickListener(OnRemoveClickListener onRemoveClickListener){
        this.onRemoveClickListener = onRemoveClickListener;
    }

    public void setCurrentSong(Song currentSong){
        this.currentSong = currentSong;
    }

    /**
     * 删除歌曲监听器接口
     */
    public interface OnRemoveClickListener{
        //歌曲播放列表歌曲点击删除事件
        void onRemoveClick(int position);
    }
}
