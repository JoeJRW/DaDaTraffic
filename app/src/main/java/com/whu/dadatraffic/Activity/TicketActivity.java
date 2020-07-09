package com.whu.dadatraffic.Activity;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.whu.dadatraffic.Base.ViewHolder;
import com.whu.dadatraffic.R;
import com.whu.dadatraffic.Service.TicketService;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TicketActivity extends AppCompatActivity {

    TicketService ticketService = new TicketService();
    String giveTime = null;
    String endTime = "2021年8月1日";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticket);

        setCustomActionBar();

        Date date = new Date(System.currentTimeMillis());
        @SuppressLint("SimpleDateFormat") SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy年MM月dd日");
        giveTime = simpleDateFormat.format(date);
        ticketService.AddTicket("38元打车券","8折",R.drawable.icon_38,giveTime,endTime,true);
        //ticketService.AddTicket("8折优惠券","8折",R.drawable.icon_card);
        //初始化ListView控件
        ListView listView=findViewById(R.id.tlv);
        //创建一个Adapter的实例
        TicketAdapter newAdapter=new TicketAdapter();
        //设置Adapter
        listView.setAdapter(newAdapter);
    }

    class TicketAdapter extends BaseAdapter {

        @Override
        public int getCount(){       //得到item的总数

            return ticketService.Count();    //返回ListView Item条目代表的对象
        }

        @Override
        public Object getItem(int position)
        {
            return ticketService.GetTitle(position); //返回item的数据对象
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
                convertView=View.inflate(TicketActivity.this, R.layout.activity_items, null);
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
            ticketService.GetTitle(position);
            holder.title.setText(ticketService.GetTitle(position));
            holder.price.setText(ticketService.GetDiscount(position));
            holder.image.setImageResource(ticketService.GetIcon(position));
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
        actionBar.setTitle("优惠券");
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