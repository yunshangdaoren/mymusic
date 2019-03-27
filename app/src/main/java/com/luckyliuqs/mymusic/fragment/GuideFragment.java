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
    /**
     * 定义引导页面的Fragment布局里面ImageView对象
     */
    ImageView iv;
    /**
     * ImageView资源ID
     */
    private Integer imageId;

    /**
     * 通过传入一个ImageView资源ID创建并返回一个GuideFragment对象
     * @param imageId
     * @return GuideFragment
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
        iv = findViewById(R.id.iv_fragment_guide);
    }

    @Override
    protected void initDatas() {
        super.initDatas();
        //获取ImageView资源ID，默认返回-1
        imageId = getArguments().getInt(Consts.ID,-1);
        if(imageId == -1){
            //ImageView资源ID信息不存在
            LogUtil.w("Image id can not be empty!");
            getMainActivity().finish();
            return;
        }
        //通过ID获取到本地图片，展示到ImageView控件上
        ImageUtil.showLocalImage(getMainActivity(),iv,imageId);
    }

    @Override
    public void initListener() {
        super.initListener();
    }

    /**
     * 获得Fragment布局的View
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Override
    protected View getLayoutView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //传入Fragment布局文件ID
        return inflater.inflate(R.layout.fragment_guide,null);
    }
}
