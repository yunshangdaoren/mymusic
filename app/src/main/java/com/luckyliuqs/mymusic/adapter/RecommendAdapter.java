package com.luckyliuqs.mymusic.adapter;

import android.app.Activity;
import android.content.Context;
import android.media.Image;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.luckyliuqs.mymusic.R;
import com.luckyliuqs.mymusic.Util.ImageUtil;
import com.luckyliuqs.mymusic.Util.StringUtil;
import com.luckyliuqs.mymusic.domain.Advertisement;
import com.luckyliuqs.mymusic.domain.Song;
import com.luckyliuqs.mymusic.domain.SongList;


/**
 * 推荐页面Adapter
 */
public class RecommendAdapter extends BaseRecyclerViewAdapter<Object, RecommendAdapter.BaseViewHolder>{
    //标题
    public static final int TYPE_TITLE = 0;
    //列表
    public static final int TYPE_SONGLIST = 1;
    //歌曲
    public static final int TYPE_SONG = 2;
    //广告
    public static final int TYPE_ADVERTISEMENT = 3;

    public RecommendAdapter(Context context) {
        super(context);
    }

    @NonNull
    @Override
    public BaseViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        //在这里创建不同的ViewHolder，然后绑定各自的数据
        switch (viewType){
            case TYPE_TITLE:
                //return 标题ViewHolder
                return new TitleViewHolder(getInflater().inflate(R.layout.item_title, viewGroup, false));
            case TYPE_SONGLIST:
                //return 歌单ViewHolder
                return new SongListViewHolder(getInflater().inflate(R.layout.item_songlist, viewGroup, false));
            case TYPE_SONG:
                //return 歌曲ViewHolder
                return new SongViewHolder(getInflater().inflate(R.layout.item_song, viewGroup, false));

        }
        //return 广告ViewHolder
        return new AdvertisementViewHolder(getInflater().inflate(R.layout.item_song, viewGroup, false));
    }

    /**
     * 返回子ItemView的类型
     * @param position
     * @return int
     */
    @Override
    public int getItemViewType(int position) {
        Object data = getData(position);
        if (data instanceof String){
            //代表这是标题类型
            return TYPE_TITLE;
        }else if(data instanceof SongList){
            //代表这是歌单类型
            return TYPE_SONGLIST;
        }else if(data instanceof Song){
            //代表这是歌曲类型
            return TYPE_SONG;
        }
        //代表这是广告类型
        return TYPE_ADVERTISEMENT;
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        //绑定具体数据直接调用bindData方法
        holder.bindData(getData(position));
    }

    /**
     * 设置
     * @param position
     * @return int
     */
    @Override
    public int setSpanSizeLookup(int position) {
        int viewType = getItemViewType(position);
        switch (viewType){
            case RecommendAdapter.TYPE_TITLE:
                //标题占3列
                return 3;
            case RecommendAdapter.TYPE_SONGLIST:
                //歌单占一列
                return 1;
            case RecommendAdapter.TYPE_SONG:
                //歌曲占3列
                return 3;
        }
        //广告占3列
        return 3;
    }

    /**d
     * abstract ViewHolder基类
     */
    abstract class BaseViewHolder extends BaseRecyclerViewAdapter.ViewHolder{

        public BaseViewHolder(@NonNull View itemView) {
            super(itemView);
        }
        //绑定data
        public abstract void bindData(Object data);
    }

    /**
     * Title ViewHolder类
     */
    private class TitleViewHolder extends BaseViewHolder{
        //标题
        private TextView tv_title;

        public TitleViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_title = (TextView) findViewById(R.id.tv_title);
        }

        //设置标题内容
        @Override
        public void bindData(Object data) {
            tv_title.setText(data.toString());
        }
    }

    /**
     * 歌单SongList ViewHolder类
     */
    private class SongListViewHolder extends BaseViewHolder{
        //歌单封面
        private ImageView iv_banner;
        //歌单被点击数
        private TextView tv_count;
        //歌单名称
        private TextView tv_title;

        public SongListViewHolder(@NonNull View itemView) {
            super(itemView);
            iv_banner = (ImageView) findViewById(R.id.iv_banner);
            tv_count = (TextView) findViewById(R.id.tv_count);
            tv_title = (TextView) findViewById(R.id.tv_title);
        }

        @Override
        public void bindData(Object data) {
            SongList songList = (SongList) data;
            //设置歌单封面
            ImageUtil.show((Activity)context, iv_banner, songList.getBanner());
            //设置歌单被点击数
            tv_count.setText(StringUtil.formatCount(songList.getClicks_count()));
            //设置歌单名称
            tv_title.setText(songList.getTitle());
        }
    }

    /**
     * 歌曲Song ViewHolder类
     */
    private class SongViewHolder extends BaseViewHolder{
        //歌手头像
        private final ImageView iv_avatar;
        //歌手名称
        private final TextView tv_nickName;
        //歌曲封面
        private ImageView iv_icon;
        //歌曲名称
        private TextView tv_title;
        //歌曲被评论数量
        private TextView tv_comment_count;

        public SongViewHolder(@NonNull View itemView) {
            super(itemView);
            iv_avatar = (ImageView) findViewById(R.id.iv_avatar);
            tv_nickName = (TextView) findViewById(R.id.tv_nickname);
            iv_icon = (ImageView) findViewById(R.id.iv_icon);
            tv_title = (TextView) findViewById(R.id.tv_title);
            tv_comment_count = (TextView) findViewById(R.id.tv_comment_count);
        }

        @Override
        public void bindData(Object data) {
            Song song = (Song) data;
            //设置歌曲封面
            ImageUtil.show((Activity) context, iv_icon, song.getBanner());
            //设置歌手头像
            ImageUtil.show((Activity) context, iv_avatar, song.getArtist_avatar());
            //设置歌手名称
            tv_nickName.setText(song.getArtist().getNickname());
            //设置歌曲名称
            tv_title.setText(song.getTitle());
            //设置歌曲被评论数量
            tv_comment_count.setText(StringUtil.formatCount(song.getComments_count()));
        }
    }

    /**
     * 广告Advertisement ViewHolder类
     */
    private class AdvertisementViewHolder extends BaseViewHolder{
        //广告封面
        private ImageView iv_icon;
        //广告标题
        private TextView tv_title;

        public AdvertisementViewHolder(@NonNull View itemView) {
            super(itemView);
            iv_icon = (ImageView) findViewById(R.id.iv_icon);
            tv_title = (TextView) findViewById(R.id.tv_title);
        }

        @Override
        public void bindData(Object data) {
            Advertisement advertisement = (Advertisement) data;
            //设置广告封面
            ImageUtil.show((Activity) context, iv_icon, advertisement.getBanner());
            //设置广告标题
            tv_title.setText(advertisement.getTitle());
        }
    }


}


























