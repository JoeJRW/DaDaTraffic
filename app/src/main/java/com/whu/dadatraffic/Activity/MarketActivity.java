package com.whu.dadatraffic.Activity;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import com.whu.dadatraffic.R;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;

import com.whu.dadatraffic.ViewHolder;



public class MarketActivity extends AppCompatActivity {

    private String[] titles={"围巾","苹果","耳机","马克杯","38元打车券","音箱"};
    private String[] prices={"1800分","100分/kg","2480分","880分","380分","580分"};
    //图片集合
    private  int[] icons={R.drawable.icon_scarf,R.drawable.icon_apple,R.drawable.icon_earphone,
            R.drawable.icon_cup,R.drawable.icon_card,R.drawable.icon_box};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_market);
        setCustomActionBar();

        //初始化ListView控件
        ListView listView=findViewById(R.id.lv);
        //创建一个Adapter的实例
        Adapter newAdapter=new Adapter();
        //设置Adapter
        listView.setAdapter(newAdapter);
    }

    class Adapter extends BaseAdapter {

        @Override
        public int getCount(){       //得到item的总数
            return titles.length;    //返回ListView Item条目代表的对象
        }

        @Override
        public Object getItem(int position){
            return titles[position]; //返回item的数据对象
        }
        @Override
        public long getItemId(int position){
            return position;         //返回item的id
        }


        @Override
        public View getView(int position, View convertView, ViewGroup parent){//获取item中的View视图
            ViewHolder holder;
            if(convertView==null){
                convertView=View.inflate(MarketActivity.this, R.layout.activity_items, null);
                holder=new com.whu.dadatraffic.ViewHolder();
                holder.title=convertView.findViewById(R.id.title);
                holder.price=convertView.findViewById(R.id.price);
                holder.image=convertView.findViewById(R.id.item);
                convertView.setTag(holder);
            }else{
                holder=(com.whu.dadatraffic.ViewHolder)convertView.getTag();
            }
            holder.title.setText(titles[position]);
            holder.price.setText(prices[position]);
            holder.image.setImageResource(icons[position]);
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


