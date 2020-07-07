package com.whu.dadatraffic;

import android.annotation.SuppressLint;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Order {
    private String orderID = null;
    private String driverName = null;
    private String createTime = null;
    private String startPoint = null;
    private String destination = null;
    private double price = 0.00;
    private String orderState = null;

    public Order(String orderID, String driverName, String startPoint, String destination, double price){
        Date date = new Date(System.currentTimeMillis());
        @SuppressLint("SimpleDateFormat") SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
        createTime = simpleDateFormat.format(date);
        orderState = "进行中";

        this.orderID = orderID;
        this.driverName = driverName;
        this.startPoint = startPoint;
        this.destination = destination;
        this.price = price;
    }

    public void cancelOrder()
    {
        this.orderState = "已取消";
    }

    public void completeOrder(){
        this.orderState = "已支付";
    }
}
