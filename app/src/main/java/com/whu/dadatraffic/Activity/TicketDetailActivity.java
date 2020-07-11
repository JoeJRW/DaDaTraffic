package com.whu.dadatraffic.Activity;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import com.whu.dadatraffic.R;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

public class TicketDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticket_detail);
        setCustomActionBar();
        //获取intent传递的各项属性
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        String title = bundle.getString("title");
        String Date = bundle.getString("StartDate") + " 到\n   " + bundle.getString("EndDate");
        String discount = bundle.getString("discount");
        //将图片切换为详情页的图片
        String icon = bundle.getString("icon").substring(0,bundle.getString("icon").length()-1);
        TextView tvDiscount = (TextView)findViewById(R.id.discount_effect);
        TextView tvUse = (TextView)findViewById(R.id.Use_effect);
        TextView tvDate = (TextView)findViewById(R.id.ddl_time);
        ImageView ivIcon = (ImageView)findViewById(R.id.TicketView);
        if(title.contains("元"))
        {
            title = "满"+title.substring(0,2)+"即可使用";
        }
        else
        {
            title = "满1元即可使用";
        }
        tvDiscount.setText("   "+discount);
        tvUse.setText("   "+title);
        tvDate.setText("   "+Date);
        ivIcon.setImageResource(getResourceId(icon));
    }
    //显示home按钮与标题
    private void setCustomActionBar() {
        //1.获取 actionbar 对象
        ActionBar actionBar = this.getSupportActionBar();
        //2.显示home键
        actionBar.setDisplayHomeAsUpEnabled(true);
        //3.设置标题
        actionBar.setTitle("我的优惠券");
    }
    //根据文件名获取资源ID
    public int  getResourceId(String imageName){
        Context ctx=getBaseContext();
        int resId = getResources().getIdentifier(imageName, "drawable" , ctx.getPackageName());
        return resId;
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