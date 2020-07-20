/*
 *author: 李俊
 *create: time: 2020-07-17
 *update: time:
 */
package com.whu.dadatraffic.Activity;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.whu.dadatraffic.R;

public class AddressActivity extends AppCompatActivity {
    TextView addressPlace;
    EditText addressInput;
    Button homeAddress;
    Button workAddress;
    String home="";           //记录家的地址
    String home1="";             //文本显示
    String workplace="";      //记录公司地址
    String workplace1="";        //文本显示
    private boolean isShowOrNot1 = false;
    private boolean isShowOrNot2 = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address);

        addressPlace=findViewById(R.id.addressPlace);
        addressInput=findViewById(R.id.addressInput);

        //隐藏标题栏
        ActionBar actionBar = getSupportActionBar();
        if(actionBar!=null){
            actionBar.hide();
        }

        //设置常用地址：家
        homeAddress=findViewById(R.id.home_address);
        homeAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isShowOrNot1 == false) {
                    addressPlace.setText("      家：");
                    home1=home;
                    addressInput.setText(home1);
                    addressPlace.setVisibility(View.VISIBLE);
                    addressInput.setVisibility(View.VISIBLE);
                    isShowOrNot1 = true;
                    isShowOrNot2  = false;
                    //TODO 获取addressInput的输入信息，展示出地点供选择

                    //addressInput.setText();

                }
                //再次点击，保存修改，将addressPlace和addressInput设为不可见
                else {
                    home=addressInput.getText().toString();
                    homeAddress.setText("      家："+home);
                    //TODO 将home存到数据库中

                    addressPlace.setVisibility(View.INVISIBLE);
                    addressInput.setVisibility(View.INVISIBLE);
                    isShowOrNot1  = false;
                }
            }
        });

        //设置常用地址：公司
        workAddress=findViewById(R.id.work_address);
        workAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isShowOrNot2 == false) {
                    addressPlace.setText("      公司：");
                    workplace1=workplace;
                    addressInput.setText(workplace1);
                    addressPlace.setVisibility(View.VISIBLE);
                    addressInput.setVisibility(View.VISIBLE);
                    isShowOrNot2 = true;
                    isShowOrNot1  = false;
                    //TODO 获取addressInput的输入信息，展示出地点供选择

                    //addressInput.setText();

                }
                //再次点击，保存修改， 将addressPlace和addressInput设为不可见
                else {
                    workplace=addressInput.getText().toString();
                    workAddress.setText("      公司："+workplace);
                    //TODO 将workplace存到数据库中

                    addressPlace.setVisibility(View.INVISIBLE);
                    addressInput.setVisibility(View.INVISIBLE);
                    isShowOrNot2  = false;
                }
            }
        });

        //点击返回按钮，回到上层界面
        Button addressGoBack=findViewById(R.id.address_goback);
        addressGoBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}