package com.clubank.nfc;

import android.nfc.tech.MifareClassic;
import android.util.Log;

public class MifareClassCard {

    /**
     * Constructor.
     *
     * @param sectorSize size of the sectors.
     */
    public MifareClassCard(int sectorSize) {
        sectors = new MifareSector[sectorSize];
        SECTORCOUNT = sectorSize;
        initializeCard();
    }

    /**
     * initial card
     */
    public void initializeCard() {
        for (int i = 0; i < SECTORCOUNT; i++) {
            sectors[i] = new MifareSector();
            sectors[i].sectorIndex = i;
            sectors[i].keyA = new MifareKey(MifareClassic.KEY_DEFAULT); // 现在只用keyA,BC.keyA.getBytes()
            sectors[i].keyB = new MifareKey();// keyB默认
            for (int j = 0; j < 4; j++) {
                sectors[i].blocks[j] = new MifareBlock();
                sectors[i].blocks[j].blockIndex = i * 4 + j;
            }
        }
    }

    /**
     * get sector at given index.
     *
     * @param index the index of the sector.
     * @return the sector at given index;
     */
    public MifareSector getSector(int index) {
        if (index >= SECTORCOUNT) {
            throw new IllegalArgumentException("Invaid index for sector");
        }
        return sectors[index];
    }

    /**
     * set sector at given index.
     *
     * @param index the index of the sector.
     */
    public void setSector(int index, MifareSector sector) {
        if (index >= SECTORCOUNT) {
            throw new IllegalArgumentException("Invalid index for sector");
        }
        sectors[index] = sector;
    }

    /**
     * get the count of the sector.
     *
     * @return the count of the sector.
     */
    public int getSectorCount() {
        return SECTORCOUNT;

    }

    /**
     * set the new sector count
     *
     * @param newCount net sector count
     */
    public void setSectorCount(int newCount) {
        if (SECTORCOUNT < newCount) {
            sectors = new MifareSector[newCount];
            initializeCard();
        }
        SECTORCOUNT = newCount;

    }

    /**
     * debug print information.
     */
    public void debugPrint() {
        int blockIndex = 0;
        for (int i = 0; i < SECTORCOUNT; i++) {
            MifareSector sector = sectors[i];
            if (sector != null) {
                Log.i(TAG, "Sector " + i);
                for (int j = 0; j < MifareSector.BLOCKCOUNT; j++) {
                    MifareBlock block = sector.blocks[j];
                    if (block != null) {
                        byte[] raw = block.getData();
                        String hexString = "  Block " + j + " "
                                + Converter.getHexString(raw, raw.length)
                                + "  (" + blockIndex + ")";
                        Log.i(TAG, hexString);
                    }
                    blockIndex++;
                }
            }
        }
    }

    /**
     * 字符序列转换为16进制字符串
     *
     * @param src
     * @return
     */
    public String bytesToHexString(byte[] src) {
        StringBuilder stringBuilder = new StringBuilder();
        if (src == null || src.length <= 0) {
            return null;
        }
        char[] buffer = new char[2];
        for (int i = 0; i < src.length; i++) {
            buffer[0] = Character.forDigit((src[i] >>> 4) & 0x0F, 16);
            buffer[1] = Character.forDigit(src[i] & 0x0F, 16);
            System.out.println(buffer);
            stringBuilder.append(buffer);
        }
        return stringBuilder.toString();
    }

    /**
     * the size of the sector.
     */
    private int SECTORCOUNT = 16;

    /**
     * Log TAG.
     */
    protected String TAG = "MifareCardInfo";

    /**
     * sectors.
     */
    private MifareSector[] sectors;
}
