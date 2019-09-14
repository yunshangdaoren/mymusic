package com.luckyliuqs.mymusic.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;

import com.luckyliuqs.mymusic.R;
import com.luckyliuqs.mymusic.Util.Consts;

public class BaseActivity extends AppCompatActivity{
    //创建进度对话框对象
    private ProgressDialog progressDialog;

    /**
     * 初始化控件
     */
    protected void initViews(){ }

    /**
     * 初始化数据
     */
    protected void initDatas(){}

    /**
     * 绑定监听器
     */
    protected void initListener(){}

    /**
     * 调用初始化方法进行初始化操作
     */
    private void init(){
        initViews();
        initDatas();
        initListener();
    }

    /**
     * 通过布局文件ID进行初始化操作
     * @param layoutResID
     */
    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        //在加载布局的时候就自动进行初始化操作
        init();
    }

    /**
     * 通过View对象进行初始化操作
     * @param view
     */
    @Override
    public void setContentView(View view) {
        super.setContentView(view);
        //在加载布局的时候就自动进行初始化操作
        init();
    }
    /**
     * 通过View对象和LayoutParams对象进行初始化操作
     * @param view
     */
    @Override
    public void setContentView(View view, ViewGroup.LayoutParams params) {
        super.setContentView(view, params);
        //在加载布局的时候就自动进行初始化操作
        init();
    }

    /**
     * 当前Activity获取焦点开始与用户进行交互时调用
     */
    @Override
    protected void onResume() {
        super.onResume();
    }

    /**
     * 当前Activity被其他Activity覆盖或锁屏时调用
     */
    @Override
    protected void onPause() {
        super.onPause();
    }

    /**
     * 获取Activity
     * @return
     */
    protected BaseActivity getActivity(){
        return this;
    }

    /**
     * 通过传入的类进行跳转，没有处理finish
     * @param clazz
     */
    protected void startActivity(Class<?> clazz){
        startActivity(new Intent(getActivity(),clazz));
    }
    /**
     * 通过传入的类进行跳转页面,并处理finish
     * @param clazz
     */
    protected void startActivityAfterFinishThis(Class<?> clazz){
        startActivity(new Intent(getActivity(),clazz));
        //关闭现在的Activity
        finish();
    }

    /**
     * 过传入的类和String参数进行跳转页面
     * @param clazz
     * @param string
     */
    protected void startActivityExtraString(Class<?> clazz,String string){
        Intent intent = new Intent(getActivity(),clazz);
        intent.putExtra(Consts.STRING,string);
        startActivity(intent);
    }

    protected void startActivityExtraId(Class<?> clazz, String id){
        Intent intent = new Intent(getActivity(), clazz);
        intent.putExtra(Consts.ID, id);
        startActivity(intent);
    }


    public void showLoading(){
        showLoading(getResources().getString(R.string.loading));
    }
    public void showLoading(String message) {
//        if (getActivity() != null && !getActivity().isFinishing()) {
//            progressDialog = new ProgressDialog(this);
//            progressDialog.setTitle("提示");
//            progressDialog.setMessage(message);
//            progressDialog.setCancelable(false);
//            progressDialog.show();
//        }
    }
    public void showLoading(int resId) {
        showLoading(getResources().getString(resId));
    }

    public void hideLoading() {
//        if (progressDialog != null && progressDialog.isShowing()) {
//            progressDialog.hide();
//        }
    }

}
