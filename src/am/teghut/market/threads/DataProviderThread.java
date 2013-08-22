package am.teghut.market.threads;

import java.util.List;

import am.teghut.market.api.dto.ProductBean;
import am.teghut.market.ui.SplashActivity;
import am.teghut.market.util.DataManager;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class DataProviderThread extends Thread {

	private static final String TAG = "DataProviderThread: ";
	private Context context;
	private Handler handler;
	private List<ProductBean> products;

	public DataProviderThread(Context context, Handler handler) {
		this.handler = handler;
		this.context = context;
	}

	@Override
	public void run() {
		try {
			if (DataManager.retrievingState == 0) {
				if (((SplashActivity) context).hasConnection()) {
					products = DataManager.getProducts();
				}
			}
		} catch (Exception e) {
			Log.e(TAG + "run() ", e.toString());
		} finally {
			Message msg = handler.obtainMessage();
			msg.arg1 = DataManager.retrievingState;
			handler.sendMessage(msg);
			interrupt();
		}
	}

	public List<ProductBean> getProducts() {
		return products;
	}

}
