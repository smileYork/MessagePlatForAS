package com.jht.messageplat.activity;

import com.jht.messageplat.R;
import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;

import globalData.GlobalData;
import util.ToastShow;

/**
 * * @author chenyao E-mail: 326907403@qq.com
 * 
 * @date 创建时间：2016-2-19 下午5:06:12
 * @version 1.0
 * @parameter
 * @since
 * @return
 */
public class SettingActivity extends Fragment {

	private RadioGroup rg_message_type;

	private RadioGroup rg_message_speed;

	private ImageView img_commom, img_setServer;

	private ImageView img_special;

	private TextView url_address;

	private FragmentManager fragmentManager;

	private FragmentTransaction transaction;

	private EditCommonMessage editCommonMessage = new EditCommonMessage();

	private EditServerAddress editServerAddress = new EditServerAddress();

	private ShowSpecialMessage showSpecialMessage = new ShowSpecialMessage();

	private SharedPreferences sp;

	private SharedPreferences.Editor editor;

	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		View messageLayout = inflater.inflate(R.layout.activity_setting, container, false);

		return messageLayout;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		findView();

		initView();

		setListen();

	}

	private void setListen() {

		rg_message_type.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup arg0, int checkId) {

				switch (checkId) {
				case R.id.rd_setting_common:

					editor.putInt("returnType", 0);

					editor.commit();

					GlobalData.RETURN_TYPE = 0;

					ToastShow.ToastShowShort(getActivity(),

							"已设置为统一回复短信");
					break;
				case R.id.rd_setting_special:

					editor.putInt("returnType", 1);

					editor.commit();

					GlobalData.RETURN_TYPE = 1;

					ToastShow.ToastShowShort(getActivity(), "已设置为定制短信");

					break;

				}
			}
		});

		rg_message_speed.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup arg0, int checkId) {

				switch (checkId) {
				case R.id.rb_setting_high:

					editor.putInt("returnSpeed", 0);
					editor.commit();

					GlobalData.RETURN_SPEED = 0;

					ToastShow.ToastShowShort(getActivity(), "已设置为高速");
					break;
				case R.id.rb_setting_mid:

					editor.putInt("returnSpeed", 1);

					editor.commit();

					GlobalData.RETURN_SPEED = 1;

					ToastShow.ToastShowShort(getActivity(), "已设置为中速");
					break;
				case R.id.rb_setting_slow:

					editor.putInt("returnSpeed", 2);
					editor.commit();

					GlobalData.RETURN_SPEED = 2;

					ToastShow.ToastShowShort(getActivity(), "已设置为低速");
					break;
				}
			}
		});

		img_commom.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {

				transaction = fragmentManager.beginTransaction();
				transaction.replace(R.id.content, (Fragment) editCommonMessage);
				transaction.commit();

			}
		});

		img_special.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {

				transaction = fragmentManager.beginTransaction();
				transaction.replace(R.id.content, (Fragment) showSpecialMessage);
				transaction.commit();

			}
		});

		img_setServer.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {

				transaction = fragmentManager.beginTransaction();
				transaction.replace(R.id.content, (Fragment) editServerAddress);
				transaction.commit();

			}
		});

	}

	private void initView() {

		if (GlobalData.RETURN_TYPE == 0) {

			rg_message_type.check(R.id.rd_setting_common);

		} else {

			rg_message_type.check(R.id.rd_setting_special);

		}

		if (GlobalData.RETURN_SPEED == 0) {

			rg_message_speed.check(R.id.rb_setting_high);

		} else if (GlobalData.RETURN_SPEED == 1) {

			rg_message_speed.check(R.id.rb_setting_mid);

		} else {

			rg_message_speed.check(R.id.rb_setting_slow);

		}

		url_address.setText(GlobalData.ABSTRACT_URL);

	}

	private void findView() {

		sp = getActivity().getSharedPreferences("setting", Activity.MODE_PRIVATE);// 将需要记录的数据保存在config.xml文件中

		editor = sp.edit();

		rg_message_type = (RadioGroup) getActivity().findViewById(R.id.rg_setting_message_type);

		rg_message_speed = (RadioGroup) getActivity().findViewById(R.id.rg_setting_message_speed);

		img_commom = (ImageView) getActivity().findViewById(R.id.img_setting_common);

		img_special = (ImageView) getActivity().findViewById(R.id.img_setting_special);

		img_setServer = (ImageView) getActivity().findViewById(R.id.img_setting_setHttp);

		url_address = (TextView) getActivity().findViewById(R.id.tv_setting_setHttp);

		fragmentManager = getActivity().getSupportFragmentManager();
	}

}
