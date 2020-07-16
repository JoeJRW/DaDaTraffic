package com.whu.dadatraffic.Service;

import android.os.AsyncTask;

import com.whu.dadatraffic.Base.Item;
import com.whu.dadatraffic.Utils.DBConstent;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Vector;

public class ItemService extends Item {
    private Item[] historyItems;
    /**用户在积分商城兑换商品后调用此函数，增加购买记录
     * @param items 本次购买的所有商品
     * @param numbers 对应商品的数量
     */
    public void buyItem(Item items[],int numbers[]){
        for (int i=0;i<items.length;i++){
            final String buyUrlStr = DBConstent.URL_Item + "?type=buy&title="+items[i].getTitle() + "&number="+numbers[i];
            new ItemAsyncTask().execute(buyUrlStr);
        }
    }

    /**用户查询积分商城购买记录
     * @param phoneNumber 用户登录使用的手机号
     */
    public void queryAllItem(String phoneNumber){

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

        }

    }
}
