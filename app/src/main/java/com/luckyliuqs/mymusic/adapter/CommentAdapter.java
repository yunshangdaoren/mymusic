package com.luckyliuqs.mymusic.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.luckyliuqs.mymusic.R;
import com.luckyliuqs.mymusic.Util.Consts;
import com.luckyliuqs.mymusic.Util.ImageUtil;
import com.luckyliuqs.mymusic.Util.TagUtil;
import com.luckyliuqs.mymusic.Util.TimeUtil;
import com.luckyliuqs.mymusic.activity.BaseActivity;
import com.luckyliuqs.mymusic.activity.TopicDetailActivity;
import com.luckyliuqs.mymusic.activity.UserDetailActivity;
import com.luckyliuqs.mymusic.domain.Comment;
import com.luckyliuqs.mymusic.listener.OnTagClickListener;


/**
 * 评论列表内容Adapter
 */
public class CommentAdapter extends BaseRecyclerViewAdapter<Object, CommentAdapter.BaseViewHolder> {
    /**
     * 评论页面评论标题：热门评论和最新评论
     */
    private static final int TYPE_TITLE = 0;

    /**
     * 评论页面评论内容
     */
    private static final int TYPE_COMMENT = 1;

    private OnCommentAdapter onCommentAdapter;

    public CommentAdapter(Context context) {
        super(context);
    }

    @NonNull
    @Override
    public BaseViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        if (TYPE_TITLE == viewType){
            //标题
            return new CommentAdapter.TitleViewHolder(getInflater().inflate(R.layout.item_comment_title, viewGroup, false));
        }else{
            //评论内容
            return new CommentAdapter.CommentViewHolder(getInflater().inflate(R.layout.item_comment, viewGroup, false));
        }
    }

    @Override
    public int getItemViewType(int position) {
        Object data = getData(position);
        if (data instanceof String){
            //标题
            return TYPE_TITLE;
        }

        //评论
        return TYPE_COMMENT;
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        holder.bindData(getData(position));
    }

    abstract class BaseViewHolder extends BaseRecyclerViewAdapter.ViewHolder{

        public BaseViewHolder(@NonNull View itemView) {
            super(itemView);
        }

        /**
         * 绑定数据
         * @param data
         */
        public  void bindData(Object data){

        }
    }

    /**
     * 评论页面评论标题ViewHolder
     */
    class TitleViewHolder extends BaseViewHolder{
        /**
         * 评论页面评论标题：热门评论和最新评论
         */
        private final TextView tv_title;

        public TitleViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_title = (TextView) findViewById(R.id.tv_comment_title);
        }

        @Override
        public void bindData(Object data) {
            tv_title.setText(data.toString());
        }
    }

    /**
     * 评论页面评论内容ViewHolder
     */
    class CommentViewHolder extends BaseViewHolder{
        /**
         * 用户头像
         */
        private final ImageView iv_comment_avatar;

        /**
         * 点赞图标
         */
        private final ImageView iv_comment_like;

        /**
         * 用户昵称
         */
        private final TextView tv_comment_nickname;

        /**
         * 点赞数量
         */
        private final TextView tv_comment_like_count;

        /**
         * 评论时间
         */
        private final TextView tv_comment_time;

        /**
         * 评论内容
         */
        private final TextView tv_comment_content;

        /**
         * 被回复的评论内容
         */
        private final TextView tv_comment_replay_content;
        private final LinearLayout ll_comment_like_container;
        private final CardView cv_comment_replay_container;

        public CommentViewHolder(@NonNull View itemView) {
            super(itemView);
            //用户头像
            iv_comment_avatar = (ImageView) findViewById(R.id.iv_comment_avatar);
            //点赞图标
            iv_comment_like = (ImageView) findViewById(R.id.iv_comment_like);
            //用户昵称
            tv_comment_nickname = (TextView) findViewById(R.id.tv_comment_nickname);
            //点赞数量
            tv_comment_like_count = (TextView) findViewById(R.id.tv_comment_like_count);
            //评论时间
            tv_comment_time = (TextView) findViewById(R.id.tv_comment_time);
            //评论内容
            tv_comment_content = (TextView) findViewById(R.id.tv_comment_content);
            //被回复的评论内容
            tv_comment_replay_content = (TextView) findViewById(R.id.tv_comment_replay_content);
            ll_comment_like_container = (LinearLayout) findViewById(R.id.ll_comment_like_container);
            cv_comment_replay_container = (CardView) findViewById(R.id.cd_comment_replay_container);
        }

        @Override
        public void bindData(final Object data) {
            super.bindData(data);
            //评论
            final Comment comment = (Comment) data;
            //设置用户头像
            ImageUtil.showCircle((Activity) context, iv_comment_avatar, comment.getUser().getAvatar());
            iv_comment_avatar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //点击用户头像，跳转到用户详情界面
                    Intent intent = new Intent(context, UserDetailActivity.class);
                    intent.putExtra(Consts.NICKNAME, comment.getUser().getNickname());
                    ((BaseActivity) context).startActivity(intent);
                }
            });

            //评论内容，设置后才可以点击
            tv_comment_content.setMovementMethod(LinkMovementMethod.getInstance());

            //设置评论内容的链接颜色
            tv_comment_content.setLinkTextColor(context.getResources().getColor(R.color.text_Highlight));
            tv_comment_content.setText(TagUtil.process(context, comment.getContent(), new OnTagClickListener() {
                @Override
                public void onTagClick(String content) {
                    //Tag点击
                    processTagClick(content);
                }
            }));

            tv_comment_nickname.setText(comment.getUser().getNickname());
            tv_comment_time.setText(TimeUtil.dateTimeFormat(((Comment) data).getCreate_at()));

            tv_comment_like_count.setText(String.valueOf(comment.getLike_count()));
            if (comment.isLiked()){
                iv_comment_like.setImageResource(R.drawable.ic_comment_liked);
                tv_comment_like_count.setTextColor(context.getResources().getColor(R.color.main_color));
            }else{
                iv_comment_like.setImageResource(R.drawable.ic_comment_like);
                tv_comment_like_count.setTextColor(context.getResources().getColor(R.color.text_dark));
            }

            //回复的评论
            if(comment.getParent() == null){
                cv_comment_replay_container.setVisibility(View.GONE);
            }else{
                cv_comment_replay_container.setVisibility(View.VISIBLE);
                tv_comment_replay_content.setMovementMethod(LinkMovementMethod.getInstance());
                tv_comment_replay_content.setLinkTextColor(context.getResources().getColor(R.color.text_Highlight));
                tv_comment_replay_content.setText(TagUtil.process(context, context.getString(R.string.reply_comment_show,
                        comment.getParent().getUser().getNickname(),
                        comment.getParent().getContent()), new OnTagClickListener() {
                    @Override
                    public void onTagClick(String content) {
                        //Tag点击方法
                        processTagClick(content);
                    }
                }));
            }

            ll_comment_like_container.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onCommentAdapter != null){
                        onCommentAdapter.onLikeClick((Comment) data);
                    }
                }
            });

        }
    }

    /**
     * Tag点击方法，这部分可以用监听器回调到Activity中再处理
     * @param content
     */
    private void processTagClick(String content){
        if (content.startsWith(Consts.MENTION)){
            //以@开头，跳转到用户详情界面
            Intent intent = new Intent(context, UserDetailActivity.class);
            intent.putExtra(Consts.NICKNAME, TagUtil.removeTag(content));
            ((BaseActivity) context).startActivity(intent);
        }else if (content.startsWith(Consts.HAST_TAG)){
            //以#开头，跳转到话题详情界面
            Intent intent = new Intent(context, TopicDetailActivity.class);
            intent.putExtra(Consts.TITLE, TagUtil.removeTag(content));
            ((BaseActivity) context).startActivity(intent);
        }
    }

    public void setOnCommentAdapter(OnCommentAdapter onCommentAdapter) {
        this.onCommentAdapter = onCommentAdapter;
    }

    public interface OnCommentAdapter{
        /**
         * 点赞评论
         * @param comment
         */
        void onLikeClick(Comment comment);
    }

}
