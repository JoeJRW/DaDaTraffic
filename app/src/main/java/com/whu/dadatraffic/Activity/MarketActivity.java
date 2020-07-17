package com.whu.dadatraffic.Activity;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.whu.dadatraffic.R;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.whu.dadatraffic.Service.ItemService;
import com.whu.dadatraffic.Base.ViewHolder;



public class MarketActivity extends AppCompatActivity {

    ItemService itemService = new ItemService();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_market);
        setCustomActionBar();
        /*
        itemService.AddItem("围巾","1800分",R.drawable.icon_scarf);
        itemService.AddItem("耳机","2480分",R.drawable.icon_earphone);
        itemService.AddItem("马克杯","880分",R.drawable.icon_cup);
        itemService.AddItem("38元打车券","380分",R.drawable.icon_38);
        itemService.AddItem("音箱","580分",R.drawable.icon_box);
        itemService.AddItem("8折优惠券","80分",R.drawable.icon_d8);
        */

        //初始化ListView控件
        ListView listView=findViewById(R.id.lv);
        //创建一个Adapter的实例
        MarketAdapter newMarketAdapter =new MarketAdapter();
        //设置Adapter
        listView.setAdapter(newMarketAdapter);
    }

    class MarketAdapter extends BaseAdapter {

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
                convertView.setTag(holder);
            }
            else//非空，则复用convertview
            {
                holder=(ViewHolder)convertView.getTag();
            }
            //设置该View中各项值
            holder.title.setText(itemService.GetTitle(position));
            holder.price.setText(itemService.GetPrice(position));
            holder.image.setImageResource(itemService.GetIcon(position));
            return convertView;
        }
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

}


