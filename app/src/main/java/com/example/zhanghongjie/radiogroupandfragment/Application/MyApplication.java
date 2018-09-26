package com.example.zhanghongjie.radiogroupandfragment.Application;

import android.app.Application;
import android.webkit.WebView;
import android.webkit.WebViewClient;

/**
 * @author zhanghongjie
 * @date 2018/9/14
 * @descrition
 */
public class MyApplication extends Application {

    private final String URL = "http://renyugang.io/post/75";
    private static WebView mWebView;

    @Override
    public void onCreate() {
        super.onCreate();
        //提前加载webView，提高打开速度
        mWebView = new WebView(this);
        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });
    }

    public static WebView getWebView() {
        return mWebView;
    }

    public static void destoryWebView(WebView webView) {
        if (webView != null) {
            webView.loadDataWithBaseURL(null, "", "text/html", "utf-8", null);
            webView.clearHistory();
            webView.destroy();
        }
    }
}
