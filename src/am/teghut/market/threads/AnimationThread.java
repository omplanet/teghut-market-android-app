package am.teghut.market.threads;

import am.teghut.market.ui.SplashActivity;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;


public class AnimationThread extends Thread {

	private static final String TAG = "AnimationThread: ";
	private Context context;
	private Handler handler;
	private int touchState;
	private boolean isFirstRun = true;

	public AnimationThread(Context context, Handler handler) {
		this.context = context;
		this.handler = handler;
	}

	@Override
	public void run() {
		while (touchState == 0) {
			try {
				touchState = ((SplashActivity) context).getTouchState();
				if (isFirstRun) {
					sleep(500);					
					isFirstRun = false;
				}
				else {
					sleep(3000);
				}
			} catch (Exception e) {
				Log.e(TAG + "run() ", e.toString());
			}
			Message message = handler.obtainMessage();
			message.arg1 = touchState;
			handler.sendMessage(message);
		}
	}
}
