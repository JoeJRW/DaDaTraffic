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
    private String driverPhone = "";//司机手机号
    private String createTime = null;//订单创建时间
    private String startPoint = null;//行程起始地点
    private String destination = null;//行程目的地
    private String price = "";//本次行程价格
    public String orderState = null;//订单状态
    private String evalution = "";//行程评价
    private int score = 0;//得分

    public Order(String phoneNum,String orderID,String driverPhone,String createTime, String startPoint, String destination,String price,String orderState,String evalution,int score){
        this.customerPhoneNum = phoneNum;
        this.orderID = orderID;
        this.driverPhone = driverPhone;
        this.createTime = createTime;
        this.startPoint = startPoint;
        this.destination = destination;
        this.price = price;
        this.orderState = orderState;
        this.evalution = evalution;
        this.score = score;
    }

    public Order(String customerPhoneNum,String startPoint,String destination){
        this.customerPhoneNum = customerPhoneNum;
        this.startPoint = startPoint;
        this.destination = destination;
    }

    public String getOrderID(){return orderID;}

    public String getCreateTime(){return createTime;}

    public String getStartPoint(){return startPoint;}

    public String getDestination(){return destination;}

    public void setOrderState(String orderState) {
        this.orderState = orderState;
    }

    public String getOrderState() {
        return orderState;
    }

    public String getPrice() {
        return price;
    }

    public String getCustomerPhoneNum() {
        return customerPhoneNum;
    }

    public String getEvalution(){return evalution;}

    public void setScore(int score) {
        this.score = score;
    }

    public int getScore() {
        return score;
    }

    public void setDriverPhone(String driverPhone) {
        this.driverPhone = driverPhone;
    }

    public String getDriverPhone() {
        return driverPhone;
    }

    public void setOrderID(String id){orderID=id;}

}
