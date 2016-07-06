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
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import adapter.LocalTypeAdapter;
import globalData.GlobalData;
import model.LocalType;
import util.MyDBOperation;

/**
 * * @author chenyao E-mail: 326907403@qq.com
 * 
 * @date 创建时间：2016-2-19 下午5:02:41
 * @version 1.0
 * @parameter
 * @since
 * @return
 */
public class AutoServiceActivity extends Fragment {

    // 显示列表
    private ListView list_show;

    /*
     * 在收发服务主界面需要显示的内容集合， 这里只有未处理、未上传、未回复三种， 预留接口，方便今后添加
     */
    private List<LocalType> list_type;

    // 显示适配器对象
    private LocalTypeAdapter localTypeAdapter;

    // 单个显示对象
    private LocalType tempLocalType;

    // Fragement切换管理器
    private FragmentManager fragmentManager;

    // Fragement切换器
    private FragmentTransaction transaction;

    // 点击进去之后显示的Fragement
    private ShowLocalBuffMessage showLocalBuffMessage;

    // 连接数据库对象
    private MyDBOperation db;

    // 主界面显示未处理短信数量
    private int number_waitingResolve = 0;

    // 主界面显示未上传短信数量
    private int number_waitingUpdate = 0;

    // 主界面显示未回复短信数量
    private int number_waitingSend = 0;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
	    Bundle savedInstanceState) {

	View messageLayout = inflater.inflate(R.layout.acitivity_auto_service,
		container, false);

	return messageLayout;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
	super.onActivityCreated(savedInstanceState);

	// 获取各个控件的id
	findView();

	// 初始化需要使用的对象数据和显示界面信息
	initView();

	// 设置监听器
	setListen();

    }

    private void setListen() {

	// 点击了listview的一个item
	list_show.setOnItemClickListener(new OnItemClickListener() {
	    @Override
	    public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
		    long arg3) {

		showLocalBuffMessage = new ShowLocalBuffMessage();

		transaction = fragmentManager.beginTransaction();

		Bundle bundle = new Bundle();

		bundle.putInt("type", arg2);

		bundle.putInt("position", 0);

		showLocalBuffMessage.setArguments(bundle);

		transaction.replace(R.id.content,
			(Fragment) showLocalBuffMessage);

		transaction.commit();

	    }
	});

    }

    private void initView() {

	db = new MyDBOperation(getActivity());

	number_waitingResolve = getLocalReadNumber();

	number_waitingUpdate = db.getBuffForPushNumber();

	number_waitingSend = db.getBuffForSendNumber();

	list_type = new ArrayList<LocalType>();

	tempLocalType = new LocalType(R.drawable.mail_receive_72, "待处理短信条数",
		number_waitingResolve);
	list_type.add(tempLocalType);

	tempLocalType = new LocalType(R.drawable.upload_72, "待同步信息数目",
		number_waitingUpdate);
	list_type.add(tempLocalType);

	tempLocalType = new LocalType(R.drawable.mail_send_72, "待回复短信数目",
		number_waitingSend);
	list_type.add(tempLocalType);

	localTypeAdapter = new LocalTypeAdapter(getActivity(), list_type);

	list_show.setAdapter(localTypeAdapter);

    }

    private int getLocalReadNumber() {

	int number = 0;

	String[] projection = new String[] { "count(*) as num" };

	/*
	 * read 0表示未读，1表示已读, type，1代表接收，2代表发送
	 */
	String section = "read = 0 and type = 1";

	ContentResolver cr = getActivity().getContentResolver();

	Cursor cur = cr.query(GlobalData.SMS_INBOX, projection, section, null,
		null);

	if (cur.moveToNext()) {

	    number = cur.getInt(cur.getColumnIndex("num"));

	}

	cur.close();

	return number;
    }

    private void findView() {

	list_show = (ListView) getActivity().findViewById(R.id.list_style);

	fragmentManager = getActivity().getSupportFragmentManager();

    }

}
