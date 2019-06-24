package com.luckyliuqs.mymusic.adapter;

import android.content.Context;
import android.view.View;

import com.luckyliuqs.mymusic.R;
import com.luckyliuqs.mymusic.domain.SearchHistory;

/**
 * 搜索历史Adapter
 */
public class SearchHistoryAdapter extends BaseQuickRecyclerViewAdapter<SearchHistory> {
    private OnSearchHistoryDeleteListener onSearchHistoryDeleteListener;

    public SearchHistoryAdapter(Context context, int layoutId) {
        super(context, layoutId);
    }

    @Override
    protected void bindData(ViewHolder holder, final int position, final SearchHistory searchHistory) {
        holder.setText(R.id.tv_title_search, searchHistory.getContent());

        holder.setOnClickListener(R.id.iv_delete_search, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onSearchHistoryDeleteListener != null){
                    onSearchHistoryDeleteListener.onDeleteClick(position, searchHistory);
                }
            }
        });
    }

    public void setOnSearchHistoryRemoveListener(OnSearchHistoryDeleteListener onSearchHistoryDeleteListener) {
        this.onSearchHistoryDeleteListener = onSearchHistoryDeleteListener;
    }

    public interface OnSearchHistoryDeleteListener{
        /**
         * 删除搜索历史图标点击事件
         * @param position
         * @param data
         */
        void onDeleteClick(int position, final SearchHistory data);
    }
}
