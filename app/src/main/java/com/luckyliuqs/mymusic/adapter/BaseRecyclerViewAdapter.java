package com.luckyliuqs.mymusic.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * RecyclerView Adapter抽象基类
 * @param <D>
 * @param <VH>
 */
public abstract class BaseRecyclerViewAdapter<D, VH extends BaseRecyclerViewAdapter.ViewHoler> extends RecyclerView.Adapter<VH> {
    protected final Context context;
    private final LayoutInflater inflater;
    private List<D> datas = new ArrayList<D>();
    private OnItemClickListener onItemClickListener;
    private OnItemLongClickListener onItemLongClickListener;

    public BaseRecyclerViewAdapter(Context context){
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener){
        this.onItemClickListener = onItemClickListener;
    }

    public void setOnItemLongClickListener(OnItemLongClickListener onItemLongClickListener){
        this.onItemLongClickListener = onItemLongClickListener;
    }

    public List<D> getDatas(){
        return datas;
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull final VH holder, final int position) {
        if (onItemClickListener != null){
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickListener.onItemClick(holder, position);
                }
            });
        }

        if (onItemLongClickListener != null){
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {

                @Override
                public boolean onLongClick(View v) {
                    return onItemLongClickListener.onItemLongClick(holder, position);
                }
            });
        }
    }

    //返回子item数量
    @Override
    public int getItemCount() {
        return datas.size();
    }

    //返回子item的ID
    @Override
    public long getItemId(int position) {
        return position;
    }

    //返回LayoutInflater实例
    public LayoutInflater getInflater(){
        return inflater;
    }

    //在指定index后添加data集合
    public void addData(int index, List<D> data){
        this.datas.addAll(index, data);
        notifyDataSetChanged();
    }

    //添加data集合
    public void addData(List<D> data){
        this.datas.addAll(data);
        notifyDataSetChanged();
    }

    //删除原有的data集合，添加新的data集合
    public void setData(List<D> data){
        this.datas.clear();
        this.datas.addAll(data);
        notifyDataSetChanged();;
    }

    //添加一个data
    public void addData(D data){
        datas.add(data);
        notifyItemInserted(datas.size() - 1);
    }

    //在指定下标后添加一个data
    public void addData(int index, D data){
        datas.add(index, data);
        notifyItemInserted(index);
    }

    //移除指定下标的data
    public void removeData(int index){
        datas.remove(index);
        notifyDataSetChanged();
    }

    //移除指定data
    public void removeData(D d){
        datas.remove(d);
        notifyDataSetChanged();
    }

    //得到指定下标的data
    public D getData(int position) {
        return datas.get(position);
    }

    public int setSpanSizeLookup(int position) {
        return 1;
    }

    //清除掉所有的data
    public void clearData() {
        datas.clear();
        notifyDataSetChanged();
    }

    //item点击事件接口类
    public interface OnItemClickListener{
        void onItemClick(BaseRecyclerViewAdapter.ViewHoler holder, int position);
    }

    //item长按点击事件接口类
    public interface OnItemLongClickListener{
        boolean onItemLongClick(BaseRecyclerViewAdapter.ViewHoler holder, int position);
    }


    public abstract class ViewHoler extends RecyclerView.ViewHolder{

        public ViewHoler(@NonNull View itemView) {
            super(itemView);
        }

        public final <T extends View> T findViewById(int id){
            return itemView.findViewById(id);
        }
    }
}













