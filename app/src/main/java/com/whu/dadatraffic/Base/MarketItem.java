package com.whu.dadatraffic.Base;
/*
 *author：张朝勋
 * create time：7/9
 * update time: 7/14
 */
import android.content.ClipData;

import com.whu.dadatraffic.R;

import java.io.Serializable;
import java.util.Observable;
import java.util.Vector;

public class MarketItem implements Serializable {
    protected String title = null;//商品名
    protected String price = null;//商品价格
    protected Integer icon = null;//商品图片
    protected Integer count = 0;//订购数量
    public MarketItem(){}
    public MarketItem(String title, String price, Integer icon)
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
    public Integer getCount(){
        return count;
    }
    public void CalculateCount(int number){
        count += number;
    }
}

