package com.clubank.device;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.clubank.dingxi.R;

/**
 * 首页轮播图详情页
 */
public class InformationViewActivity extends BaseActivity {
    private WebView webView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_information_view);
        Bundle b = getIntent().getExtras();
        String url = "";
        if (null != b) {
            url = b.getString("URL");
        }
        final LinearLayout prolay = findViewById(R.id.pro_lay);
        final ProgressBar bar = new ProgressBar(this, null,
                android.R.attr.progressBarStyleHorizontal);
        bar.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));

        Drawable drawable = this.getResources().getDrawable(R.drawable.blue_progress);
        bar.setProgressDrawable(drawable);
        prolay.addView(bar);
        webView = findViewById(R.id.webView);
        // 不使用缓存
        webView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        webView.getSettings().setAllowFileAccess(true);//允许访问文件
        webView.getSettings().setAllowFileAccessFromFileURLs(true);
        webView.getSettings().setAllowUniversalAccessFromFileURLs(true);
        webView.getSettings().setDomStorageEnabled(true);//支持h5
        webView.getSettings().setAppCacheEnabled(true);
        webView.getSettings().supportMultipleWindows();
        webView.getSettings().setAllowContentAccess(true);
        webView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
        // 自适应屏幕
        webView.getSettings().setUseWideViewPort(true);
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setSavePassword(true);
        webView.getSettings().setSaveFormData(true);
        webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        webView.getSettings().setLoadsImagesAutomatically(true);
        // 支持JavaScript
        webView.getSettings().setJavaScriptEnabled(true);
        // 不支持缩放(如果要支持缩放，html页面本身也要支持缩放：不能加user-scalable=no)
        webView.getSettings().setBuiltInZoomControls(false);
        webView.getSettings().setSupportZoom(false);
        webView.getSettings().setDisplayZoomControls(false);
        // 隐藏scrollbar
        webView.setVerticalScrollBarEnabled(false);
        webView.setHorizontalScrollBarEnabled(false);
        webView.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
        webView.setBackgroundColor(Color.parseColor("#FFFFFF")); // 设置背景色避免闪屏
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return false;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
            }
        });
        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                if (newProgress == 100) {
                    prolay.setVisibility(View.GONE);
                } else {
                    if (View.GONE == prolay.getVisibility()) {
                        prolay.setVisibility(View.VISIBLE);
                    }
                    bar.setProgress(newProgress);
                }
            }
        });

        if (!TextUtils.isEmpty(url)) {
            if (url.startsWith("http://") || url.startsWith("https://")) {
                webView.loadUrl(url);
            } else {
                try {
                    webView.loadDataWithBaseURL(
                            null,
                            "<html><head></head><body><font  size=\"3\">"
                                    + url
                                    + "<p align=\"center\"><strong><style type=\"text/css\">img {max-width:100%; max-height:auto;}</style></strong>&nbsp;</p></font></body></html>",
                            "text/html", "utf-8", null);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }


    @Override
    protected void onPause() {
        super.onPause();
        webView.pauseTimers();
        if (isFinishing()) {
            webView.loadUrl("about:blank");
            setContentView(new FrameLayout(this));
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        webView.resumeTimers();
    }

}



