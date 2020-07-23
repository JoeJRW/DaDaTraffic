/*
*author: 李俊
*create time: 2020-07-08
*update time: 2020-07-18 王子皓
*/

package com.whu.dadatraffic.Activity;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.webkit.WebChromeClient;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.webkit.WebView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
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
import com.whu.dadatraffic.MainActivity;
import com.whu.dadatraffic.R;
import com.whu.dadatraffic.Service.OrderService;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;


public class OrdermakeActivity extends AppCompatActivity {
    private OrderService orderService = new OrderService();
    private LocationClient locationClient;
    private MapView mapView;
    private BaiduMap mBaiduMap;
    private boolean isFirstLoc = true;
    private double latitude;
    private double longitude;
    //路线规划相关
    private RoutePlanSearch mSearch = null;
    private Timer timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SDKInitializer.initialize(getApplicationContext());
        setContentView(R.layout.activity_ordermake);
        initInfo();
        initLocation();
        initRoutePlan();

        Intent i = getIntent();
        final ArrayList address = i.getCharSequenceArrayListExtra("address");
        StartRoute(address);

        //订单状态变为end时，跳转到支付界面---------------------------7.21添加
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                orderService.checkOrderState();
                String state = OrderService.curOrder.orderState;
                Log.d("Test",OrderService.curOrder.getDriverName());
                if(state.equals("end")){
                    timer.cancel();
                    Intent intent = new Intent(OrdermakeActivity.this, OrderpayActivity.class);
                    startActivity(intent);
                }
            }
        },1000,2000);//每隔两秒发送一次，查询当前订单的状态,当查寻到订单状态为已取消或待支付或者已结束，就会终止该计时器
        /*
        Runnable payRunnable=new Runnable() {
            @Override
            public void run() {
                while (!Thread.currentThread().isInterrupted()) {
                    if (OrderService.curOrder.getOrderState() == "end") {
                        Intent intent = new Intent(OrdermakeActivity.this, OrderpayActivity.class);
                        startActivity(intent);
                        Thread.currentThread().interrupt();
                    }
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        Thread thread=new Thread(payRunnable);
        thread.start();

         */

    }


    //显示司机信息等
    public void initInfo(){
        //显示司机姓别
        String driverFistName = OrderService.curOrder.getDriverName().substring(0,1);   //7.21修改-------------------------------------------------------
        CharSequence driverName = driverFistName + "师傅";
        TextView textView1 = findViewById(R.id.drivername1);
        textView1.setText(driverName);

        //显示司机车牌号
        String carID = OrderService.curOrder.getCarID(); //7.21修改-------------------------------------------------------
        TextView carID1 = findViewById(R.id.carID1);
        carID1.setText(carID);

        //显示司机评分
        DecimalFormat df =new DecimalFormat("#.0");
        double driverScore = Double.parseDouble(df.format(OrderService.curOrder.getDriverScore()));    //7.21修改----------------------------------------
        CharSequence ScoreText = String.valueOf(driverScore);
        TextView textView2 = findViewById(R.id.driverscore1);
        textView2.setText(ScoreText);


        //点击投诉按钮跳转投诉界面
        Button bt = findViewById(R.id.ing_tousu_button);
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(OrdermakeActivity.this, TousuActivity.class);
                //传递数据到投诉界面
                intent.putExtra("driverPhone",OrderService.curOrder.getDriverPhone());
                startActivity(intent);
            }
        });

        //点击电话按钮，对司机进行拨号
        ImageButton imageButton = findViewById(R.id.calldriver1);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //获取输入的电话号码
                String phone = OrderService.curOrder.getDriverPhone();       //7.21修改-------------------------------
                Context context = OrdermakeActivity.this;
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phone));
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                try {
                    context.startActivity(intent);
                } catch (ActivityNotFoundException e) {
                    Toast.makeText(context, "请检查您的手机，无法拨打电话！", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void initLocation() {
        mapView=findViewById(R.id.orderView);
        mapView.setVisibility(View.INVISIBLE);
        mBaiduMap=mapView.getMap();
        mBaiduMap.setMyLocationEnabled(true);
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
            MyLocationData locData = new MyLocationData.Builder()
                    .accuracy(location.getRadius())
                    //此处设置开发者获取到的方向信息，顺时针0-360
                    .direction(100).latitude(latitude).longitude(longitude)
                    .build();
            mBaiduMap.setMyLocationData(locData);//给地图图层设置定位地点

            if(isFirstLoc) {//首次定位
                isFirstLoc=false;
                LatLng ll = new LatLng(latitude,longitude);//创建一个经纬度对象
                MapStatusUpdate update = MapStatusUpdateFactory.newLatLngZoom(ll,14);
                mBaiduMap.animateMapStatus(update);//设置地图图层的地理位置与缩放比例
                mapView.setVisibility(View.VISIBLE);//定位到当前城市时再显示图层
            }

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

    //路线规划初始化
    private void initRoutePlan() {
        mSearch = RoutePlanSearch.newInstance();
        mSearch.setOnGetRoutePlanResultListener(listener);
    }

    // 路线规划模块
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
            if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
            }
            assert result != null;
            if (result.error == SearchResult.ERRORNO.AMBIGUOUS_ROURE_ADDR) {
                // 起终点或途经点地址有岐义，通过以下接口获取建议查询信息
                result.getSuggestAddrInfo();
                return;
            }
            if (result.error == SearchResult.ERRORNO.NO_ERROR) {
                mBaiduMap.clear();
                DrivingRouteOverlay overlay = new DrivingRouteOverlay(mBaiduMap);
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
        PlanNode enNode = PlanNode.withCityNameAndPlaceName(list.get(2).toString(), list.get(3).toString());

        mSearch.drivingSearch((new DrivingRoutePlanOption()) //驾车规划
                .from(stNode)
                .to(enNode));
    }
}