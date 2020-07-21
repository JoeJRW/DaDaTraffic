package com.whu.dadatraffic.Service;
/*
 *author：张朝勋
 * create time：7/9
 * update time: 7/15
 */
import com.whu.dadatraffic.Base.Ticket;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Vector;

public class TicketService extends Ticket {
    Vector<Ticket> ticketList = new Vector<Ticket>();
    Vector<Ticket> usefulTicket = new Vector<Ticket>();
    int position = 0;

    public TicketService() {
        super();
    }

    //添加优惠券
    public void AddTicket(String title, String discount, Integer icon, String startDate, String endDate, boolean status, Integer resource)
    {
        ticketList.add(new Ticket( title, discount, icon, startDate, endDate, status,resource));
    }
    //删除优惠券
    public void RemoveTicket(String title)
    {
        for(int i = 1; i <= ticketList.size(); i++)
        {
            if(title == ticketList.get(i).getTitle())
            {
                position = i;
                break;
            }
        }
        ticketList.remove(position);
    }
    //获取list长度
    public int Count()
    {
        return ticketList.size();
    }
    //获取优惠券名称
    public String GetTitle(int position)
    {
        return ticketList.get(position).getTitle();
    }
    //获取优惠券效果
    public String GetDiscount(int position)
    {
        return ticketList.get(position).getDiscount();
    }
    //获取优惠券有效日期
    public String GetStartDate(int position)
    {
        return ticketList.get(position).getStartDate();
    }
    public String GetEndDate(int position)
    {
        return ticketList.get(position).getEndDate();
    }
    //获取优惠券图标
    public Integer GetIcon(int position)
    {
        return ticketList.get(position).getIcon();
    }
    //获取优惠券新图标文件名，以在传值后
    public Integer GetImageResource(int position)
    {
        return ticketList.get(position).getImageResource();
    }
    //判断优惠券是否过期
    public void JudgeStatus(int position)
    {
        SimpleDateFormat eFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try{
            Date nDate = new Date(System.currentTimeMillis());;
            Date eDate = eFormat.parse(ticketList.get(position).getStartDate());
            if(eDate.before(nDate))
            {
                RemoveTicket(ticketList.get(position).getTitle());
            }
        }
        catch (ParseException e)
        {
            e.printStackTrace();
        }
    }
}
