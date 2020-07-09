package com.whu.dadatraffic.Service;

import com.whu.dadatraffic.Base.Ticket;

import java.util.Date;
import java.util.Vector;

public class TicketService extends Ticket {
    Vector<Ticket> ticketList = new Vector<Ticket>();
    int id = 1;
    boolean found = false;
    int[] count = null;
    int position = 0;

    public TicketService() {
        super();
    }

    public void AddTicket(String title, String discount, Integer icon, String startDate, String endDate, boolean status)
    {
        ticketList.add(new Ticket(this.id, title, discount, icon, startDate, endDate, status));
        id++;
    }

    public void RemoveTicket()
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
    public Integer GetIcon(int position)
    {
        return ticketList.get(position).getIcon();
    }
}
