package globalData;

import android.net.Uri;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class GlobalData {

	public static final String AdministratorPhone = "18862182610";

	public static final String LOG = "MessagePlat";

	public static Uri SMS_INBOX = Uri.parse("content://sms/inbox");

	public static String ABSTRACT_URL = "http://15y3r57648.imwork.net/AutoMessageWeb/MessagePlatServlet";

	// public static String ABSTRACT_URL =
	// "http://192.168.100.52/AutoMessageWeb/MessagePlatServlet";

	// public static String ABSTRACT_URL =
	// "http://192.168.100.123:8080/AutoMessageWeb/MessagePlatServlet";

	public static String TABLE_PUSH = "tb_push";

	public static String TABLE_SEND = "tb_send";

	public static String TABLE_AUTOINFO = "tb_autoinfo";

	public static String TABLE_BLACKLIST = "tb_blacklist";

	public static String MESSAGE_COMMON = "【短信助手--测试短信】尊敬的客户，您好，您的订单申请我们已经收到，我们的客服人员会尽快接手，感谢您的支持!";

	public static int RETURN_TYPE = 0;

	public static int RETURN_SPEED = 0;// 0 代表高速，1代表中速，2代表低速

	public static boolean SERVICE_OPEN = false;

	public static boolean isConnect = false;

	private static AsyncHttpClient client = new AsyncHttpClient();

	public static String pushErrorBody = "服务器出现故障，短信信息不能及时处理,请及时查看！";

	public static long getMessageSleepTime = 10000;

	public static long pushMessageSleepTime = 10000;

	public static long sendMessageSleepTime = 10000;

	public static long usefulSendMessageTime = 60000;

	public static String md5Key = "86f390f0738040b5b48c42862938a97c";

	static {
		client.setTimeout(12000);
	}

	public static void getWithAbstrctAddress(String url, RequestParams params, AsyncHttpResponseHandler handle) {
		client.get(url, params, handle);
	}

	public static void postCS_URL(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
		client.post(url, params, responseHandler);
	}

	public static void getWithAbstrctAddress(String url, AsyncHttpResponseHandler handle) {
		client.get(url, handle);
	}

	public static int sleepSomeTime() {

		int speed = GlobalData.RETURN_SPEED;

		int time = 0;

		if (speed == 0) {// 高速发送
			time = 5000;
		} else if (speed == 1) {// 中速发送
			time = 15000;
		} else {// 低速发送
			time = 30000;
		}

		return time;
	}
}
