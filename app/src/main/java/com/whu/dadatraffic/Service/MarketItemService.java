package com.whu.dadatraffic.Service;
/*
 *author：张朝勋
 * create time：7/9
 * update time: 7/14
 */
import android.icu.util.IslamicCalendar;

import com.whu.dadatraffic.Base.MarketItem;
import com.whu.dadatraffic.R;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Vector;

public class MarketItemService extends MarketItem implements Serializable {
    private ArrayList<MarketItem> marketItemList = new ArrayList<MarketItem>();//商品列表
    private ArrayList<MarketItem> cartItemList = new ArrayList<MarketItem>();//购物车列表
    private ArrayList<MarketItem> mOrderItemList = new ArrayList<MarketItem>();//订单列表
    private ArrayList<ArrayList<MarketItem>> MarketOrder = new ArrayList<ArrayList<MarketItem>>();
    public ArrayList<MarketItem> Example = new ArrayList<MarketItem>();
    public ArrayList<MarketItem> Example2 = new ArrayList<MarketItem>();
    public int scoreInAll = 0;//积分总和
    public MarketItemService() {
        super();
    }
    //测试功能用
    public void addMarketOrder()
    {
        Example.add(new MarketItem("围巾", "1800分", R.drawable.icon_scarf));
        CalculateMOCount(3,0);
        MarketOrder.add(Example);
        Example2.add(new MarketItem("耳机", "2480分", R.drawable.icon_earphone));
        CalculateMOCount(2,0);
        MarketOrder.add(Example2);
    }
    public ArrayList<MarketItem> GetOrder(int position)
    {
        return MarketOrder.get(position);
    }
    public void CalculateMOCount(int num,int position)
    {
        Example.get(position).CalculateCount(num);

        SumScore();
    }//
    public int SumCount(ArrayList<MarketItem> list)
    {
        int price = 0;
        for(int i = 0; i < list.size(); i++)
        {
            price += list.get(i).getCount();
        }
        return price;
    }
    public void AddItem(String title, String price, Integer icon)
    {
        marketItemList.add(new MarketItem(title,price,icon));
    }
    public int Count()
    {
        return marketItemList.size();
    }
    //添加购物车列表元素并返回购物车列表中元素数量
    public int CartCount()
    {
        cartItemList = new ArrayList<MarketItem>();
        for(int i = 0; i < marketItemList.size(); i++)
        {
            if(GetCount(i) != 0)
            {
                cartItemList.add(GetItem(i));
            }
        }
        return cartItemList.size();
    }
    public int MOCount()
    {
        return  mOrderItemList.size();
    }
    //从三个列表中返回对应商品以进行后续操作
    public MarketItem GetCart(int position)
    {
        return cartItemList.get(position);
    }
    public MarketItem GetMOrder(int position)
    {
        return  mOrderItemList.get(position);
    }
    public MarketItem GetItem(int position)
    {
        return  marketItemList.get(position);
    }
    //获取商品的价格与数量
    public String GetPrice(int position)
    {
        return marketItemList.get(position).getPrice();
    }
    public Integer GetCount(int position) {
        return marketItemList.get(position).getCount();
    }
    //返回购物车列表与给订单列表赋值。实现在商城订单页面中的订单显示
    public ArrayList<MarketItem> GetCartItemList()
    {
        return cartItemList;
    }
    public void setmOrderItemList(ArrayList<MarketItem> list)
    {
        mOrderItemList = list;
    }
    //给列表中对应商品的count属性赋值并重新计算总分
    public void CalculateCount(int num,int position)
    {
        if(GetCount(position)==0 && num < 0) {
            return;
        }
        else
        {
            marketItemList.get(position).CalculateCount(num);
        }
        SumScore();
    }
    //计算积分总和
    public void SumMarketOrderScore()
    {
        int price;
        scoreInAll = 0;
        for(int i = 0; i < mOrderItemList.size(); i++)
        {
            price = Integer.parseInt(GetMOrder(i).getPrice().substring(0,mOrderItemList.get(i).getPrice().length()-1));
            scoreInAll += price * mOrderItemList.get(i).getCount();
        }
    }
    public void SumScore()
    {
        int price;
        scoreInAll = 0;
        for(int i = 0; i < marketItemList.size(); i++)
        {
            price = Integer.parseInt(GetPrice(i).substring(0,GetPrice(i).length()-1));
            scoreInAll += price * GetCount(i);
        }
    }
}
