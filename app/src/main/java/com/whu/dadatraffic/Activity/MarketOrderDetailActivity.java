package com.whu.dadatraffic.Activity;
/*
 *author：张朝勋
 * create time：7/15
 * update time: 7/15
 */
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.whu.dadatraffic.Base.MarketItem;
import com.whu.dadatraffic.R;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import com.whu.dadatraffic.Service.MarketItemService;
import com.whu.dadatraffic.Service.UserService;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class MarketOrderDetailActivity extends AppCompatActivity {
    MarketItemService marketItemService = new MarketItemService();
    TextView priceInAll = null;
    TextView createTime = null;
    Date curDate = new Date(System.currentTimeMillis());
    @SuppressLint("SimpleDateFormat")
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    String curTime = simpleDateFormat.format(curDate);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_market_orderdetail);
        setCustomActionBar();

        //获取传来的list并使用该list给orderList赋值
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        ArrayList<MarketItem> MarketOrderList = (ArrayList<MarketItem>)bundle.getSerializable("orderList");

        marketItemService.SetDate(curTime,MarketOrderList);
        marketItemService.setmOrderItemList(MarketOrderList);
        priceInAll = (TextView)findViewById(R.id.MarketOrderPriceinAll);
        createTime = (TextView)findViewById(R.id.MarketOrderCreateTime);


        //初始化ListView控件
        ListView listView = findViewById(R.id.marketOrderLv);
        //创建一个Adapter的实例
        MarketOrderDetailAdapter newMarketOrderDetailAdapter = new MarketOrderDetailAdapter();
        //设置Adapter
        listView.setAdapter(newMarketOrderDetailAdapter);
        marketItemService.buyItem(MarketOrderList, UserService.curUser.getPhoneNumber());
        createTime.setText("创建时间："+marketItemService.GetMOrder(0).getDate());

    }


    //显示home按钮与标题
    private void setCustomActionBar() {
        //1.获取 actionbar 对象
        ActionBar actionBar = this.getSupportActionBar();
        //2.显示home键
        actionBar.setDisplayHomeAsUpEnabled(true);
        //3.设置标题
        actionBar.setTitle("订单详情");
    }

    //返回父活动
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public class ViewHolder {
        public TextView title;
        public TextView price;
        public ImageView image;
        public TextView count;
    }



    //设置Adapter来实现listView中各商品的呈现
    public class MarketOrderDetailAdapter extends BaseAdapter {

        @Override
        public int getCount() {       //得到item的总数

            return marketItemService.MOCount();    //返回ListView Item条目代表的对象
        }

        @Override
        public Object getItem(int position) {
            return marketItemService.GetMOrder(position).getTitle(); //返回item的数据对象
        }

        @Override
        public long getItemId(int position) {
            return position;         //返回item的id
        }


        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {//获取item中的View视图,使用convertview增加系统效率
            final ViewHolder holder;
            if (convertView == null)//convertview为空则绑定各控件
            {
                convertView = View.inflate(MarketOrderDetailActivity.this, R.layout.activity_marketorder_list, null);
                holder = new ViewHolder();
                holder.title = convertView.findViewById(R.id.moTitle);
                holder.price = convertView.findViewById(R.id.moPrice);
                holder.image = convertView.findViewById(R.id.marketOrderItem);
                holder.count = convertView.findViewById(R.id.moCount);
                convertView.setTag(holder);
            } else//非空，则复用convertview
            {
                holder = (ViewHolder) convertView.getTag();
            }
            //设置该View中各项值
            holder.title.setText(marketItemService.GetMOrder(position).getTitle());
            holder.price.setText(marketItemService.GetMOrder(position).getPrice());
            holder.image.setImageResource(marketItemService.GetMOrder(position).getIcon());
            holder.count.setText(Integer.toString(marketItemService.GetMOrder(position).getCount())+"份");
            priceInAll.setText("合计："+marketItemService.scoreInAll+"积分");
            return convertView;
        }
    }
}