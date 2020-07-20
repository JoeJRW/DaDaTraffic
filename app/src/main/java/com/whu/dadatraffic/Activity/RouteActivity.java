package com.whu.dadatraffic.Activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.baidu.mapapi.CoordType;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapView;
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
import com.whu.dadatraffic.Base.Order;
import com.whu.dadatraffic.Bean.PickerViewData;
import com.whu.dadatraffic.Bean.TimeBean;
import com.whu.dadatraffic.R;
import com.whu.dadatraffic.Service.OrderService;
import com.whu.dadatraffic.Service.UserService;
import com.wzh.pickerview.OptionsPickerView;
import com.wzh.pickerview.model.IPickerViewData;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class RouteActivity extends AppCompatActivity{
    private OrderService orderService = new OrderService();
    private UserService userService = new UserService();

    private MapView mMapView = null;
    private BaiduMap mBaiduMap = null;
    //路线规划相关
    private RoutePlanSearch mSearch = null;

    private TextView tipView;
    private Button callCar;
    private Button reservasion;
    private RelativeLayout mainLayout;

    //时间选择器
    private ArrayList<TimeBean> options1Items = new ArrayList<>();
    private ArrayList<ArrayList<String>> options2Items = new ArrayList<>();
    private ArrayList<ArrayList<ArrayList<IPickerViewData>>> options3Items = new ArrayList<>();
    OptionsPickerView pvOptions;
    View vMasker;

    private String AppointTime="";

    private boolean isWaiting =false; //当前是否正在等待司机接单(true表示已经开始叫车)
    private long baseTimer;
    public static Handler tipHandler;//用于计时

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SDKInitializer.initialize(getApplicationContext());
        setContentView(R.layout.activity_route);
        SDKInitializer.setCoordType(CoordType.BD09LL);

        //没有这个HTTP申请无法实现路线规划
        SDKInitializer.setHttpsEnable(true);

        mMapView=findViewById(R.id.routeView);
        mBaiduMap=mMapView.getMap();
        initRoutePlan();

        initTimePicker();

        callCar=findViewById(R.id.CallCar);
        reservasion=findViewById(R.id.Reservation);
        mainLayout = findViewById(R.id.routeMainView);

        //---设置action bar------
        ActionBar actionBar = this.getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        Intent i = getIntent();
        final ArrayList address = i.getCharSequenceArrayListExtra("address");
        StartRoute(address);

        callCar.bringToFront();
        reservasion.bringToFront();
        callCar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO 开始叫车，等待司机接单，然后进入行程中
                if(isWaiting)//正在等车，点击后取消等车
                {
                    //取消当前订单
                    if(orderService.cancelOrder());
                    {
                        tipView.setVisibility(View.INVISIBLE);
                        tipView = null;
                        isWaiting = false;

                        callCar.setText("开始叫车");
                    }
                }
                else {//未在等车时，点击进入等车状态
                    showTipTv();
                    tips();
                    callCar.setText("取消叫车");
                    isWaiting = true;
                    //添加新订单
                    orderService.addOrder(new Order(UserService.curUser.getPhoneNumber(),address.get(1).toString(),address.get(3).toString()));
                    //检查当前订单是否被接单
                    userService.checkOrderIsRunning();
                    tipHandler = new Handler(){
                        @Override
                        public void handleMessage(Message msg) {
                            if(msg.what == 1){//当前司机已接单
                                //查询相应的订单的司机信息并显示
                                orderService.queryDriverInfo();
                                tipView = null;
                                showTipTv();
                                tipView.setText(orderService.curOrder.getDriverName().charAt(0)+"师傅正在赶来，请稍候");
                                //TODO 这里可以进行UI操作，显示司机位置，界面跳转
                            }
                        }
                    };
                }

            }
        });
        reservasion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pvOptions.show();
            }
        });
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
                Toast.makeText(RouteActivity.this, "路线规划:未找到结果,检查输入", Toast.LENGTH_SHORT).show();
                //TODO 返回主界面重新输入出发地和目的地
            }
            assert result != null;
            if (result.error == SearchResult.ERRORNO.AMBIGUOUS_ROURE_ADDR) {
                // 起终点或途经点地址有岐义，通过以下接口获取建议查询信息
                result.getSuggestAddrInfo();
                return;
            }
            if (result.error == SearchResult.ERRORNO.NO_ERROR) {
                mBaiduMap.clear();
                Toast.makeText(RouteActivity.this, "路线规划:搜索完成", Toast.LENGTH_SHORT).show();
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

    private void initTimePicker(){
        vMasker=findViewById(R.id.vMasker);
        //选项选择器
        pvOptions = new OptionsPickerView(this);
        //选项1
        options1Items.add(new TimeBean("今天"));
        options1Items.add(new TimeBean("明天"));
        options1Items.add(new TimeBean("后天"));

        //选项12
        ArrayList<String> options2Items_01=getTodayHourData();
        //选项22
        ArrayList<String> options2Items_02=getHourData();
        //选项32
        ArrayList<String> options2Items_03=getHourData();
        options2Items.add(options2Items_01);
        options2Items.add(options2Items_02);
        options2Items.add(options2Items_03);

        //选项3
        ArrayList<ArrayList<IPickerViewData>> options3Items_01 = new ArrayList<>();
        ArrayList<ArrayList<IPickerViewData>> options3Items_02 = new ArrayList<>();
        ArrayList<ArrayList<IPickerViewData>> options3Items_03 = new ArrayList<>();
        options3Items_01 =getmD2();
        options3Items_02 =getmD();
        options3Items_03 =getmD();
        options3Items.add(options3Items_01);
        options3Items.add(options3Items_02);
        options3Items.add(options3Items_03);

        //三级联动效果
        pvOptions.setPicker(options1Items, options2Items, options3Items, true);
        pvOptions.setTitle(" ");
        pvOptions.setCyclic(false, false, false);
        //设置默认选中的三级项目
        //监听确定选择按钮
        pvOptions.setSelectOptions(0, 0, 0);
        pvOptions.setOnoptionsSelectListener(new OptionsPickerView.OnOptionsSelectListener() {

            @Override
            public void onOptionsSelect(int options1, int option2, int options3) {
                //返回的分别是三个级别的选中位置
                AppointTime = options1Items.get(options1).getPickerViewText()
                        + options2Items.get(options1).get(option2)
                        + options3Items.get(options1).get(option2).get(options3).getPickerViewText();
                vMasker.setVisibility(View.GONE);
                //TODO 完善预约订单信息并返回主界面
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if(pvOptions.isShowing()){
                pvOptions.dismiss();
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * 今天 点
     */
    private ArrayList<String> getTodayHourData(){
        int max =currentHour();
        if (max<23&&currentMin()>35){
            max=max+1;
        }
        ArrayList<String> lists=new ArrayList<>();
        for (int i=max;i<24;i++){
            lists.add(i+"点");
        }
        return lists;
    }

    /**
     * 明天 后天 点
     */
    private ArrayList<String> getHourData(){
        ArrayList<String> lists=new ArrayList<>();
        for (int i=0;i<24;i++){
            lists.add(i+"点");
        }
        return lists;
    }

    /**
     * 明天 后天  分
     */
    private ArrayList<IPickerViewData> getMinData(){
        ArrayList<IPickerViewData> dataArrayList=new ArrayList<>();
        for (int i=0;i<6;i++){
            dataArrayList.add(new PickerViewData((i*10)+"分"));
        }
        return dataArrayList;
    }
    /**
     * 明天 后天
     */
    private ArrayList<ArrayList<IPickerViewData>> getmD(){
        ArrayList<ArrayList<IPickerViewData>> d=new ArrayList<>();
        for (int i=0;i<24;i++){
            d.add(getMinData());
        }
        return d;
    }

    /**
     * 明天 后天  2222
     */
    private ArrayList<ArrayList<IPickerViewData>> getmD2(){
        //14
        int max =currentHour();
        if (currentMin()>45){
            max=max+1;
        }
        int value =24-max;
        ArrayList<ArrayList<IPickerViewData>> d=new ArrayList<>();
        for (int i=0;i<value;i++){
            if (i==0){
                d.add(getTodyMinData());
            }else {
                d.add(getMinData());
            }

        }
        return d;
    }

    /**
     * 明天 后天  分2222
     */
    private ArrayList<IPickerViewData> getTodyMinData(){

        int min = currentMin();
        int current=0;
        if (min>35&&min<=45){
            current =0;
        }else if (min>45&&min<=55){
            current=1;
        } else if (min>55){
            current=2;
        }else if (min<=5){
            current=2;
        }else if (min>5&&min<=15){
            current=3;
        }else if (min>15&&min<=25){
            current=4;
        }else if (min>25&&min<=35){
            current=5;
        }
        int max =currentHour();
        if (max>23&& min>35){
            current=5;
        }

        ArrayList<IPickerViewData> dataArrayList=new ArrayList<>();
        for (int i=current;i<6;i++){
            dataArrayList.add(new PickerViewData((i*10)+"分"));
        }
        return dataArrayList;
    }

    private int currentMin(){
        Calendar cal = Calendar.getInstance();
        return cal.get(Calendar.MINUTE);
    }


    private int currentHour(){
        Calendar cal = Calendar.getInstance();
        return cal.get(Calendar.HOUR_OF_DAY);
    }


    //计时
    private void tips(){

        //RelativeLayout mainLayout = (RelativeLayout)findViewById(R.id.mainLayout);
        this.baseTimer = SystemClock.elapsedRealtime();


        //mainLayout.addView(timerView,0);

        Handler myhandler = new Handler() {
            public void handleMessage(android.os.Message msg) {
                if (0 == baseTimer) {
                    baseTimer = SystemClock.elapsedRealtime();
                }
                if(tipView==null){
                    return;
                }

                int time = (int) ((SystemClock.elapsedRealtime() - baseTimer) / 1000);
                String mm = new DecimalFormat("00").format(time / 60);
                String ss = new DecimalFormat("00").format(time % 60);
                if (null != tipView) {
                    tipView.setText(mm + ":" + ss);
                }
                Message message = Message.obtain();
                message.what = 0x0;
                sendMessageDelayed(message, 1000);
            }
        };
        myhandler.sendMessageDelayed(Message.obtain(myhandler, 1), 1000);
    }

    //设置计时框并显示
    private void showTipTv(){
        tipView = new TextView(this);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT,1.0f);
        tipView.setGravity(Gravity.CENTER);
        tipView.setLayoutParams(params);
        tipView.setBackgroundColor(Color.rgb(255,179,0));
        tipView.setTextSize(36);
        tipView.bringToFront();
        mainLayout.addView(tipView);
    }

}
