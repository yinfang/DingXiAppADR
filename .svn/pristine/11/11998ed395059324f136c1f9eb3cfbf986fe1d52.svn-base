package com.clubank.device;

import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.view.WindowManager;

import com.clubank.device.op.GetUserInfo;
import com.clubank.device.op.UserLogin;
import com.clubank.dingxi.R;
import com.clubank.domain.BC;
import com.clubank.domain.Result;
import com.clubank.domain.User;
import com.clubank.util.MyAsyncTask;
import com.clubank.util.MyData;
import com.clubank.util.MyRow;
import com.clubank.util.U;
import com.clubank.util.UI;

public class SplashActivity extends BaseActivity {
    private String verName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager
                .LayoutParams.FLAG_FULLSCREEN);
        try {
            verName = this.getPackageManager().
                    getPackageInfo(this.getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        saveSetting("checkVersion", false);//启动页不进行版本检测，其他页面检测
        CountDownTimer countDownTimer = new CountDownTimer(2000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                String token = settings.getString("Token", "");
                if (!TextUtils.isEmpty(token)) {
                    new MyAsyncTask(SplashActivity.this, GetUserInfo.class, false).run(token);
                } else {
                    openIntent(LoginActivity.class, "");
                    finish();
                }
            }
        };
        countDownTimer.start();
    }

    @Override
    public void onPostExecute(Class<?> op, Result result) {
        super.onPostExecute(op, result);
        Bundle b=new Bundle();
        b.putBoolean("isLogout",false);
        openIntent(LoginActivity.class, "登陆",b);
        if (op == GetUserInfo.class) {
            if (result.code == BC.SUCCESS) {
                MyData data = (MyData) result.obj;
                MyRow ro = data.get(0);
                User.getIn().setToken(settings.getString("Token", ""));
                User.getIn().setMobileNo(ro.getString("MobileNo"));
                User.getIn().setName(ro.getString("UserName"));
                User.getIn().setHeadIcon(ro.getString("Pic"));
                openIntent(MainActivity.class, R.string.app_name);
            } else if (result.code == 104) {//登录token失效
                openIntent(LoginActivity.class, "",b);
            } else {
                openIntent(LoginActivity.class, "",b);
            }
            finish();
        }
    }

}
