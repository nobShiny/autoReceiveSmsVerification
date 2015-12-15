package com.lsj.messagetext;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.telephony.SmsMessage;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by shiny_jia
 * on 2015-12-13 16:43.
 * 处理收到短信后的广播
 * 注意：由于部分手机rom厂商修改了底层广播标识，导致有些手机收不到系统发送的广播，所以这种办法视情况使用。（推荐使用）
 */
public class AutoGetMeassageReceiver extends BroadcastReceiver {

    private Context mContext;
    private Handler mHandler;
    private int mLong;

    public AutoGetMeassageReceiver(){}

    public AutoGetMeassageReceiver(Context context, Handler handler, int codeLong) {
        mContext = context;
        mHandler = handler;
        mLong = codeLong;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        abortBroadcast();
        Log.d("AutoGetMeassageReceiver", "接到短信了吗:" + intent.getAction().equals(MainActivity.SMS_RECEIVED_ACTION));
        if (intent.getAction().equals(MainActivity.SMS_RECEIVED_ACTION)) {
            //原始数据被叫做PDU,一个PDU就是一个数据段，如果短信比较长的话可能是由几个PDU组成的。
            Object[] pdus = (Object[]) intent.getExtras().get("pdus");
            for (Object pdu : pdus) {
                SmsMessage smsMessage = SmsMessage.createFromPdu((byte[]) pdu);
                String sender = smsMessage.getDisplayOriginatingAddress();
                //短信内容
                String content = smsMessage.getDisplayMessageBody();
                long date = smsMessage.getTimestampMillis();
                Date tiemDate = new Date(date);
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                //发送时间
                String time = simpleDateFormat.format(tiemDate);
                checkCodeAndSend(content.toString(), time);
            }
        }
    }

    /**
     * @param content
     */
    private void checkCodeAndSend(String content,String time) {
        // 话说.如果我们的短信提供商的短信号码是固定的话.前面可以加一个判断
        // 正则表达式验证是否含有验证码
        Pattern pattern = Pattern.compile("\\d{" + mLong + "}");// compile的是规则
        Matcher matcher = pattern.matcher(content);// matcher的是内容
        Log.e("smsbc", "发件人为："+content);
        if (matcher.find()) {
            String code = matcher.group(0);
            mHandler.obtainMessage(1, code).sendToTarget();
            Log.e("smsbc", "广播接收器接收到短信的时间:" + time);
        } else {
            Log.e("smsbc", "短信中没有找到符合规则的验证码");
        }
    }
}
