package com.luckyliuqs.mymusic.Util;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.RemoteViews;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.luckyliuqs.mymusic.MainActivity;
import com.luckyliuqs.mymusic.R;
import com.luckyliuqs.mymusic.domain.Song;
import com.luckyliuqs.mymusic.domain.User;
import com.luckyliuqs.mymusic.manager.UserManager;
import com.luckyliuqs.mymusic.manager.impl.UserManagerImpl;

import static android.content.Context.NOTIFICATION_SERVICE;

/**
 * 通知工具类
 */
public class NotificationUtil {
    private static final int NOTIFICATION_MUSIC_ID = 10000;
    private static final int NOTIFICATION_UNLOCK_LYRIC_ID = 10001;
    private static NotificationManager notificationManager;

    /**
     * 显示播放歌曲通知栏
     * @param context
     * @param song
     * @param isPlaying
     */
    public static void showMusicNotification(final Context context, final Song song, final boolean isPlaying){
        RequestOptions options = new RequestOptions();
        options.centerCrop();
        if (song.getSource() == Song.SOURCE_ONLINE){
            //如果为在线歌曲，需要拼接地址
            Glide.with(context).asBitmap().load(ImageUtil.getImageURI(song.getBanner())).apply(options).into(new SimpleTarget<Bitmap>() {
                @Override
                public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                    //这个布局的根View的尺寸不能引用dimen文件，故写死
                    //设置标准通知数据
                    RemoteViews contentView = new RemoteViews(context.getPackageName(), R.layout.notification_music_play);
                    //设置歌曲封面
                    contentView.setImageViewBitmap(R.id.iv_banner_notification_music_play, resource);
                    //设置歌曲名称
                    contentView.setTextViewText(R.id.tv_title_notification_music_play, song.getTitle());
                    //设置歌手 - 专辑
                    contentView.setTextViewText(R.id.tv_artist_album_notification_music_play, String.format("%s - %s", song.getArtist_name(), song.getAlbum_title()));
                    int playIconResourceId = (isPlaying==true) ? R.drawable.ic_music_notification_pause : R.drawable.ic_music_notification_play;
                    //设置播放或暂停图标
                    contentView.setImageViewResource(R.id.iv_play_notification_music_play, playIconResourceId);

                    //设置标准通知事件：点击事件
                    //播放或暂停
                    PendingIntent playPendingIntent = PendingIntent.getBroadcast(context, Consts.ACTION_PLAY.hashCode(), new Intent(Consts.ACTION_PLAY), PendingIntent.FLAG_UPDATE_CURRENT);
                    contentView.setOnClickPendingIntent(R.id.iv_play_notification_music_play, playPendingIntent);
                    //下一首
                    PendingIntent nextPendingIntent = PendingIntent.getBroadcast(context, Consts.ACTION_NEXT.hashCode(), new Intent(Consts.ACTION_NEXT), PendingIntent.FLAG_UPDATE_CURRENT);
                    contentView.setOnClickPendingIntent(R.id.iv_next_notification_music_play, playPendingIntent);
                    //桌面歌词
                    PendingIntent lyricPendingIntent = PendingIntent.getBroadcast(context, Consts.ACTION_LYRIC.hashCode(), new Intent(Consts.ACTION_LYRIC), PendingIntent.FLAG_UPDATE_CURRENT);
                    contentView.setOnClickPendingIntent(R.id.iv_screen_lyric_notification_music_play, playPendingIntent);

                    //设置可展开通知数据
                    RemoteViews contentBigView = new RemoteViews(context.getPackageName(), R.layout.notification_music_play_big);
                    //设置歌曲封面
                    contentBigView.setImageViewBitmap(R.id.iv_banner_notification_music_play_large, resource);
                    //设置歌曲名称
                    contentBigView.setTextViewText(R.id.tv_title_notification_music_play_large, song.getTitle());
                    //设置歌手 - 专辑
                    contentBigView.setTextViewText(R.id.tv_artist_album_notification_music_play_large, String.format("%s - %s", song.getArtist_name(), song.getAlbum_title()));
                    //设置播放或暂停图标
                    contentBigView.setImageViewResource(R.id.iv_play_notification_music_play_large, playIconResourceId);

                    //设置标准通知事件：点击事件
                    //播放或暂停
                    PendingIntent likePendingIntent = PendingIntent.getBroadcast(context, Consts.ACTION_LIKE.hashCode(), new Intent(Consts.ACTION_LIKE), PendingIntent.FLAG_UPDATE_CURRENT);
                    contentBigView.setOnClickPendingIntent(R.id.iv_like_notification_music_play_large, likePendingIntent);
                    //上一首
                    PendingIntent previousPendingIntent = PendingIntent.getBroadcast(context, Consts.ACTION_PREVIOUS.hashCode(), new Intent(Consts.ACTION_PREVIOUS), PendingIntent.FLAG_UPDATE_CURRENT);
                    contentBigView.setOnClickPendingIntent(R.id.iv_previous_notification_music_play_large, previousPendingIntent);
                    contentBigView.setOnClickPendingIntent(R.id.iv_play_notification_music_play_large, playPendingIntent);
                    contentBigView.setOnClickPendingIntent(R.id.iv_next_notification_music_play_large, nextPendingIntent);
                    contentBigView.setOnClickPendingIntent(R.id.iv_screen_lyric_notification_music_play_large, lyricPendingIntent);

                    Intent intent = new Intent(context, MainActivity.class);
                    intent.setAction(Consts.ACTION_MUSIC_PLAYER);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    PendingIntent contentPendingIntent = PendingIntent.getActivity(context, Consts.ACTION_LYRIC.hashCode(), intent, PendingIntent.FLAG_UPDATE_CURRENT);

                    NotificationCompat.Builder builder = new NotificationCompat.Builder(context,"musicNotification")
                            .setAutoCancel(false)
                            .setSmallIcon(R.drawable.ic_logo)
                            .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_logo))
                            .setCustomContentView(contentView)
                            .setCustomBigContentView(contentBigView)
                            .setPriority(NotificationCompat.PRIORITY_MAX)
                            .setContentIntent(contentPendingIntent);

                    NotificationUtil.notify(context, NOTIFICATION_MUSIC_ID, builder.build());

                }
            });
        }else{
            //如果为本地歌曲
            Glide.with(context).asBitmap().load(song.getAlbum_banner()).apply(options).into(new SimpleTarget<Bitmap>() {
                @Override
                public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                    //这个布局的根View的尺寸不能引用dimen文件，故写死
                    //设置标准通知数据
                    RemoteViews contentView = new RemoteViews(context.getPackageName(), R.layout.notification_music_play);
                    //设置歌曲封面
                    contentView.setImageViewBitmap(R.id.iv_banner_notification_music_play, resource);
                    //设置歌曲名称
                    contentView.setTextViewText(R.id.tv_title_notification_music_play, song.getTitle());
                    //设置歌手 - 专辑
                    contentView.setTextViewText(R.id.tv_artist_album_notification_music_play, String.format("%s - %s", song.getArtist_name(), song.getAlbum_title()));
                    int playIconResourceId = (isPlaying==true) ? R.drawable.ic_music_notification_pause : R.drawable.ic_music_notification_play;
                    //设置播放或暂停图标
                    contentView.setImageViewResource(R.id.iv_play_notification_music_play, playIconResourceId);

                    //设置标准通知事件：点击事件
                    //播放或暂停
                    PendingIntent playPendingIntent = PendingIntent.getBroadcast(context, Consts.ACTION_PLAY.hashCode(), new Intent(Consts.ACTION_PLAY), PendingIntent.FLAG_UPDATE_CURRENT);
                    contentView.setOnClickPendingIntent(R.id.iv_play_notification_music_play, playPendingIntent);
                    //下一首
                    PendingIntent nextPendingIntent = PendingIntent.getBroadcast(context, Consts.ACTION_NEXT.hashCode(), new Intent(Consts.ACTION_NEXT), PendingIntent.FLAG_UPDATE_CURRENT);
                    contentView.setOnClickPendingIntent(R.id.iv_next_notification_music_play, playPendingIntent);
                    //桌面歌词
                    PendingIntent lyricPendingIntent = PendingIntent.getBroadcast(context, Consts.ACTION_LYRIC.hashCode(), new Intent(Consts.ACTION_LYRIC), PendingIntent.FLAG_UPDATE_CURRENT);
                    contentView.setOnClickPendingIntent(R.id.iv_screen_lyric_notification_music_play, playPendingIntent);

                    //设置可展开通知数据
                    RemoteViews contentBigView = new RemoteViews(context.getPackageName(), R.layout.notification_music_play_big);
                    //设置歌曲封面
                    contentBigView.setImageViewBitmap(R.id.iv_banner_notification_music_play_large, resource);
                    //设置歌曲名称
                    contentBigView.setTextViewText(R.id.tv_title_notification_music_play_large, song.getTitle());
                    //设置歌手 - 专辑
                    contentBigView.setTextViewText(R.id.tv_artist_album_notification_music_play_large, String.format("%s - %s", song.getArtist_name(), song.getAlbum_title()));
                    //设置播放或暂停图标
                    contentBigView.setImageViewResource(R.id.iv_play_notification_music_play_large, playIconResourceId);

                    //设置标准通知事件：点击事件
                    //播放或暂停
                    PendingIntent likePendingIntent = PendingIntent.getBroadcast(context, Consts.ACTION_LIKE.hashCode(), new Intent(Consts.ACTION_LIKE), PendingIntent.FLAG_UPDATE_CURRENT);
                    contentBigView.setOnClickPendingIntent(R.id.iv_like_notification_music_play_large, likePendingIntent);
                    //上一首
                    PendingIntent previousPendingIntent = PendingIntent.getBroadcast(context, Consts.ACTION_PREVIOUS.hashCode(), new Intent(Consts.ACTION_PREVIOUS), PendingIntent.FLAG_UPDATE_CURRENT);
                    contentBigView.setOnClickPendingIntent(R.id.iv_previous_notification_music_play_large, previousPendingIntent);
                    contentBigView.setOnClickPendingIntent(R.id.iv_play_notification_music_play_large, playPendingIntent);
                    contentBigView.setOnClickPendingIntent(R.id.iv_next_notification_music_play_large, nextPendingIntent);
                    contentBigView.setOnClickPendingIntent(R.id.iv_screen_lyric_notification_music_play_large, lyricPendingIntent);

                    Intent intent = new Intent(context, MainActivity.class);
                    intent.setAction(Consts.ACTION_MUSIC_PLAYER);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    PendingIntent contentPendingIntent = PendingIntent.getActivity(context, Consts.ACTION_LYRIC.hashCode(), intent, PendingIntent.FLAG_UPDATE_CURRENT);

                    NotificationCompat.Builder builder = new NotificationCompat.Builder(context,"musicNotification")
                            .setAutoCancel(false)
                            .setSmallIcon(R.drawable.ic_logo)
                            .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_logo))
                            .setCustomContentView(contentView)
                            .setCustomBigContentView(contentBigView)
                            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                            .setContentIntent(contentPendingIntent);

                    NotificationUtil.notify(context, NOTIFICATION_MUSIC_ID, builder.build());

                }
            });
        }

    }

    private static void notify(Context context, int id, Notification notification){
        initNotificationManager(context);
        notificationManager.notify(id, notification);
    }

    private static void initNotificationManager(Context context){
        if (notificationManager == null){
            notificationManager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);

        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String channelId = "musicNotification";
            String channelName = "音乐播放器通知栏";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            createNotificationChannel(context, channelId, channelName, importance);
        }
    }

    @TargetApi(Build.VERSION_CODES.O)
    private static void createNotificationChannel(Context context, String channelId, String channelName, int importance) {
        NotificationChannel channel = new NotificationChannel(channelId, channelName, importance);
        notificationManager.createNotificationChannel(channel);
    }

    /**
     * 显示聊天消息（不在聊天界面才显示）
     *
     * @param context
     * @param userId
     * @param content
     */
    public static void showMessageNotification(final Context context, final String userId, final String content, final int unreadCount) {
        Log.i("Notification", "进入聊天消息发送方法，接收到消息内容为：" + content);
        Log.i("Notification", "用户id为: " + userId);
        //Log.i("Notification", "用户id: " + userId + "    hashcode:" + userId.hashCode());
        UserManager userManager = UserManagerImpl.getInstance(context);
        //获取消息发送者的的信息
        userManager.getUser(userId, new UserManager.OnUserListener() {
            @Override
            public void onUser(final User user) {
                //获取用户信息，获取用户头像的Bitmap
                //这里的嵌套可以通过RxJava转换成链式
                RequestOptions options = new RequestOptions();
                options.circleCrop();

                final RemoteViews contentView = new RemoteViews(context.getPackageName(), R.layout.notification_message_online);


                if(user.getAvatar() != null) {
                    Glide.with(context).asBitmap().load(user.getAvatar()).apply(options).into(new SimpleTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                            //如果用户头像信息不为null，则填充用户头像
                            contentView.setImageViewBitmap(R.id.iv_message_avatar, resource);
                        }
                    });

                }else{
                    //如果用户头像信息为null，则填充默认的头像
                    contentView.setImageViewResource(R.id.iv_message_avatar, R.drawable.default_avatar);
                }

                //填充用户及未读信息
                if (unreadCount > 1) {
                    //多条未读消息
                    contentView.setTextViewText(R.id.tv_message_user_info, String.format("%s（%d条消息）", user.getNickname(), unreadCount));
                } else {
                    //单条未读消息
                    contentView.setTextViewText(R.id.tv_message_user_info, user.getNickname());
                }

                //填充聊天消息
                contentView.setTextViewText(R.id.tv_message_info, content);

                //点击通知，跳转到聊天界面
                Intent intent = new Intent(context, MainActivity.class);
                intent.putExtra(Consts.ID, userId);
                intent.setAction(Consts.ACTION_MESSAGE);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                PendingIntent contentPendingIntent = PendingIntent.getActivity(context, Consts.ACTION_LYRIC.hashCode(), intent, PendingIntent.FLAG_UPDATE_CURRENT);

//                        NotificationCompat.Builder builder = new NotificationCompat.Builder(context,"messageNotification")
//                                .setSmallIcon(R.drawable.ic_logo)
//                                .setCustomContentView(contentView)
//                                .setAutoCancel(true)
//                                .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_logo))
//                                .setContentIntent(contentPendingIntent);


                NotificationCompat.Builder builder = new NotificationCompat.Builder(context,"messageNotification")
                        .setAutoCancel(false)
                        .setSmallIcon(R.drawable.ic_logo)
                        .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_logo))
                        .setCustomContentView(contentView)
                        .setPriority(NotificationCompat.PRIORITY_HIGH)
                        .setContentIntent(contentPendingIntent);

                //用发送者的id来显示通知
                NotificationUtil.notifyMessage(context, userId.hashCode(), builder.build());

            }
        });

    }


    //Message
    private static void notifyMessage(Context context, int id, Notification notification){
        initMessageNotificationManager(context);
        notificationManager.notify(id, notification);
    }

    private static void initMessageNotificationManager(Context context){
        if (notificationManager == null){
            notificationManager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);

        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String channelId = "messageNotification";
            String channelName = "聊天消息通知栏";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            createMessageNotificationChannel(context, channelId, channelName, importance);
        }
    }
    //Message

    @TargetApi(Build.VERSION_CODES.O)
    private static void createMessageNotificationChannel(Context context, String channelId, String channelName, int importance) {
        NotificationChannel channel = new NotificationChannel(channelId, channelName, importance);
        notificationManager.createNotificationChannel(channel);
    }

}

































