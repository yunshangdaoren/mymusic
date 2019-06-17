package com.luckyliuqs.mymusic.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.luckyliuqs.mymusic.R;
import com.luckyliuqs.mymusic.adapter.BaseRecyclerViewAdapter;
import com.luckyliuqs.mymusic.adapter.SongListAdapter;
import com.luckyliuqs.mymusic.domain.SongList;

import java.util.List;

/**
 * 选择歌曲收藏到歌单dialog fragment
 */
public class SelectSongListDialogFragment extends BottomSheetDialogFragment {
    private RecyclerView rv_select_song_list;
    private SongListAdapter songListAdapter;
    private List<SongList> songListList;
    private OnSelectSongListListener onSelectSongListListener;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_select_song_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        rv_select_song_list = view.findViewById(R.id.rv_select_song_list);
        rv_select_song_list.setHasFixedSize(true);

        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        rv_select_song_list.setLayoutManager(linearLayoutManager);
        DividerItemDecoration itemDecoration = new DividerItemDecoration(getActivity(), RecyclerView.VERTICAL);
        rv_select_song_list.addItemDecoration(itemDecoration);

        songListAdapter = new SongListAdapter(getActivity(), R.layout.item_me_song_list);
        songListAdapter.setOnItemClickListener(new BaseRecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseRecyclerViewAdapter.ViewHolder holder, int position) {
                //关闭dialog fragment
                dismiss();
                //歌单点击事件
                onSelectSongListListener.onSelectSongListClick(songListList.get(position));
            }
        });

        rv_select_song_list.setAdapter(songListAdapter);
        songListAdapter.setData(songListList);

    }

    /**
     * show方法
     * @param fragmentManager
     * @param songListList
     * @param onSelectSongListListener
     */
    public static void show(FragmentManager fragmentManager, List<SongList> songListList, OnSelectSongListListener onSelectSongListListener){
        SelectSongListDialogFragment selectSongListDialogFragment = new SelectSongListDialogFragment();
        selectSongListDialogFragment.setData(songListList);
        selectSongListDialogFragment.setListener(onSelectSongListListener);
        selectSongListDialogFragment.show(fragmentManager, "SelectSongListDialogFragment");
    }

    private void setData(List<SongList> songListList){
        this.songListList = songListList;
    }

    private void setListener(OnSelectSongListListener onSelectSongListListener){
        this.onSelectSongListListener = onSelectSongListListener;
    }

    /**
     * 歌单点击事件接口
     */
    public interface OnSelectSongListListener{

        /**
         * 歌单点击事件
         * @param songList
         */
        void onSelectSongListClick(SongList songList);
    }

}

























