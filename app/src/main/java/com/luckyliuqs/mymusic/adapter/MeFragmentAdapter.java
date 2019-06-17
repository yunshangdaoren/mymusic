package com.luckyliuqs.mymusic.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.luckyliuqs.mymusic.R;
import com.luckyliuqs.mymusic.Util.ImageUtil;
import com.luckyliuqs.mymusic.activity.BaseActivity;
import com.luckyliuqs.mymusic.domain.MeFragmentUI;
import com.luckyliuqs.mymusic.domain.SongList;

import java.util.ArrayList;
import java.util.List;

/**
 * 我的音乐界面Adapter类
 */
public class MeFragmentAdapter extends BaseExpandableListAdapter {
    /**
     * 监听器
     */
    private OnMeFragmentListener onMeFragmentListener;
    private final Context context;
    private final LayoutInflater inflater;

    /**
     * 储存创建的歌单和收藏的歌单列表数据
     */
    private List<MeFragmentUI> datas = new ArrayList<>();

    public MeFragmentAdapter(Context context) {
        this.context=context;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getGroupCount() {
        return datas.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return datas.get(groupPosition).getList().size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return datas.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return datas.get(groupPosition).getList().get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return 0;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        GroupViewHolder viewHolder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_me_song_list_title, parent, false);
            viewHolder = new GroupViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (GroupViewHolder) convertView.getTag();
        }
        viewHolder.setData(getGroupData(groupPosition),isExpanded);
        return convertView;
    }


    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        ChildViewHolder viewHolder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_me_song_list, parent, false);
            viewHolder = new ChildViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ChildViewHolder) convertView.getTag();
        }
        viewHolder.setData(getChildData(groupPosition,childPosition));
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    /**
     * @param position
     * @return 返回指定父item位置的歌单列表数据
     */
    public MeFragmentUI getGroupData(int position){
        return datas.get(position);
    }

    /**
     * @param groupPosition
     * @param childPosition
     * @return 返回指定子item位置的歌单数据
     */
    public SongList getChildData(int groupPosition, int childPosition){
        return datas.get(groupPosition).getList().get(childPosition);
    }

    /**
     * 添加新的歌单列表数据
     * @param data
     */
    public void setData(ArrayList<MeFragmentUI> data){
        this.datas.clear();
        this.datas.addAll(data);
        notifyDataSetChanged();
    }

    public List<MeFragmentUI> getDatas() {
        return datas;
    }

    class GroupViewHolder{
        //歌单名称
        private final TextView tv_title;
        //更多图标
        private final ImageView iv_more;

        public GroupViewHolder(View view){
            tv_title = view.findViewById(R.id.tv_title);
            iv_more = view.findViewById(R.id.iv_more);
        }

        /**
         * 设置数据
         * @param data 表示歌单数据
         * @param isExpanded 表示下拉列表是否展开
         */
        public void setData(MeFragmentUI data, boolean isExpanded){
            tv_title.setText(data.getTitle());
            iv_more.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onMeFragmentListener.onSongListGroupSettingsClick();
                }
            });
        }
    }

    class ChildViewHolder{
        //歌单封面
        private final ImageView iv_icon;
        //歌单名称
        private final TextView tv_title;
        //歌单评论数量
        private final TextView tv_count;

        private ChildViewHolder(View view){
            iv_icon = view.findViewById(R.id.iv_icon);
            tv_title = view.findViewById(R.id.tv_title);
            tv_count = view.findViewById(R.id.tv_count);
        }

        public void setData(SongList data){
            //设置歌单封面
            ImageUtil.show((BaseActivity)context, iv_icon, data.getBanner());
            //设置歌单名称
            tv_title.setText(data.getTitle());
            //设置歌单评论数量
            tv_count.setText(context.getResources().getString(R.string.song_count, data.getSongs_count()));
        }
    }

    public void setOnMeFragmentListener(OnMeFragmentListener onMeFragmentListener){
        this.onMeFragmentListener = onMeFragmentListener;
    }

    public interface OnMeFragmentListener{
        //歌单设置点击事件
        void onSongListGroupSettingsClick();
    }
}






















