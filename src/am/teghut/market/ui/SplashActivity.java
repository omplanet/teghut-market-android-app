package am.teghut.market.ui;

import java.util.ArrayList;
import java.util.List;

import am.teghut.market.R;
import am.teghut.market.api.dto.DataHolder;
import am.teghut.market.api.dto.ProductBean;
import am.teghut.market.api.dto.ProductWrapper;
import am.teghut.market.component.CustomDialog;
import am.teghut.market.component.CustomProgressBar;
import am.teghut.market.constant.Const;
import am.teghut.market.threads.AnimationThread;
import am.teghut.market.threads.DataProviderThread;
import am.teghut.market.util.AndroidUtil;
import am.teghut.market.util.DataManager;
import am.teghut.market.util.LanguageManager;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences.Editor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

@SuppressLint("HandlerLeak")
public class SplashActivity extends AppRootActivity {

	private static final String TAG = "SplashActivity: ";
	private Context context;

	private DataProviderThread dataProviderThread;
	private AnimationThread animThread;

	private CustomDialog languageDialog;
	private AlertDialog connectionDialog;

	private RadioGroup langGroup;
	private RadioButton armLangRadio;
	private RadioButton enLangRadio;
	private RadioButton rusLangRadio;

	private ImageView animImage;
	private TextView welcomeText;
	private TextView aboutTeghut;
	private Button okButton;

	private List<ProductBean> products;
	private LanguageManager langManager;

	private int selected = -1;
	private int touchState;
	private int listSize;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);
		initUi();
		context = this;
		animImage = (ImageView) findViewById(R.id.animatedImage);
		dataProviderThread = new DataProviderThread(this, handler);
		if (!isLangDefined()) {
			showLangDialog();
		} else {
			selected = getLanguage();
		}

		if (selected != -1) {
			dataProviderThread.start();
		}
	}

	@Override
	protected void onResume() {
		LanguageManager langManager = new LanguageManager(this, getLanguage());
		words = langManager.getWords();
		setWelcomeText();
		if (settings != null && exit != null) {
			setMenusText();
		}
		super.onResume();
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		closeDialog(languageDialog);
		closeDialog(connectionDialog);
		super.onSaveInstanceState(outState);
	}

	private void initUi() {
		welcomeText = (TextView) findViewById(R.id.splashTextView1);
		aboutTeghut = (TextView) findViewById(R.id.splashTextView2);
		setWelcomeText();
	}

	private void showLangDialog() {
		languageDialog = new CustomDialog(this);
		languageDialog.setContentView(R.layout.dialog_language);

		langGroup = (RadioGroup) languageDialog
				.findViewById(R.id.langRadioGroup);
		armLangRadio = (RadioButton) languageDialog
				.findViewById(R.id.armLangRadioBtn);
		enLangRadio = (RadioButton) languageDialog
				.findViewById(R.id.enLangRadioBtn);
		rusLangRadio = (RadioButton) languageDialog
				.findViewById(R.id.rusLangRadioBtn);
		okButton = (Button) languageDialog.findViewById(R.id.langDialogOkBtn);

		setRadioButtonsListener();
		okButton.setOnClickListener(okButtonListener);

		languageDialog.setTitle(words.get(Const.SET_DEFAULT_LANG));
		languageDialog.show();
	}

	private void setRadioButtonsListener() {
		armLangRadio.setOnClickListener(radioBtnListener);
		enLangRadio.setOnClickListener(radioBtnListener);
		rusLangRadio.setOnClickListener(radioBtnListener);
	}

	private String getDefaultLanguage(int selected) {
		switch (selected) {
		case R.id.armLangRadioBtn:
			return res.getStringArray(R.array.languageEntries)[0];
		case R.id.enLangRadioBtn:
			return res.getStringArray(R.array.languageEntries)[1];
		case R.id.rusLangRadioBtn:
			return res.getStringArray(R.array.languageEntries)[2];
		default:
			return "";
		}
	}

	private void setDefaultLanguage(View view) {
		Editor editor = sharedPrefs.edit();
		selected = langGroup.getCheckedRadioButtonId();
		String language = getDefaultLanguage(selected);
		editor.putString(Const.KEY_LANG_LIST_PREFERENCE, language);
		editor.commit();
		languageDialog.cancel();

		LanguageManager lM = new LanguageManager(this, selected);
		AppRootActivity.words = lM.getWords();
		dataProviderThread.start();
	}

	private void enableOkButton(View view) {
		if (!okButton.isEnabled()) {
			okButton.setEnabled(true);
		}
	}

	private void startCreator() {
		DataManager.retrievingState = 0;
		ProductBean[] pBeans = new ProductBean[products.size()];
		products.toArray(pBeans);
		new WrapperCreator().execute(pBeans);
	}

	public void showAnimation() {
		Animation animation = AnimationUtils.loadAnimation(this,
				R.anim.animation_image);
		animation.setRepeatMode(Animation.REVERSE);
		animation.setRepeatCount(Animation.INFINITE);
		animImage.setVisibility(View.VISIBLE);
		animImage.startAnimation(animation);
	}

	private void initAnimImage(int lngIndex) {
		animImage = (ImageView) findViewById(R.id.animatedImage);
		switch (lngIndex) {
		case 0:
			animImage.setBackgroundResource(R.drawable.click_here_hy);
			break;
		case 1:
			animImage.setBackgroundResource(R.drawable.click_here_en);
			break;
		case 2:
			animImage.setBackgroundResource(R.drawable.click_here_ru);
			break;
		default:
			animImage.setBackgroundResource(R.drawable.click_here_hy);
			break;
		}
	}

	private void goToProductsActivity() {
		Intent i = new Intent(Const.PRODUCTS_ACTIVITY);
		startActivity(i);
		this.finish();
	}

	private boolean isSelected() {
		return selected != -1;
	}

	public int getTouchState() {
		return touchState;
	}

	public void go(View view) {
		if (isSelected() && touchState == 0) {
			goToProductsActivity();
			touchState = 1;
		}
	}

	private void showConnectionDialog() {
		View v = getInflatedView(R.layout.dialog_no_connection,
				(ViewGroup) findViewById(R.id.connectionLayout));

		Builder builder = new Builder(this);

		TextView t = (TextView) v.findViewById(R.id.noConnectionText);
		t.setText(words.get(Const.NO_CONNECTION));

		builder.setPositiveButton("OK", connDlgListener);

		builder.setView(v);

		connectionDialog = builder.create();
		connectionDialog.show();
	}

	Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			int total = msg.arg1;
			try {
				if (total == 1) {
					List<ProductBean> beans = dataProviderThread.getProducts();
					if (beans != null && beans.size() > 0) {
						products = beans;
						listSize = beans.size();

						if (isSelected()) {
							startCreator();
						}
					}
				} else {
					showConnectionDialog();
				}
			} catch (Exception e) {
				Log.e(TAG, "handleMessage() " + e.getMessage());
			}
		}
	};

	Handler animHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			touchState = msg.arg1;
			if (touchState == 1) {
				animThread.interrupt();
			} else {
				initAnimImage(getLanguage());
				showAnimation();
			}
		}
	};

	private void startAnimThread() {
		animThread = new AnimationThread(context, animHandler);
		animThread.start();
	}

	public boolean hasConnection() {
		ConnectivityManager cm = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
		NetworkInfo netInfo = cm.getActiveNetworkInfo();
		return netInfo != null && netInfo.isConnected();
	}

	private void setWelcomeText() {
		welcomeText.setText(words.get(Const.WELCOME_TEXT));
		aboutTeghut.setText(words.get(Const.ABOUT_TEGHUT));
	}

	private OnClickListener radioBtnListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			enableOkButton(v);
		}
	};

	private OnClickListener okButtonListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			setDefaultLanguage(v);
			langManager = new LanguageManager(SplashActivity.this,
					getLanguage());
			words = langManager.getWords();
			setWelcomeText();
			setFooterText();
		}
	};

	private DialogInterface.OnClickListener connDlgListener = new DialogInterface.OnClickListener() {

		@Override
		public void onClick(DialogInterface dialog, int which) {
			connectionDialog.cancel();
		}
	};

	private class WrapperCreator extends AsyncTask<ProductBean, Integer, Void> {

		private CustomProgressBar pBar;
		private int step;
		private int toIndex;
		private StringBuilder sBuilder;

		@Override
		protected Void doInBackground(ProductBean... params) {
			DataHolder.setWrappedProducts(getWrapped(params, toIndex));
			return null;
		}

		@Override
		protected void onPreExecute() {
			initProgressBar();
		}

		@Override
		protected void onPostExecute(Void result) {
			if (pBar.getVisibility() == View.VISIBLE) {
				pBar.setVisibility(View.GONE);
			}
			startAnimThread();
		}

		@Override
		protected void onProgressUpdate(Integer... values) {
			step++;
			pBar.setProgress(step);
			sBuilder.append(getPercent(step));
			sBuilder.append(words.get(Const.DOWNLOADED));
			pBar.setText(sBuilder.toString());
			sBuilder.delete(0, sBuilder.length());
		}

		private int getPercent(int i) {
			return i * 100 / toIndex;
		}

		private void initProgressBar() {
			sBuilder = new StringBuilder();
			toIndex = listSize / 1; // TODO control downloading products size //
									// during test
			pBar = (CustomProgressBar) findViewById(R.id.downloadProgress);
			pBar.setVisibility(View.VISIBLE);
			pBar.setMax(toIndex);
		}

		private List<ProductWrapper> getWrapped(ProductBean[] pBeans,
				int toIndex) {
			List<ProductWrapper> wrapped = new ArrayList<ProductWrapper>();
			if (pBeans != null && pBeans.length > 0) {
				listSize = pBeans.length;
				Integer[] values = new Integer[listSize];
				for (int i = 0; i < pBeans.length; i++) {
					if (i < toIndex) {
						ProductBean pBean = pBeans[i];
						ProductWrapper pWrapper = AndroidUtil.wrap(pBean);
						wrapped.add(pWrapper);
						values[i] = Integer.valueOf(i);
						publishProgress(values);
					}
				}
			}
			return wrapped;
		}
	}
}
