package com.whu.dadatraffic.Service;
/*
 *author：张朝勋
 * create time：7/9
 * update time: 7/9
 */
import com.whu.dadatraffic.Base.MarketItem;

import java.util.Vector;

public class MarketItemService extends MarketItem {
    Vector<MarketItem> marketItemList = new Vector<MarketItem>();

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
    public String GetTitle(int position)
    {
        return marketItemList.get(position).getTitle();
    }
    public String GetPrice(int position)
    {
        return marketItemList.get(position).getPrice();
    }
    public Integer GetIcon(int position)
    {
        return marketItemList.get(position).getIcon();
    }
}
