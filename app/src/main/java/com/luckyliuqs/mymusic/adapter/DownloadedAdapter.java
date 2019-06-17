package com.luckyliuqs.mymusic.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.luckyliuqs.mymusic.R;
import com.luckyliuqs.mymusic.Util.OrmUtil;
import com.luckyliuqs.mymusic.domain.Song;
import com.luckyliuqs.mymusic.fragment.SongMoreDialogFragment;
import com.luckyliuqs.mymusic.manager.PlayListManager;

import cn.woblog.android.downloader.callback.DownloadManager;
import cn.woblog.android.downloader.domain.DownloadInfo;

/**
 * 下载管理主页：下载完成Fragment页面Adapter
 */
public class DownloadedAdapter extends BaseRecyclerViewAdapter<DownloadInfo, DownloadedAdapter.ViewHolder> {
    private final OrmUtil ormUtil;
    private final DownloadManager downloadManager;
    private final FragmentManager fragmentManager;
    private final PlayListManager playListManager;

    public DownloadedAdapter(Context context, OrmUtil ormUtil, FragmentManager fragmentManager, DownloadManager downloadManager, PlayListManager playListManager) {
        super(context);
        this.ormUtil = ormUtil;
        this.fragmentManager = fragmentManager;
        this.downloadManager = downloadManager;
        this.playListManager = playListManager;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new ViewHolder(getInflater().inflate(R.layout.item_song_detail, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        DownloadInfo downloadInfo = getData(position);

        //获取业务数据，如歌曲名称
        Song song = ormUtil.findSongById(downloadInfo.getId());
        holder.bindBaseData(song, downloadInfo, position);
    }

    public class ViewHolder extends BaseRecyclerViewAdapter.ViewHolder{
        /**
         * 歌曲序号
         */
        private final TextView tv_item_song_position;

        /**
         * 歌曲名称
         */
        private final TextView tv_item_song_title;

        /**
         * 歌曲已下载图标
         */
        private final ImageView iv_item_song_downloaded;

        /**
         * 歌曲描述信息：歌手-专辑
         */
        private final TextView tv_item_song_info;

        /**
         * 歌曲更多图标
         */
        private final ImageView iv_item_song_more;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_item_song_position = itemView.findViewById(R.id.tv_item_song_position);
            tv_item_song_title = itemView.findViewById(R.id.tv_item_song_title);
            iv_item_song_downloaded = itemView.findViewById(R.id.iv_item_song_downloaded);
            tv_item_song_info = itemView.findViewById(R.id.tv_item_song_info);
            iv_item_song_more = itemView.findViewById(R.id.iv_item_song_more);

        }

        public void bindBaseData(final  Song song, final DownloadInfo downloadInfo, int position){
            //设置歌曲序号
            tv_item_song_position.setText(String.valueOf(position + 1));
            //设置歌曲名称
            tv_item_song_title.setText(song.getTitle());
            //设置歌曲描述信息：歌手-专辑
            tv_item_song_info.setText(song.getArtist_name() +" - " + song.getAlbum_title());

            //设置当前播放歌曲文字显示颜色
            if (song.equals(playListManager.getPlayData())) {
                //将正在播放的歌曲颜色变为红色
                tv_item_song_title.setTextColor(context.getResources().getColor(R.color.main_color));
            }else{
                //将正在播放的歌曲颜色变为黑色
                tv_item_song_title.setTextColor(context.getResources().getColor(R.color.text));
            }

            //判断是否下载了，从而是否显示已下载图标
            if (downloadInfo != null && downloadInfo.getStatus() == DownloadInfo.STATUS_COMPLETED){
                //如果该歌曲已经下载，显示已下载图标
                iv_item_song_downloaded.setVisibility(View.VISIBLE);
            }else{
                //如果该歌曲还未下载，隐藏已下载图标
                iv_item_song_downloaded.setVisibility(View.GONE);
            }

            iv_item_song_more.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SongMoreDialogFragment.show(fragmentManager, song, true, new SongMoreDialogFragment.OnMoreListener() {
                        @Override
                        public void onCollectionClick(Song song) {

                        }

                        @Override
                        public void onDownloadClick(Song song) {

                        }

                        @Override
                        public void onDeleteClick(Song song) {
                            //从下载任务中删除
                            downloadManager.remove(downloadInfo);

                            //从当前已下载界面中删除
                            removeData(downloadInfo);
                            notifyDataSetChanged();
                        }
                    });
                }
            });

        }
    }
}
