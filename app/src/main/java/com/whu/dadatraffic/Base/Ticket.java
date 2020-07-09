package com.whu.dadatraffic.Base;

import java.util.Date;
import java.util.Vector;

public class Ticket {
    public int id = 1;
    protected String title = null;
    protected String discount = null;
    protected Integer icon = null;
    protected String startDate = null;
    protected String endDate = null;
    protected Boolean status = null;
    public Ticket() { }


    public Ticket(int id,String title, String discount, Integer icon, String startDate, String endDate,boolean status)
    {
        this.id = id;
        this.title = title;
        this.discount = discount;
        this.icon = icon;
        this.startDate = startDate;
        this.endDate = endDate;
        this.status = status;
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

    public Boolean getStatus() {
        return status;
    }

    public int getId() {
        return id;
    }
}
