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

public class OrderService {
    private boolean flag = false;//表示当前操作是否成功
    private volatile boolean isRun = true;//表示当前异步进程是否正在运行
    public static Order curOrder;
    public Order[] historyOrders;

    /**
     * 添加新建订单
     * @param order 新建的Order对象
     * @return 新建订单成功返回true，新建订单失败返回false
     */
    public void addOrder(Order order) {
        curOrder = order;
        //OrdersActivity.orderList.add(order);
        //存入数据库
        final String addUrlStr = DBConstent.URL_Order + "?type=add&phonenumber=" + order.getCustomerPhoneNum()  + "&start=" + order.getStartPoint() +"&destination="
                +order.getDestination()+"&createtime=" + order.getCreateTime();
        new AddAsyncTask().execute(addUrlStr);
    }

    /**
     * 取消当前订单
     * @return 取消成功返回true，取消失败返回false
     */
    public boolean cancelOrder() {
        final String cancelUrlStr = DBConstent.URL_Order + "?type=cancel&orderid=" + curOrder.getOrderID();
        new OrderAsyncTask().execute(cancelUrlStr,"cancel");
        curOrder = null;
        while (isRun){

        }

        return flag;
    }

    //根据订单编号接受相应订单
    public boolean acceptOrder(String orderID, Driver driver) {
        final String acceptUrlStr = DBConstent.URL_Order + "?type=accept&orderid=" + orderID+"&drivername="+driver.getName()+"&carnumber="+
                driver.getCarId();
        new OrderAsyncTask().execute(acceptUrlStr,"accept");
        while (isRun){
        }

        return flag;
    }

    /**
     * 完成当前订单,支付成功后将价格存入数据库
     * @param price 本次订单金额
     * @return 取消成功返回true，取消失败返回false
     */
    public boolean completeOrder(double price) {
        final String completeSqlStr = DBConstent.URL_Order+"?type=complete&orderid=" + curOrder.getOrderID() + "&price=" +price;
        return flag;
    }


    /**
     * 查看当前订单的司机相关信息
     * 用户端在订单被接受后调用
     */
    public void queryDriverInfo(){
        String queryUrl = DBConstent.URL_Order + "?type=query&orderid=" + curOrder.getOrderID();
        new OrderAsyncTask().execute(queryUrl,"query");
    }

    /**
     * AsyncTask类的三个泛型参数：
     * （1）Param 在执行AsyncTask是需要传入的参数，可用于后台任务中使用
     * （2）后台任务执行过程中，如果需要在UI上先是当前任务进度，则使用这里指定的泛型作为进度单位
     * （3）任务执行完毕后，如果需要对结果进行返回，则这里指定返回的数据类型
     */
    public class OrderAsyncTask extends AsyncTask<String, Integer, String> {
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

            if(type.equals("query")){
                String info[]=response.toString().split(";");
                curOrder.setDriverName(info[0]);
                curOrder.setCarNumber(info[1]);
            }

            return response.toString(); // 这里返回的结果就作为onPostExecute方法的入参
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
            if(result.equals("200")){
                flag=true;
            }
            else {
                flag=false;
            }
            isRun=false;
        }

    }


    /**
     * AsyncTask类的三个泛型参数：
     * （1）Param 在执行AsyncTask是需要传入的参数，可用于后台任务中使用
     * （2）后台任务执行过程中，如果需要在UI上先是当前任务进度，则使用这里指定的泛型作为进度单位
     * （3）任务执行完毕后，如果需要对结果进行返回，则这里指定返回的数据类型
     */
    public class AddAsyncTask extends AsyncTask<String, Integer, String> {
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

            return response.toString(); // 这里返回的结果就作为onPostExecute方法的入参
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
            if (!result.equals("100"))
                curOrder.setOrderID(result);
        }
    }
}
