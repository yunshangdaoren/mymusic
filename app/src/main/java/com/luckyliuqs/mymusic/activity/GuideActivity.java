package com.luckyliuqs.mymusic.activity;

import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.WindowManager;

import com.luckyliuqs.mymusic.MainActivity;
import com.luckyliuqs.mymusic.R;
import com.luckyliuqs.mymusic.Util.PackageUtil;
import com.luckyliuqs.mymusic.adapter.GuideAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import me.relex.circleindicator.CircleIndicator;

/**
 * 引导页面，只在首次安装或更新后才会显示，且只会显示一次，其他情况下引导页面不再显示
 */
public class GuideActivity extends BaseCommonActivity {
    /**
     * 创建ViewPager对象
     */
    private ViewPager viewPager;
    /**
     * 创建GuideAdapter对象
     */
    private GuideAdapter adapter;
    /**
     * 创建下角标对象
     */
    private CircleIndicator indicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);
        //去除状态栏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getActivity();
    }

    @Override
    protected void initViews() {
        super.initViews();
        viewPager = findViewById(R.id.vp_guide);
        indicator = findViewById(R.id.indicator);
    }

    /**
     * 初始化数据
     */
    @Override
    protected void initDatas() {
        super.initDatas();
        //传入Context和FragmentManager，实例化GuideAdapter对象
        adapter = new GuideAdapter(getActivity(), getSupportFragmentManager());
        //为ViewPager添加适配器
        viewPager.setAdapter(adapter);
        //为下角标添加ViewPager
        indicator.setViewPager(viewPager);
        //Register an observer to receive callbacks related to the adapter's data changing.
        adapter.registerDataSetObserver(indicator.getDataSetObserver());
        //创建ArrayList储存图片资源ID
        List<Integer> datas = new ArrayList<>();
        datas.add(R.drawable.guide1);
        datas.add(R.drawable.guide2);
        datas.add(R.drawable.guide3);
        datas.add(R.drawable.bg_1);
        datas.add(R.drawable.bg_2);
        datas.add(R.drawable.bg_3);
        //adapter设置数据
        adapter.setDatas(datas);
    }

    /**
     *
     */
    @OnClick(R.id.bt_login_or_register)
    public void loginOrRegisterBtn(){
        setGuideNotShow();
        //跳转到登录注册页面，并将引导页面finish
        startActivityAfterFinishThis(LoginActivity.class);
    }

    @OnClick(R.id.bt_tourist_login)
    public void tourustExperienceBtn(){
        setGuideNotShow();
        //跳转到首页，并将引导页面finish
        startActivityAfterFinishThis(MainActivity.class);
    }

    /**
     * 设置此版本下引导页面不再显示
     */
    private void setGuideNotShow(){
        //储存版本信息及false
        sp.putBoolean(String.valueOf(PackageUtil.getVersionCode(getApplicationContext())),false);
    }

    /**
     * 重写并不再调用父类的方法，实现用户点击返回键不能关闭当前的页面功能
     */
    @Override
    public void onBackPressed() {

    }
}
