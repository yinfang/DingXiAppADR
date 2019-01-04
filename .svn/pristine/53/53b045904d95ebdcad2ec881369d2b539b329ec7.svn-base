
package com.clubank.picc;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.device.PiccManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.clubank.dingxi.R;
import com.clubank.util.UI;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MyPICCActivity extends Activity implements OnClickListener {
    private static final int MSG_AUTHEN_FAIL = 2;
    private static final int MSG_READ_FAIL = 5;
    private static final int MSG_SHOW_BLOCK_DATA = 6;
    private static final int MSG_INIT_FAIL = 7;
    private static final int MSG_INIT_SUCCESS = 8;
    private String AuthenKey = "ffffffffffff";
    private Button bOpen;
    private Button bRead;
    private EditText resEd;
    private PiccManager piccReader;
    private Handler handler;
    private ExecutorService exec;
    int blkNo = 0;//0扇区0块
    int scan_card = -1;
    int SNLen = -1;
    byte keyBuf[] = {
            (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff
    };

    @SuppressLint("HandlerLeak")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picc_main);
        piccReader = new PiccManager();
        exec = Executors.newSingleThreadExecutor();
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                // TODO Auto-generated method stub
                switch (msg.what) {
                    case MSG_INIT_FAIL:
                        SoundTool.getMySound(MyPICCActivity.this).playMusic("error");
                        UI.showToast(MyPICCActivity.this, "初始化失败！");
                        break;
                    case MSG_INIT_SUCCESS:
                        SoundTool.getMySound(MyPICCActivity.this).playMusic("success");
                        UI.showToast(MyPICCActivity.this, "初始化成功！");
                        break;
                    case MSG_AUTHEN_FAIL:
                        SoundTool.getMySound(MyPICCActivity.this).playMusic("error");
                        UI.showToast(MyPICCActivity.this, "秘钥验证失败！");
                        break;
                    case MSG_READ_FAIL:
                        SoundTool.getMySound(MyPICCActivity.this).playMusic("error");
                        UI.showToast(MyPICCActivity.this, "读卡失败！");
                        break;
                    case MSG_SHOW_BLOCK_DATA:
                        SoundTool.getMySound(MyPICCActivity.this).playMusic("success");
                        String data = (String) msg.obj;
                        if (data != null && !data.equals("")) {
                            String card = data.substring(0, 8);// 取0-8位为card
                            resEd.setText(Integer.parseInt(card, 16)+"");
                        }
                        break;
                    default:
                        break;
                }
                super.handleMessage(msg);
            }
        };
    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        bOpen = findViewById(R.id.picc_open);
        bRead = findViewById(R.id.picc_read);
        resEd = findViewById(R.id.rev_data);
        bOpen.setOnClickListener(this);
        bRead.setOnClickListener(this);
        super.onResume();
    }

    /**
     * Check if a (hex) string is pure hex (0-9, A-F, a-f) and 16 byte
     * (32 chars) long. If not show an error Toast in the context.
     *
     * @param hexString The string to check.
     * @return True if sting is hex an 16 Bytes long, False otherwise.
     */
    public static boolean isHexAnd16Byte(String hexString) {
        if (hexString.matches("[0-9A-Fa-f]+") == false) {
            // Error, not hex.

            return false;
        }
        return true;
    }

    @Override
    public void onClick(View view) {
        // TODO Auto-generated method stub
        if (view == bOpen) {
            exec.execute(new Thread(new Runnable() {
                @Override
                public void run() {
                    // TODO Auto-generated method stub
                    piccReader.open();
                    byte CardType[] = new byte[2];
                    byte Atq[] = new byte[14];
                    char SAK = 1;
                    byte sak[] = new byte[1];
                    sak[0] = (byte) SAK;
                    byte SN[] = new byte[10];
                    scan_card = piccReader.request(CardType, Atq);
                    Message msg;
                    if (scan_card > 0) {
                        SNLen = piccReader.antisel(SN, sak);
                        handler.sendEmptyMessage(MSG_INIT_SUCCESS);
                    } else {
                        handler.sendEmptyMessage(MSG_INIT_FAIL);
                    }
                }
            }, "picc open"));
        } else if (view == bRead) {
            exec.execute(new Thread(new Runnable() {

                @Override
                public void run() {
                    // TODO Auto-generated method stub
                    int ret = -1;
                    byte SN[] = new byte[10];
                    if (isHexAnd16Byte(AuthenKey)) {
                        byte[] keyData = hexStringToBytes(AuthenKey);
                        ret = piccReader.m1_keyAuth(0, blkNo, keyData.length, keyData, SNLen, SN);
                    } else {
                        ret = piccReader.m1_keyAuth(0, blkNo, 6, keyBuf, SNLen, SN);
                    }
                    if (ret == -1) {
                        handler.sendEmptyMessage(MSG_AUTHEN_FAIL);
                        return;
                    }
                    byte pReadBuf[] = new byte[20];
                    int result = piccReader.m1_readBlock(blkNo, pReadBuf);
                    if (result == -1) {
                        handler.sendEmptyMessage(MSG_READ_FAIL);
                    } else {
                        Message msg = handler.obtainMessage(MSG_SHOW_BLOCK_DATA);
                        msg.obj = bytesToHexString(pReadBuf, result);
                        handler.sendMessage(msg);
                    }
                }
            }, "picc read"));
        }
    }

    public static String bytesToHexString(byte[] src, int len) {
        StringBuilder stringBuilder = new StringBuilder("");
        if (src == null || src.length <= 0) {
            return null;
        }
        if (len <= 0) {
            return "";
        }
        for (int i = 0; i < len; i++) {
            int v = src[i] & 0xFF;
            String hv = Integer.toHexString(v);
            if (hv.length() < 2) {
                stringBuilder.append(0);
            }
            stringBuilder.append(hv);
        }
        return stringBuilder.toString();
    }

    public static byte[] hexStringToBytes(String hexString) {
        if (hexString == null || hexString.equals("")) {
            return null;
        }
        hexString = hexString.toUpperCase();
        int length = hexString.length() / 2;
        char[] hexChars = hexString.toCharArray();
        byte[] d = new byte[length];
        for (int i = 0; i < length; i++) {
            int pos = i * 2;
            d[i] = (byte) (charToByte(hexChars[pos]) << 4 | charToByte(hexChars[pos + 1]));
        }
        return d;
    }

    private static byte charToByte(char c) {
        return (byte) "0123456789ABCDEF".indexOf(c);
    }

    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        SoundTool.getMySound(this).release();
        super.onPause();
    }


    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        exec.shutdown();
        super.onDestroy();
    }
}
