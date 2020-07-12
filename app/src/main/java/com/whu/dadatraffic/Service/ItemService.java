package com.whu.dadatraffic.Service;
/*
 *author：张朝勋
 * create time：7/9
 * update time: 7/9
 */
import com.whu.dadatraffic.Base.Item;

import java.util.Vector;

public class ItemService extends Item {
    Vector<Item> itemList = new Vector<Item>();

    public ItemService() {
        super();
    }

    public void AddItem(String title, String price, Integer icon)
    {
        itemList.add(new Item(title,price,icon));
    }
    public int Count()
    {
        return itemList.size();
    }
    public String GetTitle(int position)
    {
        return itemList.get(position).getTitle();
    }
    public String GetPrice(int position)
    {
        return itemList.get(position).getPrice();
    }
    public Integer GetIcon(int position)
    {
        return itemList.get(position).getIcon();
    }
}
