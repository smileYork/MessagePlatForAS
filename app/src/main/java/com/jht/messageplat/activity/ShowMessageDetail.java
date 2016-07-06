package com.jht.messageplat.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import com.jht.messageplat.R;

/**
 * * @author chenyao E-mail: 326907403@qq.com
 * 
 * @date 创建时间：2016-2-26 上午10:18:00
 * @version 1.0
 * @parameter
 * @since
 * @return
 */
public class ShowMessageDetail extends Fragment {

    private Button bt_click_return;

    private FragmentManager fragmentManager;

    private FragmentTransaction transaction;

    private ShowLocalBuffMessage showLocalBuffMessage;

    private String phone, content, date;

    private TextView tv_phone, tv_content, tv_date;

    private int type, position;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
	    Bundle savedInstanceState) {

	View messageLayout = inflater.inflate(
		R.layout.fragement_show_buff_detail, container, false);
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

	phone = getArguments().getString("phone");

	content = getArguments().getString("content");

	date = getArguments().getString("date");

	type = getArguments().getInt("type");

	position = getArguments().getInt("position");

	tv_phone.setText(phone);

	tv_content.setText(content);

	tv_date.setText(date);

    }

    private void setListen() {

	bt_click_return.setOnClickListener(new OnClickListener() {

	    @Override
	    public void onClick(View arg0) {

		Bundle bundle = new Bundle();

		bundle.putInt("type", type);

		bundle.putInt("position", position);

		showLocalBuffMessage = new ShowLocalBuffMessage();

		showLocalBuffMessage.setArguments(bundle);

		transaction = fragmentManager.beginTransaction();

		transaction.replace(R.id.content,
			(Fragment) showLocalBuffMessage);

		transaction.commit();

	    }
	});

    }

    private void findView() {

	fragmentManager = getActivity().getSupportFragmentManager();

	bt_click_return = (Button) getActivity().findViewById(
		R.id.bt_fragement_show_message_detail_return);

	tv_phone = (TextView) getActivity().findViewById(
		R.id.tv_fragement_show_message_detail_phone);

	tv_content = (TextView) getActivity().findViewById(
		R.id.tv_fragement_show_message_detail_content);

	tv_date = (TextView) getActivity().findViewById(
		R.id.tv_fragement_show_message_detail_time);

	tv_content.setMovementMethod(ScrollingMovementMethod.getInstance());

    }

}