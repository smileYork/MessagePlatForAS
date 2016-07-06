package adapter;

import java.util.List;
import com.jht.messageplat.R;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import model.LocalType;

public class LocalTypeAdapter extends BaseAdapter implements OnClickListener {

    private Context mContext;

    private List<LocalType> list_type;

    public LocalTypeAdapter(Context mContext, List<LocalType> list_type) {
	this.mContext = mContext;
	this.list_type = list_type;
    }

    public View getView(final int position, View view, ViewGroup arg2) {
	ViewHolder viewHolder = null;
	if (view == null) {
	    viewHolder = new ViewHolder();
	    view = LayoutInflater.from(mContext).inflate(
		    R.layout.item_service_show, null);
	    viewHolder.message = (TextView) view
		    .findViewById(R.id.item_tv_message);
	    viewHolder.number = (TextView) view
		    .findViewById(R.id.item_tv_number);
	    viewHolder.photo = (ImageView) view.findViewById(R.id.item_photo);
	    viewHolder.direct = (ImageView) view
		    .findViewById(R.id.item_img_direct);
	    view.setTag(viewHolder);
	}

	viewHolder = (ViewHolder) view.getTag();
	setView(viewHolder, list_type.get(position).getPhotoId(), list_type
		.get(position).getMessage(), list_type.get(position)
		.getNumber(), R.drawable.arrow_right_16);

	return view;

    }

    final static class ViewHolder {
	TextView message;
	TextView number;
	ImageView photo;
	ImageView direct;
    }

    public void setView(ViewHolder viewHolder, int imgSourceForPhoto,
	    String message, int number, int imgSourceForDirect) {
	viewHolder.photo.setImageResource(imgSourceForPhoto);
	viewHolder.direct.setImageResource(imgSourceForDirect);
	viewHolder.message.setText(message);
	viewHolder.number.setText(number + "条");
    }

    @Override
    public int getCount() {

	return list_type.size();
    }

    @Override
    public Object getItem(int arg0) {

	return list_type.get(arg0);
    }

    @Override
    public long getItemId(int arg0) {

	return arg0;
    }

    @Override
    public void onClick(DialogInterface arg0, int arg1) {

    }

}