package com.whu.dadatraffic.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.whu.dadatraffic.Base.Order;
import com.whu.dadatraffic.R;
import com.whu.dadatraffic.Service.DriverService;

import java.text.BreakIterator;
import java.text.DecimalFormat;

public class DriverMainActivity extends AppCompatActivity {
    private DriverService driverService = new DriverService();
    private Button acceptBtn = null;
    private Button refuseBtn = null;
    private Button startAcceptBtn = null;
    private TextView userInfoTv = null;
    private TextView departureTv = null;
    private TextView destinationTv = null;
    private TextView timerView = null;
    //private LinearLayout btnLayout = null;

    private boolean isOpen = false;//表示当前司机是否正在营业
    private long baseTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_main);
        initUI();

        //开始接单按钮绑定点击事件
        startAcceptBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //处于接单状态时
                if(isOpen) {
                    startAcceptBtn.setBackgroundColor(Color.rgb(255,179,0));
                    isOpen=false;
                    refuseOrder();
                    //修改服务器司机状态
                }
                //未开始接单时
                else {
                    startAcceptBtn.setBackgroundColor(Color.RED);
                    isOpen=true;
                    acceptBtn.setClickable(true);
                    refuseBtn.setClickable(true);

                    //test
                    getNewOrder(new Order("12345678900","whu","wuhan"));
                    //修改服务器司机状态
                }
            }
        });

        //接单按钮绑定点击事件
        acceptBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //tips();
                //接受订单向服务器发消息
            }
        });

        refuseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                refuseOrder();
            }
        });

    }

    private void initUI()
    {
        acceptBtn = (Button)findViewById(R.id.btn_accept_driver);
        refuseBtn = (Button)findViewById(R.id.btn_refuse_driver);
        startAcceptBtn =(Button)findViewById(R.id.btn_startAccept_driver);
        userInfoTv = (TextView)findViewById(R.id.tv_userInfo_driver);
        departureTv = (TextView)findViewById(R.id.tv_departure_driver);
        destinationTv = (TextView)findViewById(R.id.tv_destination_driver);
        timerView = (TextView) this.findViewById(R.id.tv_timer_driver);

        acceptBtn.setClickable(false);
        refuseBtn.setClickable(false);
    }

    //当接到新的订单时，修改UI，设置按钮可点击
    private void getNewOrder(Order newOrder){
        acceptBtn.setBackgroundColor(Color.rgb(118,232,129));
        refuseBtn.setBackgroundColor(Color.rgb(251,95,95));
        acceptBtn.setClickable(true);
        refuseBtn.setClickable(true);

        userInfoTv.setText("手机号为："+newOrder.getCustomerPhoneNum()+"的乘客等待接单");
        departureTv.setText("出发地："+newOrder.getStartPoint());
        destinationTv.setText("目的地"+newOrder.getDestination());
    }

    //拒绝订单时，修改UI
    private void refuseOrder(){
        userInfoTv.setText("当前无订单");
        departureTv.setText("出发地：");
        destinationTv.setText("目的地");
        acceptBtn.setBackgroundColor(Color.rgb(155,159,155));
        refuseBtn.setBackgroundColor(Color.rgb(155,159,155));
        acceptBtn.setClickable(false);
        refuseBtn.setClickable(false);
    }



}