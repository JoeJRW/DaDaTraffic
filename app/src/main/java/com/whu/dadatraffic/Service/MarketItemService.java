package com.whu.dadatraffic.Service;
/*
 *author：张朝勋
 * create time：7/9
 * update time: 7/14
 */
import android.os.AsyncTask;

import com.whu.dadatraffic.Base.MarketItem;
import com.whu.dadatraffic.R;
import com.whu.dadatraffic.Utils.DBConstent;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class MarketItemService extends MarketItem implements Serializable {

    private ArrayList<MarketItem> historyItems = new ArrayList<MarketItem>();
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

    /**用户在积分商城兑换商品后调用此函数，增加购买记录
     * @param items 本次购买的所有商品
     */
    public void buyItem(MarketItem items[], String phoneNumber){
        for (int i=0;i<items.length;i++){
            final String buyUrlStr = DBConstent.URL_Item + "?type=buy&phonenumber=" + phoneNumber + "&title="+items[i].getTitle() + "&count="+items[i].getCount();
            new ItemAsyncTask().execute(buyUrlStr);
        }
    }

    /**用户查询积分商城购买记录,将用户购买的所有商品存入historyItems。
     * @param phoneNumber 用户登录使用的手机号
     */
    public void queryAllItem(String phoneNumber){
        final String queryUrlStr = DBConstent.URL_Item + "?type=query&phonenumber=" + phoneNumber;
        new ItemAsyncTask().doInBackground(queryUrlStr);
    }

    public class ItemAsyncTask extends AsyncTask<String, Integer, String> {
        @Override
        protected void onPreExecute() {
            //Log.w("WangJ", "task onPreExecute()");
        }

        /**
         * @param params 这里的params是一个数组，即AsyncTask在激活运行是调用execute()方法传入的参数
         */
        @Override
        protected String doInBackground(String... params) {
            //Log.w("WangJ", "task doInBackground()");
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

            return response.toString();
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            // 如果在doInBackground方法，那么就会立刻执行本方法
            // 本方法在UI线程中执行，可以更新UI元素，典型的就是更新进度条进度，一般是在下载时候使用
        }

        /**
         * 运行在UI线程中，所以可以直接操作UI元素
         * @param
         */

        @Override
        protected void onPostExecute(String result) {
            String results[] = result.split(";");
            for (int i = 1;i<Integer.parseInt(results[0]);i+=3){
                historyItems.add(new MarketItem(results[i],results[i+1],Integer.parseInt(results[i+2])));
            }
        }

    }
}
