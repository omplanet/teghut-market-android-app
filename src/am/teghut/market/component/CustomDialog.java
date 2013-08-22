package am.teghut.market.component;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.KeyEvent;

public class CustomDialog extends Dialog {
	
	private Context context;

	public CustomDialog(Context context, boolean cancelable,
			OnCancelListener cancelListener) {
		super(context, cancelable, cancelListener);
		this.context = context;
	}

	public CustomDialog(Context context, int theme) {
		super(context, theme);
		this.context = context;
	}

	public CustomDialog(Context context) {
		super(context);
		this.context = context;
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			this.cancel();
			((Activity) context).finish();
		}
		return super.onKeyDown(keyCode, event);
	}

}
