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

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import com.whu.dadatraffic.Service.MarketItemService;

import java.util.ArrayList;
import java.util.Vector;

public class MarketOrderActivity extends AppCompatActivity {
    MarketItemService marketItemService = new MarketItemService();
    TextView priceInAll = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_market_order);
        setCustomActionBar();


        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        ArrayList<MarketItem> MarketOrderList = (ArrayList<MarketItem>)bundle.getSerializable("orderList");

        marketItemService.setmOrderItemList(MarketOrderList);
        priceInAll = (TextView)findViewById(R.id.MarketOrderPriceinAll);
        //初始化ListView控件
        ListView listView = findViewById(R.id.marketOrderLv);
        //创建一个Adapter的实例
        MarketOrderAdapter newMarketOrderAdapter = new MarketOrderAdapter();
        //设置Adapter
        listView.setAdapter(newMarketOrderAdapter);
    }


    //显示home按钮与标题
    private void setCustomActionBar() {
        //1.获取 actionbar 对象
        ActionBar actionBar = this.getSupportActionBar();
        //2.显示home键
        actionBar.setDisplayHomeAsUpEnabled(true);
        //3.设置标题
        actionBar.setTitle("商城订单");
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
    public class MarketOrderAdapter extends BaseAdapter {

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
                convertView = View.inflate(MarketOrderActivity.this, R.layout.activity_marketorder_list, null);
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
            marketItemService.SumMarketOrderScore();
            priceInAll.setText("合计："+marketItemService.scoreInAll+"积分");
            return convertView;
        }
    }
}