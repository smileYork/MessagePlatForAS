package adapter;

import java.util.List;
import com.jht.messageplat.R;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import model.ShowItem;

/**
 * * @author chenyao E-mail: 326907403@qq.com
 * 
 * @date 创建时间：2016-2-24 下午5:42:27
 * @version 1.0
 * @parameter
 * @since
 * @return
 */
public class ListServiceShowAdapter extends BaseAdapter {

    private List<ShowItem> list_show;

    private Context mContext;

    public ListServiceShowAdapter(Context context, List<ShowItem> list_show) {
	this.mContext = context;
	this.list_show = list_show;

    }

    @Override
    public int getCount() {

	return list_show.size();
    }

    @Override
    public Object getItem(int arg0) {

	return list_show.get(arg0);
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
		    R.layout.item_local_buff_show, null);

	    viewHolder.number = (TextView) view
		    .findViewById(R.id.show_buff_item_left_id);

	    viewHolder.phone = (TextView) view
		    .findViewById(R.id.show_buff_item_phone);

	    viewHolder.content = (TextView) view
		    .findViewById(R.id.show_buff_item_content);

	    viewHolder.date = (TextView) view
		    .findViewById(R.id.show_buff_item_date);

	    view.setTag(viewHolder);
	}

	viewHolder = (ViewHolder) view.getTag();

	int tempCount = position + 1;

	viewHolder.number.setText(tempCount + "");

	viewHolder.phone.setText(list_show.get(position).getPhone());

	viewHolder.content.setText(list_show.get(position).getContent());

	viewHolder.date.setText(list_show.get(position).getTime());

	return view;

    }

    final static class ViewHolder {
	TextView number;
	TextView phone;
	TextView content;
	TextView date;
    }
}
