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
    private LinearLayout UI_1=findViewById(R.id.btnLayout1);
    private LinearLayout UI_2=findViewById(R.id.btnLayout2);
    private LinearLayout UI_3=findViewById(R.id.btnLayout3);
    private LinearLayout UI_4=findViewById(R.id.btnLayout4);
    private Button startAcceptBtn,cancelAcceptBtn,getPassengerGtn,confirmReachBtn;
    private ImageButton callPassenger1,callPassenger2;
    private TextView setOffPlace1,setOffPlace2,destination1,destination2;
    private String str_setOffPlace,str_destination,passengerPhoneNum;
    private boolean isOpen = false;//表示当前司机是否正在营业
    private long baseTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_main);
        initUI_1();

        //开始接单按钮绑定点击事件
        startAcceptBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!isOpen) {
                    UI_1.setVisibility(View.GONE);
                    initUI_2();
                    isOpen=true;
                    //TODO 修改司机状态为在营业（正在接单或接到单）

                }
            }
        });

        //取消接单按钮绑定点击事件
        cancelAcceptBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isOpen){
                    UI_2.setVisibility(View.GONE);
                    initUI_1();
                    isOpen=false;
                    //TODO 修改司机状态为未接单
                }
            }
        });

        //TODO 接到订单消息事件
        //接收服务器消息事件
        //调用getNewOrder()跳转到行程中


        //确认乘客上车按钮绑定点击事件
        getPassengerGtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UI_3.setVisibility(View.GONE);
                initUI_4();
            }
        });

        //达到目的地按钮绑定点击事件
        confirmReachBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isOpen=false;
                UI_4.setVisibility(View.GONE);
                initUI_1();
                Toast.makeText(DriverMainActivity.this,"订单完成", Toast.LENGTH_SHORT).show();
            }
        });


    }

    private void initUI_1()
    {
        startAcceptBtn =(Button)findViewById(R.id.btn_startAccept_driver);
        UI_1.setVisibility(View.VISIBLE);
    }

    private void initUI_2()
    {
        cancelAcceptBtn =(Button)findViewById(R.id.btn_cancelAccept_driver);
        UI_2.setVisibility(View.VISIBLE);
    }
    private void initUI_3()
    {
        getPassengerGtn=findViewById(R.id.btn_getPassenger_driver);
        callPassenger1=findViewById(R.id.callPassenger1);
        setOffPlace1=findViewById(R.id.setOffPlace_text1);
        destination1=findViewById(R.id.destination_text1);
        setOffPlace1.setText(str_setOffPlace);
        destination1.setText(str_destination);
        UI_3.setVisibility(View.VISIBLE);
    }
    private void initUI_4()
    {
        confirmReachBtn=findViewById(R.id.btn_confirmReach_driver);
        callPassenger2=findViewById(R.id.callPassenger2);
        setOffPlace2=findViewById(R.id.setOffPlace_text2);
        destination2=findViewById(R.id.destination_text2);
        setOffPlace2.setText(str_setOffPlace);
        destination2.setText(str_destination);
        UI_4.setVisibility(View.VISIBLE);
    }

    //当接到新的订单时，修改UI
    private void getNewOrder(Order newOrder){

        //TODO 获取订单出发点、目的地、乘客手机号
        //str_setOffPlace="";
        //str_destination="";
        //passengerPhoneNum="";
        UI_2.setVisibility(View.GONE);
        initUI_3();
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