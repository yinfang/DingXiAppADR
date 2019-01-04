package com.clubank.device;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Base64;
import android.view.View;

import com.clubank.dingxi.R;
import com.clubank.util.MyRow;
import com.clubank.util.U;

/**
 * 课程预约成功
 */
public class BookingClassSuccessActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_class_success);
        Bundle b = getIntent().getExtras();
        if (null != b) {
            MyRow ro = U.getRow(b, "row");
            setDetails(ro);
        }
    }

    private void setDetails(MyRow row) {
        setEText(R.id.class_name, row.getString("LessonName"));
        setEText(R.id.class_time, row.getString("LessonTime"));
        setEText(R.id.total_class, row.getInt("ConsumeHour") + "节/" + row.getInt("TotalHour") + "节");
        if (!checkNull(row.getString("Remarks"))) {
            try {
                setEText(R.id.mark_info, new String(android.util.Base64.decode(row.getString("Remarks"), android.util.Base64.NO_WRAP)));
            } catch (Exception e) {

            }
        }
    }

    public void doWork(View view) {
        switch (view.getId()) {
            case R.id.finish://返回首页
                openIntent(MainActivity.class, R.string.app_name);
              /*  Bundle b = new Bundle();
                b.putString("from", "AppPrivielge_07");
                openIntent(ClassMngActivity.class, R.string.booking_mng, b);*/
                finish();
                break;
        }
    }
}



