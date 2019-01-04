package com.clubank.device;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.clubank.dingxi.R;
import com.clubank.nfc.NFCMainActivity;
import com.clubank.picc.MyPICCActivity;
import com.easier.code.util.CaptureActivity;

/**
 * 扫码 nfc
 */
public class ScanTestActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticket_scan);
    }

    public void doWork(View v) {
        switch (v.getId()) {
            case R.id.scan://扫描
                openIntent(CaptureActivity.class, R.string.scan, 1001);
                break;
            case R.id.nfc_read://nfc读卡
                openIntent(NFCMainActivity.class, R.string.nfc_read, 1002);
                break;
            case R.id.picc_read://picc读卡
                openIntent(MyPICCActivity.class, R.string.picc_read);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1001 && resultCode == RESULT_OK) {
            setEText(R.id.scan_result, data.getStringExtra("RESULT"));
        } else if (requestCode == 1002 && resultCode == RESULT_OK) {
            setEText(R.id.read_result, "cardNo:" + data.getStringExtra("cardNo") + "\n" +
                    "statusCode:" + data.getStringExtra("statusCode"));
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
