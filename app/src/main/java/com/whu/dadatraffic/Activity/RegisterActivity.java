/*
 *作者：施武轩 创建时间：2020.7.7 更新时间：2020.7.13
 */

package com.whu.dadatraffic.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.mob.MobSDK;
import com.whu.dadatraffic.Base.Driver;
import com.whu.dadatraffic.Base.User;
import com.whu.dadatraffic.R;
import com.whu.dadatraffic.Service.DriverService;
import com.whu.dadatraffic.Service.UserService;
import com.whu.dadatraffic.Utils.LocalStorageUtil;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;

public class RegisterActivity extends AppCompatActivity {
    private UserService userService = new UserService();
    private DriverService driverService = new DriverService();
    public static RegisterActivity instance = null;
    private Button registerBtn = null;//确认注册按钮
    private EditText phoneNumberEt = null;//手机号输入框
    private EditText nameEt = null;//姓名输入框
    private EditText passwordEt = null;//密码输入框
    private EditText confirmPasswordEt = null;//确认密码输入框
    private TextView hintTv = null;//提示框
    private CheckBox isDriverCb_reg = null;//我是司机复选框
    private TextView carNumTv = null;
    private EditText carNumEt = null;
    private EditText verificationCode=null;
    private Button getVerificationCode=null;


    private boolean isDriver = false;//表示当前注册用户身份是否是司机
    private String phoneNumber = null;
    private String name = null;
    private String password1 = null;
    private String password2 = null;
    private String carNumber = null;
    private String code=null;
    private int i=60;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        instance = this;
        MobSDK.init(this, "302d311aed140", "df8f512666f47b13bf0f78941c293e0c");
        SMSSDK.registerEventHandler(eventHandler);
        initUI();

        //获取短信验证码按钮绑定事件
        getVerificationCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                phoneNumber = phoneNumberEt.getText().toString();
                if (TextUtils.isEmpty(phoneNumber)) {
                    Toast.makeText(getApplicationContext(), "手机号码不能为空",
                            Toast.LENGTH_SHORT).show();
                    return;
                }else{
                    //判断手机号是否有误
                    if(phoneNumber.length() != 11){
                        Toast.makeText(getApplicationContext(), "手机号码为11位数噢",
                                Toast.LENGTH_SHORT).show();
                        return;
                    }else{
                        SMSSDK.getVerificationCode("86", phoneNumber);
                        getVerificationCode.setClickable(false);
                        //开始倒计时
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                for (; i > 0; i--) {
                                    handler.sendEmptyMessage(-1);
                                    if (i <= 0) {
                                        break;
                                    }
                                    try {
                                        Thread.sleep(1000);
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                }
                                //倒计时结束执行
                                handler.sendEmptyMessage(-2);
                            }
                        }).start();
                    }
                }
            }
        });

        //给确认注册按钮绑定事件
        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                phoneNumber = phoneNumberEt.getText().toString();
                code = verificationCode.getText().toString();
                name = nameEt.getText().toString();
                password1 = passwordEt.getText().toString();
                password2 = confirmPasswordEt.getText().toString();

                //判断手机号是否有误
                if(phoneNumber.length() != 11){
                    hintTv.setText("手机号格式错误");
                    //canRegister = false;
                    return;
                }

                //判断是否输入姓名
                if(name.isEmpty()){
                    hintTv.setText("请输入姓名");
                    //canRegister = false;
                    return;
                }

                //判断是否输入密码
                if(password1.isEmpty()){
                    hintTv.setText("请输入密码");
                    //canRegister = false;
                    return;
                }

                //判断确认密码与原密码是否一致
                if(!password1.equals(password2)){
                    hintTv.setText("两次输入的密码不一致");
                    //canRegister = false;
                    return;
                }

                if (isDriver){
                    carNumber = carNumEt.getText().toString();
                    if(carNumber.isEmpty()){
                        hintTv.setText("车牌号不可为空");
                        return;
                    }
                }
                //提交验证码，进行验证
                SMSSDK.submitVerificationCode("86", phoneNumber, code);

            }
        });

        //给“我是司机”单选框绑定事件
        isDriverCb_reg.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                //当选中我是司机时，要求填写车牌号
                if(isChecked){
                    isDriver = true;
                    carNumEt.setVisibility(View.VISIBLE);
                    carNumTv.setVisibility(View.VISIBLE);
                }
                //当取消选中时，隐藏车牌号相关控件
                else{
                    isDriver = false;
                    carNumEt.setVisibility(View.INVISIBLE);
                    carNumTv.setVisibility(View.INVISIBLE);
                }
            }
        });
    }

    //初始化控件，将控件与变量建立关联
    private void initUI(){
        registerBtn = (Button)findViewById(R.id.cfRegisterBtn);
        phoneNumberEt = (EditText)findViewById(R.id.phoneEditText);
        nameEt = (EditText)findViewById(R.id.nameEditText);
        passwordEt = (EditText)findViewById(R.id.passwordEditText_reg);
        confirmPasswordEt = (EditText)findViewById(R.id.cfPasswordEditText);
        hintTv = (TextView)findViewById(R.id.hintTv);
        isDriverCb_reg = (CheckBox)findViewById(R.id.isDriverCheckBox_reg);
        carNumTv = (TextView)findViewById(R.id.carNumberTextView);
        carNumEt = (EditText)findViewById(R.id.carNumberEditText);
        getVerificationCode=findViewById(R.id.button18);
        verificationCode=findViewById(R.id.verificationCode);
        //隐藏车牌号相关控件
        carNumEt.setVisibility(View.INVISIBLE);
        carNumTv.setVisibility(View.INVISIBLE);
    }

    //注册成功后的UI操作
    public void registerSuccess(String result){
        hintTv.setText(result);
        //跳转回登录界面
        //定义跳转对象
        Intent intentToLogin = new Intent().setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        //设置跳转的起始界面和目的界面
        intentToLogin.setClass(RegisterActivity.this, LoginActivity.class);
        //清空偏好配置
        LocalStorageUtil.clearSettingNote(LoginActivity.instance,"userPreferences");
        //把手机号保存到本地
        Map<String, String> map = new HashMap<String, String>();
        map.put("userphone", phoneNumber);
        LocalStorageUtil.saveSettingNote(LoginActivity.instance,"userPreferences",map);
        //启动跳转
        startActivity(intentToLogin);
    }

    //注册失败后的UI操作
    public void registerFail(String result)
    {
        hintTv.setText(result);
    }

    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            if (msg.what == -1) {
                //修改控件文本进行倒计时  i 以60秒倒计时为例
                getVerificationCode.setText( i+" s");
            } else if (msg.what == -2) {
                //修改控件文本，进行重新发送验证码
                getVerificationCode.setText("重新发送");
                getVerificationCode.setClickable(true);
                i = 60;
            } else {
                int event = msg.arg1;
                int result = msg.arg2;
                Object data = msg.obj;

                // 短信验证码验证成功后，进行注册,然后返回Login界面
                if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {
                    if (result == SMSSDK.RESULT_COMPLETE) {
                        // 验证码正确 要干的事
                        if(isDriver){
                            //司机注册
                            driverService.register(new Driver(phoneNumber,password1,name,carNumber));
                        }
                        else {
                            //用户注册
                            userService.register(new User(phoneNumber,password1,name));
                        }
                        onDestroy();
                    }else
                    {
                        Toast.makeText(RegisterActivity.this, "验证码错误", Toast.LENGTH_SHORT).show();
                    }
                } else if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE) {
                    if (result == SMSSDK.RESULT_COMPLETE) {
                        Toast.makeText(getApplicationContext(), "验证码已经发送",
                                Toast.LENGTH_SHORT).show();
                    }
                    else {
                        ((Throwable) data).printStackTrace();
                    }
                } else if (result == SMSSDK.RESULT_ERROR) {
                    try {
                        Throwable throwable = (Throwable) data;
                        throwable.printStackTrace();
                        JSONObject object = new JSONObject(throwable.getMessage());
                        String des = object.optString("detail");//错误描述
                        int status = object.optInt("status");//错误代码
                        if (status > 0 && !TextUtils.isEmpty(des)) {
                            Toast.makeText(RegisterActivity.this, des, Toast.LENGTH_SHORT).show();
                            return;
                        }
                    } catch (Exception e) {
                        //do something
                        Toast.makeText(RegisterActivity.this, "验证失败，请重试", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }
    };

    //监听SMSSDK事件,发送相应消息
    EventHandler eventHandler = new EventHandler() {
        @Override
        public void afterEvent(int event, int result, Object data) {
            Message msg = new Message();
            msg.arg1 = event;
            msg.arg2 = result;
            msg.obj = data;
            handler.sendMessage(msg);
        }
    };
    // 使用完EventHandler需注销，否则可能出现内存泄漏
    protected void onDestroy() {
        super.onDestroy();
        SMSSDK.unregisterEventHandler(eventHandler);
    }

    /*
    /**
     * AsyncTask类的三个泛型参数：
     * （1）Param 在执行AsyncTask是需要传入的参数，可用于后台任务中使用
     * （2）后台任务执行过程中，如果需要在UI上先是当前任务进度，则使用这里指定的泛型作为进度单位
     * （3）任务执行完毕后，如果需要对结果进行返回，则这里指定返回的数据类型

    public class RegisterAsyncTask extends AsyncTask<String, Integer, String> {
        @Override
        protected void onPreExecute() {
            //Log.w("WangJ", "task onPreExecute()");
        }

        /**
         * @param params 这里的params是一个数组，即AsyncTask在激活运行是调用execute()方法传入的参数

        @Override
        protected String doInBackground(String... params) {
            //Log.w("WangJ", "task doInBackground()");
            HttpURLConnection connection = null;
            StringBuilder response = new StringBuilder();
            try {
                URL url = new URL(params[0]); // 声明一个URL
                connection = (HttpURLConnection) url.openConnection(); // 打开该URL连接
                connection.setRequestMethod("GET"); // 设置请求方法，“POST或GET”，我们这里用GET，在说到POST的时候再用POST
                connection.setConnectTimeout(80000); // 设置连接建立的超时时间
                connection.setReadTimeout(80000); // 设置网络报文收发超时时间
                InputStream in = connection.getInputStream();  // 通过连接的输入流获取下发报文，然后就是Java的流处理
                BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return response.toString(); // 这里返回的结果就作为onPostExecute方法的入参
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            // 如果在doInBackground方法，那么就会立刻执行本方法
            // 本方法在UI线程中执行，可以更新UI元素，典型的就是更新进度条进度，一般是在下载时候使用
        }

        /**
         * 运行在UI线程中，所以可以直接操作UI元素
         * @param


        @Override
        protected void onPostExecute(String result) {

            if(result.equals("100")) {
                hintTv.setText("当前号码已注册");
                //canRegister = false;
            }
            else if(result.equals("200")) {
                hintTv.setText("注册成功");
                //canRegister = true;
                //hintTv2.setText(result);
                //跳转回登录界面
                //定义跳转对象
                Intent intentToLogin = new Intent().setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                //设置跳转的起始界面和目的界面
                intentToLogin.setClass(RegisterActivity.this, LoginActivity.class);
                //将用户手机号传到登录界面
                intentToLogin.putExtra("phone",phoneNumber);
                //启动跳转
                startActivity(intentToLogin);
            }
            else {
                hintTv.setText("注册失败");
                //canRegister = false;
            }
        }

    }*/
}