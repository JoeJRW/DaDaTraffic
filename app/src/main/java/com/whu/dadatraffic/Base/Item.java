package com.whu.dadatraffic.Base;

import android.content.ClipData;

import com.whu.dadatraffic.R;

import java.util.Vector;

public class Item {
    protected String title = null;
    protected String price = null;
    protected Integer icon = null;
    private int number = 0;
    public Item(){}
    public Item(String title, String price, Integer icon)
    {
        this.title = title;
        this.price = price;
        this.icon = icon;
    }
    public String getTitle()
    {
        return title;
    }
    public String getPrice()
    {
        return price;
    }
    public Integer getIcon()
    {
        return icon;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public int getNumber() {
        return number;
    }
}

