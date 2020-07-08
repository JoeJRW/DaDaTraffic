package com.whu.dadatraffic.Activity;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.RatingBar;
import android.widget.TextView;

import com.whu.dadatraffic.Order;
import com.whu.dadatraffic.R;

import java.net.IDN;
import java.util.Objects;

public class OrderDetailActivity extends AppCompatActivity {
    public Order curOrder = null;
    public String curOrderID = null;
    private TextView carNumberTv = null;
    private TextView IDTv = null;
    private TextView timeTv = null;
    TextView driverNameTv = null;
    TextView startPointTv = null;
    TextView destinationTv = null;
    TextView remarkTv = null;
    TextView priceTv = null;
    RatingBar scoreBar = null;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);
        setCustomActionBar();

        //获取上一界面传来的订单编号
        curOrderID = getIntent().getStringExtra("ID");
        //根据编号去服务器获取订单信息

        initUI();

    }

    //设置标题栏居中
    private void setCustomActionBar() {
        ActionBar.LayoutParams lp =new ActionBar.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.MATCH_PARENT, Gravity.CENTER);
        View mActionBarView = LayoutInflater.from(this).inflate(R.layout.actionbar_layout, null);
        ActionBar actionBar = this.getSupportActionBar();
        if (actionBar != null) {
            actionBar.setCustomView(mActionBarView, lp);
        }
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayShowHomeEnabled(false);
        actionBar.setDisplayShowTitleEnabled(false);
    }

    //显示home按钮
    @Override
    protected void onStart() {
        super.onStart();
        ActionBar actionBar = this.getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
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

    @SuppressLint("SetTextI18n")
    private void initUI()
    {
        IDTv = (TextView) findViewById(R.id.orderIDTv_det);
        driverNameTv = (TextView) findViewById(R.id.driverNameTv_det);
        carNumberTv = (TextView)findViewById(R.id.carNumTv_det);
        startPointTv = (TextView) findViewById(R.id.startPointTv_det);
        destinationTv = (TextView) findViewById(R.id.destinationTv_det);
        remarkTv = (TextView) findViewById(R.id.remarkTv_det);
        priceTv = (TextView) findViewById(R.id.priceTv_det);
        scoreBar = (RatingBar) findViewById(R.id.score_det);
        timeTv = (TextView)findViewById(R.id.timetv_det);

        IDTv.setText("订单编号："+curOrderID);
        /*
        driverNameTv.setText("司机名："+curOrder.getDriverName());
        startPointTv.setText("出发点："+curOrder.getStartPoint());
        destinationTv.setText("目的地"+curOrder.getDestination());
        remarkTv.setText(curOrder.getEvalution());
        priceTv.setText("价格："+curOrder.getPrice());

         */
        //测试
        scoreBar.setRating(2.5f);

    }

}