package com.luckyliuqs.mymusic.adapter;

import android.content.Context;
import android.support.annotation.ColorRes;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.List;


/**
 *
 * @param <D>
 */
public abstract class BaseQuickRecyclerViewAdapter<D> extends BaseRecyclerViewAdapter<D, BaseQuickRecyclerViewAdapter.ViewHolder > {
    /**
     * 布局layout资源ID
     */
    private final int layoutId;

    public BaseQuickRecyclerViewAdapter(Context context, @LayoutRes int layoutId) {
        super(context);
        this.layoutId = layoutId;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new BaseQuickRecyclerViewAdapter.ViewHolder(getInflater().inflate(layoutId, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull BaseQuickRecyclerViewAdapter.ViewHolder holder, final int position) {
        super.onBindViewHolder(holder, position);
        bindData(holder, position, getData(position));
    }

    protected abstract void bindData(ViewHolder holder, int position, D data);

    /**
     * ViewHolder类
     */
    public class ViewHolder extends BaseRecyclerViewAdapter.ViewHolder{
        /**
         * 用于保存item view，有更好的性能
         */
        private final SparseArray<View> itemViews = new SparseArray<>();

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }

        /**
         * get view
         * @param id
         * @param <T>
         * @return view
         */
        public <T extends View> T getView(@IdRes int id){
            View view = itemViews.get(id);
            if (view == null){
                view = findViewById(id);
                itemViews.put(id, view);
            }
            return (T) view;
        }

        /**
         * set value by Resource id
         * @param id
         * @param value
         * @return
         */
        public ViewHolder setText(@IdRes int id, CharSequence value){
            TextView textView = getView(id);
            textView.setText(value);
            return this;
        }

        /**
         * set value by String Resource id
         * @param id
         * @param value
         * @return
         */
        public ViewHolder setText(@IdRes int id, @StringRes int value){
            TextView textView = getView(id);
            textView.setText(value);
            return this;
        }

        /**
         * set image value by Resource id
         * @param id
         * @param value
         * @return
         */
        public ViewHolder setImageResource(@IdRes int id, int value){
            ImageView imageView = getView(id);
            imageView.setImageResource(value);
            return this;
        }

        /**
         * set visibility by Resource id
         * @param id
         * @param visibility
         * @return
         */
        public ViewHolder setVisibility(@IdRes int id, int visibility){
            View view = getView(id);
            view.setVisibility(visibility);
            return this;
        }

        /**
         * set text color by Resource id
         * @param id
         * @param resId
         */
        public void setTextColorsRes(@IdRes int id, @ColorRes int resId){
            TextView textView = getView(id);
            textView.setTextColor(textView.getResources().getColor(resId));
        }

        /**
         * set onClickListener by Resource id
         * @param id
         * @param onClickListener
         */
        public void setOnClickListener(@IdRes int id, View.OnClickListener onClickListener){
            View view = getView(id);
            view.setOnClickListener(onClickListener);
        }

        /**
         * set progress bar max value by Resource id
         * @param id
         * @param size
         */
        public void setMax(@IdRes int id, long size){
            ProgressBar progressBar = getView(id);
            progressBar.setMax((int) size);
        }

        /**
         * set progress value
         * @param id
         * @param progress
         */
        public void setProgress(@IdRes int id, long progress){
            ProgressBar progressBar = getView(id);
            progressBar.setProgress((int) progress);
        }

        //todo 可以添加更多类型的方法
    }
}
