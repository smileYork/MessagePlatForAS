package observer;

import org.apache.http.Header;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import globalData.GlobalData;
import util.MyDBOperation;
import util.NetWorkJudgeUtil;

/**
 * * @author chenyao E-mail: 326907403@qq.com
 * 
 * @date 创建时间：2016-3-1 下午4:31:58
 * @version 1.0
 * @parameter
 * @since
 * @return
 */
@SuppressLint("SimpleDateFormat")
public class SmsReceiver extends BroadcastReceiver {

    private MyDBOperation db;

    // 通过本地软存储设置变量值
    private SharedPreferences mySharedPreferences;

    private SharedPreferences.Editor editor;

    @Override
    public void onReceive(Context context, Intent intent) {

	try {

	    db = new MyDBOperation(context);

	    mySharedPreferences = context.getSharedPreferences("memory",
		    Activity.MODE_PRIVATE);

	    editor = mySharedPreferences.edit();

	    switch (getResultCode()) {

	    case Activity.RESULT_OK:

		Bundle bundle = intent.getExtras();

		String key = bundle.getString("key");

		if (key.equals("SmsObserver")) {

		    Log.i(GlobalData.LOG, "---------短信发送成功----------");

		    db.deletSucessSendFromBuff(bundle.getInt("id"));

		}

		break;
	    default:

		Log.i(GlobalData.LOG, "---------短信发送失败----------");

		int sendMessageFailSumNumber = mySharedPreferences.getInt(
			"sendMessageFailSumNumber", 0);

		if (sendMessageFailSumNumber >= 50) {
		    // 发送短信失败超过设定值
		    pushMessageToServer(context);

		} else {

		    sendMessageFailSumNumber++;

		    editor.putInt("sendMessageFailSumNumber",
			    sendMessageFailSumNumber);

		    editor.commit();
		}
		break;
	    }
	} catch (Exception e) {
	    e.getStackTrace();
	}

    }

    private void pushMessageToServer(final Context context) {

	long preSendMessageToAdministratorTime = mySharedPreferences.getLong(
		"sendFailNumberPreTime", 0);

	long nowTime = System.currentTimeMillis();

	long difference = nowTime - preSendMessageToAdministratorTime;

	long day = difference / (24 * 60 * 60 * 1000);

	long hour = day * 24;

	if (hour >= 24) {

	    try {

		if (NetWorkJudgeUtil.getNetworkState(context) != NetWorkJudgeUtil.NONE) {

		    RequestParams params = new RequestParams();

		    // 测试数据
		    params.put("id", -1);

		    params.put("msgType", -1);

		    params.put("comeFrom", "");

		    params.put("messageBody", "");

		    params.put("recvDate", "");

		    GlobalData.getWithAbstrctAddress(GlobalData.ABSTRACT_URL,
			    params, new AsyncHttpResponseHandler() {

						@Override
						public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] responseBody) {

						}

						@Override
						public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] responseBody, Throwable error) {

						}
			    });

		    editor.putLong("sendFailNumberPreTime", nowTime);

		    editor.commit();

		} else {// 手机不能联网情况

		}
	    } catch (Exception e) {
		e.printStackTrace();
	    }

	}

    }

}
