/*
 *作者：施武轩
 * 创建时间：2020.7.10
 * 更新时间：2020.7.19
 */

package com.whu.dadatraffic.Service;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;

import androidx.annotation.NonNull;

import com.whu.dadatraffic.Activity.LoginActivity;
import com.whu.dadatraffic.Activity.OrdersActivity;
import com.whu.dadatraffic.Activity.RouteActivity;
import com.whu.dadatraffic.Base.CurOrder;
import com.whu.dadatraffic.Base.Driver;
import com.whu.dadatraffic.Base.Order;
import com.whu.dadatraffic.MainActivity;
import com.whu.dadatraffic.Utils.DBConstent;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Vector;

public class OrderService {
    private Timer timer;
    public static CurOrder curOrder;
    public static Vector<Order> historyOrders;

    /**
     * 添加新建订单
     * @param newOrder 新建的Order对象
     */
    public void addOrder(CurOrder newOrder) {
        curOrder = newOrder;
        //存入数据库
        final String addUrlStr = DBConstent.URL_CreateOrder + "?type=add&phonenumber=" + newOrder.getCustomerPhoneNum()  + "&start=" + newOrder.getStartPoint() +"&destination="
                +newOrder.getDestination()+"&createtime=" + newOrder.getCreateTime();
        new OrderAsyncTask().execute(addUrlStr,"add");
        //historyOrders.add(new Order(newOrder.getCustomerPhoneNum(),newOrder.getStartPoint(),newOrder.getDestination()));
    }

    /**
     * 取消当前订单
     */
    public void cancelOrder() {
        final String cancelUrlStr = DBConstent.URL_Order + "?type=cancel&orderid=" + curOrder.getOrderID();
        new OrderAsyncTask().execute(cancelUrlStr,"cancel");
        historyOrders.get(0).setOrderState("cancel");
        curOrder = null;
    }

    /**
    * 将当前订单的评价和评分存入数据库
     * @param score 当前订单用户的评分
     * @param evaluation 当前订单的用户评价
    */
    public void evaluate(int score,String evaluation){
        String urlStr = DBConstent.URL_Order + "?type=evaluate&orderid="+curOrder.getOrderID()+"&score="+score+"&evaluation="+evaluation;
        new OrderAsyncTask().execute(urlStr,"evaluate");
    }

    /**
     * 完成当前订单,支付成功后将价格存入数据库
     * @param price 本次订单金额
     */
    public void completeOrder(double price) {
        String completeSqlStr = DBConstent.URL_Order+"?type=complete&orderid=" + curOrder.getOrderID() + "&price=" +price;
        new OrderAsyncTask().execute(completeSqlStr,"complete");
    }

    /**
     * 查询当前用户的历史订单并存入historyOrders中
     * 需要服务器返回当前用户的订单数量和所有订单信息
     */
    public void getHistoryOrders(){
        historyOrders = new Vector<Order>();
        String getHistoryUrlStr = DBConstent.URL_Order + "?type=getorders&phonenumber="+ UserService.curUser.getPhoneNumber();
        new OrderAsyncTask().execute(getHistoryUrlStr);
    }

    /**
     * 查看当前订单的状态，并执行相应操作
     * 用户开始叫车后调用
     */
    public void queryOrderState(){
        final String queryUrl = DBConstent.URL_Order + "?type=query&orderid=" + curOrder.getOrderID();
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                new QueryAsyncTask().execute(queryUrl);
            }
        },1000,2000);//每隔两秒发送一次，查询当前订单的状态,当查寻到订单状态为已取消或待支付或者已结束，就会终止该计时器
    }

    /**
     * AsyncTask类的三个泛型参数：
     * （1）Param 在执行AsyncTask是需要传入的参数，可用于后台任务中使用
     * （2）后台任务执行过程中，如果需要在UI上先是当前任务进度，则使用这里指定的泛型作为进度单位
     * （3）任务执行完毕后，如果需要对结果进行返回，则这里指定返回的数据类型
     */
    public class OrderAsyncTask extends AsyncTask<String, Integer, String> {
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
            String type = params[1];//当前操作类型
            try {
                URL url = new URL(params[0]); // 声明一个URL
                connection = (HttpURLConnection) url.openConnection(); // 打开该URL连接
                connection.setRequestMethod("GET"); // 设置请求方法，“POST或GET”
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

            if(type.equals("add")){//当前执行的是新建订单操作
                curOrder.setOrderID(response.toString());
            }
            else if (type.equals("getHistory")){//当前执行的是查询历史订单操作
                String info[]=response.toString().split(";");
                int count = Integer.parseInt(info[0]);
                for (int i=1;i<count*10+1;i+=10){
                    historyOrders.add(new Order(info[i],info[i+1],info[i+2],info[i+3],info[i+4],info[i+5],info[i+6],info[i+7],info[i+8],Integer.parseInt(info[i+9])));
                }
            }

            return ""; // 这里返回的结果就作为onPostExecute方法的入参
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            // 如果在doInBackground方法，那么就会立刻执行本方法
            // 本方法在UI线程中执行，可以更新UI元素，典型的就是更新进度条进度，一般是在下载时候使用
        }

        /**
         * 运行在UI线程中，所以可以直接操作UI元素
         * @param result = response.toString()
         */

        @Override
        protected void onPostExecute(String result) {

        }

    }


    /**
     * AsyncTask类的三个泛型参数：
     * （1）Param 在执行AsyncTask是需要传入的参数，可用于后台任务中使用
     * （2）后台任务执行过程中，如果需要在UI上先是当前任务进度，则使用这里指定的泛型作为进度单位
     * （3）任务执行完毕后，如果需要对结果进行返回，则这里指定返回的数据类型
     */
    public class QueryAsyncTask extends AsyncTask<String, Integer, String> {
        //OnDataFinishedListener onDataFinishedListener;

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
            try {
                URL url = new URL(params[0]); // 声明一个URL
                connection = (HttpURLConnection) url.openConnection(); // 打开该URL连接
                connection.setRequestMethod("GET"); // 设置请求方法，“POST或GET”
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

            String info[]=response.toString().split(";");
            curOrder.setOrderState(info[0]);
            if(info[0].equals("start")){//查询到订单处于准备状态
                curOrder.setDriverPhone(info[1]);//设置当前订单司机手机号
                curOrder.setDriverName(info[2]);//设置当前订单司机姓名
                curOrder.setCarID(info[3]);//设置当前订单司机车牌号
                curOrder.setDriverScore(Integer.parseInt(info[4]));
            }
            if(info[0].equals("cancel")||info[0].equals("unpaid")||info[0].equals("end")){
                timer.cancel();
            }

            return ""; // 这里返回的结果就作为onPostExecute方法的入参
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

        }
    }
}
