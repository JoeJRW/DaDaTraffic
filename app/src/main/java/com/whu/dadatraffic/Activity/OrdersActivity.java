package com.whu.dadatraffic.Activity;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.whu.dadatraffic.Base.Order;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Vector;

import static com.whu.dadatraffic.R.*;

public class OrdersActivity extends AppCompatActivity {
    public Vector<Order> OrderList = new Vector<Order>();
    private LinearLayout ordersLayout = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(layout.activity_orders);

        initUI();
        OrderList.add(new Order("01","swx","whu","wuhan",50.0));
        OrderList.add(new Order("02","zcx","whu","shu",50.0));
        OrderList.add(new Order("02","zcx","whu","shu",50.0));
        initOrdersUI();

        //ordersLv.setAdapter(new ArrayAdapter<Order>(this,));
    }

    //显示home按钮
    @Override
    protected void onStart() {
        super.onStart();
        ActionBar actionBar = this.getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    //返回父活动
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
            return super.onOptionsItemSelected(item);
    }

    private void initUI()
    {
        //1.获取 actionbar 对象
        ActionBar actionBar = getSupportActionBar();
        //2.设置 图标、标题
        actionBar.setTitle(string.app_name);
        actionBar.setSubtitle("祝您一路顺风！");
        ordersLayout = (LinearLayout)findViewById(id.ordersLayout);
    }

    private void initOrdersUI()
    {
        for(int i=0;i<OrderList.size();i++){
            Order curOrder = OrderList.elementAt(i);
            LinearLayout cell = new LinearLayout(this);
            cell.setOrientation(LinearLayout.VERTICAL);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,160,1.0f);
            layoutParams.setMargins(40,20,40,20);
            cell.setLayoutParams(layoutParams);
            cell.setBackgroundColor(0xffff00f);
            cell.setClickable(true);
            //给每个layoutt绑定点击事件
            cell.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //跳转到详情界面
                    //定义跳转对象
                    Intent intentToDetail = new Intent();
                    //设置跳转的起始界面和目的界面
                    intentToDetail.setClass(view.getContext(), OrderDetailActivity.class);
                    //启动跳转
                    startActivity(intentToDetail);
                }
            });

            TextView driverTv = new TextView(this);
            driverTv.setTextSize(20);
            driverTv.setText("  司机："+curOrder.getDriverName());
            TextView routeTv = new TextView(this);
            routeTv.setTextSize(20);
            routeTv.setText("  出发点："+curOrder.getStartPoint()+"      "+"目的地："+curOrder.getDestination());
            cell.addView(driverTv);
            cell.addView(routeTv);
            ordersLayout.addView(cell);
        }
    }
}

