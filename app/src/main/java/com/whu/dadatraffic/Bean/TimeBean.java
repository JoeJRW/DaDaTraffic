package com.whu.dadatraffic.Bean;

import com.wzh.pickerview.model.IPickerViewData;

public class TimeBean implements IPickerViewData {
    private String time;

    public TimeBean(String time) {
        this.time = time;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    @Override
    public String getPickerViewText() {
        return time;
    }
}
