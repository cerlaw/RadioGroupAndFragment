package com.example.zhanghongjie.radiogroupandfragment;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.example.zhanghongjie.radiogroupandfragment.Utils.DataHelper;
import com.example.zhanghongjie.radiogroupandfragment.Utils.WebViewUtil;

import java.io.IOException;
import java.io.InputStream;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author zhanghongjie
 * @date 2018/9/14
 * @descrition
 */
public class WebViewActivity extends AppCompatActivity {

    private static final String TAG = "WebViewActivity";
    private final String URL = "http://renyugang.io/post/75";

    @BindView(R.id.webView)
    WebView mWebView;

    @BindView(R.id.tv_loadingTime)
    TextView tv_loadingTime;

    private boolean isUseLocalResource;
    private long mStartTime;
    private long mEndTime;
    private DataHelper mDataHelper;

    public static void startAction(Context context, boolean isUseLocalResource) {
        Intent intent = new Intent(context, WebViewActivity.class);
        intent.putExtra("localResource", isUseLocalResource);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.webview_activity);
        ButterKnife.bind(this);
        isUseLocalResource = getIntent().getBooleanExtra("localResource", false);
        init();
    }

    private void init() {
        WebViewUtil.configWebView(mWebView);
        mDataHelper = new DataHelper();
        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                Log.d(TAG, "page start");
                mStartTime = System.currentTimeMillis();
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                Log.d(TAG, "page finish");
                mEndTime = System.currentTimeMillis();
                long loadingTime = mEndTime - mStartTime;
                tv_loadingTime.setText(String.format("加载耗时%d毫秒", loadingTime));
                mStartTime = 0;
                mEndTime = 0;
            }

            //设置不用系统浏览器打开，直接显示在当前webView
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                Log.d(TAG, "shouldOverrideUrlLoading : url = " + url);
                view.loadUrl(url);
                return true;
            }

            @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                Log.d(TAG, "shouldOverrideUrlLoading: url = " + request.getUrl());
                return super.shouldOverrideUrlLoading(view, request);
            }

            @Nullable
            @Override
            public WebResourceResponse shouldInterceptRequest(WebView view, String url) {
                if (!isUseLocalResource) {
                    return super.shouldInterceptRequest(view, url);
                } else {
                    if (mDataHelper.hasLocalResource(url)) {
                        Log.d(TAG, "shouldInterceptRequest1: 资源命中");
                        WebResourceResponse response =
                                mDataHelper.getReplacedWebResourceResponse(getApplicationContext(),
                                        url);
                        if (response != null) {
                            return response;
                        }
                    }
                }
                return super.shouldInterceptRequest(view, url);
            }

            @TargetApi(Build.VERSION_CODES.LOLLIPOP)
            @Override
            public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request) {
                String url = request.getUrl().toString();
                Log.d(TAG, "shouldInterceptRequest2: url = " + url);
                if (!isUseLocalResource) {
                    return super.shouldInterceptRequest(view, request);
                }
                if (mDataHelper.hasLocalResource(url)) {
                    Log.d(TAG, "shouldInterceptRequest2: 资源命中");
                    WebResourceResponse response =
                            mDataHelper.getReplacedWebResourceResponse(getApplicationContext(),
                                    url);
                    if (response != null) {
                        return response;
                    }
                }
                return super.shouldInterceptRequest(view, request);
            }

            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                handler.proceed();
            }
        });

        // 设置WebChromeClient类
        mWebView.setWebChromeClient(new WebChromeClient() {
            // 获取网站标题
            @Override
            public void onReceivedTitle(WebView view, String title) {
            }

            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if (newProgress < 100) {
                    String progress = newProgress + "%";
                } else if (newProgress == 100) {
                    String progress = newProgress + "%";
                }
            }
        });
        String url = URL;
        mWebView.loadUrl(url);
//        mStartTime = System.currentTimeMillis();
    }

    @NonNull
    private WebResourceResponse getReplacedWebResourceResponse() {
        InputStream is = null;
        // 步骤2:创建一个输入流
        try {
            is = getApplicationContext().getAssets().open("images/cropped-ryg.png");
        } catch (IOException e) {
            e.printStackTrace();
        }
        // 步骤4:替换资源
        WebResourceResponse response = new WebResourceResponse("images/png", "utf-8", is);
        return response;
    }

    // 点击返回上一页面而不是退出浏览器
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && mWebView.canGoBack()) {
            mWebView.goBack();
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }

    // 销毁Webview
    @Override
    protected void onDestroy() {
        if (mWebView != null) {
            mWebView.loadDataWithBaseURL(null, "", "text/html", "utf-8", null);
            mWebView.clearHistory();

            ViewParent parent = mWebView.getParent();
            if (parent != null) {
                ((ViewGroup) parent).removeView(mWebView);
            }
            mWebView.destroy();
            mWebView = null;
        }
        super.onDestroy();

    }
}
