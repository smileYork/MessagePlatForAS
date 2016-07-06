package view;

import com.jht.messageplat.R;
import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

public class LoadingDialog extends Dialog {

    private Context context;

    private LoadingDialog dialog;

    private TextView tv_message;

    public LoadingDialog(Context context) {
	super(context);
	this.context = context;
    }

    private LoadingDialog(Context context, int theme) {
	super(context, theme);
	this.context = context;
    }

    public void showDialog() {
	LayoutInflater inflater = (LayoutInflater) context
		.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	dialog = new LoadingDialog(context, R.style.loadingDialogStyle);
	View layout = inflater.inflate(R.layout.dialog_loading, null);
	tv_message = (TextView) layout.findViewById(R.id.dialog_message);
	dialog.setContentView(layout);
	dialog.setCanceledOnTouchOutside(false);
	dialog.show();
    }

    public void closeDialog() {
	dialog.dismiss();
	dialog.cancel();
    }

    public void setMessage(String message) {
	tv_message.setText(message);
    }

}
