package com.whu.dadatraffic;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.drawerlayout.widget.DrawerLayout;


import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.sug.OnGetSuggestionResultListener;
import com.baidu.mapapi.search.sug.SuggestionResult;
import com.baidu.mapapi.search.sug.SuggestionSearch;
import com.whu.dadatraffic.Activity.OrdersActivity;
import com.whu.dadatraffic.Activity.SettingActivity;
import com.whu.dadatraffic.Activity.WalletActivity;
import com.whu.dadatraffic.Base.Order;
import com.whu.dadatraffic.Service.OrderService;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private OrderService service = new OrderService();
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle toggle;

    private EditText et_departure;
    private EditText et_destination;
    private Button btn_travel;
    private ListView placeList;

    private LocationClient locationClient;
    private MapView mapView;
    private BaiduMap mapLayer;
    private boolean isFirstLoc = true;
    private double latitude;
    private double longitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //在使用SDK各组件之前初始化context信息，传入ApplicationContext
        //注意该方法要再setContentView方法之前实现
        SDKInitializer.initialize(getApplicationContext());
        setContentView(R.layout.activity_main);

        drawerLayout = findViewById(R.id.drawer_layout);
        et_departure = (EditText) findViewById(R.id.et_departure);
        et_destination = (EditText) findViewById(R.id.et_destination);
        btn_travel = (Button) findViewById(R.id.btn_travel);
        btn_travel.setOnClickListener(this);
        placeList = findViewById(R.id.placeList);

        initLocation();
        initActionBar();
//--------------侧拉框中多个界面的跳转----------------------------------
        RelativeLayout userLayout = findViewById(R.id.user_layout);
        userLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Todo 编写界面跳换代码
                //代码示例
                //Intent intent=new Intent(MainActivity.this, LoginActivity.class);
                //startActivity(intent);
            }
        });

        RelativeLayout orderLayout = findViewById(R.id.order_layout);
        orderLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, OrdersActivity.class);
                startActivity(intent);
            }
        });

        RelativeLayout walletLayout = findViewById(R.id.wallet_layout);
        walletLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //跳转到优惠券界面
                //定义跳转对象
                Intent intentToWallet = new Intent();
                //设置跳转的起始界面和目的界面
                intentToWallet.setClass(MainActivity.this, WalletActivity.class);
                //启动跳转
                startActivity(intentToWallet);
            }
        });

        RelativeLayout settingLayout = findViewById(R.id.setting_layout);
        settingLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //跳转到优惠券界面
                //定义跳转对象
                Intent intentToSetting = new Intent();
                //设置跳转的起始界面和目的界面
                intentToSetting.setClass(MainActivity.this, SettingActivity.class);
                //启动跳转
                startActivity(intentToSetting);
            }
        });

    }
//--------------------------------------------------------------------------------------------------
    /**
     * 初始化定位参数配置
     */

    private void initLocation() {
        mapView=findViewById(R.id.bmapView);
        mapView.setVisibility(View.INVISIBLE);
        mapLayer=mapView.getMap();
        mapLayer.setMyLocationEnabled(true);
        //定位服务的客户端。宿主程序在客户端声明此类，并调用，目前只支持在主线程中启动
        locationClient = new LocationClient(this);
        //声明LocationClient类实例并配置定位参数
        LocationClientOption locationOption = new LocationClientOption();
        MyLocationListener myLocationListener = new MyLocationListener();
        //注册监听函数
        locationClient.registerLocationListener(myLocationListener);
        //可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
        locationOption.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
        //可选，默认gcj02，设置返回的定位结果坐标系，如果配合百度地图使用，建议设置为bd09ll;
        locationOption.setCoorType("bd09ll");
        //可选，默认0，即仅定位一次，设置发起连续定位请求的间隔需要大于等于1000ms才是有效的
        locationOption.setScanSpan(1000);
        //可选，设置是否需要地址信息，默认不需要
        locationOption.setIsNeedAddress(true);
        //可选，设置是否需要地址描述
        locationOption.setIsNeedLocationDescribe(true);
        //可选，设置是否需要设备方向结果
        locationOption.setNeedDeviceDirect(false);
        //可选，默认false，设置是否当gps有效时按照1S1次频率输出GPS结果
        locationOption.setLocationNotify(true);
        //可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死
        locationOption.setIgnoreKillProcess(true);
        //可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
        locationOption.setIsNeedLocationDescribe(true);
        //可选，默认false，设置是否收集CRASH信息，默认收集
        locationOption.SetIgnoreCacheException(false);
        //可选，默认false，设置是否开启Gps定位
        locationOption.setOpenGps(true);
        //可选，默认false，设置定位时是否需要海拔信息，默认不需要，除基础定位版本都可用
        locationOption.setIsNeedAltitude(false);
        //设置打开自动回调位置模式，该开关打开后，期间只要定位SDK检测到位置变化就会主动回调给开发者，该模式下开发者无需再关心定位间隔是多少，定位SDK本身发现位置变化就会及时回调给开发者
        locationOption.setOpenAutoNotifyMode();
        //设置打开自动回调位置模式，该开关打开后，期间只要定位SDK检测到位置变化就会主动回调给开发者
        locationOption.setOpenAutoNotifyMode(3000,1, LocationClientOption.LOC_SENSITIVITY_HIGHT);
        //需将配置好的LocationClientOption对象，通过setLocOption方法传递给LocationClient对象使用
        locationClient.setLocOption(locationOption);
        //开始定位
        locationClient.start();
    }
    /**
     * 实现定位回调
     */
    public class MyLocationListener implements BDLocationListener {
        //在接收到定位消息时触发
        @Override
        public void onReceiveLocation(BDLocation location){
            //此处的BDLocation为定位结果信息类，通过它的各种get方法可获取定位相关的全部结果
            //以下只列举部分获取经纬度相关（常用）的结果信息
            //更多结果信息获取说明，请参照类参考中BDLocation类中的说明
            if(location==null||mapView==null){
                return;
            }
            //获取纬度信息
            latitude = location.getLatitude();
            //获取经度信息
            longitude = location.getLongitude();
            String position = String.format("%s|%s|%s|%s|%s",
                    location.getCity(),
                    location.getDistrict(),location.getStreet(),
                    location.getStreetNumber(),location.getAddrStr());
            et_departure.setText(position);
            MyLocationData locData = new MyLocationData.Builder()
                    .accuracy(location.getRadius())
                    //此处设置开发者获取到的方向信息，顺时针0-360
                    .direction(100).latitude(latitude).longitude(longitude)
                    .build();
            mapLayer.setMyLocationData(locData);//给地图图层设置定位地点
            if(isFirstLoc) {//首次定位
                isFirstLoc=false;
                LatLng ll = new LatLng(latitude,longitude);//创建一个经纬度对象
                MapStatusUpdate update = MapStatusUpdateFactory.newLatLngZoom(ll,14);
                mapLayer.animateMapStatus(update);//设置地图图层的地理位置与缩放比例
                mapView.setVisibility(View.VISIBLE);//定位到当前城市时再显示图层
            }

        }
    }
//--------------------------------------------------------------------------------------------------
    /**
     * 实现关键字搜索定位
     */
    SuggestionSearch mSuggestionSearch = SuggestionSearch.newInstance();


    ArrayList<SuggestionResult.SuggestionInfo> resl
            = new ArrayList<SuggestionResult.SuggestionInfo>();
    ArrayAdapter<SuggestionResult.SuggestionInfo> adapter =
            new ArrayAdapter<SuggestionResult.SuggestionInfo>(this,
                    android.R.layout.simple_list_item_1, resl);

//--------------------------------------------------------------------------------------------------
    /**
     * 初始化ActionBar
     */
    private void initActionBar() {
        //1.获取 actionbar 对象
        ActionBar actionBar = getSupportActionBar();
        //2.设置 图标、标题
        actionBar.setTitle(R.string.app_name);
        actionBar.setSubtitle("祝您一路顺风！");
        //3.启用、显示 home 按钮
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
        //4.替换 home 按钮的图标 （现在显示的是 三条横线 符号）
        toggle = new ActionBarDrawerToggle(this,
                drawerLayout, 0, 0);
        //设置 actionbar 和 drawerlayout 同步状态
        toggle.syncState();
        //5.三条横线 添加动画 （现在显示的是 三条横线与←符号切换的效果）
        addAnamator(toggle);
    }

    private void addAnamator(ActionBarDrawerToggle toggle) {
        drawerLayout.setDrawerListener(toggle);
    }

    /**
     * 点击 actionbar 的 home 按钮，会执行该方法
     *
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home://点击 actionbar 的 home 按钮的点击事件
                setHomeButtonState(item);
                //或
//                setHomeButtonState();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * 设置 actionbar 的 home 按钮的点击事件
     *
     * @param item
     */
    private void setHomeButtonState(MenuItem item) {
        toggle.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_travel) {
            btn_travel.setTextColor(getResources().getColor(R.color.dark_grey));
            btn_travel.setEnabled(false);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        locationClient.stop();
        mapView.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

}