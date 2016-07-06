package util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.DigestUtils;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * * @author chenyao E-mail: 326907403@qq.com
 * 
 * @date 创建时间：2016-1-29 上午11:11:58
 * @version 1.0
 * @parameter
 * @since
 * @return
 */
public class UsuallyMethod {

	public static boolean isPhoneNumber(String phone) {

		if ((phone.length() < 11) || (phone.length() > 14)) {
			return false;
		}

		String number = phone.substring(phone.length() - 11, phone.length());

		if (number.substring(0, 1).equals("1")) {

			return true;
		}
		return false;
	}

	public static String getUUID() {

		String uuid = UUID.randomUUID().toString();

		return uuid.replace("-", "");
	}

	public static String SubStringPhone(String phone) {

		return phone.substring(phone.length() - 11, phone.length());
	}

	public static String generateSign(JSONObject jsonParam, String md5key) {
		// 返回的签名
		String sign = null;
		// 需要签名的内容
		String toSign = null;

		try {

			toSign = createSignString(jsonConvertMap(jsonParam));

			toSign = (toSign == null ? "" : toSign) + md5key;

			//sign = new String(Hex.encodeHex(DigestUtils.md5(toSign.getBytes())));

		} catch (Exception e) {

			e.printStackTrace();

		}
		return sign;
	}

	@SuppressWarnings("rawtypes")
	public static Map<String, String> jsonConvertMap(JSONObject jsonParam) throws JSONException {
		Map<String, String> params = new HashMap<String, String>();
		Iterator it = jsonParam.keys();
		// 遍历jsonObject数据，添加到Map对象
		while (it.hasNext()) {
			String key = String.valueOf(it.next());
			String value = jsonParam.getString(key);
			params.put(key, value);
		}
		return params;
	}

	public static String createSignString(Map<String, String> params) {
		List<String> keys = new ArrayList<String>(params.keySet());
		Collections.sort(keys);
		String prestr = "";
		for (int i = 0; i < keys.size(); i++) {
			String key = keys.get(i);
			String value = (String) params.get(key);
			// 去掉空置和 sign 签名
			if (value != null && !value.equals("") && !key.equalsIgnoreCase("sign")) {
				prestr = prestr + key + value;
			}
		}
		return prestr;
	}
}
