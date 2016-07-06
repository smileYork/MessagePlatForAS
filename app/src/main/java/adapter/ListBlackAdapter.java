package adapter;

import java.util.List;
import com.jht.messageplat.R;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import model.BlackPhone;

/**
 * * @author chenyao E-mail: 326907403@qq.com
 * 
 * @date 创建时间：2016-2-23 下午1:36:04
 * @version 1.0
 * @parameter
 * @since
 * @return
 */
public class ListBlackAdapter extends BaseAdapter {

    private List<BlackPhone> list_black;

    private Context mContext;

    public ListBlackAdapter(Context context, List<BlackPhone> list_black) {
	this.mContext = context;
	this.list_black = list_black;

    }

    @Override
    public int getCount() {

	return list_black.size();
    }

    @Override
    public Object getItem(int arg0) {

	return list_black.get(arg0);
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
		    R.layout.item_black_list, null);

	    viewHolder.number = (TextView) view
		    .findViewById(R.id.black_item_left_id);

	    viewHolder.phone = (TextView) view
		    .findViewById(R.id.black_item_phone);

	    view.setTag(viewHolder);
	}

	viewHolder = (ViewHolder) view.getTag();

	viewHolder.phone.setText(list_black.get(position).getPhone());

	int tempCount = position + 1;

	viewHolder.number.setText(tempCount + "");

	return view;

    }

    final static class ViewHolder {
	TextView number;
	TextView phone;
    }
}
