package com.jht.messageplat.activity;

import com.jht.messageplat.R;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import util.MyDBOperation;
import util.ToastShow;

/**
 * * @author chenyao E-mail: 326907403@qq.com
 * 
 * @date 创建时间：2016-2-23 下午2:33:47
 * @version 1.0
 * @parameter
 * @since
 * @return
 */
public class AddBlackPhone extends Fragment {

    private EditText et_onput_phone;

    private Button bt_click;

    private Button bt_click_return;

    private FragmentManager fragmentManager;

    private FragmentTransaction transaction;

    private BlackListActivity blackListActivity;

    private MyDBOperation db;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
	    Bundle savedInstanceState) {

	View messageLayout = inflater.inflate(
		R.layout.fragement_add_blackphone, container, false);
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

		String phone = et_onput_phone.getText().toString();

		if (phone.equals("")) {

		    ToastShow.ToastShowShort(getActivity(), "号码不能为空");
		} else {

		    if (!db.findSamePhone(phone)) {

			db.insertBlackPone(phone);

			ToastShow.ToastShowShort(getActivity(), "号码" + phone
				+ "添加成功!");
		    } else {
			ToastShow.ToastShowShort(getActivity(), "您输入的" + phone
				+ "已经存在列表中!");
		    }
		}

	    }
	});

	bt_click_return.setOnClickListener(new OnClickListener() {

	    @Override
	    public void onClick(View arg0) {

		blackListActivity = new BlackListActivity();

		transaction = fragmentManager.beginTransaction();

		transaction.replace(R.id.content, (Fragment) blackListActivity);

		transaction.commit();
	    }
	});

    }

    private void findView() {

	db = new MyDBOperation(getActivity());

	fragmentManager = getActivity().getSupportFragmentManager();

	et_onput_phone = (EditText) getActivity().findViewById(
		R.id.fragement_add_black_mobile);

	bt_click = (Button) getActivity().findViewById(R.id.bt_fragement_add);

	bt_click_return = (Button) getActivity().findViewById(
		R.id.bt_fragement_return);

    }

}
