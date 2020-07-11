package com.whu.dadatraffic.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MenuItem;
import com.whu.dadatraffic.R;
import androidx.appcompat.app.ActionBar;

public class NoneTicketActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_noneticket);
        setCustomActionBar();
    }
    //显示home按钮与标题
    private void setCustomActionBar() {
        //1.获取 actionbar 对象
        ActionBar actionBar = this.getSupportActionBar();
        //2.显示home键
        actionBar.setDisplayHomeAsUpEnabled(true);
        //3.设置标题
        actionBar.setTitle("优惠券");
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