/*
*作者：施武轩 创建时间：2020.7.8 更新时间：2020.7.10
 */

package com.whu.dadatraffic.Base;

import android.annotation.SuppressLint;
import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Order {
    private String customerPhoneNum = null;//乘客手机号
    private String orderID = null;//订单编号
    private String driverName = "";//司机姓名
    private String carNumber = null;//车牌号
    private String createTime = null;//订单创建时间
    private String startPoint = null;//行程起始地点
    private String destination = null;//行程目的地
    private double price = 0.00;//本次行程价格
    public String orderState = null;//订单状态
    private String evalution = "";//行程评价
    private float score = 1.0f;//得分

    public Order(String PhoneNum ,String orderID, String startPoint, String destination){
        Date date = new Date(System.currentTimeMillis());
        @SuppressLint("SimpleDateFormat") SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        createTime = simpleDateFormat.format(date);

        this.customerPhoneNum = PhoneNum;
        this.orderID = orderID;
        this.startPoint = startPoint;
        this.destination = destination;
    }

    public String getOrderID(){return orderID;}

    public String getCreateTime(){return createTime;}

    public String getStartPoint(){return startPoint;}

    public String getDestination(){return destination;}

    public void setPrice(double price) {
        this.price = price;
    }

    public double getPrice(){return  price;}

    public String getCustomerPhoneNum() {
        return customerPhoneNum;
    }

    public void setEvalution(String evalution){this.evalution = evalution;}
    public String getEvalution(){return evalution;}

    public void setScore(float score){this.score = score;}
    public float getScore(){return score;}

    public void setCarNumber(String carNumber) {
        this.carNumber = carNumber;
    }

    public String getCarNumber() {
        return carNumber;
    }

    public void setDriverName(String driverName) {
        this.driverName = driverName;
    }

    public String getDriverName(){return driverName;}

}
