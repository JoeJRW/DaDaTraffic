package com.whu.dadatraffic.Activity;
/*
 *author：张朝勋
 * create time：7/8
 * update time: 7/21
 */
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.whu.dadatraffic.Base.MarketItem;
import com.whu.dadatraffic.Base.User;
import com.whu.dadatraffic.R;
import com.whu.dadatraffic.Service.MarketItemService;
import com.whu.dadatraffic.Service.OrderService;
import com.whu.dadatraffic.Service.UserService;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class MarketOrderActivity extends AppCompatActivity {

    UserService userService = new UserService();
    MarketItemService marketItemService = new MarketItemService();
    private ArrayList<MarketItem> itemArrayList = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_market_order);
        itemArrayList = marketItemService.getHistoryItems();
        setCustomActionBar();

        //初始化ListView控件
        ListView listView = findViewById(R.id.molv);
        //创建一个Adapter的实例
        MarketOrderAdapter newMarketOrderAdapter = new MarketOrderAdapter();
        //设置Adapter
        listView.setAdapter(newMarketOrderAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {

                //跳转到商品详情界面
                //定义跳转对象
                Intent intentToTDetail = new Intent();
                //设置跳转的起始界面和目的界面
                intentToTDetail.setClass(MarketOrderActivity.this, MarketOrderDetailActivity.class);
                //传递选中View的对象
                Bundle bundle = new Bundle();
                bundle.putSerializable("orderList", itemArrayList.get(position));
                //将bundle包中数据绑定到intent
                intentToTDetail.putExtras(bundle);
                //启动跳转，并传输对应数据
                startActivity(intentToTDetail);
            }
        });
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

    //返回父活动与前往订单详情页
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
        public TextView tv_price;
    }

    //设置Adapter来实现listView中各商品的呈现
    public class MarketOrderAdapter extends BaseAdapter {

        @Override
        public int getCount() {       //得到item的总数
            return itemArrayList.size();    //返回ListView Item条目代表的对象
        }

        @Override
        public Object getItem(int position) {
            return itemArrayList.get(position).getTitle(); //返回item的数据对象
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
                holder.tv_price = convertView.findViewById(R.id.tv_moPrice);
                convertView.setTag(holder);
            } else//非空，则复用convertview
            {
                holder = (ViewHolder) convertView.getTag();
            }
            //设置该View中各项值
            holder.title.setText(itemArrayList.get(position).getTitle());
            holder.price.setText(itemArrayList.get(position).getPrice());
            holder.count.setText(itemArrayList.get(position).getCount()+"个");
            holder.image.setImageResource(itemArrayList.get(position).getIcon());
            //holder.tv_price.setText("单价：");
            //TODO 显示单个订单总价
            //System.out.println(marketItemService.scoreInAll);
            return convertView;
        }
    }
}