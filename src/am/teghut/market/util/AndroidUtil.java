package am.teghut.market.util;

import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

import am.teghut.market.R;
import am.teghut.market.api.dto.ProductBean;
import am.teghut.market.api.dto.ProductWrapper;
import am.teghut.market.ui.AppRootActivity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.Gravity;
import android.widget.Toast;

public class AndroidUtil {

	private static final String TAG = "AndroidUtil: ";

	public static int wrapState;

	public static final void showToast(Context context, String text,
			int duration) {
		int toastDuration;
		if (duration >= 0) {
			toastDuration = Toast.LENGTH_LONG;
		} else {
			toastDuration = Toast.LENGTH_SHORT;
		}

		Toast toast = Toast.makeText(context, text, toastDuration);
		int gravity = Gravity.CENTER;
		toast.setGravity(gravity, 0, 0);
		toast.show();
	}

	public static final Bitmap getImage(String imageUrl) {
		Bitmap productImg = null;
		try {
			URL photoUrl = new URL(imageUrl);
			URLConnection urlConn = photoUrl.openConnection();
			InputStream is = urlConn.getInputStream();
			productImg = BitmapFactory.decodeStream(is);
		} catch (Exception e) {
			Log.e(TAG + "getImage() ", e.toString());
		}
		if (productImg == null) {
			productImg = BitmapFactory.decodeResource(
					AppRootActivity.getAppResources(), R.drawable.no_image);
		}
		return productImg;
	}

	public static final ProductWrapper wrap(ProductBean pBean) {
		ProductWrapper pWrapper = new ProductWrapper();
		pWrapper.setId(pBean.getId());
		pWrapper.setName(pBean.getName());
		pWrapper.setImage(getImage(pBean.getImageURL()));
		pWrapper.setUnit(pBean.getUnit());
		pWrapper.setDescription(pBean.getDescription());
		pWrapper.setCurrency(pBean.getCurrency());
		pWrapper.setPricePerUnit(pBean.getPricePerUnit());
		pWrapper.setInStock(pBean.getAvailabelInStock());
		pWrapper.setDeliveryService(pBean.isDeliveryService());
		pWrapper.setLocationName(pBean.getLocationName());
		pWrapper.setLocationURL(pBean.getLocationURL());
		pWrapper.setProducerId(pBean.getProducerId());
		pWrapper.setProducerName(pBean.getProducerName());
		pWrapper.setProducerURL(pBean.getProducerURL());
		pWrapper.setCategory(pBean.getCategory());

		return pWrapper;
	}
}
