package com.clubank.nfc;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.nfc.NfcAdapter;
import android.nfc.tech.IsoDep;
import android.nfc.tech.MifareClassic;
import android.nfc.tech.NfcF;
import android.nfc.tech.NfcV;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.clubank.dingxi.R;
import com.clubank.util.UI;

@TargetApi(Build.VERSION_CODES.GINGERBREAD_MR1)
@SuppressLint("NewApi")
public class BaseActivity extends com.clubank.device.BaseActivity {
    public static final int RESULT_NO_NFC = 100;
    protected NfcAdapter nfcAdapter;
    protected PendingIntent pendingIntent;
    protected EditText carCodeEditText;
    protected EditText typeEditText;
    protected EditText keyEdittext;
    protected EditText blockIdEditText;
    protected EditText contentEditText;
    protected Button hintButton;
    protected Button mHintButton;
    protected MifareClassic mfc;
    protected LinearLayout linearLayout;
    protected LinearLayout mifareclassicLinearLayout;
    protected Button readButton;
    protected Button wriButton;
    protected Button modifyButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cpu_check);
        setHeaderTitle("nfc");
        carCodeEditText = findViewById(R.id.edittext_car_code);
        typeEditText = findViewById(R.id.edittext_car_type);
        hintButton = findViewById(R.id.button_hint1);
        linearLayout = findViewById(R.id.m1_le);
        mifareclassicLinearLayout = findViewById(R.id.mifareclassic);
        readButton = findViewById(R.id.button_read);
        wriButton = findViewById(R.id.button_write);
        modifyButton = findViewById(R.id.button_modify);
        blockIdEditText = findViewById(R.id.edittext_block_id);
        keyEdittext = findViewById(R.id.edittext_key);
        contentEditText = findViewById(R.id.edittext_content);
        mHintButton = findViewById(R.id.button_hint);
        initListener();
    }

    private void initListener() {
        hintButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                hint();

            }
        });
        mHintButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                hint();
            }
        });
    }

    public static String[][] TECHLISTS;
    @SuppressLint("NewApi")
    public static IntentFilter[] FILTERS;

    static {
        try {
            TECHLISTS = new String[][]{{IsoDep.class.getName()},
                    {NfcV.class.getName()}, {NfcF.class.getName()},};

            FILTERS = new IntentFilter[]{new IntentFilter(
                    NfcAdapter.ACTION_TECH_DISCOVERED, "*/*")};
        } catch (Exception e) {
        }
    }

    // converts byte arrays to string
    protected String ByteArrayToHexString(byte[] inarray) {
        int i, j, in;
        String[] hex = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "A",
                "B", "C", "D", "E", "F"};
        String out = "";
        for (j = 0; j < inarray.length; ++j) {
            in = inarray[j] & 0xff;
            i = (in >> 4) & 0x0f;
            out += hex[i];
            i = in & 0x0f;
            out += hex[i];
        }
        return out;
    }

    @SuppressLint("NewApi")
    protected void checkNfc() {
        if (null != nfcAdapter) {
            if (!nfcAdapter.isEnabled()) {
                startActivity(new Intent(
                        android.provider.Settings.ACTION_NFC_SETTINGS));
            }
        } else {
            UI.showInfo(this, R.string.lbl_noNFC, RESULT_NO_NFC);
        }
    }

    /**
     * 1,M1卡
     */
    protected void showView(int type) {
        switch (type) {
            case 1:
                mifareclassicLinearLayout.setVisibility(View.VISIBLE);
                linearLayout.setVisibility(View.VISIBLE);
                hintButton.setVisibility(View.GONE);

                break;

            default:
                mifareclassicLinearLayout.setVisibility(View.GONE);
                linearLayout.setVisibility(View.GONE);
                hintButton.setVisibility(View.VISIBLE);
                break;
        }

    }

    protected void hint() {
        typeEditText.setText("");
        carCodeEditText.setText("");
        keyEdittext.setText("");
        contentEditText.setText("");
        blockIdEditText.setText("");
    }

    protected void setHintToContentEd(String msg) {
        contentEditText.setText("");
        contentEditText.setHint(msg);
        contentEditText.setHintTextColor(Color.RED);
    }

    protected boolean checkBlock() {

        if ("".equals(blockIdEditText.getText().toString().trim())) {
            blockIdEditText.setText("");
            blockIdEditText.setHint("请输入块号");
            blockIdEditText.setHintTextColor(Color.RED);
            return false;
        } else {
            // int
            // block=Integer.parseInt(blockIdEditText.getText().toString().trim());
            // if((block-1)/3==0){
            // blockIdEditText.setText("");
            // blockIdEditText.setHint("该块为秘钥块！");
            // blockIdEditText.setHintTextColor(Color.RED);
            // return false;
            // }
            return true;

        }

    }

}
