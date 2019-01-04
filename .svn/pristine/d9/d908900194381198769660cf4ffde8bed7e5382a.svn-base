package com.clubank.device;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.clubank.device.op.AppPrivilege;
import com.clubank.device.op.GetUserInfo;
import com.clubank.device.op.LessonInfo;
import com.clubank.dingxi.R;
import com.clubank.domain.BC;
import com.clubank.domain.C;
import com.clubank.domain.Result;
import com.clubank.domain.User;
import com.clubank.util.MyAsyncTask;
import com.clubank.util.MyData;
import com.clubank.util.MyRow;
import com.clubank.util.UI;
import com.clubank.view.CycleViewPager;

/**
 * 课程订单列表----课程详情
 */
public class ClassOrderDetailActivity extends BaseActivity implements CycleViewPager.ImageCycleViewListener {
    private WebView webView;
    private CycleViewPager cycleViewPager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class_order_detail);
        initView();
        Bundle b = getIntent().getExtras();
        if (null != b) {
            int Leid = b.getInt("Leid");
            new MyAsyncTask(this, LessonInfo.class).run(Leid);
        }
    }

    private void initView() {
        initImageBanner();
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
        webView.setWebChromeClient(new WebChromeClient());
    }

    private void initImageBanner() {
        LinearLayout ll = findViewById(R.id.banner_ll);
        ll.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, screen.getHeight() / 3));
        cycleViewPager = (CycleViewPager) getFragmentManager()
                .findFragmentById(R.id.cycle_viewpager);
        C.baseImageUrl = "";// 图片没有前缀
        cycleViewPager.setPicUrl("ImgUrl");// 设置图片路径属性名称默认PicUrl
        cycleViewPager.setPicText("");
    }

    @Override
    public void onPostExecute(Class<?> op, Result result) {
        if (op == LessonInfo.class) {
            if (result.code == BC.SUCCESS) {
                MyRow ro = ((MyData) result.obj).get(0);
                setEText(R.id.tv_title, ro.getString("LessonName"));
                setEText(R.id.tv_price, ro.getString("Price"));
                setEText(R.id.tv_teacher_name, ro.getString("CoachNameTemp"));
                setEText(R.id.tv_class_type, ro.getString("LessonTypeName"));
                setEText(R.id.tv_if_booking, ro.getInt("NeedApply") == 0 ? "否" : "是");
                setEText(R.id.tv_valid_date, ro.getInt("EffectCount") + "天");
                setEText(R.id.tv_time_long, ro.getInt("LessonHour") + "节");
                setEText(R.id.tv_every_long, ro.getInt("SingleClassHour") + "分钟");
                setEText(R.id.tv_suit_people, ro.getString("FitCrowd"));
                MyData pics = new MyData();
                MyRow r = new MyRow();
                r.put("ImgUrl", ro.getString("ImgUrl"));
                pics.add(r);
                setDataBanner(pics);
                setWebview(ro.getString("LessonContent"));
            } else {
                UI.showToast(this, result.msg);
            }
        }
    }

    private void setDataBanner(MyData pics) {
        cycleViewPager.indicatorColor(R.drawable.background_cycle_gray, R.drawable.background_cycle_white);
        cycleViewPager.setData(pics, this);
        cycleViewPager.setCycle(true);
        // 设置轮播时间，默认2500ms112488
        cycleViewPager.setTime(5000);
        // 设置自动轮播不设置则不自动播放
        cycleViewPager.setWheel(false);
    }

    private void setWebview(String url) {
        if (url.startsWith("http://") || url.startsWith("https://")) {
            webView.loadUrl(url);
        } else {
            if (!TextUtils.isEmpty(url)) {
                String html = "<html><p><img src=\"" + url +
                        "\"/></p><style>img{max-width:100%; max-height:auto; scale:expression((this.offsetWidth > this.offsetHeight)?(this.style.width = this.offsetWidth >= " +
                        screen.getWidth() + " ? \" " +
                        screen.getWidth() + "\" : \"auto\"):(this.style.height = this.offsetHeight >= 8000 ? \"8000px\" : \"auto\")); display:inline !important;}</style></p></html>";
                try {
                    webView.loadDataWithBaseURL(null, html, "text/html", "utf-8", null);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                webView.setVisibility(View.VISIBLE);
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

    @Override
    public void onImageClick(MyRow info, int postion, View imageView) {

    }
}



