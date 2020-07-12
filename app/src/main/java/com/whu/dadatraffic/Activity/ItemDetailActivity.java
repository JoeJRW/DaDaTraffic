package com.whu.dadatraffic.Activity;
/*
 *author：张朝勋
 * create time：7/10
 * update time: 7/11
 */
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import com.whu.dadatraffic.R;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

public class ItemDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping);

        setCustomActionBar();

        //获取intent传递的各项属性
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        String title = bundle.getString("title");
        String price = bundle.getString("price");
        int icon = bundle.getInt("icon");
        TextView tvPrice = (TextView)findViewById(R.id.itname_name);
        TextView tvUse = (TextView)findViewById(R.id.cost_cost);
        ImageView ivIcon = (ImageView)findViewById(R.id.ItemView);
        tvPrice.setText("   "+price);
        tvUse.setText("   "+title);
        ivIcon.setImageResource(icon);
    }

    //显示home按钮与标题
    private void setCustomActionBar() {
        //1.获取 actionbar 对象
        ActionBar actionBar = this.getSupportActionBar();
        //2.显示home键
        actionBar.setDisplayHomeAsUpEnabled(true);
        //3.设置标题
        actionBar.setTitle("商品详情");
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