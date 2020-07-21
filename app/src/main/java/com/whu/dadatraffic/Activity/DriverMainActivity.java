package com.whu.dadatraffic.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.whu.dadatraffic.Base.Order;
import com.whu.dadatraffic.R;
import com.whu.dadatraffic.Service.DriverService;

import java.text.BreakIterator;
import java.text.DecimalFormat;

public class DriverMainActivity extends AppCompatActivity {
    LinearLayout UI_1, UI_2,UI_3,UI_4;
    //private LinearLayout UI=findViewById(R.id.driverLayout);
    private Button startAcceptBtn,cancelAcceptBtn,getPassengerGtn,confirmReachBtn;
    private ImageButton callPassenger1,callPassenger2;
    private String str_setOffPlace=" ",str_destination=" ",passengerPhoneNum="13871142476";
    private boolean isOpen = false;//表示当前司机是否正在营业

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_main);
        initUI();


        //开始接单按钮绑定点击事件
        startAcceptBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!isOpen) {
                    UI_2.setVisibility(View.VISIBLE);
                    UI_1.setVisibility(View.GONE);
//                    initUI_2();

                    isOpen=true;
                    //TODO 开始接单

                }
            }
        });

        //取消接单按钮绑定点击事件
        cancelAcceptBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isOpen){
                    UI_2.setVisibility(View.GONE);
//                    initUI_1();
                    UI_1.setVisibility(View.VISIBLE);
                    isOpen=false;
                    //TODO 取消接单
                }
            }
        });

        //TODO 接到订单消息事件
        //接收服务器消息事件
        //调用getNewOrder()跳转到行程中
        //发送消息到服务器，使乘客界面也跳到行程中


        //确认乘客上车按钮绑定点击事件
        getPassengerGtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isOpen){
                    UI_3.setVisibility(View.GONE);
//                    initUI_4();
                    UI_4.setVisibility(View.VISIBLE);
                }
            }
        });

        //达到目的地按钮绑定点击事件
        confirmReachBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isOpen){
                    isOpen=false;
                    UI_4.setVisibility(View.GONE);
//                    initUI_1()
                    UI_1.setVisibility(View.VISIBLE);
                    Toast.makeText(DriverMainActivity.this,"订单完成", Toast.LENGTH_SHORT).show();
                }
            }
        });

        //呼叫乘客按钮绑定点击事件
        callPassenger1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Context context = DriverMainActivity.this;
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + passengerPhoneNum));
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                try {
                    context.startActivity(intent);
                } catch (ActivityNotFoundException e) {
                    Toast.makeText(context, "请检查您的手机，无法拨打电话！", Toast.LENGTH_SHORT).show();
                }
            }
        });

        callPassenger2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Context context = DriverMainActivity.this;
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + passengerPhoneNum));
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                try {
                    context.startActivity(intent);
                } catch (ActivityNotFoundException e) {
                    Toast.makeText(context, "请检查您的手机，无法拨打电话！", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }

    private void initUI(){
        UI_1=findViewById(R.id.btnLayout1);
        UI_2=findViewById(R.id.btnLayout2);
        UI_3=findViewById(R.id.btnLayout3);
        UI_4=findViewById(R.id.btnLayout4);
        initUI_1();
        initUI_2();
        initUI_3();
        initUI_4();
    }

    private void initUI_1()
    {
        startAcceptBtn = findViewById(R.id.btn_startAccept_driver);
    }

    private void initUI_2()
    {
        cancelAcceptBtn = findViewById(R.id.btn_cancelAccept_driver);
        UI_2.setVisibility(View.GONE);
    }

    private void initUI_3()
    {
        getPassengerGtn=findViewById(R.id.btn_getPassenger_driver);
        callPassenger1=findViewById(R.id.callPassenger1);
        TextView setOffPlace1 = findViewById(R.id.setOffPlace_text1);
        TextView destination1 = findViewById(R.id.destination_text1);
        setOffPlace1.setText(str_setOffPlace);
        destination1.setText(str_destination);
        UI_3.setVisibility(View.GONE);
    }

    private void initUI_4()
    {
        confirmReachBtn=findViewById(R.id.btn_confirmReach_driver);
        callPassenger2=findViewById(R.id.callPassenger2);
        TextView setOffPlace2 = findViewById(R.id.setOffPlace_text2);
        TextView destination2 = findViewById(R.id.destination_text2);
        setOffPlace2.setText(str_setOffPlace);
        destination2.setText(str_destination);
        UI_4.setVisibility(View.GONE);
    }

    //当接到新的订单时，修改UI
    private void getNewOrder()
    {
        //TODO 获取订单出发点、目的地、乘客手机号
        //str_setOffPlace="";
        //str_destination="";
        //passengerPhoneNum="";
        UI_2.setVisibility(View.GONE);
//        initUI_3();
        UI_3.setVisibility(View.VISIBLE);
        //TODO 修改订单状态为进行中
    }

//    //拒绝订单时，修改UI
//    private void refuseOrder(){
//        userInfoTv.setText("当前无订单");
//        departureTv.setText("出发地：");
//        destinationTv.setText("目的地");
//        acceptBtn.setBackgroundColor(Color.rgb(155,159,155));
//        refuseBtn.setBackgroundColor(Color.rgb(155,159,155));
//        acceptBtn.setClickable(false);
//        refuseBtn.setClickable(false);
//    }



}