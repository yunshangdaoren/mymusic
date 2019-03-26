package com.luckyliuqs.mymusic.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.luckyliuqs.mymusic.R;
import com.luckyliuqs.mymusic.Util.Consts;
import com.luckyliuqs.mymusic.Util.ImageUtil;
import com.luckyliuqs.mymusic.Util.LogUtil;

/**
 * 引导页面的Fragment类
 */
public class GuideFragment extends BaseCommonFragment{
    ImageView iv;
    private Integer imageId;

    /**
     * 获取GuideFragment对象
     * @param imageId
     * @return
     */
    public static GuideFragment newInstance(int imageId){
        Bundle args = new Bundle();
        args.putSerializable(Consts.ID,imageId);
        GuideFragment fragment = new GuideFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void initViews() {
        super.initViews();
        iv = findViewById(R.id.iv);
    }

    @Override
    protected void initDatas() {
        super.initDatas();
        imageId = getArguments().getInt(Consts.ID,-1);
        if(imageId == -1){
            LogUtil.w("Image id can not be empty!");
            getMainActivity().finish();
            return;
        }

        ImageUtil.showLocalImage(getMainActivity(),iv,imageId);
    }

    @Override
    public void initListener() {
        super.initListener();
    }

    @Override
    protected View getLayoutView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_guide,null);
    }
}
