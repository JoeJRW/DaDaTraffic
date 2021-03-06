package com.whu.dadatraffic.Base;
/*
 *author：张朝勋
 * create time：7/9
 * update time: 7/15
 * update time: 7/22
 */
import java.util.Date;
import java.util.Vector;

public class Ticket {
    //public int id = 1;
    protected String title = null;//优惠券名称
    protected String discount = null;//优惠券效果
    protected Integer icon = null;//优惠券界面的图标
    protected String startDate = null;//优惠券有效日期
    protected String endDate = null;
    protected Integer imageResource = null;//优惠券详情界面的图标
    public Ticket() { }


    public Ticket(String title, String discount, Integer icon, String startDate, String endDate, Integer imageResource)
    {
        this.title = title;
        this.discount = discount;
        this.icon = icon;
        this.startDate = startDate;
        this.endDate = endDate;
        this.imageResource = imageResource;
    }

    public String getTitle() {
        return title;
    }

    public String getDiscount() {
        return discount;
    }
    public Integer getIcon(){
        return icon;
    }

    public String getEndDate() {
        return endDate;
    }

    public String getStartDate() {
        return startDate;
    }

    public Integer getImageResource() {
        return imageResource;
    }


}
