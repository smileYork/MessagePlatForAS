package service;

import org.apache.http.Header;
import org.apache.http.protocol.HTTP;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.telephony.SmsManager;
import android.util.Log;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import globalData.GlobalData;
import model.Push;
import util.MyDBOperation;
import util.NetWorkJudgeUtil;
import util.UsuallyMethod;

/**
 * * @author chenyao E-mail: 326907403@qq.com
 *
 * @version 1.0
 * @date 閸掓稑缂撻弮鍫曟？閿涳拷2016-2-26 娑撳宕�2:35:42
 * @parameter
 * @return
 */
@SuppressLint("HandlerLeak")
public class PushService extends Service {

    private MyDBOperation db;

    private Push push;

    private SharedPreferences mySharedPreferences;

    private SharedPreferences.Editor editor;

    private boolean isClose = false;

    private int pushFailNumber;

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

    @Override
    public void onCreate() {

        super.onCreate();

        InitInfo();

        new Thread(PushMessageToServer).start();
    }

    private void InitInfo() {

        db = new MyDBOperation(getApplicationContext());

        mySharedPreferences = getApplicationContext().getSharedPreferences("memory", Activity.MODE_PRIVATE);

        editor = mySharedPreferences.edit();

        isClose = false;

    }

    Runnable PushMessageToServer = new Runnable() {

        @SuppressLint("SimpleDateFormat")
        @Override
        public void run() {

            try {

                while (!isClose) {

                    // 线程的缓冲时间
                    Thread.sleep(GlobalData.pushMessageSleepTime);

                    // 从本地库中读取一条待上传的记录
                    push = db.getOnePushFromDB();

                    push = new Push(1000, "18862182610",
                            "你是我的眼，带我领略四季的变换，你是我的眼，但我领略拥挤的人潮，你是我的眼，但我阅读好憨的书海，因为你是我的眼，带我看见，这世界卡上弗兰克季后赛的俘虏和水立方绝对是浪费了较高的；安徽高考大家发挥过阿卡解放军的华国锋科技馆发送克己奉公开关加工费看帅哥大富科技哥萨克及噶是进口国个基金会攻击高",
                            "2016-06-21", 0, UsuallyMethod.getUUID());

                    if (push != null) {
                        // 如果有取到待上传的记录
                        pushLocalToServer(push);

                    } else {
                        // 都已经上传完毕，本地没有待上传的信息
                        showWhenEmpty();
                    }

                }
            } catch (InterruptedException e) {

                e.printStackTrace();
            }
        }
    };

    private void pushLocalToServer(Push tempPush) {

        try {

            if (NetWorkJudgeUtil.getNetworkState(getApplicationContext()) != NetWorkJudgeUtil.NONE) {

                push = tempPush;

                JSONObject tempObject = new JSONObject();

                tempObject.put("phone", push.getComefrom());

                tempObject.put("content", push.getMessageBody());

                tempObject.put("msgtype", push.getMsgType());

                tempObject.put("date", push.getRecvDate());

                tempObject.put("uid", push.getUuid());

                String sign = UsuallyMethod.generateSign(tempObject, GlobalData.md5Key);

                RequestParams params = new RequestParams();

                params.put("jsoncontent", tempObject.toString());

                params.put("sign", sign);

                params.setContentEncoding(HTTP.UTF_8);

                Log.i(GlobalData.LOG, "正在上传记录 ---------" + push.getId());

                GlobalData.getWithAbstrctAddress(GlobalData.ABSTRACT_URL, params, pushResponseHandler);

                editor.putInt("noNetWorkSumNumber", 0);

                editor.commit();

            } else {// 如果没有网路的时间检测超过多次，则需报告网络异常

                Log.i(GlobalData.LOG, "手机无法联网");

                int noNetWorkSumNumber = mySharedPreferences.getInt("noNetWorkSumNumber", 0);

                if (noNetWorkSumNumber >= 1500) {
                    // 发送短信给管理员
                    sendMessageToAdministrator();

                } else {

                    noNetWorkSumNumber++;

                    editor.putInt("noNetWorkSumNumber", noNetWorkSumNumber);

                    editor.commit();
                }
            }
        } catch (Exception e) {

            e.printStackTrace();

        }

    }

    @SuppressLint("SimpleDateFormat")
    protected void showWhenEmpty() {

        Log.i(GlobalData.LOG, "------------push is null--------------" + System.currentTimeMillis());

    }

    // 上传结果
    private AsyncHttpResponseHandler pushResponseHandler = new AsyncHttpResponseHandler() {

        @Override
        public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] responseBody) {

            try {

                String resultStr = new String(responseBody);

                JSONObject object;

                object = new JSONObject(resultStr);

                String code = object.getString("code");

                String uid = object.getString("uid");

                String msg = object.getString("msg");

                if ((code.equals("200") || code.contains("200")) && (!uid.isEmpty()) && (!uid.equals(""))) {

                    Log.i(GlobalData.LOG, "上传成功 " + uid);

                    db.deleteSucessPushByUuid(uid);

                    GlobalData.pushMessageSleepTime = 10000;

                    editor.putInt("pushFailNumberSumNumber", 0);

                    editor.commit();

                } else {

                    Log.i(GlobalData.LOG, "上传失败 " + msg);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

        @Override
        public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] responseBody, Throwable error) {
            pushFailNumber = mySharedPreferences.getInt("pushFailNumberSumNumber", 0);

            Log.i(GlobalData.LOG, "上传失败共 " + pushFailNumber + "次");

            if (pushFailNumber >= 10 && pushFailNumber <= 30) {// 上传失败次数在10~30之间时，降低上传频率

                GlobalData.pushMessageSleepTime = 60000;

                addPushFailNumber();

            } else if (pushFailNumber >= 30) {// 上传失败次数超过30次时，降低频率到最低

                sendMessageToAdministrator();

                GlobalData.pushMessageSleepTime = 300000;

                addPushFailNumber();

            } else {

                addPushFailNumber();

            }

        }


    };

    private void sendMessageToAdministrator() {

        long preSendMessageToAdministratorTime = mySharedPreferences.getLong("preSendMessageToAdministratorTime", 0);

        long nowTime = System.currentTimeMillis();

        long difference = nowTime - preSendMessageToAdministratorTime;

        long day = difference / (24 * 60 * 60 * 1000);

        long hour = day * 24;

        if (hour >= 24) {

            Log.i(GlobalData.LOG, "正在发送短信给管理员：" + GlobalData.AdministratorPhone);

            SmsManager smsManager = SmsManager.getDefault();

            smsManager.sendTextMessage(GlobalData.AdministratorPhone, null, GlobalData.pushErrorBody, null, null);

            editor.putLong("preSendMessageToAdministratorTime", nowTime);

            editor.commit();
        }

    }

    protected void addPushFailNumber() {

        pushFailNumber++;

        editor.putInt("pushFailNumberSumNumber", pushFailNumber);

        editor.commit();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        isClose = true;
        GlobalData.isConnect = true;
    }

}
