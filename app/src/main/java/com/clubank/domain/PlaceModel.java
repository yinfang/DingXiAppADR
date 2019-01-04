package com.clubank.domain;


import com.bigkoo.pickerview.model.IPickerViewData;

import java.util.List;

/**
 * 活动地点
 */
public class PlaceModel implements IPickerViewData {
    private String ArenaName;
    private List<SeatInfo> seleList;

    public PlaceModel() {
        super();
    }

    public PlaceModel(String ArenaName, List<SeatInfo> seleList) {
        super();
        this.ArenaName = ArenaName;
        this.seleList = seleList;
    }

    public String getName() {
        return ArenaName;
    }

    public void setName(String ArenaName) {
        this.ArenaName = ArenaName;
    }

    public List<SeatInfo> getSeleList() {
        return seleList;
    }

    public void setSeleList(List<SeatInfo> seleList) {
        this.seleList = seleList;
    }

    @Override
    public String getPickerViewText() {
        return this.ArenaName;
    }

    /**
     * 分场地点
     */
    public static class SeatInfo {
        private String SeatName;
        private String SeatID;

        public SeatInfo() {
            super();
        }

        public SeatInfo(String SeatName, String SeatID) {
            super();
            this.SeatName = SeatName;
            this.SeatID = SeatID;
        }

        public String getName() {
            return SeatName;
        }

        public void setName(String SeatName) {
            this.SeatName = SeatName;
        }

        public String getCode() {
            return SeatID;
        }

        public void setCode(String SeatID) {
            this.SeatID = SeatID;
        }
    }
}
