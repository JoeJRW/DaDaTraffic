package com.whu.dadatraffic.Activity;

import androidx.appcompat.app.AppCompatActivity;

import com.whu.dadatraffic.Order;
import com.whu.dadatraffic.R;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.Vector;

public class OrdersActivity extends AppCompatActivity {
    public Vector<Order> OrderList = new Vector<Order>();
    private ListView ordersLv = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders);

        initUI();

        OrderList.add(new Order("01","swx","whu","wuhan",50.0));

        //ordersLv.setAdapter(new ArrayAdapter<Order>(this,));
    }

    private void initUI()
    {
        ordersLv = (ListView)findViewById(R.id.ordersLv_ord);
    }
}

