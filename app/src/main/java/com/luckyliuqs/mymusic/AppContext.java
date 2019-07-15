package com.luckyliuqs.mymusic;

import android.app.Application;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Process;
import android.support.text.emoji.EmojiCompat;
import android.support.text.emoji.bundled.BundledEmojiCompatConfig;
import android.util.Log;

import com.luckyliuqs.mymusic.Util.LogUtil;
import com.luckyliuqs.mymusic.Util.MessageUtil;
import com.luckyliuqs.mymusic.Util.NotificationUtil;
import com.luckyliuqs.mymusic.Util.PackageUtil;
import com.luckyliuqs.mymusic.Util.SharedPreferencesUtil;
import com.luckyliuqs.mymusic.domain.event.OnMessageEvent;

import org.greenrobot.eventbus.EventBus;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Message;

/**
 * App Context
 */
public class AppContext extends Application {
    private static final String TAG = "AppContext";
    private static Context context;
    private static RongIMClient imClient;
    private static SharedPreferencesUtil sp;

    public static Context getContext() {
        return context;
    }

    public void onCreate() {
        super.onCreate();
        sp = SharedPreferencesUtil.getInstance(getApplicationContext());

        this.context = getApplicationContext();

        //初始化emoji
        EmojiCompat.Config config = new BundledEmojiCompatConfig(this);
        EmojiCompat.init(config);

//        //Share SDK
//        MobSDK.init(this);
//
        //初始化 Rong IM
        RongIMClient.init(this);

        if (!sp.isLoginInfoEmpty()) {
            //如果用户登录信息不为空，则创建RongIMClient连接
            imConnect();
        }

        //当前版本的SDK会导致引用崩溃后挂起，不打印日志，可以先设置如下代码让能打印崩溃日志，挂起问题他们后面会处理
//        Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
//            @Override
//            public void uncaughtException(Thread t, Throwable e) {
//                e.printStackTrace();
//            }
//        });



       closeAndroidPDialog();
    }

    /**
     * 创建RongIMClient连接
     */
    public void imConnect() {
        //获取到APP名称
        String appName = PackageUtil.getAppName(getApplicationContext(), Process.myPid());
        //获取到包名
        String packageName = getApplicationInfo().packageName;
        //判断当前应用的包名。
        if (packageName.equals(appName)) {
           // Log.i(TAG, "imConnect: 包名一致");
            Log.i(TAG,"rong connect "+appName+","+packageName+","+(Looper.myLooper() == Looper.getMainLooper()));
            //if (Looper.myLooper() == Looper.getMainLooper()) {
            //由于使用的多进程，所以会调用多次，该初始化要主线程
            imClient = RongIMClient.connect(sp.getIMToken(), new RongIMClient.ConnectCallback() {
                @Override
                public void onTokenIncorrect() {
                    Log.d(TAG, "im onTokenIncorrect: ");
                }

                @Override
                public void onSuccess(String s) {
                    Log.d(TAG, "im onSuccess: " + s);
                }

                @Override
                public void onError(RongIMClient.ErrorCode errorCode) {
                    Log.d(TAG, "im onError: " + errorCode);
                }
            });

            imClient.setOnReceiveMessageListener(new RongIMClient.OnReceiveMessageListener() {
                @Override
                public boolean onReceived(Message message, int i) {
                    //该方法的调用不再主线程
                    Log.d(TAG, "ImClient onReceivedMessage: " + message + "," + i + "," + (Looper.myLooper() == Looper.getMainLooper()));

                    if (EventBus.getDefault().hasSubscriberForEvent(OnMessageEvent.class)) {
                        //如果有监听该事件，表示在聊天界面
                        EventBus.getDefault().post(new OnMessageEvent(message));
                    } else {
                        Log.i(TAG, "Imclient let handler obtainMessage: ");
                        handler.obtainMessage(0,message).sendToTarget();
                    }

                    return false;
                }
            });
        }else{
            //Log.i(TAG, "imConnect: 包名不一致");
            //Log.i(TAG,"rong connect "+appName+","+packageName+","+(Looper.myLooper() == Looper.getMainLooper()));
        }


    }

//    @Override
//    protected void attachBaseContext(Context base) {
//        super.attachBaseContext(base);
//        MultiDex.install(this);
//    }

    /**
     * RongIMClient logout
     */
    public static void logout() {
        imClient.logout();
    }

    /**
     * @return RongIMClient
     */
    public static RongIMClient getImClient() {
        return imClient;
    }

    private Handler handler=new Handler(Looper.myLooper()){
        @Override
        public void handleMessage(android.os.Message msg) {
            super.handleMessage(msg);
            //如果没有就显示通知
            final Message message= (Message) msg.obj;
            imClient.getUnreadCount(Conversation.ConversationType.PRIVATE, message.getSenderUserId(), new RongIMClient.ResultCallback<Integer>() {
                @Override
                public void onSuccess(Integer integer) {
                    Log.i(TAG, "Handler onSuccess receive message " + message.getContent());
                    NotificationUtil.showMessageNotification(context.getApplicationContext(),message.getSenderUserId(), MessageUtil.getContent(message.getContent()),integer);
                }

                @Override
                public void onError(RongIMClient.ErrorCode errorCode) {

                }
            });

        }
    };

    private void closeAndroidPDialog(){
        try {
            Class aClass = Class.forName("android.content.pm.PackageParser$Package");
            Constructor declaredConstructor = aClass.getDeclaredConstructor(String.class);
            declaredConstructor.setAccessible(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            Class cls = Class.forName("android.app.ActivityThread");
            Method declaredMethod = cls.getDeclaredMethod("currentActivityThread");
            declaredMethod.setAccessible(true);
            Object activityThread = declaredMethod.invoke(null);
            Field mHiddenApiWarningShown = cls.getDeclaredField("mHiddenApiWarningShown");
            mHiddenApiWarningShown.setAccessible(true);
            mHiddenApiWarningShown.setBoolean(activityThread, true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}