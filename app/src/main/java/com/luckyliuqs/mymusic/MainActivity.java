package com.luckyliuqs.mymusic;

import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.luckyliuqs.mymusic.Util.UserUtil;
import com.luckyliuqs.mymusic.activity.BaseTitleActivity;
import com.luckyliuqs.mymusic.activity.LoginActivity;
import com.luckyliuqs.mymusic.activity.SettingActivity;
import com.luckyliuqs.mymusic.activity.UserDetailActivity;
import com.luckyliuqs.mymusic.adapter.HomeAdapter;
import com.luckyliuqs.mymusic.api.Api;
import com.luckyliuqs.mymusic.domain.User;
import com.luckyliuqs.mymusic.domain.event.LogoutSuccessEvent;
import com.luckyliuqs.mymusic.domain.response.DetailResponse;
import com.luckyliuqs.mymusic.reactivex.HttpListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;

import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends BaseTitleActivity implements View.OnClickListener, ViewPager.OnPageChangeListener {
    private DrawerLayout drawerLayout;
    //头像
    private ImageView iv_avatar;
    private TextView tv_nickname;
    private TextView tv_description;
    private ViewPager viewPager;
    private HomeAdapter homeAdapter;

    private ImageView iv_music;
    private ImageView iv_recommend;
    private ImageView iv_video;

    //包含设置及图标的LinearLayout
    private LinearLayout ll_settings;
    private LinearLayout ll_my_friend;
    private LinearLayout ll_message_container;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void initViews() {
        super.initViews();
        //延迟到点击设置才注册
        EventBus.getDefault().register(this);

        iv_avatar = findViewById(R.id.iv_avatar);
        tv_nickname = findViewById(R.id.tv_nickname);
        tv_description = findViewById(R.id.tv_description);

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        iv_music = findViewById(R.id.iv_music);
        iv_recommend = findViewById(R.id.iv_recommend);
        iv_video = findViewById(R.id.iv_video);

        ll_settings = findViewById(R.id.ll_settings);

        viewPager = findViewById(R.id.vp);
        //缓存三个页面
        viewPager.setOffscreenPageLimit(3);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
    }

    @Override
    protected void initDatas() {
        super.initDatas();

        //创建首页适配器对象
        homeAdapter = new HomeAdapter(getActivity(),getSupportFragmentManager());
        viewPager.setAdapter(homeAdapter);
        ArrayList<Integer> datas = new ArrayList<>();
        datas.add(0);
        datas.add(1);
        datas.add(2);
        homeAdapter.setDatas(datas);

        //显示用户信息
        showUserInfo();
    }

    private void showData(User data) {
        //将显示用户信息放到单独的类中，是为了重用，因为在用户详情界面会用到
        UserUtil.showUser(getActivity(),data,iv_avatar,tv_nickname,tv_description);
    }

    @Override
    protected void initListener() {
        super.initListener();
        iv_music.setOnClickListener(this);
        iv_recommend.setOnClickListener(this);
        iv_video.setOnClickListener(this);

        ll_settings.setOnClickListener(this);

        //为ViewPager翻动Fragment页面添加监听器
        viewPager.addOnPageChangeListener(this);

    }

    /**
     * 点击用户头像
     */
    @OnClick(R.id.iv_avatar)
    public void avatorClick(){
        if(!sp.isLoginInfoEmpty()){
            //如果用户已经登录了，则跳转到用户详情界面
            startActivityExtraString(UserDetailActivity.class,sp.getUserId());
        }else{
            //用户还没登录，则跳转到登录界面
            startActivity(LoginActivity.class);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_music:
                //跳转到我的音乐页面：第一个参数表示页面下标；第二个参数表示是否可以左右滚动
                viewPager.setCurrentItem(0,true);
                break;
            case R.id.iv_recommend:
                viewPager.setCurrentItem(1,true);
                break;
            case R.id.iv_video:
                viewPager.setCurrentItem(2,true);
                break;
            case R.id.ll_settings:    //点击侧滑菜单栏的设置，跳转到设置界面

                startActivity(SettingActivity.class);
                closeDrawer();
                break;
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    /**
     * ViewPager的Fragment页面翻动的监听事件
     * @param position
     */
    @Override
    public void onPageSelected(int position) {
        if(position == 0){
            iv_music.setImageResource(R.drawable.ic_music_selected);
            iv_recommend.setImageResource(R.drawable.ic_recommend);
            iv_video.setImageResource(R.drawable.ic_video);
        }else if(position == 1){
            iv_music.setImageResource(R.drawable.ic_music);
            iv_recommend.setImageResource(R.drawable.ic_recommend_selected);
            iv_video.setImageResource(R.drawable.ic_video);
        }else{
            iv_music.setImageResource(R.drawable.ic_music);
            iv_recommend.setImageResource(R.drawable.ic_recommend);
            iv_video.setImageResource(R.drawable.ic_video_selected);
        }

    }
    @Override
    public void onPageScrollStateChanged(int state) {
    }

    /**
     * 关闭侧滑菜单方法
     */
    private void closeDrawer(){
        drawerLayout.closeDrawer(Gravity.START);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void logoutSuccessEvent(LogoutSuccessEvent event) {
        showUserInfo();
    }

    /**
     * 用于显示用户信息：分为登录状态和未登录状态两种形式显示
     */
    private void showUserInfo() {
        //用户信息这部分，进来是看不到的，所以可以延后初始化
        if (!sp.isLoginInfoEmpty()) {
            //调用用户信息接口
            Api.getInstance().userDetail(sp.getUserId())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new HttpListener<DetailResponse<User>>(getActivity()) {
                        @Override
                        public void onSucceeded(DetailResponse<User> data) {
                            super.onSucceeded(data);
                            showData(data.getData());
                        }
                    });

        } else {
            UserUtil.showNotLoginUser(getActivity(), iv_avatar, tv_nickname, tv_description);
        }
    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }
}
