package com.clubank.device;

import android.Manifest;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.clubank.util.VersionUtil;
import com.clubank.view.AlertDialog;
import com.clubank.View.ListViewInScrollView;
import com.clubank.device.op.AppPrivilege;
import com.clubank.device.op.GetUserInfo;
import com.clubank.device.op.Homepage;
import com.clubank.device.op.Logout;
import com.clubank.device.op.ProcessTips;
import com.clubank.device.op.UploadUserHeader;
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
import com.google.gson.internal.LinkedTreeMap;
import com.youth.banner.Banner;

import java.util.ArrayList;

public class MainActivity extends CamareAndCropActivity implements AdapterView.OnItemClickListener, CycleViewPager.ImageCycleViewListener {
    private Banner banner;
    private MainListAdapter adapter;
    private DrawerLayout mDrawerLayout;
    private CycleViewPager cycleViewPager;
    private MyRow readMsgRow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setHeaderTitle(R.string.app_name);
        saveSetting("checkVersion", true);//进入主界面后开启版本检测
        hide(R.id.header_back);
        initView();
        refreshData();
    }

    @Override
    public void refreshData() {
        super.refreshData();
        new MyAsyncTask(this, GetUserInfo.class).run(User.getIn().getToken());

    }

    @Override
    protected void onResume() {
        super.onResume();
        new MyAsyncTask(this, ProcessTips.class).run();
    }

    private void initView() {
        initEvents();
        readMsgRow = new MyRow();
        ListViewInScrollView listview = findViewById(R.id.main_list);
        listview.setOnItemClickListener(this);
        adapter = new MainListAdapter(this, new MyData());
        listview.setAdapter(adapter);

        ImageView photoBg = findViewById(R.id.photo_bg);
        photoBg.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, screen.getHeight() / 3 + 50));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {//状态栏透明
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
            int statusBarHeight = -1;
            //获取status_bar_height资源的ID
            int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
            if (resourceId > 0) {
                //根据资源ID获取响应的尺寸值
                statusBarHeight = getResources().getDimensionPixelSize(resourceId);
                findViewById(R.id.status_view).setMinimumHeight(statusBarHeight);
            } else {
                statusBarHeight = UI.toPixel(this, 24);
                findViewById(R.id.status_view).setMinimumHeight(statusBarHeight);
            }
        }
        initImageBanner();
    }

    private void initImageBanner() {
        LinearLayout ll = findViewById(R.id.banner_ll);
        ll.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, screen.getHeight() / 3));
        cycleViewPager = (CycleViewPager) getFragmentManager()
                .findFragmentById(R.id.cycle_viewpager);
        C.baseImageUrl = "";// 图片没有前缀
        cycleViewPager.setPicUrl("NewPhoto");// 设置图片路径属性名称默认PicUrl
        cycleViewPager.setPicText("NewsTitle");
    }

    private void setDataBanner(MyData pics) {
        cycleViewPager.indicatorColor(R.drawable.background_cycle_gray, R.drawable.background_cycle_white);
        cycleViewPager.setData(pics, this);
        cycleViewPager.setCycle(true);
        // 设置轮播时间，默认2500ms112488
        cycleViewPager.setTime(5000);
        // 设置自动轮播不设置则不自动播放
        cycleViewPager.setWheel(true);
    }

    private void initEvents() {
        mDrawerLayout = findViewById(R.id.drawer_ll);
        mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED,
                Gravity.RIGHT);
        mDrawerLayout.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                Log.d("wyy", "侧拉菜单打开了");
                drawerView.setClickable(true);
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                Log.d("wyy", "侧拉菜单关闭了");
                drawerView.setClickable(false);
            }

            @Override
            public void onDrawerStateChanged(int newState) {
            }
        });
    }

    @Override
    public void onPostExecute(Class<?> op, Result result) {
        super.onPostExecute(op, result);
        if (op == AppPrivilege.class) {
            if (result.code == BC.SUCCESS) {
                MyData data = (MyData) result.obj;
                if (null != adapter) {
                    adapter.clear();
                }
                for (int i = 0; i < data.size(); i++) {
                    if (readMsgRow != null) {
                        String code = data.get(i).getString("Code");
                        switch (code) {
                            case "AppPrivielge_01"://活动申请
                                data.get(i).put("reminds", readMsgRow.getInt("actApplyCount"));//活动申请
                                break;
                            case "AppPrivielge_02"://活动审批
                                data.get(i).put("reminds", readMsgRow.getInt("actReviewCount"));//活动审批
                                break;
                            case "AppPrivielge_03"://审批记录
                                data.get(i).put("reminds", readMsgRow.getInt("actRecordeCount"));//审批记录
                                break;
                            default:
                                data.get(i).put("reminds", 0);//审批记录
                                break;
                        }
                    }
                    adapter.add(data.get(i));
                }
                adapter.notifyDataSetChanged();
            } else {
                UI.showToast(this, result.msg);
            }
        } else if (op == GetUserInfo.class) {
            if (result.code == BC.SUCCESS) {
                MyData data = (MyData) result.obj;
                MyRow ro = data.get(0);
                User.getIn().setMobileNo(ro.getString("MobileNo"));
                User.getIn().setName(ro.getString("UserName"));
                User.getIn().setHeadIcon(ro.getString("Pic"));
                setEText(R.id.name, ro.getString("UserName"));
                setEText(R.id.phone, ro.getString("MobileNo"));
                setImage(R.id.member_header, ro.getString("Pic"), R.mipmap.icon_member_input);
                setImage(R.id.head_photo, ro.getString("Pic"), R.mipmap.icon_member_input);
//                setEText(R.id.vip_type, "普通会员");
                new MyAsyncTask(this, Homepage.class).run();
                new MyAsyncTask(this, ProcessTips.class).run();
            } else {
                UI.showToast(this, result.msg);
            }
        } else if (op == Homepage.class) {
            if (result.code == BC.SUCCESS) {
                MyData data = (MyData) result.obj;
                setDataBanner(data);
            }
        } else if (op == Logout.class) {
            if (result.code == BC.SUCCESS) {
                User.getIn().setToken("");
                saveSetting("Token", "");
                finish();
                Bundle b=new Bundle();
                b.putBoolean("isLogout",true);
                openIntent(LoginActivity.class, "",b);
            } else {
                UI.showToast(this, result.msg);
            }
        } else if (op == ProcessTips.class) {//首页小红点提示
            if (result.code == BC.SUCCESS) {
                readMsgRow = ((MyData) result.obj).get(0);
            } else {
                UI.showToast(this, result.msg);
            }
            new MyAsyncTask(this, AppPrivilege.class).run();
        } else if (op == UploadUserHeader.class) {//上传用户头像
            if (result.code == BC.SUCCESS) {
                Object ob = ((ArrayList) result.obj).get(0);
                setImage(R.id.head_photo, ((LinkedTreeMap) ob).get("Pic"));
                setImage(R.id.member_header, ((LinkedTreeMap) ob).get("Pic"));
            } else {
                UI.showToast(this, result.msg);
            }
        }
    }

    public void doWork(View view) {
        switch (view.getId()) {
            case R.id.head_photo_ll://打开侧边栏
                mDrawerLayout.openDrawer(Gravity.LEFT);
                mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED,
                        Gravity.LEFT);
                break;
            case R.id.head_photo://更换用户头像
                getPictureView = view;
                checkAllNeedPermissions();
                break;
            case R.id.member_info_ll://个人信息
                UI.showToast(this, "个人信息");
                break;
            case R.id.modify_pass_ll://修改密码
                UI.showToast(this, "修改密码");
                break;
            case R.id.login_out://注销登陆
                showAlert();
                break;
            case R.id.version_code://查看当前版本号
                UI.showToast(this, VersionUtil.getVersionCode(this)+"");
                break;
        }
    }

    public void showAlert() {
        final AlertDialog ad = new AlertDialog(this);
        ad.setTitle(getString(R.string.sure_loginout));
        ad.setNegativeButton("取消", 0, 0, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ad.dismiss();
            }
        });
        ad.setPositiveButton("确认", 0, 0, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new MyAsyncTask(MainActivity.this, Logout.class).run();
                ad.dismiss();
            }
        });
    }

    @Override
    protected String[] getNeedPermissions() {
        return new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};
    }

    @Override
    protected void permissionGrantedSuccess() {
        isCrop = true;
        int[] images = new int[]{com.clubank.common.R.drawable.take_picture,
                com.clubank.common.R.drawable.choose_picture};
        getPicture(getPictureView, getResources().getStringArray(R.array.takePic), images);
    }

    protected void getPicture(View view, String[] pic_options, int[] images) {
        showListDialog(view, pic_options, images);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        MyRow row = (MyRow) adapterView.getItemAtPosition(i);
        String code = row.getString("Code");
        Bundle b = new Bundle();
        switch (code) {
            case "AppPrivielge_01"://活动申请
                openIntent(ActiveApplyActivity.class, R.string.active_apply);
                break;
            case "AppPrivielge_02"://活动审批
                openIntent(ApproveListActivity.class, R.string.active_approve);
                break;
            case "AppPrivielge_03"://审批记录
                openIntent(ApproveRecordListActivity.class, R.string.approve_record);
                break;
            case "AppPrivielge_04"://扫码核销
                openIntent(TicketScanActivity.class, R.string.ticket_verify);
                break;
            case "AppPrivielge_05"://排课管理
                b.putString("from", "AppPrivielge_05");
                openIntent(ClassMngActivity.class, R.string.class_mng, b);
                break;
            case "AppPrivielge_06"://课程订单
                openIntent(ClassOrderListActivity.class, R.string.class_order);
                break;
            case "AppPrivielge_07"://新建预约
                b.putString("from", "AppPrivielge_07");
                openIntent(ClassMngActivity.class, R.string.booking_mng, b);
                break;
            case "AppPrivielge_08"://预约记录
                openIntent(BookingRecordListActivity.class, R.string.booking_record);
                break;
            case "AppPrivielge_09"://营业统计
                openIntent(BussinessStatisticsActivity.class, R.string.business_statistic);
                break;
            case "AppPrivielge_10"://会员录入
                openIntent(MemberInputActivity.class, R.string.member_input);
                break;
        }
    }

    @Override
    protected void doUploadImage(String tmpfile, Bitmap small) {
        super.doUploadImage(tmpfile, small);
        new MyAsyncTask(this, UploadUserHeader.class).run(tmpfile);
    }

    /**
     * 轮播图点击
     */
    @Override
    public void onImageClick(MyRow info, int postion, View imageView) {
        Bundle b = new Bundle();
        b.putString("URL", info.getString("NewsUrl"));
        openIntent(InformationViewActivity.class, R.string.info_detail, b);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finishApp();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finishApp();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void finishApp() {
        app.finishAllActivites();
    }
}

