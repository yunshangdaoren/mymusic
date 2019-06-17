package com.luckyliuqs.mymusic.activity;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.luckyliuqs.mymusic.R;
import com.luckyliuqs.mymusic.adapter.DownloadManagerAdapter;

import net.lucode.hackware.magicindicator.MagicIndicator;
import net.lucode.hackware.magicindicator.ViewPagerHelper;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.ColorTransitionPagerTitleView;

import java.util.ArrayList;

/**
 * 下载管理Activity页面
 */
public class DownloadManagerActivity extends BaseTitleActivity {
    private ViewPager viewPager;
    MagicIndicator tabs;
    private DownloadManagerAdapter adapater;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download_manager);
    }

    @Override
    protected void initViews() {
        super.initViews();
        enableBackMenu();

        tabs = findViewById(R.id.tabs_download);
        viewPager = findViewById(R.id.vp_download);
        viewPager.setOffscreenPageLimit(2);
    }

    @Override
    protected void initDatas() {
        super.initDatas();
        adapater = new DownloadManagerAdapter(getActivity(), getSupportFragmentManager());
        viewPager.setAdapter(adapater);
        fetchData();
    }

    private void fetchData(){
        final ArrayList<Integer> datas = new ArrayList<>();
        datas.add(0);
        datas.add(1);
        adapater.setDatas(datas);

        //将TabLayout与ViewPager关联起来
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
                //设置标题
                colorTransitionPagerTitleView.setText(adapater.getPageTitle(index));
                //设置点击事件
                colorTransitionPagerTitleView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //设置ViewPager当前页面
                        viewPager.setCurrentItem(index);
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

        ViewPagerHelper.bind(tabs, viewPager);

    }

}























