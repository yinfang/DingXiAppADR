package com.clubank.nfc;

import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.content.Intent;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.MifareClassic;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;

import com.clubank.dingxi.R;
import com.clubank.util.UI;

import java.io.IOException;

@SuppressLint("NewApi")
public class NFCMainActivity extends BaseActivity {
    private long long_time;
    private int countDown = 16;
    private byte[] testByte = {0x2, 0x2, 0x2, 0x2, 0x2, 0x2, 0x2, 0x2, 0x2,
            0x2, 0x2, 0x2, 0x2, 0x2, 0x2, 0x2};// 16个分区,每个分区4个块，每个块可以存放16个字节的数据=1024byte=1K

    // 关于MifareClassic卡的背景介绍：数据分为16个区(Sector) ,每个区有4个块(Block) ，每个块可以存放16字节的数据。
    // 每个区最后一个块称为Trailer ，主要用来存放读写该区Block数据的Key ，可以有A，B两个Key，每个Key
    // 长度为6个字节，缺省的Key值一般为全FF或是0. 由 MifareClassic.KEY_DEFAULT 定义。
    // 因此读写Mifare Tag 首先需要有正确的Key值（起到保护的作用），如果鉴权成功
    // 然后才可以读写该区数据。

    @SuppressLint("NewApi")
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);

        nfcAdapter = NfcAdapter.getDefaultAdapter(this);
        pendingIntent = PendingIntent.getActivity(this, 0, new Intent(this,
                getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);

        // 检查nfc是否开启
        checkNfc();
        handler.postDelayed(runnable, 1000);
        onNewIntent(getIntent());
        initListener();
    }

    Handler handler = new Handler();
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            countDown--;
            /*txtView.setText(getString(R.string.lbl_countDown) + countDown
                    + getString(R.string.lbl_second));*/
            if (countDown == 0) {
//                UI.showToast(NFCMainActivity.this, "读卡失败");
            }
            handler.postDelayed(this, 1000);
        }
    };

    private void initListener() {
        readButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkBlock()) {
                    MifareClassicCard mifareClassicCard = new MifareClassicCard(
                            mfc);
                    int block = Integer.parseInt(blockIdEditText.getText()
                            .toString().trim());
                    String content = mifareClassicCard.readCarCode(block,
                            keyEdittext.getText().toString().trim());
                    if ("秘钥错误".equals(content) || "读取失败".equals(content))
                        setHintToContentEd(content);
                    else
                        contentEditText.setText(content);
                }

            }
        });
        wriButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (checkBlock()) {
                    MifareClassicCard mifareClassicCard = new MifareClassicCard(
                            mfc);
                    int block = Integer.parseInt(blockIdEditText.getText()
                            .toString().trim());
                    String content = contentEditText.getText().toString()
                            .trim();
                    String result = mifareClassicCard.wirteCarCode(content,
                            block, keyEdittext.getText().toString().trim());
                    if ("".equals(result))
                        setHintToContentEd("写入成功");
                    else
                        setHintToContentEd(result);

                }

            }
        });

        modifyButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkBlock()) {
                    MifareClassicCard mifareClassicCard = new MifareClassicCard(
                            mfc);
                    int block = Integer.parseInt(blockIdEditText.getText()
                            .toString().trim());
                    String content = contentEditText.getText().toString()
                            .trim();
                    String key = keyEdittext.getText().toString().trim();
                    String result = mifareClassicCard.modifyPassword(block,
                            content, key);
                    if ("".equals(result))
                        setHintToContentEd("修改成功");
                    else
                        setHintToContentEd(result);
                }

            }
        });
    }

    @Override
    public void processDialogOK(int type, Object tag) {
        super.processDialogOK(type, tag);
        if (type == RESULT_NO_NFC) {
            finish();
        }
    }

    @SuppressLint("NewApi")
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        if (nfcAdapter != null)
            nfcAdapter.enableForegroundDispatch(this, pendingIntent, FILTERS,
                    TECHLISTS);

    }

    @SuppressLint("NewApi")
    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        if (nfcAdapter != null)
            nfcAdapter.disableForegroundDispatch(this);
    }

    @SuppressLint("NewApi")
    protected void onNewIntent(Intent intent) {
        // TODO Auto-generated method stub
        super.onNewIntent(intent);
        Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
        if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(intent.getAction())) {

        } else {
            if (tag != null) {
                // 获取卡类型，根据卡类型可推出卡协议
                String[] techList = tag.getTechList();
                StringBuffer techString = new StringBuffer();
                for (int i = 0; i < techList.length; i++) {
                    techString.append(techList[i]);
                    techString.append(";\n");
                }
                typeEditText.setText(techString.toString());
                // 获取卡id
                byte[] id = tag.getId();
                carCodeEditText.setText(ByteArrayToHexString(id));

                mfc = MifareClassic.get(tag);
                resolveIntent(mfc);
                if (mfc != null) {
                    showView(1);
                } else {
                    showView(0);
                }
            }

        }
    }

    // 读卡
    void resolveIntent(MifareClassic mfc) {
        MifareClassCard mifareClassCard = null;
        try {
            mfc.connect();
            int bCount = 0;
            int bIndex = 0;
            int secCount = mfc.getSectorCount();
            mifareClassCard = new MifareClassCard(secCount);
            for (int j = 0; j < secCount; j++) {
                MifareSector mifareSector = new MifareSector();
                mifareSector.sectorIndex = j;
                boolean auth = mfc.authenticateSectorWithKeyA(j, MifareClassic.KEY_DEFAULT);// MifareClassic.KEY_DEFAULT
                mifareSector.authorized = auth;
                if (auth) {// 认证校验keyA密码是否正确，匹配每扇区的第4块，如果OK，就可以读取数据。
                    bCount = mfc.getBlockCountInSector(j);
                    bCount = Math.min(bCount, MifareSector.BLOCKCOUNT);
                    bIndex = mfc.sectorToBlock(j);
                    for (int i = 0; i < bCount; i++) {
                        byte[] data = mfc.readBlock(bIndex);
                        if (j == 12 && i == 2) {
                            try {
                                mfc.writeBlock(bIndex, testByte);
                            } catch (IOException e) {
                                Log.d("Error", "写错误信息" + e.toString());
                            }
                        }
                        MifareBlock mifareBlock = new MifareBlock(data);
                        mifareBlock.blockIndex = bIndex;
                        bIndex++;
                        mifareSector.blocks[i] = mifareBlock;
                    }
                    mifareClassCard.setSector(mifareSector.sectorIndex,
                            mifareSector);
                } else {
                    UI.showToast(this, "第" + secCount + "扇区keyA密码不正确");
                }
                Log.d("Error", "第" + (j + 1) + "个扇区正在读取");
            }
            readCardNo(mifareClassCard);
        } catch (IOException e) {
            Log.d("Error", "写错误信息" + e.toString());
        } finally {
            if (mifareClassCard != null) {
                mifareClassCard.debugPrint();
            }
        }
    }

    private void readCardNo(MifareClassCard mcc) {
        MifareSector mifareSector = mcc.getSector(0); // 0扇区
        MifareBlock mifareBlock = mifareSector.blocks[0];// 第1块
        byte[] data = mifareBlock.getData(); // 取块数据.
        /*for (int i = 0; i < data.length; i++) {
            if (data[i] == -1) {//7.5  data[i] == -1
                data[i] = 0x20;
            }
        }
        String s = new String(data).trim();*/
//        s = s.replace("TSPGC", "");
        String ss = mcc.bytesToHexString(data);
        if (ss != null && !ss.equals("")) {
            String card = ss.substring(0, 8);// 取0-8位为card
            setEText(R.id.edittext_inner_id, Integer.parseInt(card, 16) + "");
        }
    }
}
