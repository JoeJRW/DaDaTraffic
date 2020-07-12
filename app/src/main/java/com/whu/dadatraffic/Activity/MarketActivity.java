package com.whu.dadatraffic.Activity;
/*
*author：张朝勋
* create time：7/8
* update time: 7/11
 */
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.whu.dadatraffic.R;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.whu.dadatraffic.Service.ItemService;

import java.util.Map;


public class MarketActivity extends AppCompatActivity {

    ItemService itemService = new ItemService();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_market);
        setCustomActionBar();

        initItems();

        //初始化ListView控件
        ListView listView=findViewById(R.id.lv);
        //创建一个Adapter的实例
        MarketAdapter newMarketAdapter =new MarketAdapter();
        //设置Adapter
        listView.setAdapter(newMarketAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                //跳转到优惠券详情界面
                //定义跳转对象
                Intent intentToTDetail = new Intent();
                //设置跳转的起始界面和目的界面
                intentToTDetail.setClass(MarketActivity.this, ItemDetailActivity.class);
                //传递选中View的对象
                Bundle bundle = new Bundle();
                bundle.putCharSequence("title",itemService.GetTitle(position));
                bundle.putCharSequence("price",itemService.GetPrice(position));
                bundle.putInt("icon",itemService.GetIcon(position));
                //将bundle包中数据绑定到intent
                intentToTDetail.putExtras(bundle);
                //启动跳转，并传输对应数据
                startActivity(intentToTDetail);
            }
        });
    }


    private void initItems()
    {
        itemService.AddItem("围巾","1800分",R.drawable.icon_scarf);
        itemService.AddItem("耳机","2480分",R.drawable.icon_earphone);
        itemService.AddItem("马克杯","880分",R.drawable.icon_cup);
        itemService.AddItem("38元打车券","380分",R.drawable.icon_38);
        itemService.AddItem("音箱","580分",R.drawable.icon_box);
        itemService.AddItem("8折优惠券","80分",R.drawable.icon_d8);
    }

    //显示home按钮与标题
    private void setCustomActionBar() {
        //1.获取 actionbar 对象
        ActionBar actionBar = this.getSupportActionBar();
        //2.显示home键
        actionBar.setDisplayHomeAsUpEnabled(true);
        //3.设置标题
        actionBar.setTitle("积分商城");
    }

    public class ViewHolder {
        public TextView title;
        public TextView price;
        public ImageView image;
        public TextView add,reduce,count;
    }

    //返回父活动
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home)
        {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    //设置Adapter来实现listView中各商品的呈现
     public class MarketAdapter extends BaseAdapter {


        @Override
        public int getCount(){       //得到item的总数

            return itemService.Count();    //返回ListView Item条目代表的对象
        }

        @Override
        public Object getItem(int position)
        {
            return itemService.GetTitle(position); //返回item的数据对象
        }

        @Override
        public long getItemId(int position)
        {
            return position;         //返回item的id
        }


        @Override
        public View getView(int position, View convertView, ViewGroup parent){//获取item中的View视图,使用convertview增加系统效率
            ViewHolder holder;
            if(convertView==null)//convertview为空则绑定各控件
            {
                convertView=View.inflate(MarketActivity.this, R.layout.activity_items, null);
                holder=new ViewHolder();
                holder.title=convertView.findViewById(R.id.title);
                holder.price=convertView.findViewById(R.id.price);
                holder.image=convertView.findViewById(R.id.item);
                //todo 完成积分商城购物车功能
//                holder.add=convertView.findViewById(R.id.add);
//                holder.reduce = convertView.findViewById(R.id.reduce);
//                holder.count = convertView.findViewById(R.id.count);
                convertView.setTag(holder);
            }
            else//非空，则复用convertview
            {
                holder=(ViewHolder)convertView.getTag();
            }
            //设置该View中各项值
            holder.title.setText(itemService.GetTitle(position));
//            holder.add.setBackgroundColor(Color.GRAY);
//            holder.reduce.setBackgroundColor(Color.GRAY);

            holder.price.setText(itemService.GetPrice(position));
            holder.image.setImageResource(itemService.GetIcon(position));
            return convertView;
        }
    }

}


