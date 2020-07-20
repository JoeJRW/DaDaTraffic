
package com.whu.dadatraffic.Base;
/*
 *author：张朝勋
 * create time：7/9
 * update time: 7/18
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
    protected String time = "";//购买时间
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

    public void setIcon(Integer icon) {
        this.icon = icon;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTime() {
        return time;
    }

    public void setPrice(String price) {
        this.price = price;
    }
    public void setTitle(String title) {
        this.title = title;
    }

    public void setCount(Integer count) {
        this.count = count;
    }


}

