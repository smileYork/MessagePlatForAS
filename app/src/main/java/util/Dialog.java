package util;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface.OnClickListener;

public class Dialog {

	public static Builder singleDailog(Context context, String title, String item1, String item2,
			OnClickListener listen, int choice) {
		Builder dialog = new Builder(context).setTitle(title)
				.setSingleChoiceItems(new String[] { item1, item2 }, choice, listen);
		return dialog;
	}
	
	
}
