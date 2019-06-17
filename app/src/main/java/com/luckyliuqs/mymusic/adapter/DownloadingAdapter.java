package com.luckyliuqs.mymusic.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.luckyliuqs.mymusic.R;
import com.luckyliuqs.mymusic.Util.FIleUtil;
import com.luckyliuqs.mymusic.Util.OrmUtil;
import com.luckyliuqs.mymusic.callback.MyDownloadListener;
import com.luckyliuqs.mymusic.domain.Song;
import com.luckyliuqs.mymusic.domain.event.DownloadStatusChanged;

import org.greenrobot.eventbus.EventBus;

import java.lang.ref.SoftReference;
import java.util.List;

import cn.woblog.android.downloader.callback.DownloadManager;
import cn.woblog.android.downloader.domain.DownloadInfo;

/**
 * 下载管理主页：正在下载Fragment页面Adapter
 */
public class DownloadingAdapter extends BaseRecyclerViewAdapter<DownloadInfo, DownloadingAdapter.ViewHolder> {
    private final OrmUtil ormUtil;
    private final DownloadManager downloadManager;

    public DownloadingAdapter(Context context, OrmUtil ormUtil, final DownloadManager downloadManager) {
        super(context);
        this.ormUtil = ormUtil;
        this.downloadManager = downloadManager;

        //某个正在下载歌曲的点击事件
        setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(BaseRecyclerViewAdapter.ViewHolder holder, int position) {
                DownloadInfo downloadInfo = getData(position);
                switch (downloadInfo.getStatus()){
                    case DownloadInfo.STATUS_NONE:
                    case DownloadInfo.STATUS_PAUSED:
                    case DownloadInfo.STATUS_ERROR:
                        //如果歌曲处于none、暂停、出错状态，点击后则开始下载
                        downloadManager.resume(downloadInfo);
                        break;
                    case DownloadInfo.STATUS_DOWNLOADING:
                    case DownloadInfo.STATUS_PREPARE_DOWNLOAD:
                    case DownloadInfo.STATUS_WAIT:
                        //如果歌曲处于正在下载、准备下载、等待下载状态，点击后则暂停下载
                        downloadManager.pause(downloadInfo);
                        break;

                }
            }
        });
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        //加载布局
        return new ViewHolder(getInflater().inflate(R.layout.item_downloading, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        DownloadInfo downloadInfo = getData(position);
        //获取下载业务数据：如歌曲名称
        Song song = ormUtil.findSongById(downloadInfo.getId());
        holder.bindBaseData(song);

        //下载数据
        holder.bindData(downloadInfo, position);
    }

    public class ViewHolder extends BaseRecyclerViewAdapter.ViewHolder{
        /**
         * 正在下载的歌曲的标题信息
         */
        private final TextView tv_item_downloading_title;

        /**
         * 下载暂停描述信息
         */
        private final TextView tv_item_downloading_pause_info;

        /**
         * 下载进度值描述信息
         */
        private final TextView tv_item_downloading_progress;

        /**
         * 删除正在下载的歌曲图标
         */
        private final ImageView iv_item_downloading_delete;

        /**
         * 下载进度
         */
        private final ProgressBar pb_item_downloading;

        private final LinearLayout ll_item_downloading_container;
        /**
         *
         */
        private DownloadInfo downloadInfo;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_item_downloading_title = itemView.findViewById(R.id.tv_item_downloading_title);
            tv_item_downloading_pause_info = itemView.findViewById(R.id.tv_item_downloading_pause_info);
            tv_item_downloading_progress = itemView.findViewById(R.id.tv_item_downloading_progress);
            iv_item_downloading_delete = itemView.findViewById(R.id.iv_item_downloading_delete);
            pb_item_downloading = itemView.findViewById(R.id.pb_item_downloading);
            ll_item_downloading_container = itemView.findViewById(R.id.ll_item_downloading_container);
        }

        //绑定下载业务数据
        public void bindBaseData(Song song){
            //设置下载歌曲名称
            tv_item_downloading_title.setText(song.getTitle());
        }

        //下载数据
        public void bindData(final DownloadInfo downloadInfo, int position){
            this.downloadInfo = downloadInfo;
            //设置回调
            downloadInfo.setDownloadListener(new MyDownloadListener(new SoftReference<Object>(ViewHolder.this)) {
                @Override
                public void onRefresh() {
                    if (getUserTag() != null && getUserTag().get() != null){
                        ViewHolder viewHolder = (ViewHolder) getUserTag().get();
                        viewHolder.refresh();
                    }
                }
            });

            refresh();

            //删除按钮点击事件
            iv_item_downloading_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    downloadManager.remove(downloadInfo);
                }
            });
        }

        private void refresh(){
            switch (downloadInfo.getStatus()){
                case DownloadInfo.STATUS_PAUSED:
                case DownloadInfo.STATUS_ERROR:
                    //如果处于下载暂停或者出错状态，下载进度条gone，下载暂停描述文字visible
                    ll_item_downloading_container.setVisibility(View.GONE);
                    tv_item_downloading_pause_info.setVisibility(View.VISIBLE);
                    tv_item_downloading_pause_info.setText(R.string.click_download);

                    try{
                        //获取下载进度值
                        pb_item_downloading.setProgress((int) (downloadInfo.getProgress() * 100.0 / downloadInfo.getSize()));
                    }catch (Exception e){
                        e.printStackTrace();
                    }

                    //设置下载进度描述信息
                    tv_item_downloading_progress.setText(context.getString(R.string.download_progress, FIleUtil.formatFileSize(downloadInfo.getProgress()), FIleUtil.formatFileSize(downloadInfo.getSize())));
                    break;
                case DownloadInfo.STATUS_DOWNLOADING:
                case DownloadInfo.STATUS_PREPARE_DOWNLOAD:
                    //如果处于正在下载或准备下载状态，下载进度条visible，下载暂停描述文字gone
                    ll_item_downloading_container.setVisibility(View.VISIBLE);
                    tv_item_downloading_pause_info.setVisibility(View.GONE);

                    try {
                        pb_item_downloading.setProgress((int) (downloadInfo.getProgress() * 100.0 / downloadInfo.getSize()));
                    }catch (Exception e){
                        e.printStackTrace();
                    }

                    //设置下载进度描述信息
                    tv_item_downloading_progress.setText(context.getString(R.string.download_progress, FIleUtil.formatFileSize(downloadInfo.getProgress()), FIleUtil.formatFileSize(downloadInfo.getSize())));
                    break;
                case DownloadInfo.STATUS_COMPLETED:
                    //通知下载成功
                    publicDownloadSuccessStatus();

                    //从下载界面中移除当前下载完成的歌曲
                    removeData(downloadInfo);
                    notifyDataSetChanged();
                case DownloadInfo.STATUS_REMOVED:
                    ////从下载界面中移除当前正在下载的歌曲
                    removeData(downloadInfo);
                    notifyDataSetChanged();
                    break;
                case DownloadInfo.STATUS_WAIT:
                    //如果处于等待下载状态，下载进度条Visible，下载暂停描述文字Gone，下载进度描述信息为等待下载
                    ll_item_downloading_container.setVisibility(View.VISIBLE);
                    tv_item_downloading_pause_info.setVisibility(View.GONE);
                    tv_item_downloading_progress.setText(R.string.wait_download);
                    pb_item_downloading.setProgress(0);
                    break;
            }
        }

        /**
         * 通知下载成功
         */
        private void publicDownloadSuccessStatus(){
            //publish download success info
            EventBus.getDefault().post(new DownloadStatusChanged(downloadInfo));
        }
    }
}





























