package com.luckyliuqs.mymusic.activity;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.graphics.Palette;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.luckyliuqs.mymusic.R;
import com.luckyliuqs.mymusic.Util.Consts;
import com.luckyliuqs.mymusic.Util.StringUtil;
import com.luckyliuqs.mymusic.adapter.UserDetailAdapter;
import com.luckyliuqs.mymusic.api.Api;
import com.luckyliuqs.mymusic.domain.User;
import com.luckyliuqs.mymusic.domain.response.DetailResponse;
import com.luckyliuqs.mymusic.reactivex.HttpListener;

import net.lucode.hackware.magicindicator.MagicIndicator;
import net.lucode.hackware.magicindicator.ViewPagerHelper;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.ColorTransitionPagerTitleView;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class UserDetailActivity extends BaseTitleActivity {
    //用户昵称
    private String nickName;
    //用户ID
    private String id;
    private UserDetailAdapter adapter;
    private User user;

    @BindView(R.id.abl)
    AppBarLayout abl;
    @BindView(R.id.tabs)
    MagicIndicator tabs;
    @BindView(R.id.vp)
    ViewPager vp;
    @BindView(R.id.iv_avatar)
    ImageView iv_vaatar;
    @BindView(R.id.tv_nickname)
    TextView tv_nickName;
    @BindView(R.id.tv_info)
    TextView tv_info;
    @BindView(R.id.bt_follow)
    Button bt_follow;
    @BindView(R.id.bt_send_message)
    Button bt_send_message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_detail);
    }

    @Override
    protected void initViews() {
        super.initViews();
        enableBackMenu();

        //设置ViewPager缓存3个页面
        vp.setOffscreenPageLimit(3);
        //setUpUI("0");
    }

    @Override
    protected void initDatas() {
        super.initDatas();
        //获取传递进来的用户昵称信息
        nickName = getIntent().getStringExtra(Consts.NICKNAME);
        //获取传递进来的用户ID信息
        id = getIntent().getStringExtra(Consts.ID);

        if(StringUtils.isNotEmpty(id)){
            //如果ID不为空，就通过ID查询
            fetchDataById(id);
        }else if(StringUtils.isNotEmpty(nickName)){
            //通过昵称查询，主要用在@昵称中
            fetchDataByNickName(nickName);
        }else{
            //参数有误，直接finish（）
            finish();
        }

    }

    /**
     * 通过ID查询
     * @param id
     */
    private void fetchDataById(String id) {
        Api.getInstance().userDetail(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new HttpListener<DetailResponse<User>>(getActivity()){
                    @Override
                    public void onSucceeded(DetailResponse<User> data){
                        super.onSucceeded(data);
                        next(data.getData());
                    }
                });
    }

    /**
     * 通过昵称查询
     * @param nickName
     */
    private void fetchDataByNickName(String nickName) {
        Api.getInstance().userDetailByNickName(nickName)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new HttpListener<DetailResponse<User>>(getActivity()) {
                    @Override
                    public void onSucceeded(DetailResponse<User> data) {
                        super.onSucceeded(data);
                        next(data.getData());
                    }
                });
    }

    public void next(User user){
        this.user = user;
        setUpUI(user.getId());

        RequestOptions options = new RequestOptions();
        options.circleCrop();
        RequestBuilder<Bitmap> bitmapRequestBuilder = Glide.with(this)
                                                                .asBitmap()
                                                                .apply(options)
                                                                .load(user.getAvatar());
        bitmapRequestBuilder.into(new SimpleTarget<Bitmap>() {
            @Override
            public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {
                iv_vaatar.setImageBitmap(resource);

                Palette.from(resource).generate(new Palette.PaletteAsyncListener() {
                    @Override
                    public void onGenerated(@NonNull Palette palette) {
                         Palette.Swatch swatch = palette.getVibrantSwatch();
                         if (swatch != null){
                             int rgb = swatch.getRgb();
                             abl.setBackgroundColor(rgb);
                             //设置状态栏
                             if(android.os.Build.VERSION.SDK_INT >= 21){
                                 Window window = getWindow();
                                 window.setStatusBarColor(rgb);
                                 window.setNavigationBarColor(rgb);
                             }
                         }
                    }
                });

            }
        });

        tv_nickName.setText(user.getNickname());
        tv_info.setText(getResources().getString(R.string.user_detail_count_info, user.getFollowings_count(), user.getFollowers_count()));

        showFollowStatus();
    }

    private void showFollowStatus(){
        if (user.getId().equals(sp.getUserId())){
            //表示是用户自己，则隐藏关注按钮和发送消息按钮
            bt_follow.setVisibility(View.GONE);
            bt_send_message.setVisibility(View.GONE);
        }else{
            //表示是其他用户，判断用户自己是否关注了其他用户
            bt_follow.setVisibility(View.VISIBLE);
            if (user.isFollowing()){
                //表示已经关注了
                bt_follow.setText("取消关注");
                bt_send_message.setVisibility(View.VISIBLE);
            }else{
                //表示还没关注
                bt_follow.setText("关注");
                bt_send_message.setVisibility(View.GONE);
            }
        }
    }

    private void setUpUI(String id){
        adapter = new UserDetailAdapter(getActivity(), getSupportFragmentManager());
        adapter.setUserId(id);
        vp.setAdapter(adapter);

        final ArrayList<Integer> datas = new ArrayList<Integer>();
        datas.add(0);
        datas.add(1);
        datas.add(2);
        adapter.setDatas(datas);

        //将Tablayout和ViewPager关联起来
        CommonNavigator commonNavigator = new CommonNavigator(getActivity());
        commonNavigator.setAdapter(new CommonNavigatorAdapter() {
            @Override
            public int getCount() {
                return datas.size();
            }

            @Override
            public IPagerTitleView getTitleView(Context context, final int index) {
                ColorTransitionPagerTitleView colorTransitionPagerTitleView = new ColorTransitionPagerTitleView(context);
                colorTransitionPagerTitleView.setNormalColor(getResources().getColor(R.color.text_white));
                colorTransitionPagerTitleView.setSelectedColor(Color.WHITE);
                colorTransitionPagerTitleView.setText(adapter.getPageTitle(index));
                colorTransitionPagerTitleView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        vp.setCurrentItem(index);
                    }
                });
                return colorTransitionPagerTitleView;
            }

            @Override
            public IPagerIndicator getIndicator(Context context) {
                LinePagerIndicator indicator = new LinePagerIndicator(context);
                indicator.setMode(LinePagerIndicator.MODE_WRAP_CONTENT);
                indicator.setColors(Color.WHITE);
                return indicator;
            }
        });
        commonNavigator.setAdjustMode(true);
        tabs.setNavigator(commonNavigator);

        ViewPagerHelper.bind(tabs, vp);
    }

    @OnClick(R.id.bt_follow)
    public void bt_follow(){

    }

    @OnClick(R.id.bt_send_message)
    public void bt_send_message(){

    }
}






















