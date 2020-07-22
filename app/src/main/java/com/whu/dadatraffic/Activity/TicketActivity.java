package com.whu.dadatraffic.Activity;
/*
 *author：张朝勋
 * create time：7/7
 * update time: 7/15
 */
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.whu.dadatraffic.R;
import com.whu.dadatraffic.Service.TicketService;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TicketActivity extends AppCompatActivity {

    TicketService ticketService = new TicketService();
    String giveTime = null;
    String endTime = "2021-08-01 23:59:59";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticket);

        setCustomActionBar();

        //使用现在时间创建优惠券
        Date date = new Date(System.currentTimeMillis());
        @SuppressLint("SimpleDateFormat") SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        giveTime = simpleDateFormat.format(date);
        //ticketService.AddTicket("38元打车券","38元",R.drawable.icon_dc38d,giveTime,endTime,true,R.drawable.icon_dc38);
        //ticketService.AddTicket("8折优惠券","8折",R.drawable.icon_dc8d,giveTime,endTime,true,R.drawable.icon_dc8);
        //初始化ListView控件
        ListView listView=findViewById(R.id.tlv);
        //创建一个Adapter的实例
        final TicketAdapter ticketAdapter=new TicketAdapter();
        //设置Adapter
        listView.setAdapter(ticketAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                //跳转到优惠券详情界面
                //定义跳转对象
                Intent intentToTDetail = new Intent();
                //设置跳转的起始界面和目的界面
                intentToTDetail.setClass(TicketActivity.this, TicketDetailActivity.class);
                //传递选中View的属性
                Bundle bundle = new Bundle();
                bundle.putCharSequence("title",ticketService.GetTitle(position));
                bundle.putCharSequence("discount",ticketService.GetDiscount(position));
                bundle.putCharSequence("StartDate",ticketService.GetStartDate(position));
                bundle.putCharSequence("EndDate",ticketService.GetEndDate(position));
                bundle.putInt("icon",ticketService.GetImageResource(position));
                //将bundle包中数据绑定到intent
                intentToTDetail.putExtras(bundle);
                //启动跳转，并传输对应数据
                startActivity(intentToTDetail);
            }
        });
    }

    public class ViewHolder {
        public ImageView image;
    }
    //设置Adapter来实现listView中各优惠券的呈现
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
                convertView=View.inflate(TicketActivity.this, R.layout.activity_ticket_list, null);
                holder=new ViewHolder();
                holder.image=convertView.findViewById(R.id.ticket);
                convertView.setTag(holder);
            }
            else//非空，则复用convertview
            {
                holder=(ViewHolder)convertView.getTag();
            }
            //设置该View中各项值
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