package com.jht.messageplat.activity;

import java.util.List;
import com.jht.messageplat.R;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemLongClickListener;

import adapter.ListSpecialMessageAdapter;
import model.AutoInfo;
import util.MyDBOperation;
import util.ToastShow;

/**
 * * @author chenyao E-mail: 326907403@qq.com
 * 
 * @date 创建时间：2016-2-24 下午3:07:58
 * @version 1.0
 * @parameter
 * @since
 * @return
 */
public class ShowSpecialMessage extends Fragment {

    private ListView mListView;

    private List<AutoInfo> list_autoInfo;

    private ImageView imageButton;

    private FragmentManager fragmentManager;

    private FragmentTransaction transaction;

    private EditSpecialMessage editSpecialMessage;

    private ListSpecialMessageAdapter adapter;

    private MyDBOperation db;

    private Button bt_click_return;

    private SettingActivity settingActivity;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
	    Bundle savedInstanceState) {

	View messageLayout = inflater.inflate(
		R.layout.fragement_show_special_message_list, container, false);

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

	mListView.setOnItemLongClickListener(new OnItemLongClickListener() {

	    @Override
	    public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
		    final int arg2, long arg3) {

		AlertDialog.Builder dialog = new AlertDialog.Builder(
			getActivity());
		dialog.setTitle("警告")
			.setIcon(android.R.drawable.ic_dialog_info)
			.setMessage(
				"确定要删除关键字为"
					+ list_autoInfo.get(arg2).getAutoInfo()
					+ "的定制短信吗？")
			.setPositiveButton("确定",
				new DialogInterface.OnClickListener() {
				    @Override
				    public void onClick(DialogInterface dialog,
					    int which) {
					dialog.cancel();// 取消弹出框

					db = new MyDBOperation(getActivity());

					db.deleteSpecailFromLocalSpecailList(list_autoInfo
						.get(arg2).getKey());

					list_autoInfo.remove(arg2);

					adapter.notifyDataSetChanged();

					ToastShow.ToastShowLong(getActivity(),
						"删除成功");
				    }

				})
			.setNegativeButton("取消",
				new DialogInterface.OnClickListener() {

				    @Override
				    public void onClick(DialogInterface dialog,
					    int arg1) {
					dialog.cancel();// 取消弹出框
				    }
				})

			.create().show();

		return false;
	    }
	});

	imageButton.setOnClickListener(new OnClickListener() {

	    @Override
	    public void onClick(View arg0) {

		editSpecialMessage = new EditSpecialMessage();

		transaction = fragmentManager.beginTransaction();
		transaction
			.replace(R.id.content, (Fragment) editSpecialMessage);
		transaction.commit();

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

    private void initView() {

	db = new MyDBOperation(getActivity());

	list_autoInfo = db.getLocalSpecailMessageList();

	adapter = new ListSpecialMessageAdapter(getActivity(), list_autoInfo);

	mListView.setAdapter(adapter);

    }

    private void findView() {

	mListView = (ListView) getActivity().findViewById(R.id.lv_list_special);

	imageButton = (ImageView) getActivity().findViewById(
		R.id.img_special_list_add);

	fragmentManager = getActivity().getSupportFragmentManager();

	bt_click_return = (Button) getActivity().findViewById(
		R.id.bt_fragement_special_show_return);

    }

}
