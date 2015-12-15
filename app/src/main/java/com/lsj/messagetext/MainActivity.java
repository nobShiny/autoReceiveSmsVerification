package com.lsj.messagetext;

import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {

    private EditText et;
    private Button button ;
    private AutoGetMeassageReceiver autoGetMeassageReceiver;
    public static final String SMS_RECEIVED_ACTION = "android.provider.Telephony.SMS_RECEIVED";
    public static final int MSG_RECEIVED_CODE = 1;
    private SmsObserver mObserver;

    //更新UI操作，动作由上一界面传来
    private Handler mhandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what == MSG_RECEIVED_CODE){
                String code = (String)msg.obj;
                et.setText(code);
                Toast.makeText(MainActivity.this, "短信来了", Toast.LENGTH_SHORT).show();
            }
        }
    };

    //注册广播
    @Override
    protected void onStart() {
        super.onStart();
        IntentFilter inf = new IntentFilter(SMS_RECEIVED_ACTION);
        inf.setPriority(Integer.MAX_VALUE);
        autoGetMeassageReceiver = new AutoGetMeassageReceiver(MainActivity.this,mhandler,4);
        inf.addDataScheme("sms");
        registerReceiver(autoGetMeassageReceiver, inf);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        et = (EditText) findViewById(R.id.et);
        button = (Button) findViewById(R.id.button);

//        //初始化内容观察者
//        mObserver = new SmsObserver(MainActivity.this, mhandler);
//        Uri uri = Uri.parse("content://sms");
//        //注册内容观察者
//        getContentResolver().registerContentObserver(uri, true, mObserver);
    }

    //注销广播
    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(autoGetMeassageReceiver);
    }

//    //注销内容观察者
//    @Override
//    protected void onPause() {
//        super.onPause();
//        getContentResolver().unregisterContentObserver(mObserver);
//    }
}
