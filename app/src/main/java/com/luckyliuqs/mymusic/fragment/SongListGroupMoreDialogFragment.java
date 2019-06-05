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

import com.luckyliuqs.mymusic.R;

/**
 * 歌单分组更多：创建歌单和歌单管理
 */
public class SongListGroupMoreDialogFragment extends BottomSheetDialogFragment implements View.OnClickListener {
    private OnSongListGroupMoreListener onSongListGroupMoreListener;
    /**
     * 创建歌单
     */
    private LinearLayout ll_create_song_list;

    /**
     * 歌单管理
     */
    private LinearLayout ll_song_list_manager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_dialog_song_list_group_more, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ll_create_song_list = view.findViewById(R.id.ll_create_song_list);
        ll_song_list_manager = view.findViewById(R.id.ll_song_list_manager);

        initData();
        initListener();
    }

    private void initData(){

    }

    private void initListener(){
        ll_create_song_list.setOnClickListener(this);
        ll_song_list_manager.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.ll_create_song_list:
                onSongListGroupMoreListener.onCreateSongList();;
                break;
            case R.id.ll_song_list_manager:
                onSongListGroupMoreListener.onManagerSongList();
        }
    }

    public static void show(FragmentManager fragmentManager, OnSongListGroupMoreListener onSongListGroupMoreListener){
        SongListGroupMoreDialogFragment songListGroupMoreDialogFragment = new SongListGroupMoreDialogFragment();
        songListGroupMoreDialogFragment.setOnSongListGroupMoreListener(onSongListGroupMoreListener);
        songListGroupMoreDialogFragment.show(fragmentManager, "SongListGroupMoreDialogFragment");
    }

    /**
     * 设置监听器
     * @param onSongListGroupMoreListener
     */
    private void setOnSongListGroupMoreListener(OnSongListGroupMoreListener onSongListGroupMoreListener){
        this.onSongListGroupMoreListener = onSongListGroupMoreListener;
    }

    public interface OnSongListGroupMoreListener{
        /**
         * 创建歌单
         */
        void onCreateSongList();

        /**
         * 歌单管理
         */
        void onManagerSongList();
    }
}
