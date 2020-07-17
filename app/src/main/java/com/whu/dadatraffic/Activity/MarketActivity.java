package com.whu.dadatraffic.Activity;
/*
*author：张朝勋
* create time：7/8
* update time: 7/15
 */
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.whu.dadatraffic.R;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.whu.dadatraffic.Service.MarketItemService;


public class MarketActivity extends AppCompatActivity{

    MarketItemService marketItemService = new MarketItemService();
    TextView priceInAll = null;
    TextView buy = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_market);
        setCustomActionBar();
        initItems();
        //初始化ListView控件
        ListView listView = findViewById(R.id.lv);
        //创建一个Adapter的实例
        MarketAdapter newMarketAdapter = new MarketAdapter();
        //设置Adapter
        listView.setAdapter(newMarketAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                //跳转到商品详情界面
                //定义跳转对象
                Intent intentToTDetail = new Intent();
                //设置跳转的起始界面和目的界面
                intentToTDetail.setClass(MarketActivity.this, MarketItemDetailActivity.class);
                //传递选中View的对象
                Bundle bundle = new Bundle();
                bundle.putCharSequence("title", marketItemService.GetItem(position).getTitle());
                bundle.putCharSequence("price", marketItemService.GetPrice(position));
                bundle.putInt("icon", marketItemService.GetItem(position).getIcon());
                //将bundle包中数据绑定到intent
                intentToTDetail.putExtras(bundle);
                //启动跳转，并传输对应数据
                startActivity(intentToTDetail);
            }
        });
        //点击总积分文本跳出已选择的商品列表
        priceInAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //构建一个popupwindow的布局
                View popupView = MarketActivity.this.getLayoutInflater().inflate(R.layout.activity_cart, null);
                //读取此时用户已选择商品，并计算总积分
                int a = marketItemService.CartCount();
                String[] data = new String[a+1];
                data[0] = "已选商品：";
                for(int i = 1; i < a+1; i++)
                {
                    data[i] = marketItemService.GetCart(i-1).getTitle()+marketItemService.GetCart(i-1).getCount()+"个";
                }
                ListView cartLv = (ListView) popupView.findViewById(R.id.cartLv);
                cartLv.setAdapter(new ArrayAdapter<String>(MarketActivity.this,android.R.layout.simple_list_item_1,data));

                //创建PopupWindow对象，指定宽度和高度
                PopupWindow window = new PopupWindow(popupView, 400, 600);
                // TODO:设置动画
//                window.setAnimationStyle(R.style.popup_window_anim);
                //设置背景颜色
                window.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#F8F8F8")));
                //设置可以获取焦点
                window.setFocusable(true);
                //设置可以触摸弹出框以外的区域
                window.setOutsideTouchable(true);
                //更新popupwindow的状态
                window.update();
                //以下拉的方式显示，并设置显示的位置
                window.showAsDropDown(priceInAll, 0, 20);
            }
        });
        //点击购买生成对话框，供用户选择是否确定购买
        buy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //创建对话框
                AlertDialog confirmDialog = new AlertDialog.Builder(MarketActivity.this).create();
                confirmDialog.setTitle("确认下单吗？");
                confirmDialog.setMessage("此次购物将花费"+marketItemService.scoreInAll+"积分");

                //确认按钮
                confirmDialog.setButton(DialogInterface.BUTTON_POSITIVE, "是", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(MarketActivity.this,"已生成订单，感谢您的光临！",Toast.LENGTH_SHORT).show();
                        int a = marketItemService.CartCount();
                        //跳转到商城订单界面
                        //定义跳转对象
                        Intent intentToMOrder = new Intent();
                        //设置跳转的起始界面和目的界面
                        intentToMOrder.setClass(MarketActivity.this, MarketOrderDetailActivity.class);
                        //传递选中View的对象
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("orderList", marketItemService.GetCartItemList());
                        //将bundle包中数据绑定到intent
                        intentToMOrder.putExtras(bundle);
                        //启动跳转，并传输对应数据
                        startActivity(intentToMOrder);
                    }
                });
                //取消按钮
                confirmDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "否", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(MarketActivity.this,"已取消生成订单，欢迎您继续挑选！",Toast.LENGTH_SHORT).show();
                    }
                });
                confirmDialog.show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //实例化MenuInflater对象
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.marketmenu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    private void initView() {
        buy = (TextView)findViewById(R.id.tv_buy);
        priceInAll = (TextView)findViewById(R.id.tv_priceinAll);
    }

    //初始化商城中的元素
    private void initItems() {
        marketItemService.AddItem("围巾", "1800分", R.drawable.icon_scarf);
        marketItemService.AddItem("耳机", "2480分", R.drawable.icon_earphone);
        marketItemService.AddItem("马克杯", "880分", R.drawable.icon_cup);
        marketItemService.AddItem("38元打车券", "380分", R.drawable.icon_38);
        marketItemService.AddItem("音箱", "580分", R.drawable.icon_box);
        marketItemService.AddItem("8折优惠券", "80分", R.drawable.icon_d8);
        initView();
    }

    //显示home按钮与标题
    private void setCustomActionBar() {
        //1.获取 actionbar 对象
        ActionBar actionBar = this.getSupportActionBar();
        //2.显示home键
        actionBar.setDisplayHomeAsUpEnabled(true);
        //3.设置标题
        actionBar.setTitle("积分商城");
    }

    //返回父活动与前往订单详情页
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        if(item.getItemId() == R.id.toOrder){
            Intent intentToTDetail = new Intent();
            //设置跳转的起始界面和目的界面
            intentToTDetail.setClass(MarketActivity.this, MarketOrderActivity.class);
            //启动跳转，并传输对应数据
            startActivity(intentToTDetail);
        }
        return super.onOptionsItemSelected(item);
    }

    public class ViewHolder {
        public TextView title;
        public TextView price;
        public ImageView image;
        public TextView add, reduce, count;
    }



    //设置Adapter来实现listView中各商品的呈现
    public class MarketAdapter extends BaseAdapter {

        @Override
        public int getCount() {       //得到item的总数

            return marketItemService.Count();    //返回ListView Item条目代表的对象
        }

        @Override
        public Object getItem(int position) {
            return marketItemService.GetItem(position).getTitle(); //返回item的数据对象
        }

        @Override
        public long getItemId(int position) {
            return position;         //返回item的id
        }


        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {//获取item中的View视图,使用convertview增加系统效率
            final ViewHolder holder;
            if (convertView == null)//convertview为空则绑定各控件
            {
                convertView = View.inflate(MarketActivity.this, R.layout.activity_market_item, null);
                holder = new ViewHolder();
                holder.title = convertView.findViewById(R.id.title);
                holder.price = convertView.findViewById(R.id.price);
                holder.image = convertView.findViewById(R.id.item);
                holder.add = convertView.findViewById(R.id.add);
                holder.reduce = convertView.findViewById(R.id.reduce);
                holder.count = convertView.findViewById(R.id.count);
                convertView.setTag(holder);
            } else//非空，则复用convertview
            {
                holder = (ViewHolder) convertView.getTag();
            }
            //设置该View中各项值
            holder.title.setText(marketItemService.GetItem(position).getTitle());
            holder.add.setBackgroundColor(Color.GRAY);
            holder.reduce.setBackgroundColor(Color.GRAY);
            holder.price.setText(marketItemService.GetItem(position).getPrice());
            holder.image.setImageResource(marketItemService.GetItem(position).getIcon());
            holder.add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    marketItemService.CalculateCount(1,position);
                    holder.count.setText(Integer.toString(marketItemService.GetItem(position).getCount()));
                    priceInAll.setText("合计："+ marketItemService.scoreInAll +"积分");
                }
            });
            holder.reduce.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    marketItemService.CalculateCount(-1,position);
                    holder.count.setText(Integer.toString(marketItemService.GetItem(position).getCount()));
                    priceInAll.setText("合计："+marketItemService.scoreInAll+"积分");
                }
            });
            return convertView;
        }
    }

}


