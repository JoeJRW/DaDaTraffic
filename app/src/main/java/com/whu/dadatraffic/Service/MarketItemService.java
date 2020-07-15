package com.whu.dadatraffic.Service;
/*
 *author：张朝勋
 * create time：7/9
 * update time: 7/14
 */
import com.whu.dadatraffic.Base.MarketItem;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Vector;

public class MarketItemService extends MarketItem implements Serializable {
    private ArrayList<MarketItem> marketItemList = new ArrayList<MarketItem>();
    private ArrayList<MarketItem> cartItemList = new ArrayList<MarketItem>();
    private ArrayList<MarketItem> mOrderItemList = new ArrayList<MarketItem>();
    public int scoreInAll = 0;
    public MarketItemService() {
        super();
    }

    public void AddItem(String title, String price, Integer icon)
    {
        marketItemList.add(new MarketItem(title,price,icon));
    }
    public int Count()
    {
        return marketItemList.size();
    }
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
    public String GetPrice(int position)
    {
        return marketItemList.get(position).getPrice();
    }
    public Integer GetCount(int position) {
        return marketItemList.get(position).getCount();
    }
    public ArrayList<MarketItem> GetCartItemList()
    {
        return cartItemList;
    }
    public void setmOrderItemList(ArrayList<MarketItem> list)
    {
        mOrderItemList = list;
    }
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
