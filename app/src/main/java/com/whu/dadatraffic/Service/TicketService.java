package com.whu.dadatraffic.Service;
/*
 *author：张朝勋
 * create time：7/9
 * update time: 7/15
 */
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.whu.dadatraffic.Activity.MarketOrderActivity;
import com.whu.dadatraffic.Activity.WalletActivity;
import com.whu.dadatraffic.Base.Ticket;
import com.whu.dadatraffic.R;
import com.whu.dadatraffic.Utils.DBConstent;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Vector;

public class TicketService extends Ticket {
    public static Vector<Ticket> ticketList = new Vector<Ticket>();
    private Vector<Integer> position;

    public TicketService() {
        super();
    }


    //添加优惠券
    public void AddTicket(String title, String discount, Integer icon, String startDate, String endDate, Integer resource)
    {
        ticketList.add(new Ticket( title, discount, icon, startDate, endDate, resource));
    }

    public int getResourceId(String fileName) {
        try {
            Field field = R.drawable.class.getField(fileName);
            return Integer.parseInt(field.get(null).toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }


    //移除不可用优惠券
    public void RemoveTicket(String title)
    {
        position = new Vector<Integer>();
        if(!ticketList.isEmpty())
        {
            int found = 0;
            for (int i = 1; i <= ticketList.size(); i++) {
                if (title.equals(ticketList.get(i-1).getTitle())) {
                    position.add(i-1);
                    found++;
                }
            }
            for (int i = found-1; i >= 0; i--) {
                ticketList.remove(position.get(i));
            }
        }
        else
        {
            return;
        }
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
    //获取优惠券新图标文件名，以在传值后调用新图片
    public Integer GetImageResource(int position)
    {
        return ticketList.get(position).getImageResource();
    }
    //判断该情况下优惠券是否可用
    public void JudgeUse(double price)
    {
        if(price<38)
        {
            RemoveTicket("38元打车券");
            RemoveTicket("48元打车券");
            RemoveTicket("58元打车券");
        }
        if(price<48)
        {
            RemoveTicket("48元打车券");
            RemoveTicket("58元打车券");
        }
        if(price<58)
        {
            RemoveTicket("58元打车券");
        }
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

    /**
     * 在数据库中删除相应的优惠券
     * 使用优惠券时调用
     * @param ticket 购使用的优惠券对象
     */
    public void useTicket(Ticket ticket){
        String useUrl = DBConstent.URL_Ticket + "?type=use&userphone="+UserService.curUser.getPhoneNumber()+"&title="+ticket.getTitle()
                +"&start="+ticket.getStartDate()+"&end="+ticket.getEndDate();
        new TicketAsyncTask().execute(useUrl,"use");
    }

    /**
     * 向服务器查询当前用户的所有优惠券
     * 查看用户优惠券时调用
     * @param flag 提示当前操作是否要进行UI操作
     */
    public void queryAllTicket(boolean flag){
        ticketList = new Vector<Ticket>();
        String queryUrl = DBConstent.URL_Ticket + "?type=query&userphone="+UserService.curUser.getPhoneNumber();
        if(flag) {//代表当前执行查询后需要操作UI
            new TicketAsyncTask().execute(queryUrl, "queryok");
        }
        else {
            new TicketAsyncTask().execute(queryUrl, "query");
        }
    }

    /**
     * 为当前用户添加优惠券
     * 购买优惠券时调用
     * @param ticket 购买的优惠券对象
     */
    public void addTicket(Ticket ticket){
        String addUrl = DBConstent.URL_Ticket + "?type=add&userphone="+UserService.curUser.getPhoneNumber()+"&title="+ticket.getTitle()+"&discount="+ticket.getDiscount()
                +"&start="+ticket.getStartDate()+"&end="+ticket.getEndDate()+"&icon="+ticket.getIcon()+"&image="+ticket.getImageResource();
        new TicketAsyncTask().execute(addUrl,"add");
    }

    public class TicketAsyncTask extends AsyncTask<String, Integer, String> {
        @Override
        protected void onPreExecute() {
        }

        /**
         * @param params 这里的params是一个数组，即AsyncTask在激活运行是调用execute()方法传入的参数
         */
        @Override
        protected String doInBackground(String... params) {
            HttpURLConnection connection = null;
            StringBuilder response = new StringBuilder();
            String type=params[1];
            try {
                URL url = new URL(params[0]); // 声明一个URL
                connection = (HttpURLConnection) url.openConnection(); // 打开该URL连接
                connection.setRequestMethod("GET"); // 设置请求方法，“POST或GET”，我们这里用GET，在说到POST的时候再用POST
                connection.setConnectTimeout(80000); // 设置连接建立的超时时间
                connection.setReadTimeout(80000); // 设置网络报文收发超时时间
                InputStream in = connection.getInputStream();  // 通过连接的输入流获取下发报文，然后就是Java的流处理
                BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            if (type.equals("query")||type.equals("queryok")){
                String info[] = response.toString().split(";");
                int count = Integer.parseInt(info[0]);
                for(int i=1;i<count*6+1;i+=6){
                    Ticket ticket = new Ticket(info[i],info[i+1],Integer.parseInt(info[i+2]),info[i+3],info[i+4],Integer.parseInt(info[i+5]));
                    ticketList.add(ticket);
                }
                return type;
            }

            return response.toString();
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            // 如果在doInBackground方法，那么就会立刻执行本方法
            // 本方法在UI线程中执行，可以更新UI元素，典型的就是更新进度条进度，一般是在下载时候使用
        }

        /**
         * 运行在UI线程中，所以可以直接操作UI元素
         * @param result doInBackground方法的返回值
         */
        @Override
        protected void onPostExecute(String result) {
            if(result.equals("queryok")){
                WalletActivity.instance.showCount();
            }
        }

    }
}
