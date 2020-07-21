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

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.CoordType;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.overlayutil.DrivingRouteOverlay;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.route.BikingRouteResult;
import com.baidu.mapapi.search.route.DrivingRoutePlanOption;
import com.baidu.mapapi.search.route.DrivingRouteResult;
import com.baidu.mapapi.search.route.IndoorRouteResult;
import com.baidu.mapapi.search.route.MassTransitRouteResult;
import com.baidu.mapapi.search.route.OnGetRoutePlanResultListener;
import com.baidu.mapapi.search.route.PlanNode;
import com.baidu.mapapi.search.route.RoutePlanSearch;
import com.baidu.mapapi.search.route.TransitRouteResult;
import com.baidu.mapapi.search.route.WalkingRouteResult;
import com.baidu.mapapi.search.sug.OnGetSuggestionResultListener;
import com.baidu.mapapi.search.sug.SuggestionResult;
import com.baidu.mapapi.search.sug.SuggestionSearch;
import com.whu.dadatraffic.Adapter.AutoEditTextAdapter;
import com.whu.dadatraffic.Base.Order;
import com.whu.dadatraffic.MainActivity;
import com.whu.dadatraffic.R;
import com.whu.dadatraffic.Service.DriverService;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.BreakIterator;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class DriverMainActivity extends AppCompatActivity {
    LinearLayout UI_1, UI_2,UI_3,UI_4;
    //private LinearLayout UI=findViewById(R.id.driverLayout);
    private Button startAcceptBtn,cancelAcceptBtn,getPassengerGtn,confirmReachBtn;
    private ImageButton callPassenger1,callPassenger2;
    private String str_setOffPlace=" ",str_destination=" ",passengerPhoneNum="13871142476";
    private boolean isOpen = false;//表示当前司机是否正在营业

    private LocationClient locationClient;
    private MapView mapView;
    private BaiduMap mapLayer;
    private boolean isFirstLoc = true;
    private double latitude;
    private double longitude;
    private LatLng latLng;
    private String mCity;
    private RoutePlanSearch mSearch = null;
    private ArrayList routeList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SDKInitializer.initialize(getApplicationContext());
        setContentView(R.layout.activity_driver_main);
        SDKInitializer.setCoordType(CoordType.BD09LL);
        //没有这个HTTP申请无法实现路线规划
        SDKInitializer.setHttpsEnable(true);

        initUI();
        initLocation();
        initRoutePlan();

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
                    mapLayer.clear();
                    mapLayer=mapView.getMap();
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
        routeList.add(mCity);
        routeList.add(str_setOffPlace);
        routeList.add(str_destination);
        UI_2.setVisibility(View.GONE);
//        initUI_3();
        UI_3.setVisibility(View.VISIBLE);
        //TODO 修改订单状态为进行中
        StartRoute(routeList);
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


    private void initLocation() {
        mapView=findViewById(R.id.driverMap);
        mapView.setVisibility(View.INVISIBLE);
        mapLayer=mapView.getMap();
        mapLayer.setMyLocationEnabled(true);
        //定位服务的客户端。宿主程序在客户端声明此类，并调用，目前只支持在主线程中启动
        locationClient = new LocationClient(this);
        //声明LocationClient类实例并配置定位参数
        LocationClientOption locationOption = new LocationClientOption();
        DriverMainActivity.MyLocationListener myLocationListener = new DriverMainActivity.MyLocationListener();
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
            mCity=location.getCity();
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

    private void initRoutePlan() {
        mSearch = RoutePlanSearch.newInstance();
        mSearch.setOnGetRoutePlanResultListener(listener);
    }

    public OnGetRoutePlanResultListener listener = new OnGetRoutePlanResultListener() {
        @Override
        public void onGetWalkingRouteResult(WalkingRouteResult walkingRouteResult) {

        }

        @Override
        public void onGetTransitRouteResult(TransitRouteResult transitRouteResult) {

        }

        @Override
        public void onGetMassTransitRouteResult(MassTransitRouteResult massTransitRouteResult) {

        }

        @Override
        public void onGetDrivingRouteResult(DrivingRouteResult result) {
            if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) { }
            assert result != null;
            if (result.error == SearchResult.ERRORNO.AMBIGUOUS_ROURE_ADDR) {
                // 起终点或途经点地址有岐义，通过以下接口获取建议查询信息
                result.getSuggestAddrInfo();
                return;
            }
            if (result.error == SearchResult.ERRORNO.NO_ERROR) {
                mapLayer.clear();
                DrivingRouteOverlay overlay = new DrivingRouteOverlay(mapLayer);
                overlay.setData(result.getRouteLines().get(0));
                overlay.addToMap();
                overlay.zoomToSpan();
            }
        }

        @Override
        public void onGetIndoorRouteResult(IndoorRouteResult indoorRouteResult) {

        }

        @Override
        public void onGetBikingRouteResult(BikingRouteResult bikingRouteResult) {

        }
    };

    private void StartRoute(ArrayList list) {
        SDKInitializer.initialize(getApplicationContext());
        //经纬度规划路线和动态输入规划路线二选一
        // 设置起、终点信息 动态输入规划路线
        PlanNode stNode = PlanNode.withCityNameAndPlaceName(list.get(0).toString(), list.get(1).toString());
        PlanNode enNode = PlanNode.withCityNameAndPlaceName(list.get(0).toString(), list.get(2).toString());

        mSearch.drivingSearch((new DrivingRoutePlanOption()) //驾车规划
                .from(stNode)
                .to(enNode));
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