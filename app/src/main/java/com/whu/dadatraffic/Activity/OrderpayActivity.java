/*
*author: 李俊
*create time: 2020-07-08
*update time: 2020-07-16
*/

package com.whu.dadatraffic.Activity;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.alipay.sdk.app.AuthTask;
import com.alipay.sdk.app.EnvUtils;
import com.alipay.sdk.app.PayTask;
import com.whu.dadatraffic.AlipayTool.AuthResult;
import com.whu.dadatraffic.AlipayTool.OrderInfoUtil2_0;
import com.whu.dadatraffic.AlipayTool.PayResult;
import com.whu.dadatraffic.R;
import com.whu.dadatraffic.MainActivity;
import com.whu.dadatraffic.Service.OrderService;

import java.text.DecimalFormat;
import java.util.Map;

public class OrderpayActivity extends AppCompatActivity {
    private int payId=0;   //支付方式的Id
    double allPrice;       //总车费
    double discountPrice;  //抵扣车费
    double endPrice;       //最终支付金额

    //用于支付宝支付业务的入参 app_id
    public static final String APPID = "2016102500760122";
    //私钥
    public static final String RSA2_PRIVATE = "MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQCqeGwKvYJpSiI1NbUnhrjzldZa2CbDhduTkZNMkZifDluj0dS2RkgJmdJS+rxh7lMNWXG88mvzPHefaQloG6FLtdwfwpJI+tlJUVuTZAlMRWanhG7sJVaC4JrKTDJ0JDSBlZLbaOpAB3OUbZI3j/M2PNuYXlR/IO4E8XnADhx5zN3X6t2PBZrZt9q1vsuOBkP6F3+NgyxgiGoOcw2GepTvKjb7OF1Se9RmuDVCjOFRNNk/rbls5J8NaL8GxkoN4Ab99oLTdDehTa3jHNA4b5+zIwXV2jI6G4PUPmVk7ewAI2Qot5ruJVS1xVeh+PYelAtea19HbaOyQjSf2ebwoPx9AgMBAAECggEAcSGL6jDMbF2ziAaFm56v/Y/R+uX6C8Y4650v2R/C3sPjkzt85B6OTjjUOReHj6JfYbubXN6xP3JAnP1TLdEwYRNm63jwJhi7vQmOMamw9foU31VnW7aHzEGM2HZrAWLJqbn/BTeCRk3rqXRyFgelS7hlr5/iiZ/r9kHfH9L9mOKD36faTA8R5lDJ7j57t7iX3TTzBcq8pWzlMGZ2AjQThhtikfQPAEzyXyjiqsiNseifz/1lB17lFq9zGr95gksosAInmVNuDQqFVL/w7SELB86iMvOy3lz+NVfbweK1y6gOb/BtJITlMO9bB+siLa3uF1/UrlR+XgIEFgt1k5JhrQKBgQDgAEbkc1doPkKYKnrCI0W4/t9KmWhS/z6CJ1LV7ZwBh/2F2Bqm1ckaqYVEGqICSqpF2/ctwyY0/i2Khc5JrVhQdmkynVPGmTgAyB2BFoLqMCGscVt6mEnMj5kcKJXoh+z5B42vQsh86fYNC6SfDVpHZft0pxX+xAsmAYjXx/93bwKBgQDC0ob2E82F82iXgJ2Urz7n9TSFVxjc7CXrzbNLVDnyIx8CRhTEMYi/nNeUmbdXoIdyLtT85L1bpasidMUA7QNrVbXSuA9jNuAC5HM5Xx2gbc/OgMEZyWjG3rYgFpIQLmKiMvO6R73npeyXkxXWB43mwsZhr/+D0BscG9miaKE00wKBgQDDftpgVV3qp7PdM+3Rc5FNRHvCkqKfFQ8L7p/3/xkRqQsVfia8hoauU1bwukG8uEdlEvXmJZQ52cALTn5chQI3rzBesTpFcMGUxRIi8G+vTfSFmKVbGIAIHSdmkPFMcRzbtsDsPd/WTSal+gxhl/i7qYDNLWof4B9OuzYgBPer1wKBgQCWPl9AdMMh0zZM6C4eVQuxPMOVNTcCYMdZ01e69yh82KL4UO2A5CZuhtkmS2k+FZl+8CjnIsv5Wojg2KZ48U+avg2rhLCxNceJ034ct4KUjzscKOVCKrY379cKZf2cpvj/10Vo2hzVAXu05QSI+2hulrUA7wahM6NEiSHMgt4XawKBgHCwInMIjYqebIB4ANr7YvuHCfxhMxOE69ByNTMGfHU5PXqHYnErphVpboAoOCBUvJeeD23NtjbaaQlQS4RBHOb+YCDG6gAZ8tvLJr4YSmA93wj7cbgep08miI2+2rAs3/0xmSpTJMqIoe6HrfYcJJrmVWIEyvz8JfD3AkJUxdvH";
    public static final String RSA_PRIVATE = "";
    //用于支付宝账户登录授权业务的入参 pid。
    public static final String PID = "2088102181056734";
    //用于支付宝账户登录授权业务的入参 target_id。
    public static final String TARGET_ID = "olumux8031@sandbox.com";

    private static final int SDK_PAY_FLAG = 1;
    private static final int SDK_AUTH_FLAG = 2;

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
        @SuppressWarnings("unused")
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SDK_PAY_FLAG: {
                    @SuppressWarnings("unchecked")
                    PayResult payResult = new PayResult((Map<String, String>) msg.obj);
                    /**
                     * 对于支付结果，请商户依赖服务端的异步通知结果。同步通知结果，仅作为支付结束的通知。
                     */
                    String resultInfo = payResult.getResult();// 同步返回需要验证的信息
                    String resultStatus = payResult.getResultStatus();
                    // 判断resultStatus 为9000则代表支付成功
                    if (TextUtils.equals(resultStatus, "9000")) {
                        // 该笔订单是否真实支付成功，需要依赖服务端的异步通知。
                        showAlert(OrderpayActivity.this, getString(R.string.pay_success) + payResult);
                        Intent intent=new Intent(OrderpayActivity.this,OrderendActivity.class)
                                .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    } else {
                        // 该笔订单真实的支付结果，需要依赖服务端的异步通知。
                        showAlert(OrderpayActivity.this, getString(R.string.pay_failed) + payResult);
                    }
                    break;
                }
                case SDK_AUTH_FLAG: {
                    @SuppressWarnings("unchecked")
                    AuthResult authResult = new AuthResult((Map<String, String>) msg.obj, true);
                    String resultStatus = authResult.getResultStatus();

                    // 判断resultStatus 为“9000”且result_code
                    // 为“200”则代表授权成功，具体状态码代表含义可参考授权接口文档
                    if (TextUtils.equals(resultStatus, "9000") && TextUtils.equals(authResult.getResultCode(), "200")) {
                        // 获取alipay_open_id，调支付时作为参数extern_token 的value
                        // 传入，则支付账户为该授权账户
                        showAlert(OrderpayActivity.this, getString(R.string.auth_success) + authResult);
                    } else {
                        // 其他状态值则为授权失败
                        showAlert(OrderpayActivity.this, getString(R.string.auth_failed) + authResult);
                    }
                    break;
                }
                default:
                    break;
            }
        };
    };

    /**
     * 支付宝支付业务示例
     */
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    public void payV2(View v) {
        if (TextUtils.isEmpty(APPID) || (TextUtils.isEmpty(RSA2_PRIVATE) && TextUtils.isEmpty(RSA_PRIVATE))) {
            showAlert(this, getString(R.string.error_missing_appid_rsa_private));
            return;
        }

        /*
         * 这里只是为了方便直接向商户展示支付宝的整个支付流程；所以Demo中加签过程直接放在客户端完成；
         * 真实App里，privateKey等数据严禁放在客户端，加签过程务必要放在服务端完成；
         * 防止商户私密数据泄露，造成不必要的资金损失，及面临各种安全风险；
         *
         * orderInfo 的获取必须来自服务端；
         */
        boolean rsa2 = (RSA2_PRIVATE.length() > 0);
        Map<String, String> params = OrderInfoUtil2_0.buildOrderParamMap(APPID, rsa2);
        String orderParam = OrderInfoUtil2_0.buildOrderParam(params);

        String privateKey = rsa2 ? RSA2_PRIVATE : RSA_PRIVATE;
        String sign = OrderInfoUtil2_0.getSign(params, privateKey, rsa2);
        final String orderInfo = orderParam + "&" + sign;

        final Runnable payRunnable = new Runnable() {

            @Override
            public void run() {
                PayTask alipay = new PayTask(OrderpayActivity.this);
                Map<String, String> result = alipay.payV2(orderInfo, true);
                Log.i("msp", result.toString());

                Message msg = new Message();
                msg.what = SDK_PAY_FLAG;
                msg.obj = result;
                mHandler.sendMessage(msg);
            }
        };

        // 必须异步调用
        Thread payThread = new Thread(payRunnable);
        payThread.start();
    }

    /**
     * 支付宝账户授权业务示例
     */
//    public void authV2(View v) {
//        if (TextUtils.isEmpty(PID) || TextUtils.isEmpty(APPID)
//                || (TextUtils.isEmpty(RSA2_PRIVATE) && TextUtils.isEmpty(RSA_PRIVATE))
//                || TextUtils.isEmpty(TARGET_ID)) {
//            showAlert(this, getString(R.string.error_auth_missing_partner_appid_rsa_private_target_id));
//            return;
//        }
//
//        /*
//         * 这里只是为了方便直接向商户展示支付宝的整个支付流程；所以Demo中加签过程直接放在客户端完成；
//         * 真实App里，privateKey等数据严禁放在客户端，加签过程务必要放在服务端完成；
//         * 防止商户私密数据泄露，造成不必要的资金损失，及面临各种安全风险；
//         *
//         * authInfo 的获取必须来自服务端；
//         */
//        boolean rsa2 = (RSA2_PRIVATE.length() > 0);
//        Map<String, String> authInfoMap = OrderInfoUtil2_0.buildAuthInfoMap(PID, APPID, TARGET_ID, rsa2);
//        String info = OrderInfoUtil2_0.buildOrderParam(authInfoMap);
//
//        String privateKey = rsa2 ? RSA2_PRIVATE : RSA_PRIVATE;
//        String sign = OrderInfoUtil2_0.getSign(authInfoMap, privateKey, rsa2);
//        final String authInfo = info + "&" + sign;
//        Runnable authRunnable = new Runnable() {
//
//            @Override
//            public void run() {
//                // 构造AuthTask 对象
//                AuthTask authTask = new AuthTask(OrderpayActivity.this);
//                // 调用授权接口，获取授权结果
//                Map<String, String> result = authTask.authV2(authInfo, true);
//
//                Message msg = new Message();
//                msg.what = SDK_AUTH_FLAG;
//                msg.obj = result;
//                mHandler.sendMessage(msg);
//            }
//        };
//
//        // 必须异步调用
//        Thread authThread = new Thread(authRunnable);
//        authThread.start();
//    }

    /**
     * 获取支付宝 SDK 版本号。
     */
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    public void showSdkVersion(View v) {
        PayTask payTask = new PayTask(this);
        String version = payTask.getVersion();
        showAlert(this, getString(R.string.alipay_sdk_version_is) + version);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    private static void showAlert(Context ctx, String info) {
        showAlert(ctx, info, null);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    private static void showAlert(Context ctx, String info, DialogInterface.OnDismissListener onDismiss) {
        new AlertDialog.Builder(ctx)
                .setMessage(info)
                .setPositiveButton(R.string.confirm, null)
                .setOnDismissListener(onDismiss)
                .show();
    }

    private static void showToast(Context ctx, String msg) {
        Toast.makeText(ctx, msg, Toast.LENGTH_LONG).show();
    }

    private static String bundleToString(Bundle bundle) {
        if (bundle == null) {
            return "null";
        }
        final StringBuilder sb = new StringBuilder();
        for (String key: bundle.keySet()) {
            sb.append(key).append("=>").append(bundle.get(key)).append("\n");
        }
        return sb.toString();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        EnvUtils.setEnv(EnvUtils.EnvEnum.SANDBOX);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orderpay);

        //隐藏标题栏
        ActionBar actionBar = getSupportActionBar();
        if(actionBar!=null){
            actionBar.hide();
        }

        //显示司机姓别
        String driverFistName=OrderService.curOrder.getDriverName().substring(0,1);   //7.21修改----------------------------------------
        CharSequence driverName=driverFistName+"师傅";
        TextView textView1=findViewById(R.id.drivername2);
        textView1.setText(driverName);

        //显示司机车牌号
        String carID=OrderService.curOrder.getCarID(); //7.21修改----------------------------------------------------------------
        TextView carID2=findViewById(R.id.carID2);
        carID2.setText(carID);

        //显示司机评分
        DecimalFormat df1 =new DecimalFormat("#.0");
        double driverScore = Double.parseDouble(df1.format(OrderService.curOrder.getDriverScore()));    //7.21修改------------------------------
        CharSequence scoreText=String.valueOf(driverScore);
        TextView textView2=findViewById(R.id.driverscore2);
        textView2.setText(scoreText);

        //显示车费合计
        allPrice=Double.parseDouble(OrderService.curOrder.getPrice());   //7.21修改 --------------------------------
        CharSequence allPriceText=String.valueOf(allPrice)+"元";
        TextView textView3=findViewById(R.id.allprice);
        textView3.setText(allPriceText);

        //点击优惠券选择按钮，进入优惠券选择界面 TODO ----------------版本2修改-------------------------------------------
        Button couponOption=findViewById(R.id.coupon_option);
        couponOption.setOnClickListener(new View.OnClickListener() {
           @Override
            public void onClick(View view) {
//                Intent intent=new Intent(OrderpayActivity.this,优惠券选择界面);
//                startActivity(intent);
            }
        });

        //显示车费抵扣
        discountPrice=8.0;   //TODO -------------------------------版本2修改--------------------------------------
        CharSequence discountPriceText="-"+discountPrice+"元";
        TextView textView4=findViewById(R.id.discountprice);
        textView4.setText(discountPriceText);

        //显示确认支付车费
        endPrice=allPrice-discountPrice;
        DecimalFormat df =new DecimalFormat("#.00");
        CharSequence payPriceText="确认支付"+df.format(endPrice)+"元";
        Button button2=findViewById(R.id.pay);
        button2.setText(payPriceText);

        //获取支付方式的ID，ID为wechatpaybutton或zfbpaybutton
        RadioGroup radioGroup=findViewById(R.id.payOptionGroup);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                RadioButton radioButton=findViewById(i);
                payId=radioButton.getId();
            }
        });

        //点击支付按钮，跳转到相应支付平台
        button2.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
            @Override
            public void onClick(View view) {
                if(payId==findViewById(R.id.wechatpaybutton).getId())
                {
                    //跳转微信支付页面
                    Toast.makeText(OrderpayActivity.this,"微信支付功能关闭，请用支付宝支付",Toast.LENGTH_SHORT).show();
                }
                else if(payId==findViewById(R.id.zfbpaybutton).getId())
                {
                    //跳转支付宝支付页面
                    payV2(view);
                }
                else
                {
                    Toast.makeText(OrderpayActivity.this,"请选择支付方式",Toast.LENGTH_SHORT).show();
                }
            }
        });

        //点击投诉按钮跳转投诉界面
        Button button1=findViewById(R.id.pay_tousu_button);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(OrderpayActivity.this,TousuActivity.class);
                //传递数据到投诉界面
                intent.putExtra("driverPhone", OrderService.curOrder.getDriverPhone());
                startActivity(intent);
            }
        });

        //点击电话按钮，对司机进行拨号--------------------------------版本2添加-----------------------------------
        ImageButton imageButton=findViewById(R.id.calldriver2);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //获取输入的电话号码
                String phone = OrderService.curOrder.getDriverPhone();       //7.21修改-------------------------------
                Context context = OrderpayActivity.this;
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
}