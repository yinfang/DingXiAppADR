package com.clubank.device;

import android.os.Bundle;
import android.view.View;

import com.clubank.dingxi.R;
import com.clubank.util.MyData;
import com.clubank.util.MyRow;
import com.clubank.util.U;

/**
 * 会员录入完成
 */
public class MemberInputSuccessActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meminput_success);
        Bundle b = getIntent().getExtras();
        MyRow ro = U.getRow(b, "row");
        if (null != ro) {
            setDetails(ro);
        }
    }

    private void setDetails(MyRow row) {
        setEText(R.id.member_name, row.getString("Name"));
        setEText(R.id.member_sex, row.getString("Gender"));
        setEText(R.id.member_no, row.getString("IDnumber"));
        setEText(R.id.member_phone, row.getString("MobileNo"));
        setEText(R.id.member_phone1, row.getString("UrgentMobileNo"));
        setEText(R.id.car_number, row.getString("CarNumber"));
    }

    public void doWork(View view) {
        switch (view.getId()) {
            case R.id.finish://返回主页
                openIntent(MainActivity.class, R.string.app_name);
                finish();
                break;
        }
    }
}



