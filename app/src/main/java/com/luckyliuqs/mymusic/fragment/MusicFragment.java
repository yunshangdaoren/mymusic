package com.luckyliuqs.mymusic.fragment;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.luckyliuqs.mymusic.R;
import com.luckyliuqs.mymusic.adapter.MusicUIAdapter;

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
 * 音乐Fragment页面
 */
public class MusicFragment extends BaseCommonFragment{
    //指示器MagicIndicator
    private MagicIndicator tabs;
    //ViewPager
    private ViewPager vp;

    //Adapter
    private MusicUIAdapter adapter;

    public static MusicFragment newInstance() {
        
        Bundle args = new Bundle();

        MusicFragment fragment = new MusicFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void initViews() {
        super.initViews();
        tabs = findViewById(R.id.tabs);
        vp = findViewById(R.id.vp);
    }

    @Override
    protected void initDatas() {
        super.initDatas();
        //这里要使用childFragmentManager
        adapter = new MusicUIAdapter(getActivity(), getChildFragmentManager());
        vp.setAdapter(adapter);

        final ArrayList<Integer> datas = new ArrayList<>();
        datas.add(0);
        datas.add(1);
        datas.add(2);
        //为Adapter设置data
        adapter.setDatas(datas);

        //将TabLayout与ViewPager关联起来
        CommonNavigator commonNavigator = new CommonNavigator(getMainActivity());
        commonNavigator.setAdapter(new CommonNavigatorAdapter() {

            @Override
            public int getCount() {
                return datas.size();
            }

            @Override
            public IPagerTitleView getTitleView(Context context, final int index) {
                ColorTransitionPagerTitleView colorTransitionPagerTitleView = new ColorTransitionPagerTitleView(context);

                //默认的颜色
                colorTransitionPagerTitleView.setNormalColor(Color.WHITE);

                //设置选中后的颜色
                colorTransitionPagerTitleView.setSelectedColor(Color.WHITE);
                //设置标题
                colorTransitionPagerTitleView.setText(adapter.getPageTitle(index));
                //设置点击监听事件
                colorTransitionPagerTitleView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //设置ViewPager当前的Fragment
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

        //将TabLayout与ViewPager绑定
        ViewPagerHelper.bind(tabs, vp);
    }

    @Override
    protected View getLayoutView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_music,null);
    }
}


















