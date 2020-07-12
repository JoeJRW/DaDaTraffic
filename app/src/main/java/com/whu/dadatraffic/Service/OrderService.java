/*
 *作者：施武轩 创建时间：2020.7.10 更新时间：2020.7.10
 */

package com.whu.dadatraffic.Service;

import com.whu.dadatraffic.Activity.OrdersActivity;
import com.whu.dadatraffic.Base.Order;

public class OrderService {

    //添加订单
    public boolean addOrder(Order order) {
        return OrdersActivity.orderList.add(order);
        //存入数据库
    }

    //根据订单编号取消相应订单
    public boolean cancelOrder(String orderID) {
        for (int i = 0; i < OrdersActivity.orderList.size(); i++) {
            if (OrdersActivity.orderList.elementAt(i).getOrderID().equals(orderID)) {
                OrdersActivity.orderList.elementAt(i).orderState = "已取消";
                return true;
            }
        }
        return false;
        //操作数据库
    }

    //根据订单编号接受相应订单
    public boolean acceptOrder(String orderID) {
        for (int i = 0; i < OrdersActivity.orderList.size(); i++) {
            if (OrdersActivity.orderList.elementAt(i).getOrderID().equals(orderID)) {
                OrdersActivity.orderList.elementAt(i).orderState = "进行中";
                return true;
            }
        }
        return false;
        //操作数据库
    }

    //根据订单编号完成订单
    public boolean completeOrder(String orderID) {
        for (int i = 0; i < OrdersActivity.orderList.size(); i++) {
            if (OrdersActivity.orderList.elementAt(i).getOrderID().equals(orderID)) {
                OrdersActivity.orderList.elementAt(i).orderState = "已完成";
                return true;
            }
        }
        return false;
    }
}
