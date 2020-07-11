package com.whu.dadatraffic.Service;

import com.whu.dadatraffic.Base.Ticket;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Vector;

public class TicketService extends Ticket {
    Vector<Ticket> ticketList = new Vector<Ticket>();
    // id = 1;
    boolean found = false;
    int position = 0;

    public TicketService() {
        super();
    }

    public void AddTicket(String title, String discount, Integer icon, String startDate, String endDate, boolean status, String name)
    {
        ticketList.add(new Ticket( title, discount, icon, startDate, endDate, status,name));
    }

    public void RemoveTicket(String title)
    {
        for(int i = 1; i <= ticketList.size(); i++)
        {
            if(title == ticketList.get(i).getTitle())
            {
                found = true;
                position = i;
                break;
            }
        }
        ticketList.remove(position);
    }
    public int Count()
    {
        return ticketList.size();
    }
    public String GetTitle(int position)
    {
        return ticketList.get(position).getTitle();
    }
    public String GetDiscount(int position)
    {
        return ticketList.get(position).getDiscount();
    }
    public String GetStartDate(int position)
    {
        return ticketList.get(position).getStartDate();
    }
    public String GetEndDate(int position)
    {
        return ticketList.get(position).getEndDate();
    }
    public Integer GetIcon(int position)
    {
        return ticketList.get(position).getIcon();
    }
    public String GetName(int position)
    {
        return ticketList.get(position).getImageResource();
    }
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
