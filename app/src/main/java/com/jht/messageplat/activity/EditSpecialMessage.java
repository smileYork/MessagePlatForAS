package com.jht.messageplat.activity;

import com.jht.messageplat.R;
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

import util.MyDBOperation;
import util.ToastShow;

/**
 * * @author chenyao E-mail: 326907403@qq.com
 * 
 * @date 创建时间：2016-2-23 下午4:13:32
 * @version 1.0
 * @parameter
 * @since
 * @return
 */
public class EditSpecialMessage extends Fragment {

    private EditText et_input_message_key;

    private EditText et_input_message_content;

    private Button bt_click;

    private Button bt_click_return;

    private FragmentManager fragmentManager;

    private FragmentTransaction transaction;

    private SettingActivity settingActivity;

    private MyDBOperation db;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
	    Bundle savedInstanceState) {

	View messageLayout = inflater.inflate(
		R.layout.fragement_set_specail_message, container, false);
	return messageLayout;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
	super.onActivityCreated(savedInstanceState);

	findView();

	setListen();

    }

    private void setListen() {
	bt_click.setOnClickListener(new OnClickListener() {

	    @Override
	    public void onClick(View arg0) {

		String key = et_input_message_key.getText().toString();

		String content = et_input_message_content.getText().toString();

		if (key.equals("") || content.equals("")) {
		    ToastShow.ToastShowShort(getActivity(), "关键字与回复内容非空");

		} else {

		    db = new MyDBOperation(getActivity());

		    if (db.findSpecialMessageByKey(key)) {

			ToastShow.ToastShowShort(getActivity(), "关键字:" + key
				+ " 已存在");

		    } else {
			db.addSpecialMessage(key, content);
			ToastShow.ToastShowShort(getActivity(), "添加成功!");

		    }

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

    private void findView() {
	fragmentManager = getActivity().getSupportFragmentManager();

	et_input_message_key = (EditText) getActivity().findViewById(
		R.id.et_fragement_special_message_key);

	et_input_message_content = (EditText) getActivity().findViewById(
		R.id.et_fragement_special_message_content);

	bt_click = (Button) getActivity().findViewById(
		R.id.bt_fragement_set_special_click);

	bt_click_return = (Button) getActivity().findViewById(
		R.id.bt_fragement_set_special_return);

    }

}
