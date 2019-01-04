package com.clubank.device;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Base64;
import android.view.View;

import com.clubank.dingxi.R;
import com.clubank.util.MyData;
import com.clubank.util.MyRow;
import com.clubank.util.U;

/**
 * 审批完成
 */
public class ApproveFinishActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_approve_finish);
        Bundle b = getIntent().getExtras();
        MyRow ro = U.getRow(b, "row");
        if (null != ro) {
            setDetails(ro);
        }
    }

    private void setDetails(MyRow row) {
        setEText(R.id.active_title, row.getString("ProcessName"));
        setEText(R.id.apply_people, row.getString("CreateUser"));
        setEText(R.id.active_time, row.getString("StartTime").replace("T", " ").substring(0, 10) +
                "   " + row.getString("StartTime").replace("T", " ").substring(11, 16) + "～" +
                row.getString("EndTime").replace("T", " ").substring(11, 16));
        setEText(R.id.active_place, row.getString("Address"));
        setEText(R.id.apply_result, row.getString("approve_result"));
        if (!checkNull(row.getString("approve_advice"))) {
        try{
            setEText(R.id.apply_advice, new String(android.util.Base64.decode(row.getString("approve_advice"),android.util.Base64.NO_WRAP)));
        } catch (Exception e) {

        }
        }
    }

    public void doWork(View view) {
        switch (view.getId()) {
            case R.id.finish://返回活动审批 列表
//                openIntent(MainActivity.class, R.string.app_name);
                finish();
                break;
        }
    }
}



