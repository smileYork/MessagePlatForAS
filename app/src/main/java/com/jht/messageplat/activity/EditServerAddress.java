package com.jht.messageplat.activity;

import org.apache.http.Header;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import com.jht.messageplat.R;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import globalData.GlobalData;
import util.NetWorkJudgeUtil;
import util.ToastShow;
import view.LoadingDialog;

/**
 * * @author chenyao E-mail: 326907403@qq.com
 * 
 * @date 创建时间：2016-2-26 上午11:24:47
 * @version 1.0
 * @parameter
 * @since
 * @return
 */
@SuppressLint("HandlerLeak")
public class EditServerAddress extends Fragment {

	private EditText et_input_server;

	private Button bt_click;

	private Button bt_click_check;

	private Button bt_click_return;

	private FragmentManager fragmentManager;

	private FragmentTransaction transaction;

	private SettingActivity settingActivity;

	private boolean isCheck = false;

	private LoadingDialog loadDialog;

	private SharedPreferences sp;

	private SharedPreferences.Editor editor;

	private String abstract_url = "";

	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		View messageLayout = inflater.inflate(R.layout.fragement_setting_server_address, container, false);
		return messageLayout;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		findView();

		initView();

		setListen();

	}

	private void initView() {

		et_input_server.setText(GlobalData.ABSTRACT_URL);
	}

	private void setListen() {

		// 检查动作
		bt_click_check.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {

				String message = et_input_server.getText().toString();

				if (message.equals("")) {

					ToastShow.ToastShowShort(getActivity(), "服务器地址不能为空!");
				}

				else {

					loadDialog.showDialog();

					loadDialog.setMessage("正在检查接口是否有效，请稍后...");

					checkAddress(message);

				}

			}
		});

		// 提交时间
		bt_click.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {

				if (!isCheck) {

					ToastShow.ToastShowShort(getActivity(), "请先检查服务器地址是否有效!");
				}

				else {

					editor.putBoolean("url_change", true);

					editor.putString("url_address", abstract_url);

					editor.commit();

					GlobalData.ABSTRACT_URL = abstract_url;

					ToastShow.ToastShowShort(getActivity(), "设置成功!");
				}

			}
		});

		bt_click_return.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {

				settingActivity = new SettingActivity();

				transaction = fragmentManager.beginTransaction();

				transaction.replace(R.id.content, (Fragment) settingActivity);

				transaction.commit();
			}
		});
	}

	// 检查输入的服务器地址是否有用
	public void checkAddress(String NewUrl) {
		try {

			if (NetWorkJudgeUtil.getNetworkState(getActivity()) != NetWorkJudgeUtil.NONE) {

				RequestParams params = new RequestParams();

				// 测试数据
				params.put("id", -1);

				params.put("msgType", -1);

				params.put("comeFrom", "");

				params.put("messageBody", "");

				params.put("recvDate", "");

				abstract_url = NewUrl;

				GlobalData.getWithAbstrctAddress(NewUrl, params, new AsyncHttpResponseHandler() {

					@Override
					public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] responseBody) {

						String result = new String(responseBody);

						if (result.equals("SUCCESS")) {

							ToastShow.ToastShowLong(getActivity(), "该地址检测成功，可以使用！");
							isCheck = true;

						} else {
							ToastShow.ToastShowLong(getActivity(), "该地址不能上传数据！");
						}
						loadDialog.closeDialog();


					}

					@Override
					public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] responseBody, Throwable error) {
						ToastShow.ToastShowLong(getActivity(), "该地址不可用，请重新输入！");
						loadDialog.closeDialog();
					}

				});

			} else {
				ToastShow.ToastShowLong(getActivity(), "网络访问失败，请检查手机网络!");
				loadDialog.closeDialog();
			}
		} catch (Exception e) {
			ToastShow.ToastShowLong(getActivity(), "系统繁忙，请稍后再试!");
			loadDialog.closeDialog();
			e.printStackTrace();
		}

	}

	private void findView() {

		sp = getActivity().getSharedPreferences("setting", Activity.MODE_PRIVATE);// 将需要记录的数据保存在config.xml文件中

		editor = sp.edit();

		fragmentManager = getActivity().getSupportFragmentManager();

		loadDialog = new LoadingDialog(getActivity());

		et_input_server = (EditText) getActivity().findViewById(R.id.et_set_server_address);

		bt_click = (Button) getActivity().findViewById(R.id.bt_fragement_set_server_sure);

		bt_click_return = (Button) getActivity().findViewById(R.id.bt_fragement_set_server_return);

		bt_click_check = (Button) getActivity().findViewById(R.id.bt_fragement_set_server_check);

	}

}
