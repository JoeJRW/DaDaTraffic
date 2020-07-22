/*
 *作者：施武轩 创建时间：2020.7.8 更新时间：2020.7.10
 */

package com.whu.dadatraffic.Activity;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.whu.dadatraffic.Base.Order;
import com.whu.dadatraffic.R;
import com.whu.dadatraffic.Service.OrderService;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import java.util.Vector;

import static com.whu.dadatraffic.R.*;

public class OrdersActivity extends AppCompatActivity {
    private Vector<Order> orderList = null;
    private LinearLayout ordersLayout = null;
    private ScrollView mainScroll = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(layout.activity_orders);
        orderList = OrderService.historyOrders;
        mainScroll = (ScrollView)findViewById(id.ordersScroll);

        initUI();

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
        if(item.getItemId() == android.R.id.home)
        {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * 调用onCreate(), 目的是刷新数据,  从另一activity界面返回到该activity界面时, 此方法自动调用
     */
    @Override
    protected void onResume() {
        super.onResume();
        initOrdersUI();
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
        //清空当前布局
        ordersLayout.removeAllViews();

        LinearLayout list = new LinearLayout(this);
        list.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT,1.0f);
        list.setLayoutParams(layoutParams);
        for(int i = 0; i< orderList.size(); i++){
            //获取订单信息
            final Order curOrder = orderList.elementAt(i);
            final int index = i;
            //设计每个订单的布局（自定义layout）
            LinearLayout cell = new LinearLayout(this);
            cell.setOrientation(LinearLayout.VERTICAL);
            //LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT,1.0f);
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
                    //传递点击的Order编号
                    intentToDetail.putExtra("select",index);
                    //启动跳转
                    startActivity(intentToDetail);
                }
            });

            //layout内部子控件设计
            TextView driverTv = new TextView(this);
            driverTv.setTextSize(20);
            driverTv.setText("  司机电话："+curOrder.getDriverPhone());
            TextView stateTv = new TextView(this);
            stateTv.setTextSize(20);
            stateTv.setText("  订单状态："+curOrder.orderState);

            TextView startTv = new TextView(this);
            startTv.setTextSize(20);
            startTv.setText("  出发点："+curOrder.getStartPoint());
            TextView destinationTv = new TextView(this);
            destinationTv.setTextSize(20);
            destinationTv.setText("  目的地："+curOrder.getDestination());
            //添加子控件
            cell.addView(driverTv);
            cell.addView(stateTv);
            cell.addView(startTv);
            cell.addView(destinationTv);
            //添加自定义layout
            list.addView(cell);
        }
        mainScroll.addView(list);
        ordersLayout.addView(mainScroll);
    }
}

