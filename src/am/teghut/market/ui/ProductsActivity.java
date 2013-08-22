package am.teghut.market.ui;

import java.util.ArrayList;
import java.util.List;

import am.teghut.market.R;
import am.teghut.market.adapter.ProductsListAdapter;
import am.teghut.market.adapter.ProductsPagerAdapter;
import am.teghut.market.api.dto.DataHolder;
import am.teghut.market.api.dto.PreOrderBean;
import am.teghut.market.api.dto.ProductWrapper;
import am.teghut.market.component.CustomButton;
import am.teghut.market.constant.Const;
import am.teghut.market.layout.FooterLayout;
import am.teghut.market.listener.PageChangeListener;
import am.teghut.market.util.AndroidUtil;
import am.teghut.market.util.DataManager;
import am.teghut.market.util.Utils;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.MarginLayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class ProductsActivity extends AppRootActivity implements TextWatcher {

	private static final String TAG = "ProductsActivity: ";
	public static int currentScreen;
	public static boolean isFirstRun = true;

	private CustomButton orderButton;
	private List<ProductWrapper> products;
	private List<TextView> pagingTextViews;
	private List<View> screens;

	private AlertDialog orderDialog;
	private AlertDialog thankYouDialog;

	private View layout;
	private FooterLayout footerLayout;
	private ViewPager pager;

	private RelativeLayout footer;
	private LinearLayout listContainer;
	private LinearLayout next;
	private LinearLayout back;

	private TextView charsCountView;
	private TextView firstPoint;
	private TextView afterPoint;
	private TextView preDialogTitle;
	private TextView nextText;
	private TextView backText;

	private EditText buyerEdit;
	private EditText contactEdit;
	private EditText messageEdit;
	private CheckBox prefDelivery;

	private String buyerName;
	private String contact;
	private String message;
	private String productId;
	private boolean isDeliver;

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_products);
		try {
			products = DataHolder.getWrappedProducts();
			initUi();
		} catch (Exception e) {
			Log.e(TAG + "onCreate() ", e.toString());
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		changeTexts();
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		closeDialog(orderDialog);
		closeDialog(thankYouDialog);
		super.onSaveInstanceState(outState);
	}

	private void initUi() {
		listContainer = (LinearLayout) findViewById(R.id.lt);
		pager = new ViewPager(this);
		pager.setAdapter(new ProductsPagerAdapter(createScreens()));
		pager.setCurrentItem(currentScreen);
		addListFooter();
		setListMargin(pagingTextViews);
		PageChangeListener pageListener = new PageChangeListener(footerLayout);
		pager.setOnPageChangeListener(pageListener);
		listContainer.addView(pager);
		if (isFirstRun) {
			pagingTextViews.get(0).setClickable(false);
			isFirstRun = false;
		}
	}

	private void setListMargin(List<TextView> textViews) {
		if (textViews.size() > 1) {
			setListContainerMargin(displayWidth / 10);
		} else {
			setListContainerMargin(0);
			footer.setVisibility(View.GONE);
		}
	}

	private void setEditTextsListener() {
		buyerEdit.addTextChangedListener(this);
		contactEdit.addTextChangedListener(this);
		messageEdit.addTextChangedListener(this);
	}

	private int getMessageLength(EditText editText) {
		return editText.getText().length();
	}

	private String getEditValue(EditText editText) {
		return editText.getText().toString().trim();
	}

	public void closeOrder(View view) {
		orderButton.setEnabled(true);
		closeDialog(orderDialog);
	}

	public void submitOrder(View v) {

		buyerName = getEditValue(buyerEdit);
		contact = getEditValue(contactEdit);
		message = getEditValue(messageEdit);
		if (isInfoCompleted()) {
			AndroidUtil.showToast(this, words.get(Const.REQUIRED_FIELDS), 1);
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
						closeDialog(orderDialog);
						thankYouDialog();
					} catch (Exception e) {
						AndroidUtil.showToast(ProductsActivity.this,
								words.get(Const.NO_CONNECTION), 1);
						closeDialog(orderDialog);
						Log.e(TAG, e.toString());
					}
				}
			}.start();
		}
	}

	private void thankYouDialog() {

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
			}
		});

		builder.setView(v);
		thankYouDialog = builder.create();
		thankYouDialog.show();
	}

	private boolean isInfoCompleted() {
		return Utils.isEmpty(contact) || Utils.isEmpty(message)
				|| Utils.isEmpty(buyerName);
	}

	public void orderProduct(View view) {
		orderButton = (CustomButton) view;
		orderButton.setEnabled(false);
		preOrderDialog(view);
	}

	public void preOrderDialog(final View v) {
		productId = ((CustomButton) v).getProductId();
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

	private List<ProductWrapper> getNextProductList(int position) {
		int itemsCount = Const.ITEMS_PER_PAGE;
		List<ProductWrapper> tempList = new ArrayList<ProductWrapper>();
		if (position * itemsCount + itemsCount < products.size()) {
			for (int i = position * itemsCount; i < position * itemsCount
					+ itemsCount; i++) {
				tempList.add(products.get(i));
			}
		} else {
			for (int i = position * itemsCount; i < products.size(); i++) {
				tempList.add(products.get(i));
			}
		}
		return tempList;
	}

	private void addPagingListeners(List<TextView> textViews) {
		for (int i = 0; i < textViews.size(); i++) {
			TextView view = textViews.get(i);
			view.setOnClickListener(new PageOnClickListener(i));
		}
	}

	private void initListFooter() {
		footerLayout = new FooterLayout(products.size(), this);
		pagingTextViews = footerLayout.getTextVierws();
		next = footerLayout.getNextButton();
		back = footerLayout.getBackButton();
		nextText = footerLayout.getNextText();
		backText = footerLayout.getBackText();
		firstPoint = footerLayout.getFirstPoint();
		afterPoint = footerLayout.getAfterPoint();
		footerLayout.drawBorder(currentScreen);
		footer = (RelativeLayout) findViewById(R.id.footer_id);
		footer.addView(footerLayout.getContainer());
	}

	private void addListFooter() {
		initListFooter();
		addPagingListeners(pagingTextViews);
		setFooterLayoutListeners();
	}

	private void setFooterLayoutListeners() {
		next.setOnClickListener(new FooterLayoutsListener());
		back.setOnClickListener(new FooterLayoutsListener());
		firstPoint.setOnClickListener(new FooterLayoutsListener());
		afterPoint.setOnClickListener(new FooterLayoutsListener());
	}

	private void setListContainerMargin(int top) {
		MarginLayoutParams params = (ViewGroup.MarginLayoutParams) listContainer
				.getLayoutParams();
		params.setMargins(0, 0, 0, top);
		listContainer.setLayoutParams(params);
	}

	public void goToProductInfoPage(View view, int id) {
		ProductWrapper pWrapper = getProductById(id);

		if (pWrapper != null) {
			Intent i = new Intent(Const.PRODUCT_INFO_ACTIVITY);
			i.putExtra(Const.KEY_PRODUCT_WRAPPER, pWrapper);
			startActivity(i);
		}
	}

	private ProductWrapper getProductById(int id) {
		return DataHolder.getWrappedProducts().get(id);
	}

	private List<View> createScreens() {
		screens = new ArrayList<View>();
		int productCount = listSizeController(products.size());

		for (int i = 0; i < productCount; i++) {
			ListView list = new ListView(this);
			list.setAdapter(new ProductsListAdapter(this, getNextProductList(i)));
			list.setDividerHeight(0);
			list.setBackgroundColor(res.getColor(R.color.list_bg));
			list.setOnItemClickListener(listClickListener);
			screens.add(list);
		}
		return screens;
	}

	private int listSizeController(int itemCount) {
		if (itemCount % Const.ITEMS_PER_PAGE == 0) {
			return itemCount / Const.ITEMS_PER_PAGE;
		} else {
			return products.size() / Const.ITEMS_PER_PAGE + 1;
		}
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

	private boolean isDeliver() {
		isDeliver = prefDelivery.isChecked();
		return isDeliver;
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

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			finishActivitiesByExit();
		}
		return super.onKeyDown(keyCode, event);
	}

	private OnItemClickListener listClickListener = new OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			int productId = 10 * currentScreen + position;
			goToProductInfoPage(view, productId);
		}
	};

	public class PageOnClickListener implements View.OnClickListener {

		private int pageIndex;

		public PageOnClickListener(int pageIndex) {
			this.pageIndex = pageIndex;
		}

		@Override
		public void onClick(View v) {
			pagingTextViews.get(currentScreen).setClickable(true);
			pager.setCurrentItem(pageIndex);
			footerLayout.drawBorder(pageIndex);
			footerLayout.repaintPages(pageIndex);
			currentScreen = pageIndex;
			pagingTextViews.get(currentScreen).setClickable(false);
		}
	}

	private class FooterLayoutsListener implements View.OnClickListener {
		@Override
		public void onClick(View v) {
			if (v == next) {
				if (currentScreen < pagingTextViews.size() - 1) {
					pagingTextViews.get(currentScreen).setClickable(true);
					currentScreen = currentScreen + 1;
					footerLayout.drawBorder(currentScreen);
					pager.setCurrentItem(currentScreen);
					pagingTextViews.get(currentScreen).setClickable(false);
				}
			} else if (v == back) {
				if (currentScreen >= 1) {
					pagingTextViews.get(currentScreen).setClickable(true);
					currentScreen = currentScreen - 1;
					footerLayout.drawBorder(currentScreen);
					pager.setCurrentItem(currentScreen);
					pagingTextViews.get(currentScreen).setClickable(false);
				}
			} else if (v == firstPoint) {
				if (currentScreen >= 2
						&& currentScreen < pagingTextViews.size() - 1) {
					pagingTextViews.get(currentScreen).setClickable(true);
					currentScreen = currentScreen - 2;
					footerLayout.drawBorder(currentScreen);
					pager.setCurrentItem(currentScreen);
					pagingTextViews.get(currentScreen).setClickable(false);
				} else if (currentScreen >= 2) {
					pagingTextViews.get(currentScreen).setClickable(true);
					currentScreen = currentScreen - 4;
					footerLayout.drawBorder(currentScreen);
					pager.setCurrentItem(currentScreen);
					pagingTextViews.get(currentScreen).setClickable(false);
				}
			} else if (v == afterPoint) {
				if (currentScreen < pagingTextViews.size() - 2
						&& currentScreen > 0) {
					pagingTextViews.get(currentScreen).setClickable(true);
					currentScreen = currentScreen + 2;
					footerLayout.drawBorder(currentScreen);
					pager.setCurrentItem(currentScreen);
					pagingTextViews.get(currentScreen).setClickable(false);
				} else if (currentScreen < pagingTextViews.size() - 2) {
					pagingTextViews.get(currentScreen).setClickable(true);
					currentScreen = currentScreen + 4;
					footerLayout.drawBorder(currentScreen);
					pager.setCurrentItem(currentScreen);
					pagingTextViews.get(currentScreen).setClickable(false);
				}
			}
		}
	}

	private void changeTexts() {
		nextText.setText(words.get(Const.NEXT));
		backText.setText(words.get(Const.BACK));
		for (int i = 0; i < screens.size(); i++) {
			ListView list = (ListView) screens.get(i);
			ProductsListAdapter adapter = (ProductsListAdapter) list
					.getAdapter();
			adapter.notifyDataSetChanged();
		}
	}
}
