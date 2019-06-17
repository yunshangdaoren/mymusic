package com.luckyliuqs.mymusic.adapter;

import android.content.Context;
import android.widget.ImageView;

import com.luckyliuqs.mymusic.R;
import com.luckyliuqs.mymusic.Util.ImageUtil;
import com.luckyliuqs.mymusic.activity.BaseActivity;
import com.luckyliuqs.mymusic.domain.SongList;

/**
 * 歌单adapter
 */
public class SongListAdapter extends BaseQuickRecyclerViewAdapter<SongList>{

    public SongListAdapter(Context context, int layoutId) {
        super(context, layoutId);
    }

    @Override
    protected void bindData(ViewHolder holder, int position, SongList songList) {
        ImageUtil.show((BaseActivity) context, (ImageView) holder.getView(R.id.iv_icon), songList.getBanner());
        holder.setText(R.id.tv_title, songList.getTitle());
        holder.setText(R.id.tv_count, context.getResources().getString(R.string.song_count, songList.getSongs_count()));
    }
}
