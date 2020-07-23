package com.whu.dadatraffic;
/**
 * author:王子皓
 * create time：2020.07.07
 * update time：2020.07.15 仍未完成...
 * log：2020.07.07&07.08 完成侧拉框的代码编写
 *      2020.07.08&07.09 完成百度地图SDK的环境配置和定位功能的实现
 *      2020.07.10&07.11 完成出发地和目的地文本框代码编写，主要实现sug检索和显示相关热词，并在点击后补全地址
 *      2020.07.15 添加向路线规划窗口传递信息的功能
 */
import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
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
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.baidu.mapapi.search.sug.OnGetSuggestionResultListener;
import com.baidu.mapapi.search.sug.SuggestionResult;
import com.baidu.mapapi.search.sug.SuggestionSearch;
import com.baidu.mapapi.search.sug.SuggestionSearchOption;
import com.whu.dadatraffic.Activity.OrdersActivity;
import com.whu.dadatraffic.Activity.RouteActivity;
import com.whu.dadatraffic.Activity.SettingActivity;
import com.whu.dadatraffic.Activity.WalletActivity;
import com.whu.dadatraffic.Adapter.AutoEditTextAdapter;
import com.whu.dadatraffic.Base.Order;
import com.whu.dadatraffic.Base.User;
import com.whu.dadatraffic.Service.OrderService;


import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import so.orion.slidebar.GBSlideBar;
import so.orion.slidebar.GBSlideBarAdapter;
import so.orion.slidebar.GBSlideBarListener;
import com.whu.dadatraffic.Adapter.SlideAdapter;
import com.whu.dadatraffic.Service.UserService;

public class MainActivity extends AppCompatActivity {
    private OrderService orderService = new OrderService();
    private UserService userService = new UserService();
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle toggle;

    private AutoCompleteTextView et_departure;
    private AutoCompleteTextView et_destination;
    private Button btn_travel;

    private LocationClient locationClient;
    private MapView mapView;
    private BaiduMap mapLayer;
    private boolean isFirstLoc = true;
    private double latitude;
    private double longitude;

    private GBSlideBar gbSlideBar;
    private SlideAdapter mAdapter;

    private SuggestionSearch mSuggestionSearch;
    private List<String> stringlist = new ArrayList<>();
    private List<String> stringlist2 = new ArrayList<>();
    private LatLng latLng;
    private String mCity1;
    private String mCity2;

    private GeoCoder mCoder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //在使用SDK各组件之前初始化context信息，传入ApplicationContext
        //注意该方法要再setContentView方法之前实现
        SDKInitializer.initialize(getApplicationContext());
        setContentView(R.layout.activity_main);
        /*设置用户信息
        userService.setCurrentUserInfo();

         */

        drawerLayout = findViewById(R.id.drawer_layout);
        et_departure = (AutoCompleteTextView) findViewById(R.id.et_departure);
        et_destination = (AutoCompleteTextView) findViewById(R.id.et_destination);
        btn_travel = (Button) findViewById(R.id.btn_travel);
        btn_travel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //根据输入的出发地和目的地，生成路程规划界面，并将路线信息传递给RouteActivity
                String address1=et_departure.getText().toString();
                String address2=et_destination.getText().toString();
                if(address1=="" || address2=="") {
                    return;
                }
                ArrayList<String> addressList = new ArrayList<>();
                addressList.add(mCity1);
                addressList.add(address1);
                addressList.add(mCity2);
                addressList.add(address2);
                Intent i = new Intent(MainActivity.this, RouteActivity.class);
                i.putStringArrayListExtra("address", addressList);
                startActivity(i);
            }
        });

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

        //滑动条--------------------------------------------------------------------需添加对应车型服务
        gbSlideBar = (GBSlideBar) findViewById(R.id.gbslidebar);
        Resources resources = getResources();
        mAdapter = new SlideAdapter(resources, new int[]{
                R.drawable.btn_tag_selector,
                R.drawable.btn_more_selector,
                R.drawable.btn_reject_selector});
        mAdapter.setTextColor(new int[]{
                Color.parseColor("#FFB300"),
                Color.parseColor("#FFB300"),
                Color.parseColor("#FFB300")
        });
        Log.i("edanelx",mAdapter.getCount()+"");
        gbSlideBar.setAdapter(mAdapter);
        gbSlideBar.setPosition(2);
        gbSlideBar.setOnGbSlideBarListener(new GBSlideBarListener() {
            @Override
            public void onPositionSelected(int position) {
                Log.d("edanelx","selected "+position);
            }
        });

//-----------------监听目的地文本框-------------------------------------------------------------------
        setDestination();
//----------------实例化mSuggestionSearch ，并添加监听器。用于处理搜索到的结果---------------------------
        mSuggestionSearch = SuggestionSearch.newInstance();
        mSuggestionSearch.setOnGetSuggestionResultListener(listener);
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
            latLng = new LatLng(location.getLatitude(), location.getLongitude());
            //获取纬度信息
            latitude = location.getLatitude();
            //获取经度信息
            longitude = location.getLongitude();
            String pos=location.getLocationDescribe();
            String position = String.format("%s%s%s%s%s",
                    location.getCity(),
                    location.getDistrict(),location.getStreet(),
                    location.getStreetNumber(),location.getAddrStr());
            et_departure.setText(position);
            mCity1=location.getCity();
            MyLocationData locData = new MyLocationData.Builder()
                    .accuracy(location.getRadius())
                    //此处设置开发者获取到的方向信息，顺时针0-360
                    .direction(100).latitude(latitude).longitude(longitude)
                    .build();
            mapLayer.setMyLocationData(locData);//给地图图层设置定位地点

            setEdit(location.getCity());

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

    //监听输入框
    public void setEdit(final String city) {
        //点击就自动提示
        et_departure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                et_departure.showDropDown();

            }
        });

        et_departure.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                locationClient.stop();
                mSuggestionSearch.requestSuggestion((new SuggestionSearchOption())
                        .keyword(et_departure.getText().toString())
                        .city(city)
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
                    mCity1=resl.get(i).getCity();
                    latLng = resl.get(i).pt;
                }
                AutoEditTextAdapter adapter = new AutoEditTextAdapter(stringlist,stringlist2, MainActivity.this);
                et_departure.setAdapter(adapter);
            }
        }
    };
//-----------------设置目的地文本框监听和热点查询------------------------------------------------------
    private void setDestination(){
        final SuggestionSearch destinationSuggestionSearch = SuggestionSearch.newInstance();
        destinationSuggestionSearch.setOnGetSuggestionResultListener(destinationListener);

        et_destination.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                et_destination.showDropDown();

            }
        });

        et_destination.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                destinationSuggestionSearch.requestSuggestion((new SuggestionSearchOption())
                        .keyword(et_destination.getText().toString())
                        .city(mCity1)
                        .citylimit(false));
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    OnGetSuggestionResultListener destinationListener = new OnGetSuggestionResultListener() {
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
                    //latLng = resl.get(i).pt;
                    mCity2=resl.get(i).getCity();
                }
                AutoEditTextAdapter adapter = new
                        AutoEditTextAdapter(stringlist,stringlist2, MainActivity.this);
                et_destination.setAdapter(adapter);
            }
        }
    };
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


    private void initLongClick(){
        mCoder = GeoCoder.newInstance();
        mCoder.setOnGetGeoCodeResultListener(coderListener);
        mapLayer.setOnMapLongClickListener(new BaiduMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng latLng) {
                if(et_departure.isFocused()) {
                    mapLayer.clear();
                    OverlayOptions options = new MarkerOptions().position(latLng)
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.icon_start))
                            .zIndex(5);
                    mapLayer.addOverlay(options);
                    Toast.makeText(MainActivity.this,"地点设置成功", Toast.LENGTH_LONG).show();
                }
                else if(et_destination.isFocused()){
                    mapLayer.clear();
                    OverlayOptions options = new MarkerOptions().position(latLng)
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.icon_end))
                            .zIndex(5);
                    mapLayer.addOverlay(options);
                    Toast.makeText(MainActivity.this,"地点设置成功", Toast.LENGTH_LONG).show();
                }
                else {
                    return;
                }
            }
        });
    }

    OnGetGeoCoderResultListener coderListener = new OnGetGeoCoderResultListener() {
        @Override
        public void onGetGeoCodeResult(GeoCodeResult geoCodeResult) {

        }

        @Override
        public void onGetReverseGeoCodeResult(ReverseGeoCodeResult reverseGeoCodeResult) {
            if (reverseGeoCodeResult == null || reverseGeoCodeResult.error != SearchResult.ERRORNO.NO_ERROR) {
                //没有找到检索结果
                return;
            } else {
                //详细地址
                String address = reverseGeoCodeResult.getAddress();
                //行政区号
                int adCode = reverseGeoCodeResult. getCityCode();
                //地址信息
                PoiInfo info = reverseGeoCodeResult.getPoiList().get(0);
            }
        }
    };

}