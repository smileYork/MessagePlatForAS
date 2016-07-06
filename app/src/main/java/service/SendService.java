package service;

import java.util.ArrayList;
import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.telephony.SmsManager;
import android.util.Log;
import globalData.GlobalData;
import model.Send;
import observer.SmsReceiver;
import util.MyDBOperation;

/**
 * * @author chenyao E-mail: 326907403@qq.com
 * 
 * @date 创建时间：2016-2-26 下午2:35:54
 * @version 1.0
 * @parameter
 * @since
 * @return
 */
public class SendService extends Service {

    private MyDBOperation db;

    private Send send;

    private SmsManager smsManager;

    private PendingIntent sentPI;

    private String SMS_SEND_ACTIOIN = "SMS_SEND_ACTIOIN";

    private boolean isClose = false;

    private IntentFilter mFilter01;

    private SmsReceiver smsReceiver;

    private Intent itSend;

    @Override
    public IBinder onBind(Intent arg0) {
	return null;
    }

    @Override
    public void onCreate() {

	super.onCreate();

	isClose = false;

	InitInfo();

	new Thread(sendMessageToCustomer).start();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

	return super.onStartCommand(intent, flags, startId);
    }

    Runnable sendMessageToCustomer = new Runnable() {

	@Override
	public void run() {

	    try {
		while (!isClose) {

		    Thread.sleep(sleepSomeTime());

		    send = db.getOneSendFromDB();

		    if (send != null) {

			sendMessageToCustomer(send);

		    } else {

			showWhenEmpty();

		    }

		}
	    } catch (Exception e) {

		e.printStackTrace();
	    }
	}
    };

    int sleepSomeTime() {

	int speed = GlobalData.RETURN_SPEED;

	int time = 0;

	if (speed == 0) {// 高速发送
	    time = 10000;
	} else if (speed == 1) {// 中速发送
	    time = 15000;
	} else {// 低速发送
	    time = 30000;
	}

	return time;
    }

    @SuppressLint("SimpleDateFormat")
    protected void showWhenEmpty() {

	Log.i(GlobalData.LOG,
		"------------send is null--------------"
			+ System.currentTimeMillis());
    }

    private void InitInfo() {

	db = new MyDBOperation(getApplicationContext());

	smsManager = SmsManager.getDefault();

	mFilter01 = new IntentFilter(SMS_SEND_ACTIOIN);

	smsReceiver = new SmsReceiver();

	registerReceiver(smsReceiver, mFilter01);

    }

    /**
     * 参数说明 destinationAddress:收信人的手机号码 scAddress:发信人的手机号码 text:发送信息的内容
     * sentIntent:发送是否成功的回执，用于监听短信是否发送成功。
     * DeliveryIntent:接收是否成功的回执，用于监听短信对方是否接收成功。
     */

    private void sendMessageToCustomer(Send send) {

	Log.i(GlobalData.LOG, "正在发送信息给" + send.getComFrom());

	itSend = new Intent(SMS_SEND_ACTIOIN);

	itSend.putExtra("key", "SmsObserver");

	itSend.putExtra("id", send.getId());

	sentPI = PendingIntent.getBroadcast(getApplicationContext(),
		(int) System.currentTimeMillis(), itSend,
		PendingIntent.FLAG_UPDATE_CURRENT);

	if (send.getReturnBody().length() > 70) {
	    ArrayList<String> msgs = smsManager.divideMessage(send
		    .getReturnBody());
	    for (String msg : msgs) {
		smsManager.sendTextMessage(send.getComFrom(), null, msg,
			sentPI, null);
	    }

	} else {
	    smsManager.sendTextMessage(send.getComFrom(), null,
		    send.getReturnBody(), sentPI, null);
	}

    }

    @Override
    public void onDestroy() {

	super.onDestroy();

	isClose = true;

	unregisterReceiver(smsReceiver);
    }

}
