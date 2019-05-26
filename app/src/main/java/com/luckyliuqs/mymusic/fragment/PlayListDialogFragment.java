package com.luckyliuqs.mymusic.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.jdsjlzx.recyclerview.LRecyclerView;
import com.github.jdsjlzx.recyclerview.LRecyclerViewAdapter;
import com.luckyliuqs.mymusic.R;
import com.luckyliuqs.mymusic.adapter.BaseRecyclerViewAdapter;
import com.luckyliuqs.mymusic.adapter.PlayListAdapter;
import com.luckyliuqs.mymusic.domain.Song;

import java.util.List;

/**
 * 歌曲播放列表Dialog fragment
 */
public class PlayListDialogFragment extends BottomSheetDialogFragment {
    private LRecyclerView rv;
    private PlayListAdapter adapter;
    private LinearLayout ll_loop_model_container;

    /**
     * 播放全部
     */
    private TextView tv_play_all;


    private TextView tv_count;

    /**
     * 收藏
     */
    private Button bt_collection;

    /**
     * 删除全部
     */
    private ImageView iv_delete_all;

    /**
     * 歌单歌曲列表
     */
    private List<Song> songList;
    private BaseRecyclerViewAdapter.OnItemClickListener onItemClickListener;
    private PlayListAdapter.OnRemoveClickListener onRemoveClickListener;

    /**
     * 当前播放的歌曲
     */
    private Song currentSong;
    private LRecyclerViewAdapter adapterWrapper;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_dialog_play_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        rv = view.findViewById(R.id.rv);
        rv.setHasFixedSize(true);

        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        rv.setLayoutManager(linearLayoutManager);

        //设置分割线
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getActivity(), RecyclerView.VERTICAL);
        rv.addItemDecoration(dividerItemDecoration);

        adapter = new PlayListAdapter(getActivity(), R.layout.item_play_list);
        adapter.setCurrentSong(currentSong);

        //歌曲列表歌曲点击事件
        adapter.setOnItemClickListener(new BaseRecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseRecyclerViewAdapter.ViewHolder holder, int position) {
                onItemClickListener.onItemClick(holder, position);
            }
        });

        adapter.setOnRemoveClickListener(onRemoveClickListener);

        adapterWrapper = new LRecyclerViewAdapter(adapter);
        adapterWrapper.addHeaderView(createHeaderView());

        rv.setAdapter(adapterWrapper);
        rv.setPullRefreshEnabled(false);
        rv.setLoadMoreEnabled(false);

        adapter.setData(songList);
    }


    private View createHeaderView(){
        View top = getLayoutInflater().inflate(R.layout.header_play_list, (ViewGroup)rv.getParent(), false);
        ll_loop_model_container = top.findViewById(R.id.ll_loop_model_container);
        tv_play_all = top.findViewById(R.id.tv_play_all);
        bt_collection = top.findViewById(R.id.bt_collection);
        iv_delete_all = top.findViewById(R.id.iv_delete_all);
        tv_count = top.findViewById(R.id.tv_count);

        return top;
    }

    public void setData(List<Song> songList){
        this.songList = songList;
        if (adapter != null){
            adapter.setData(songList);
        }
    }

    public void removeData(int index){
        if (adapter != null){
            adapter.removeData(index);
        }
    }

    public void setOnRemoveClickListener(PlayListAdapter.OnRemoveClickListener onRemoveClickListener){
        this.onRemoveClickListener = onRemoveClickListener;
    }

    public void setOnItemClickListener(BaseRecyclerViewAdapter.OnItemClickListener onItemClickListener){
        this.onItemClickListener = onItemClickListener;
    }

    public void setCurrentSong(Song currentSong){
        this.currentSong = currentSong;
        if (adapter != null){
            adapter.setCurrentSong(currentSong);
        }
    }

    public void notifyDataSetChanged(){
        adapter.notifyDataSetChanged();
    }


}

























