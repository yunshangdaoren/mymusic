package com.luckyliuqs.mymusic.callback;

import java.lang.ref.SoftReference;

import cn.woblog.android.downloader.callback.AbsDownloadListener;
import cn.woblog.android.downloader.exception.DownloadException;

/**
 * 歌曲下载监听器：将所有回调都调用onRefresh方法
 */
public abstract class MyDownloadListener extends AbsDownloadListener {

    public MyDownloadListener(){
        super();
    }

    public MyDownloadListener(SoftReference<Object> useTag){
        super(useTag);
    }

    @Override
    public void onStart() {
        onRefresh();
    }

    /**
     * onRefresh
     */
    public abstract void onRefresh();

    @Override
    public void onWaited() {
        onRefresh();
    }

    @Override
    public void onPaused() {
        onRefresh();
    }

    @Override
    public void onDownloading(long progress, long size) {
        onRefresh();
    }

    @Override
    public void onRemoved() {
        onRefresh();
    }

    @Override
    public void onDownloadSuccess() {
        onRefresh();
    }

    @Override
    public void onDownloadFailed(DownloadException e) {
        onRefresh();
    }
}
