package com.clubank.device;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.clubank.dingxi.R;

/**
 * 活动申请成功界面
 */
public class ApplySuccessActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apply_success);
    }

    public void doWork(View view) {
        switch (view.getId()) {
            case R.id.go_record_list://查看申请记录
                Intent intent = new Intent(this, ApplyRecordListActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("title", getString(R.string.active_record));
                startActivity(intent);
                finish();
                overridePendingTransition( R.anim.forward_enter,R.anim.forward_exit);
              /*  openIntent(ApplyRecordListActivity.class, R.string.active_record);
                finish();*/
                break;
        }
    }
}



