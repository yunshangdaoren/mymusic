package com.luckyliuqs.mymusic.activity;

import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.WindowManager;

import com.luckyliuqs.mymusic.R;
import com.luckyliuqs.mymusic.adapter.GuideAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import me.relex.circleindicator.CircleIndicator;

/**
 * 引导页面
 */
public class GuideActivity extends BaseCommonActivity {
    //@BindView(R.id.vp_guide_index)
    private ViewPager viewPager;
    private GuideAdapter adapter;
    //@BindView(R.id.indicator)
    private CircleIndicator indicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);
        //去除状态栏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);


    }

    @Override
    protected void initViews() {
        super.initViews();
        viewPager = findViewById(R.id.vp_guide_index);
        indicator = findViewById(R.id.indicator);
    }

    @Override
    protected void initDatas() {
        super.initDatas();
        adapter = new GuideAdapter(getActivity(), getSupportFragmentManager());
        viewPager.setAdapter(adapter);

        indicator.setViewPager(viewPager);
        adapter.registerDataSetObserver(indicator.getDataSetObserver());

        List<Integer> datas = new ArrayList<>();
        datas.add(R.drawable.guide1);
        datas.add(R.drawable.guide2);
        datas.add(R.drawable.guide3);
        adapter.setDatas(datas);

    }
}
