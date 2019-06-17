package com.luckyliuqs.mymusic.fragment;

import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.luckyliuqs.mymusic.R;
import com.luckyliuqs.mymusic.adapter.DownloadingAdapter;

import java.util.List;

import cn.woblog.android.downloader.DownloadService;
import cn.woblog.android.downloader.callback.DownloadManager;
import cn.woblog.android.downloader.domain.DownloadInfo;

/**
 * 下载管理主页：正在下载Fragment页面
 */
public class DownloadingFragment extends BaseCommonFragment implements View.OnClickListener {
    private RecyclerView recyclerView;
    private DownloadingAdapter downloadingAdapter;
    private DownloadManager downloadManager;
    private LinearLayout ll_downloading_pause_or_resume_all;
    private TextView tv_downloading_pause_or_resume;
    private LinearLayout ll_downloading_delete_all;

    /**
     * 表示下载页面是否有歌曲处于正在下载状态，默认false
     */
    private boolean isDownloading = false;

    public static DownloadingFragment newInstance(){
        Bundle args = new Bundle();
        DownloadingFragment fragment = new DownloadingFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected View getLayoutView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_downloading, container, false);
    }

    @Override
    protected void initViews() {
        super.initViews();
        ll_downloading_pause_or_resume_all = findViewById(R.id.ll_downloading_pause_or_resume_all);
        tv_downloading_pause_or_resume = findViewById(R.id.tv_downloading_pause_or_resume);
        ll_downloading_delete_all = findViewById(R.id.ll_downloading_delete_all);

        recyclerView = findViewById(R.id.rv_downloading);
        recyclerView.setHasFixedSize(true);

        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getActivity(), RecyclerView.VERTICAL);
        recyclerView.addItemDecoration(dividerItemDecoration);
    }

    @Override
    protected void initDatas() {
        super.initDatas();
        downloadManager = DownloadService.getDownloadManager(getActivity().getApplicationContext());
        downloadingAdapter = new DownloadingAdapter(getMainActivity(), orm, downloadManager);
        recyclerView.setAdapter(downloadingAdapter);

        //获取所有正在下载的歌曲数据
        List<DownloadInfo> allDownloading = downloadManager.findAllDownloading();
        //设置所有正在下载的歌曲数据
        downloadingAdapter.setData(allDownloading);
        for (DownloadInfo downloadInfo : allDownloading){
            //如果有一个歌曲的状态是正在下载，则显示全部暂停下载
            if (downloadInfo.getStatus() == DownloadInfo.STATUS_DOWNLOADING || downloadInfo.getStatus() == DownloadInfo.STATUS_PREPARE_DOWNLOAD){
                isDownloading = true;
                break;
            }
        }

        //显示全部开始下载或全部暂停下载信息
        setPauseOrResumeButtonStatus();
    }

    /**
     * 显示全部开始下载或全部暂停下载信息
     */
    private void setPauseOrResumeButtonStatus(){
        if (isDownloading){
            //如果正在下载，显示全部暂停下载
            tv_downloading_pause_or_resume.setText(R.string.all_pause);
        }else{
            //如果暂停下载，显示全部开始下载
            tv_downloading_pause_or_resume.setText(R.string.all_resume);
        }
    }

    @Override
    public void initListener() {
        super.initListener();
        ll_downloading_pause_or_resume_all.setOnClickListener(this);
        ll_downloading_delete_all.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ll_downloading_pause_or_resume_all:
                //全部开始下载或全部暂停下载歌曲
                pauseOrResumeAll();
                break;
            case R.id.ll_downloading_delete_all:
                //删除全部下载任务歌曲
                deleteAll();
                break;
        }
    }

    /**
     * 全部开始下载或全部暂停下载歌曲
     */
    private void pauseOrResumeAll(){
        if (isDownloading){
            //全部暂停下载歌曲
            pauseAll();
            isDownloading = false;
        }else{
            //全部开始下载歌曲
            resumeAll();
            isDownloading = true;
        }

        //刷新下载状态信息
        setPauseOrResumeButtonStatus();
    }

    /**
     * 全部暂停下载歌曲
     */
    private void pauseAll(){
        List<DownloadInfo> downloadInfoList = downloadingAdapter.getDatas();
        for (DownloadInfo downloadInfo : downloadInfoList){
            //暂停下载
            downloadManager.pauseForce(downloadInfo);
        }
    }

    /**
     * 全部开始下载歌曲
     */
    private void resumeAll(){
        List<DownloadInfo> downloadInfoList = downloadingAdapter.getDatas();
        for (DownloadInfo downloadInfo : downloadInfoList){
            //开始下载
            downloadManager.resumeForce(downloadInfo);
        }
    }
    /**
     * 删除全部下载任务歌曲
     */
    private void deleteAll(){
        List<DownloadInfo> downloadInfoList = downloadingAdapter.getDatas();
        for (DownloadInfo downloadInfo : downloadInfoList){
            //移除下载任务歌曲
            downloadManager.remove(downloadInfo);
        }

        //清除下载页面所有下载歌曲数据
        downloadingAdapter.clearData();
    }

}






























