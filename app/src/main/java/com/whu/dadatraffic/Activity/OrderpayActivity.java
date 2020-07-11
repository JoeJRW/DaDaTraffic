package com.whu.dadatraffic.Activity;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.whu.dadatraffic.R;

public class OrderpayActivity extends AppCompatActivity {
    private int payId=0;   //支付方式的Id
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orderpay);

        //隐藏标题栏
        ActionBar actionBar = getSupportActionBar();
        if(actionBar!=null){
            actionBar.hide();
        }

        //显示司机姓别
        String driverFistName="某";   //需修改-------------------------------------------------------------------
        CharSequence driverName=driverFistName+"师傅";
        TextView textView1=findViewById(R.id.drivername2);
        textView1.setText(driverName);

        //显示司机车牌号
        String carID="鄂A123456";      //需修改------------------------------------------------------------------
        TextView carID2=findViewById(R.id.carID2);
        carID2.setText(carID);

        //显示司机评分
        double driverScore=5.0;    //需修改----------------------------------------------------------------------
        CharSequence scoreText=String.valueOf(driverScore);
        TextView textView2=findViewById(R.id.driverscore2);
        textView2.setText(scoreText);

        //显示车费合计
        double allPrice=8.0;   //需修改--------------------------------------------------------------------------
        CharSequence allPriceText=String.valueOf(allPrice)+"元";
        TextView textView3=findViewById(R.id.allprice);
        textView3.setText(allPriceText);

        //点击优惠券选择按钮，进入优惠券选择界面---------------------------------------------------------------------
        Button couponOption=findViewById(R.id.coupon_option);
        couponOption.setOnClickListener(new View.OnClickListener() {
           @Override
            public void onClick(View view) {
//                Intent intent=new Intent(OrderpayActivity.this,优惠券选择界面);
//                startActivity(intent);
            }
        });

        //显示车费抵扣
        double discountPrice=2.0;   //需修改---------------------------------------------------------------------
        CharSequence discountPriceText="-"+discountPrice+"元";
        TextView textView4=findViewById(R.id.discountprice);
        textView4.setText(discountPriceText);

        //显示确认支付车费
        double endPrice=allPrice-discountPrice;
        CharSequence payPriceText="确认支付"+endPrice+"元";
        Button button2=findViewById(R.id.pay);
        button2.setText(payPriceText);

        //获取支付方式的ID，ID为wechatpaybutton或zfbpaybutton---------------------------------------------------
        RadioGroup radioGroup=findViewById(R.id.payOptionGroup);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                RadioButton radioButton=findViewById(i);
                payId=radioButton.getId();
            }
        });

        //点击支付按钮，跳转到相应支付平台
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(payId==findViewById(R.id.wechatpaybutton).getId())
                {
                    //跳转微信支付页面--------------------------------------------------------------------------
                }
                else if(payId==findViewById(R.id.zfbpaybutton).getId())
                {
                    //跳转支付宝支付页面------------------------------------------------------------------------
                }
                else
                {
                    //Toast.makeText(OrderpayActivity.this,"请选择支付方式",Toast.LENGTH_SHORT).show();
                }
            }
        });

        //点击投诉按钮跳转投诉界面
        Button button1=findViewById(R.id.pay_tousu_button);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(OrderpayActivity.this,TousuActivity.class);
                startActivity(intent);
            }
        });

        //点击电话按钮，对司机进行拨号------------------------------------------------------------------------------
        ImageButton imageButton=findViewById(R.id.calldriver2);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //获取输入的电话号码
                //String phone = et_phone.getText().toString().trim();
                //创建打电话的意图
                //Intent intent = new Intent();
                //设置拨打电话的动作
                //intent.setAction(Intent.ACTION_CALL);
                //设置拨打电话的号码
                //intent.setData(Uri.parse("tel:" + phone));
                //开启打电话的意图
                //startActivity(intent);
            }
        });

    }
}