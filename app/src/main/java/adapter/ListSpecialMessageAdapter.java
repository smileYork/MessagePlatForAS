package adapter;

import java.util.List;
import com.jht.messageplat.R;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import model.AutoInfo;

/**
 * * @author chenyao E-mail: 326907403@qq.com
 * 
 * @date 创建时间：2016-2-24 下午3:21:35
 * @version 1.0
 * @parameter
 * @since
 * @return
 */
public class ListSpecialMessageAdapter extends BaseAdapter {

    private List<AutoInfo> list_special;

    private Context mContext;

    public ListSpecialMessageAdapter(Context context,
	    List<AutoInfo> list_autoInfo) {
	this.mContext = context;
	this.list_special = list_autoInfo;

    }

    @Override
    public int getCount() {

	return list_special.size();
    }

    @Override
    public Object getItem(int arg0) {

	return list_special.get(arg0);
    }

    @Override
    public long getItemId(int arg0) {

	return arg0;
    }

    @Override
    public View getView(int position, View view, ViewGroup arg2) {

	ViewHolder viewHolder = null;

	if (view == null) {

	    viewHolder = new ViewHolder();

	    view = LayoutInflater.from(mContext).inflate(
		    R.layout.item_special_list, null);

	    viewHolder.number = (TextView) view
		    .findViewById(R.id.special_item_left_id);

	    viewHolder.key = (TextView) view
		    .findViewById(R.id.special_item_key);

	    viewHolder.content = (TextView) view
		    .findViewById(R.id.special_item_content);

	    view.setTag(viewHolder);
	}

	viewHolder = (ViewHolder) view.getTag();

	viewHolder.key.setText(list_special.get(position).getKey());

	viewHolder.content.setText(list_special.get(position).getAutoInfo());

	int tempCount = position + 1;

	viewHolder.number.setText(tempCount + "");

	return view;

    }

    final static class ViewHolder {
	TextView number;
	TextView key;
	TextView content;
    }
}
