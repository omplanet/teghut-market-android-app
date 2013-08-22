package am.teghut.market.adapter;

import java.util.List;

import am.teghut.market.R;
import am.teghut.market.api.dto.ProductWrapper;
import am.teghut.market.component.CustomButton;
import am.teghut.market.constant.Const;
import am.teghut.market.ui.AppRootActivity;
import am.teghut.market.ui.ProductsActivity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ProductsListAdapter extends BaseAdapter {

	private static final String TAG = "ProductsListAdapter: ";

	private LayoutInflater inflater;

	private int productId;
	private int lngIndex;

	private List<ProductWrapper> wrappedProducts;
	private ProductWrapper productWrapper;
	private Context context;

	public ProductsListAdapter(Context context, List<ProductWrapper> products) {
		this.context = context;
		this.wrappedProducts = products;
		inflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		return wrappedProducts.size();
	}

	@Override
	public Object getItem(int position) {
		return wrappedProducts.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = null;
		lngIndex = ((AppRootActivity) context).getLanguage();
		try {
			if (convertView != null) {
				view = convertView;
			} else {
				view = inflater.inflate(R.layout.product_item, parent, false);
			}
			productWrapper = wrappedProducts.get(position);
			productId = (int) productWrapper.getId();

			setImageView(view, productWrapper);
			setProductInfo(view, productWrapper);
			setProductPrice(view, productWrapper);
			setOrderBtnListener(view);
		} catch (Exception e) {
			Log.e(TAG + "getView(): ", e.toString());
		}
		view.setFocusable(false);
		return view;
	}

	private void setImageView(View view, ProductWrapper pWrapper) {
		ImageView imageView = (ImageView) view.findViewById(R.id.productImage);
		imageView.setImageBitmap(pWrapper.getImage());
	}

	private void setProductInfo(View v, ProductWrapper pWrapper) {
		TextView productName = (TextView) v.findViewById(R.id.productName);
		TextView productCount = (TextView) v.findViewById(R.id.productCount);

		productName.setText(pWrapper.getName(lngIndex));
		if (pWrapper.getInStock() <= 0) {
			productCount.setText(AppRootActivity.words.get(Const.NOT_IN_STOCK));
		} else {
			productCount.setText(AppRootActivity.words.get(Const.IN_STOCK)
					+ " " + pWrapper.getInStock() + " " + pWrapper.getUnit());
		}
		TextView pDesc = (TextView) v.findViewById(R.id.pDescription);
		TextView pLocation = (TextView) v.findViewById(R.id.pLocation);
		TextView pProducer = (TextView) v.findViewById(R.id.pProducer);

		pDesc.setText(AppRootActivity.words.get(Const.DESCRIPTION) + " "
				+ pWrapper.getDescription(lngIndex));
		pLocation.setText(AppRootActivity.words.get(Const.LOCATION) + " "
				+ pWrapper.getLocationName(lngIndex));
		pProducer.setText(AppRootActivity.words.get(Const.PRODUCER) + " "
				+ pWrapper.getProducerName(lngIndex));

	}

	private void setProductPrice(View view, ProductWrapper pWrapper) {
		TextView productPrice = (TextView) view.findViewById(R.id.productPrice);
		productPrice.setText(pWrapper.getPricePerUnit() + " "
				+ pWrapper.getCurrency(lngIndex));
		TextView per = (TextView) view.findViewById(R.id.per);
		per.setText("per " + pWrapper.getUnit());
	}

	private void setOrderBtnListener(View view) {
		CustomButton orderButton = (CustomButton) view
				.findViewById(R.id.preOrderBtn);
		orderButton.setText(AppRootActivity.words.get(Const.PRE_ORDER));
		orderButton.setOnClickListener(orderClickListener);
		orderButton.setProductId(String.valueOf(productId));
	}

	private OnClickListener orderClickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			((ProductsActivity) context).orderProduct(v);
		}
	};

}
