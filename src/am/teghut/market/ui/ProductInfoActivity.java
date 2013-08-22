package am.teghut.market.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import am.teghut.market.R;
import am.teghut.market.api.dto.PreOrderBean;
import am.teghut.market.api.dto.ProductWrapper;
import am.teghut.market.constant.Const;
import am.teghut.market.util.AndroidUtil;
import am.teghut.market.util.DataManager;
import am.teghut.market.util.LanguageManager;
import am.teghut.market.util.Utils;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ProductInfoActivity extends AppRootActivity implements
		TextWatcher, android.view.View.OnClickListener {

	private static final String TAG = "ProductInfoActivity: ";

	private HashMap<String, String> words;

	private ProductWrapper pWrapper;
	private LanguageManager lManager;

	private View layout;

	private String productId;
	private String buyerName;
	private String contact;
	private String message;
	private boolean isDeliver;

	private AlertDialog orderDialog;
	private AlertDialog thankYouDialog;

	private LinearLayout line;

	private ImageView pImage;
	private TextView pName;
	private TextView pDesc;
	private TextView pPrice;
	private TextView pStock;
	private TextView pDelivery;
	private TextView pLoc;
	private TextView pProd;
	private TextView pProcucerName;

	private TextView preDialogTitle;
	private TextView charsCountView;

	private CheckBox prefDelivery;

	private EditText buyerEdit;
	private EditText contactEdit;
	private EditText messageEdit;

	private Button orderButton;
	private Button backButton;

	private int lngIndex;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_product_info);

		lngIndex = getLanguage();
		lManager = new LanguageManager(this, getLanguage());
		words = lManager.getWords();
		pWrapper = getIntent().getParcelableExtra(Const.KEY_PRODUCT_WRAPPER);
		initViews();
		setInfo(pWrapper);
	}

	@Override
	protected void onResume() {
		lngIndex = getLanguage();
		lManager = new LanguageManager(this, getLanguage());
		words = lManager.getWords();
		setInfo(pWrapper);
		super.onResume();
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		closeDialog(orderDialog);
		closeDialog(thankYouDialog);
		super.onSaveInstanceState(outState);
	}

	private void initViews() {
		pImage = (ImageView) findViewById(R.id.selectedProductImg);
		pName = (TextView) findViewById(R.id.selectedProductName);
		pDesc = (TextView) findViewById(R.id.selectedProductDesc);
		pPrice = (TextView) findViewById(R.id.selectedProductPrice);
		pStock = (TextView) findViewById(R.id.selectedProductStock);
		pDelivery = (TextView) findViewById(R.id.selectedProductDelivery);
		pLoc = (TextView) findViewById(R.id.selectedProductLocation);
		pProd = (TextView) findViewById(R.id.selectedProductProducer);
		pProcucerName = (TextView) findViewById(R.id.selectedProductProducerName);
		line = (LinearLayout) findViewById(R.id.link_line);
		orderButton = (Button) findViewById(R.id.pInfoOrderBtn);
		backButton = (Button) findViewById(R.id.pInfoBackBtn);
	}

	private void setInfo(ProductWrapper pWrapper) {
		pImage.setImageBitmap(pWrapper.getImage());
		pName.setText(pWrapper.getName(lngIndex));
		pDesc.setText(words.get(Const.DESCRIPTION) + " "
				+ pWrapper.getDescription(lngIndex));
		pPrice.setText(words.get(Const.PRICE) + " "
				+ pWrapper.getPricePerUnit() + " "
				+ pWrapper.getCurrency(lngIndex));
		if (pWrapper.getInStock() <= 0) {
			pStock.setText(words.get(Const.NOT_IN_STOCK));
		} else {
			pStock.setText(words.get(Const.IN_STOCK) + " "
					+ pWrapper.getInStock() + " " + pWrapper.getUnit());
		}
		pDelivery.setText(words.get(Const.DELIVERY)
				+ " "
				+ (pWrapper.isDeliveryService() ? words.get(Const.YES) : words
						.get(Const.NO)));
		pLoc.setText(words.get(Const.LOCATION) + " "
				+ pWrapper.getLocationName(lngIndex));
		pProd.setText(words.get(Const.PRODUCER));
		producerUrlChecker(pWrapper.getProducerURL(lngIndex));
		orderButton.setText(words.get(Const.PRE_ORDER));
		backButton.setText(words.get(Const.BACK));
	}

	public void preOrderDlg(final View v) {
		productId = String.valueOf(pWrapper.getId());
		AlertDialog.Builder builder;
		layout = getInflatedView(R.layout.dialog_pre_order,
				(ViewGroup) findViewById(R.id.parent_layout));

		preDialogTitle = (TextView) layout.findViewById(R.id.orderDialogTitle);
		preDialogTitle.setText(words.get(Const.ORDER_TITLE));
		TextView buyerName = (TextView) layout.findViewById(R.id.buyerName);
		buyerName.setText(words.get(Const.BUYER_NAME));
		buyerEdit = (EditText) layout.findViewById(R.id.buyerNameEdit);
		buyerEdit.setHint(words.get(Const.BUYER_NAME_HINT));
		TextView contactNumber = (TextView) layout
				.findViewById(R.id.contactNumber);
		contactNumber.setText(words.get(Const.CONTACT_TEXT_TITLE));
		contactEdit = (EditText) layout.findViewById(R.id.contact);
		contactEdit.setHint(words.get(Const.CONTACT_HINT));
		TextView msgTitle = (TextView) layout.findViewById(R.id.messageTitle);
		msgTitle.setText(words.get(Const.MESSAGE_TEXT_TITLE));
		messageEdit = (EditText) layout.findViewById(R.id.message);
		messageEdit.setHint(words.get(Const.MESSAGE_HINT));
		prefDelivery = (CheckBox) layout.findViewById(R.id.preferedDelivery);
		prefDelivery.setText(words.get(Const.PREFER_DELIVERY));
		charsCountView = (TextView) layout.findViewById(R.id.charsRemaining);
		String txt = "(" + Const.CHARS_COUNT + " "
				+ words.get(Const.CHARS_REMAINING) + ")";
		charsCountView.setText(txt);
		Button submitBtn = (Button) layout.findViewById(R.id.submitBtn);
		submitBtn.setText(words.get(Const.SUBMIT_BUTTON));
		Button closeBtn = (Button) layout.findViewById(R.id.closeBtn);
		closeBtn.setText(words.get(Const.CLOSE_BUTTON));

		setEditTextsListener();
		builder = new AlertDialog.Builder(this);
		builder.setView(layout);
		orderDialog = builder.create();
		orderDialog.show();
	}

	public void submitOrder(View v) {

		buyerName = getEditValue(buyerEdit);
		contact = getEditValue(contactEdit);
		message = getEditValue(messageEdit);
		if (isInfoCompleted()) {
			AndroidUtil.showToast(this,
					"You did not complete required information", 0);
		} else {
			final PreOrderBean orderBean = new PreOrderBean();
			orderBean.setCustomerName(buyerName);
			orderBean.setContact(contact);
			orderBean.setMessage(message);
			orderBean.setProductId(productId);
			orderBean.setDeliver(isDeliver());
			orderButton.setEnabled(true);
			new Thread() {
				@Override
				public void run() {
					try {
						DataManager.sendPreOrder(orderBean);
					} catch (Exception e) {
						Log.e(TAG, e.toString());
					}
				}
			}.start();
			orderDialog.cancel();
			thankYouDialog(v);
		}
	}

	private void thankYouDialog(final View view) {

		View v = getInflatedView(R.layout.dialog_thank_you,
				(ViewGroup) findViewById(R.id.thanksLayout));

		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		TextView tV = (TextView) v.findViewById(R.id.thanksText);
		builder.setTitle("");
		tV.setText(words.get(Const.THANK_YOU));
		builder.setPositiveButton("Close", new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.cancel();
				backToProducts(view);
			}
		});

		builder.setView(v);
		thankYouDialog = builder.create();
		thankYouDialog.show();
	}

	public void closeOrder(View view) {
		orderButton.setEnabled(true);
		orderDialog.cancel();
	}

	public void backToProducts(View v) {
		Intent i = new Intent(Const.PRODUCTS_ACTIVITY);
		startActivity(i);
	}

	private boolean isInfoCompleted() {
		return Utils.isEmpty(contact) || Utils.isEmpty(message)
				|| Utils.isEmpty(buyerName);
	}

	private boolean isDeliver() {
		isDeliver = prefDelivery.isChecked();
		return isDeliver;
	}

	private void setEditTextsListener() {
		buyerEdit.addTextChangedListener(this);
		contactEdit.addTextChangedListener(this);
		messageEdit.addTextChangedListener(this);
	}

	private int getMessageLength(EditText editText) {
		return editText.getText().length();
	}

	private List<String> getEditTextValues() {
		List<String> editValues = new ArrayList<String>();
		String buyer = getEditValue(buyerEdit);
		String contact = getEditValue(contactEdit);
		String message = getEditValue(messageEdit);

		editValues.add(buyer);
		editValues.add(contact);
		editValues.add(message);
		return editValues;
	}

	private String getEditValue(EditText editText) {
		return editText.getText().toString().trim();
	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count,
			int after) {
	}

	@Override
	public void afterTextChanged(Editable s) {

		if (Utils.isEmpty(getEditTextValues())) {
			orderButton.setEnabled(false);
		} else {
			orderButton.setEnabled(true);
		}

		if (messageEdit.hasFocus()) {
			charsCountView.setText("("
					+ String.valueOf(Const.CHARS_COUNT
							- getMessageLength(messageEdit)) + " "
					+ words.get(Const.CHARS_REMAINING) + ")");
		}
	}

	private void producerUrlChecker(String url) {
		pProcucerName.setText(pWrapper.getProducerName(lngIndex));
		if (!Utils.isEmpty(url)) {
			pProcucerName.setOnClickListener(this);
			line.setVisibility(View.VISIBLE);
		}
	}

	@Override
	public void onClick(View v) {
		Intent intent = new Intent(ProductInfoActivity.this,
				ProducerWebActivity.class);
		Bundle b = new Bundle();
		b.putString("url", pWrapper.getProducerURL(lngIndex));
		intent.putExtras(b);
		startActivity(intent);
	}

}
