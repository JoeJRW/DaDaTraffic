/*
 *author: 李俊
 *create: time: 2020-07-17
 *update: time:
 */
package com.whu.dadatraffic.Activity;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.baidu.mapapi.search.sug.OnGetSuggestionResultListener;
import com.baidu.mapapi.search.sug.SuggestionResult;
import com.baidu.mapapi.search.sug.SuggestionSearch;
import com.baidu.mapapi.search.sug.SuggestionSearchOption;
import com.whu.dadatraffic.Adapter.AutoEditTextAdapter;
import com.whu.dadatraffic.MainActivity;
import com.whu.dadatraffic.R;

import java.util.ArrayList;
import java.util.List;

public class AddressActivity extends AppCompatActivity {
    TextView addressPlace;
    AutoCompleteTextView addressInput;
    Button homeAddress;
    Button workAddress;
    String home="";           //记录家的地址
    String home1="";             //文本显示
    String workplace="";      //记录公司地址
    String workplace1="";        //文本显示
    private boolean isShowOrNot1 = false;
    private boolean isShowOrNot2 = false;

    private SuggestionSearch mSuggestionSearch;
    private List<String> stringlist = new ArrayList<>();
    private List<String> stringlist2 = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address);

        addressPlace=findViewById(R.id.addressPlace);
        addressInput=findViewById(R.id.addressInput);//输入框
        setEdit();

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

    //-----------------设置目的地文本框监听和热点查询------------------------------------------------------
    private void setEdit(){
        //----------------实例化mSuggestionSearch ，并添加监听器。用于处理搜索到的结果---------------------------
        mSuggestionSearch = SuggestionSearch.newInstance();
        mSuggestionSearch.setOnGetSuggestionResultListener(listener);

        addressInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addressInput.showDropDown();

            }
        });

        addressInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                mSuggestionSearch.requestSuggestion((new SuggestionSearchOption())
                        .keyword(addressInput.getText().toString())
                        .city("宁波")
                        .citylimit(false));
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    OnGetSuggestionResultListener listener = new OnGetSuggestionResultListener() {
        public void onGetSuggestionResult(SuggestionResult res) {
            if (res == null || res.getAllSuggestions() == null) {//未找到相关结果
                return;
            } else {//获取在线建议检索结果
                List<SuggestionResult.SuggestionInfo> resl = res.getAllSuggestions();
                stringlist.clear();
                stringlist2.clear();
                for (int i = 0; i < resl.size(); i++) {
                    stringlist.add(resl.get(i).key);
                    stringlist2.add(resl.get(i).city+resl.get(i).district+resl.get(i).key);
                }
                AutoEditTextAdapter adapter = new
                        AutoEditTextAdapter(stringlist,stringlist2, AddressActivity.this);
                addressInput.setAdapter(adapter);
            }
        }
    };

}