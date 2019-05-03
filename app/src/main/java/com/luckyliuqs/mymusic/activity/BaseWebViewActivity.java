package com.luckyliuqs.mymusic.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.luckyliuqs.mymusic.R;
import com.luckyliuqs.mymusic.Util.Consts;

public class BaseWebViewActivity extends BaseTitleActivity {
    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base_web_view);
    }

    public static void start(Activity activity, String title, String url){
        Intent intent = new Intent(activity, BaseWebViewActivity.class);
        intent.putExtra(Consts.TITLE, title);
        intent.putExtra(Consts.URL, url);
        activity.startActivity(intent);
    }

    @Override
    protected void initViews() {
        super.initViews();
        enableBackMenu();

        webView = findViewById(R.id.webview);
        WebSettings webSettings = webView.getSettings();
        webSettings.setAllowFileAccess(true);
        webSettings.setSupportMultipleWindows(true);
        webSettings.setJavaScriptEnabled(true);
        webSettings.setBlockNetworkImage(false);
        webSettings.setAllowUniversalAccessFromFileURLs(true);
        webSettings.setAllowFileAccessFromFileURLs(true);
        webSettings.setDomStorageEnabled(true);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            webSettings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
    }

    @Override
    protected void initDatas() {
        super.initDatas();
        //获取传递进来的标题
        String title = getIntent().getStringExtra(Consts.TITLE);
        //获取传递进来的URL
        String url = getIntent().getStringExtra(Consts.URL);

        //设置标题
        setTitle(title);

        if (!TextUtils.isEmpty(url)){
            //URL非空，则加载URL
            webView.loadUrl(url);
        }else{
            //URL为空，则finish
            finish();
        }

    }
}
