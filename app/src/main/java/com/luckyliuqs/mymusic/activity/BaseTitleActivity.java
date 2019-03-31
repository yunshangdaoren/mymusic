package com.luckyliuqs.mymusic.activity;


import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;

import com.luckyliuqs.mymusic.R;

import butterknife.BindView;

/**
 * 标题栏基类
 */
public class BaseTitleActivity extends BaseCommonActivity{

    @BindView(R.id.toolbar)
    protected Toolbar toolbar;

    @Override
    protected void initViews() {
        super.initViews();
        //设置标题栏
        setSupportActionBar(toolbar);
    }

    /**
     * 设置标题
     * @param title
     */
    @Override
    public void setTitle(CharSequence title) {
        if(!TextUtils.isEmpty(title)){
            super.setTitle(title);
        }
    }

    /**
     * 将返回菜单设置显示
     */
    protected void enableBackMenu(){
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
