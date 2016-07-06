package com.jht.messageplat.activity;

import java.util.ArrayList;
import java.util.List;
import com.jht.messageplat.R;
import android.content.ContentResolver;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;

import adapter.ListServiceShowAdapter;
import globalData.GlobalData;
import model.ShowItem;
import util.MyDBOperation;
import util.TimeAndDateType;

/**
 * * @author chenyao E-mail: 326907403@qq.com
 * 
 * @date 创建时间：2016-2-23 下午4:53:00
 * @version 1.0
 * @parameter
 * @since
 * @return
 */
public class ShowLocalBuffMessage extends Fragment {

    private Button bt_click_return;

    private FragmentManager fragmentManager;

    private FragmentTransaction transaction;

    private AutoServiceActivity autoServiceActivity;

    private List<ShowItem> show_list = new ArrayList<ShowItem>();

    private int type, position;

    private ListView listView;

    private ListServiceShowAdapter adapter;

    private ShowMessageDetail showMessageDetail;

    private MyDBOperation db;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
	    Bundle savedInstanceState) {

	View messageLayout = inflater.inflate(
		R.layout.fragement_show_local_buff_message, container, false);
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

	type = getArguments().getInt("type");

	position = getArguments().getInt("position");

	if (type == 0) {

	    show_list = getMessageFromLocal();

	} else if (type == 1) {

	    show_list = db.getBuffForShowPush();

	} else {

	    show_list = db.getBuffForShowSend();

	}

	adapter = new ListServiceShowAdapter(getActivity(), show_list);

	listView.setAdapter(adapter);

	listView.setSelection(position);

    }

    private void setListen() {

	listView.setOnItemClickListener(new OnItemClickListener() {

	    @Override
	    public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
		    long arg3) {

		showMessageDetail = new ShowMessageDetail();

		transaction = fragmentManager.beginTransaction();

		Bundle bundle = new Bundle();

		bundle.putInt("type", type);

		bundle.putInt("position", arg2);

		bundle.putString("phone", show_list.get(arg2).getPhone());

		bundle.putString("content", show_list.get(arg2).getContent());

		bundle.putString("date", show_list.get(arg2).getTime());

		showMessageDetail.setArguments(bundle);

		transaction.replace(R.id.content, (Fragment) showMessageDetail);

		transaction.commit();
	    }
	});

	bt_click_return.setOnClickListener(new OnClickListener() {

	    @Override
	    public void onClick(View arg0) {

		autoServiceActivity = new AutoServiceActivity();
		transaction = fragmentManager.beginTransaction();
		transaction.replace(R.id.content,
			(Fragment) autoServiceActivity);
		transaction.commit();
	    }
	});

    }

    private void findView() {

	db = new MyDBOperation(getActivity());

	fragmentManager = getActivity().getSupportFragmentManager();

	bt_click_return = (Button) getActivity().findViewById(
		R.id.bt_fragement_show_return);

	listView = (ListView) getActivity().findViewById(
		R.id.lv_local_buff_show);

    }

    private List<ShowItem> getMessageFromLocal() {

	ContentResolver cr = getActivity().getContentResolver();

	List<ShowItem> temp_show_list = new ArrayList<ShowItem>();

	String[] projection = new String[] { "_id", "address", "body", "date",
		"type" };// type 短信类型，1代表接收，2代表发送

	String section = "read = 0 and type = 1";// 0表示未读，1表示已读

	Cursor cur = cr.query(GlobalData.SMS_INBOX, projection, section, null,
		null);

	if (null == cur)
	    return temp_show_list;
	while (cur.moveToNext()) {

	    ShowItem msg = new ShowItem();

	    msg.setPhone(cur.getString(cur.getColumnIndex("address")));

	    msg.setContent(cur.getString(cur.getColumnIndex("body")));

	    msg.setTime(TimeAndDateType.GetStringFromLong(cur.getLong(cur
		    .getColumnIndex("date"))));

	    temp_show_list.add(msg);

	}

	cur.close();

	return temp_show_list;
    }

}
