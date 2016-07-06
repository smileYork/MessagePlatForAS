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

import globalData.GlobalData;
import util.MyDBOperation;
import util.ToastShow;

/**
 * * @author chenyao E-mail: 326907403@qq.com
 * 
 * @date 创建时间：2016-2-23 下午4:12:57
 * @version 1.0
 * @parameter
 * @since
 * @return
 */
public class EditCommonMessage extends Fragment {

    private EditText et_input_message;

    private Button bt_click;

    private Button bt_click_return;

    private FragmentManager fragmentManager;

    private FragmentTransaction transaction;

    private SettingActivity settingActivity;

    private MyDBOperation db;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
	    Bundle savedInstanceState) {

	View messageLayout = inflater.inflate(
		R.layout.fragement_set_common_message, container, false);
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

	db = new MyDBOperation(getActivity());

	String content = db.getCommonMessage();

	if (!content.equals("")) {

	    GlobalData.MESSAGE_COMMON = content;

	    et_input_message.setText(content);

	} else {
	    et_input_message.setText(GlobalData.MESSAGE_COMMON);
	}

    }

    private void setListen() {

	bt_click.setOnClickListener(new OnClickListener() {

	    @Override
	    public void onClick(View arg0) {

		String message = et_input_message.getText().toString();

		if (message.equals("")) {

		    ToastShow.ToastShowShort(getActivity(), "回复内容不能为空!");
		}

		else {
		    db = new MyDBOperation(getActivity());

		    db.setCommonMessage(message);

		    GlobalData.MESSAGE_COMMON = message;

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

    private void findView() {

	fragmentManager = getActivity().getSupportFragmentManager();

	et_input_message = (EditText) getActivity().findViewById(
		R.id.et_common_message);

	bt_click = (Button) getActivity().findViewById(
		R.id.bt_fragement_set_common_click);

	bt_click_return = (Button) getActivity().findViewById(
		R.id.bt_fragement_set_common_return);

    }

}
