package util;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.widget.Toast;

/**
 * * @author chenyao E-mail: 326907403@qq.com
 * 
 * @date 创建时间：2016-2-19 下午4:17:21
 * @version 1.0
 * @parameter
 * @since
 * @return
 */
public class AlterDialogShow {

    public static void WarninDialog(final Context context, String message) {

	AlertDialog.Builder dialog = new AlertDialog.Builder(context);
	dialog.setTitle("提示")
		.setMessage(message)
		.setPositiveButton("确定", new DialogInterface.OnClickListener() {
		    @Override
		    public void onClick(DialogInterface dialog, int which) {
			// 点击了确定按钮
			dialog.cancel();// 取消弹出框
		    }
		}).create().show();

    }

    public static void ToastShowShort(Context context, String message) {
	Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

}
