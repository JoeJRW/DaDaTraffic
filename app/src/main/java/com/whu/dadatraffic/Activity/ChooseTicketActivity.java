/*
 *author：张朝勋
 * create time：7/22
 * update time:
 */
package com.whu.dadatraffic.Activity;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.whu.dadatraffic.Base.Ticket;
import com.whu.dadatraffic.R;
import com.whu.dadatraffic.Service.TicketService;
import com.whu.dadatraffic.Service.UserService;

public class ChooseTicketActivity extends AppCompatActivity {
    double price = 0;
    TicketService ticketService = new TicketService();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticket);
        setCustomActionBar();


        ListView listView=findViewById(R.id.tlv);
        //创建一个Adapter的实例
        final ChooseTicketAdapter chooseTicketAdapter=new ChooseTicketAdapter();
        //设置Adapter
        listView.setAdapter(chooseTicketAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                final Ticket curTicket = (Ticket)ticketService.ticketList.get(i);
                //创建对话框
                AlertDialog confirmDialog = new AlertDialog.Builder(ChooseTicketActivity.this).create();
                confirmDialog.setTitle("确认使用该优惠券吗？");
                confirmDialog.setMessage("您已选择"+ticketService.ticketList.get(i).getTitle());
                //确认按钮
                confirmDialog.setButton(DialogInterface.BUTTON_POSITIVE, "是", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(ChooseTicketActivity.this,"优惠券已使用！",Toast.LENGTH_SHORT).show();
                        ticketService.useTicket(curTicket);
                        //跳转到订单支付界面
                        //定义跳转对象
                        Intent intentToPay = new Intent();
                        //设置跳转的起始界面和目的界面
                        intentToPay.setClass(ChooseTicketActivity.this, OrderpayActivity.class);
                        //传递选中View的属性
                        Bundle bundle = new Bundle();
                        bundle.putCharSequence("discount",ticketService.GetDiscount(i));
                        //将bundle包中数据绑定到intent
                        intentToPay.putExtras(bundle);
                        //启动跳转，并传输对应数据
                        startActivity(intentToPay);
                    }
                });
                //取消按钮
                confirmDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "否", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(ChooseTicketActivity.this,"已取消使用，请您重新选择优惠券！",Toast.LENGTH_SHORT).show();
                    }
                });
                confirmDialog.show();
            }
        });
    }

    public class ViewHolder {
        public ImageView image;
    }
    //设置Adapter来实现listView中各优惠券的呈现
    class ChooseTicketAdapter extends BaseAdapter {

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
                convertView=View.inflate(ChooseTicketActivity.this, R.layout.activity_ticket_list, null);
                holder=new ViewHolder();
                holder.image=convertView.findViewById(R.id.ticket);
                convertView.setTag(holder);
            }
            else//非空，则复用convertview
            {
                holder=(ViewHolder)convertView.getTag();
            }
            //设置该View中各项值
            holder.image.setImageResource(ticketService.GetImageResource(position));
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
        actionBar.setTitle("选择优惠券");
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