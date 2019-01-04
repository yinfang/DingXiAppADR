package com.clubank.device;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;

import com.clubank.device.op.TicketWriteOff;
import com.clubank.dingxi.R;
import com.clubank.domain.BC;
import com.clubank.util.MyAsyncTask;
import com.clubank.util.MyRow;
import com.clubank.util.UI;
import com.easier.code.util.CaptureActivity;
import com.google.zxing.Result;

/**
 * 扫码核销
 */
public class TicketScanActivity extends CaptureActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.scan);
        super.onCreate(savedInstanceState);
    }

    public void doWork(View v) {
        switch (v.getId()) {
            case R.id.highlight_ll://打开闪关灯
                flashHandler();
                break;
        }
    }

    @Override
    public void handleDecode(Result obj, Bitmap barcode) {
        inactivityTimer.onActivity();
        viewfinderView.drawResultBitmap(barcode);
        playBeepSoundAndVibrate();
        String code = obj.getText();
        new MyAsyncTask(this, TicketWriteOff.class).run(code);
    }

    @Override
    public void onPostExecute(Class<?> op, com.clubank.domain.Result result) {
        super.onPostExecute(op, result);
        if (op == TicketWriteOff.class) {
            if (result.code == BC.SUCCESS) {
                MyRow row = (MyRow) result.obj;
                Bundle b = new Bundle();
                b.putSerializable("row", row);
                String type = row.getString("type");
                if (type.equals("ticket")) { //tyep：ticket 票务核销 lesson 签课核销
                    openIntent(TicketScanSuccessActivity.class, R.string.verify_success, b);
                } else if (type.equals("lesson")) {
                    openIntent(TicketScanSuccessActivity.class, R.string.sign_info, b);
                } else if (type.equals("paper")) {
                    openIntent(TicketScanSuccessActivity.class, R.string.xiao_success, b);
                }
                finish();
            } else {
              /*  viewfinderView.drawResultBitmap(null);
                viewfinderView.invalidate();*/
                continuePreview();
                UI.showToast(this, result.msg);
            }
        }
    }
}
