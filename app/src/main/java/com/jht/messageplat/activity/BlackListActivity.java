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
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ImageView;
import android.widget.ListView;

import adapter.ListBlackAdapter;
import model.BlackPhone;
import util.MyDBOperation;
import util.ToastShow;

/**
 * * @author chenyao E-mail: 326907403@qq.com
 * 
 * @date 创建时间：2016-2-19 下午5:05:59
 * @version 1.0
 * @parameter
 * @since
 * @return
 */
public class BlackListActivity extends Fragment {

    private ListView mListView;

    // 黑名单列表
    private List<BlackPhone> list_black;

    // 点击添加按钮
    private ImageView imageButton;

    private FragmentManager fragmentManager;

    private FragmentTransaction transaction;

    private AddBlackPhone addBlackPhone = new AddBlackPhone();

    private ListBlackAdapter adapter;

    private MyDBOperation db;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
	    Bundle savedInstanceState) {

	View messageLayout = inflater.inflate(R.layout.activity_black_list,
		container, false);

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
				"确定要将" + list_black.get(arg2).getPhone()
					+ "从黑名单中移除吗？")
			.setPositiveButton("确定",
				new DialogInterface.OnClickListener() {
				    @Override
				    public void onClick(DialogInterface dialog,
					    int which) {
					dialog.cancel();// 取消弹出框

					db = new MyDBOperation(getActivity());

					db.deletePhoneFromLocalBlackList(list_black
						.get(arg2).getPhone());

					list_black.remove(arg2);

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

		transaction = fragmentManager.beginTransaction();
		transaction.replace(R.id.content, (Fragment) addBlackPhone);
		transaction.commit();

	    }
	});

    }

    private void initView() {

	db = new MyDBOperation(getActivity());

	list_black = db.getLocalBlackList();

	adapter = new ListBlackAdapter(getActivity(), list_black);

	mListView.setAdapter(adapter);

    }

    private void findView() {

	mListView = (ListView) getActivity().findViewById(R.id.list_black);

	imageButton = (ImageView) getActivity().findViewById(
		R.id.black_list_add);

	fragmentManager = getActivity().getSupportFragmentManager();

    }

}
