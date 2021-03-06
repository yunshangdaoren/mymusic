package com.luckyliuqs.mymusic.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.luckyliuqs.mymusic.Util.Consts;
import com.luckyliuqs.mymusic.activity.BaseActivity;

/**
 * Fragment基类
 */
public abstract class BaseFragment extends Fragment{
    /**
     * 初始化控件
     */
    protected void initViews(){
    }

    /**
     * 初始化样式：颜色，宽高，背景等等
     */
    protected void initStyles(){
    }

    /**
     * 初始化数据
     */
    protected void initDatas(){
    }

    /**
     * 初始化监听器
     */
    public void initListener(){
    }

    /**
     * 当Fragment正在运行时调用
     */
    @Override
    public void onResume() {
        super.onResume();
    }

    /**
     * Fragment加载布局时调用
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return View
     */
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = getLayoutView(inflater,container,savedInstanceState);
        return view;
    }

    /**
     * 进行所有的初始化操作，同时调用View初始化完成的方法
     * @param view
     * @param savedInstanceState
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        initViews();
        initStyles();
        initDatas();
        initListener();
        super.onViewCreated(view, savedInstanceState);
    }

    /**
     * 返回要显示的View
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return View
     */
    protected abstract View getLayoutView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState);


    /**
     * 封装findViewById，继承BaseFragment的子类直接调用此方法即可，前面不用再使用view.findViewById
     * @param id
     * @param <T>
     * @return 指定ID的控件
     */
    public final <T extends View> T findViewById(int id){
        return getView().findViewById(id);
    }

    /**
     *Fragment被移除时调用
     */
    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    /**
     * 带ID参数信息的Activity之间跳转
     * @param clazz
     * @param id
     */
    protected void startActivityExtraId(Class<?> clazz, String id) {
        Intent intent = new Intent(getActivity(), clazz);
        if (!TextUtils.isEmpty(id)) {
            intent.putExtra(Consts.ID,id);
        }
        startActivity(intent);
    }

    /**
     * 指定Activity类名进行跳转，并finish本身
     * @param clazz
     */
    protected void startActivityAfterFinishThis(Class<?> clazz) {
        startActivity(new Intent(getActivity(),clazz));
        getActivity().finish();
    }
    /**
     * 指定Activity类名进行跳转
     * @param clazz
     */
    protected void startActivity(Class<?> clazz) {
        Intent intent = new Intent(getActivity(), clazz);
        startActivity(intent);
    }

    /**
     * 获取到BaseActivity对象
     * @return
     */
    public BaseActivity getMainActivity() {
        return (BaseActivity) getActivity();
    }
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            onVisibleToUser();
        } else {
            onInvisibleToUser();
        }
    }

    protected void onInvisibleToUser() {
    }

    protected void onVisibleToUser() {
    }

}
