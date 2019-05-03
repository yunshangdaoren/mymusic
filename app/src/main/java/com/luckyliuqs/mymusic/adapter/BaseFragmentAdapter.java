package com.luckyliuqs.mymusic.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Fragment Adapter基类
 */
public abstract class BaseFragmentAdapter<T> extends FragmentPagerAdapter {
    /**
     * 定义上下文Context对象
     */
    protected final Context context;
    /**
     * 创建ArrayList储存泛型数据资源
     */
    protected  final List<T> datas = new ArrayList<T>();

    public BaseFragmentAdapter(Context context, FragmentManager fm){
        super(fm);
        this.context = context;
    }

    /**
     * 设置数据
     * @param dataList
     */
    public void setDatas(List<T> dataList){
        if(dataList != null && dataList.size() > 0){
            datas.clear();
            datas.addAll(dataList);
            //通知数据已发生改变
            notifyDataSetChanged();
        }
    }

    /**
     * 添加数据
     * @param dataList
     */
    public void addDatas(List<T> dataList){
        if(dataList != null && dataList.size() > 0){
            datas.addAll(dataList);
            notifyDataSetChanged();
        }
    }

    /**
     * 获取指定下标的数据
     * @param position
     * @return
     */
    public T getData(int position){
        return datas.get(position);
    }

    /**
     * 返回数据的总数
     * @return
     */
    @Override
    public int getCount() {
        return datas.size();
    }

}
