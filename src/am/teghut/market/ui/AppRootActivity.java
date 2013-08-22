package am.teghut.market.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import am.teghut.market.R;
import am.teghut.market.constant.Const;
import am.teghut.market.util.LanguageManager;
import am.teghut.market.util.Utils;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * application root <code>Activity</code> for implementing common methods here
 */
public class AppRootActivity extends Activity {

	private static final String TAG = "AppRootActivity: ";
	public static List<Activity> activitiesList = new ArrayList<Activity>();
	public static HashMap<String, String> words = new HashMap<String, String>();
	protected static Resources res;

	protected LayoutInflater inflater;
	protected SharedPreferences sharedPrefs;
	protected Window window;

	private TextView linkText;
	private TextView createdBy;

	protected int displayWidth;
	protected int displayHeight;
	private boolean isStarted;

	private View viewGroup;
	private LinearLayout container;

	protected MenuItem settings;
	protected MenuItem exit;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		try {
			viewGroup = getInflatedView(R.layout.activity_about_us, null);
			LanguageManager lManager = new LanguageManager(this, getLanguage());
			words = lManager.getWords();
			createdBy = (TextView) viewGroup.findViewById(R.id.created_by);
			linkText = (TextView) viewGroup.findViewById(R.id.group);
			setFooterText();
			setLinkTextListener();
			requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
			res = getResources();
			setDisplayWidthHeight();
			addActivities(this);
			window = getWindow();
			sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
		} catch (Exception e) {
			Log.e(TAG + "onCreate() ", e.toString());
		}

	}

	@Override
	public void setContentView(int layoutResID) {
		View view = getInflatedView(layoutResID, null);
		container = (LinearLayout) viewGroup.findViewById(R.id.container);
		container.addView(view);
		super.setContentView(viewGroup);
	}

	@Override
	protected void onResume() {
		if (!isStarted) {
			window.setFeatureInt(Window.FEATURE_CUSTOM_TITLE,
					R.layout.activity_title);
			isStarted = true;
		}
		LanguageManager langManager = new LanguageManager(this, getLanguage());
		words = langManager.getWords();
		setFooterText();
		if (settings != null && exit != null) {
			setMenusText();
		}
		super.onResume();
	}

	protected void setFooterText() {
		createdBy.setText(words.get(Const.POWERED_BY));
		linkText.setText(words.get(Const.MARKET_COMMUNITY));
	}

	protected View getInflatedView(int resource, ViewGroup viewGroup) {
		inflater = (LayoutInflater) this
				.getSystemService(LAYOUT_INFLATER_SERVICE);
		return inflater.inflate(resource, viewGroup);
	}

	protected boolean isLangDefined() {
		String str = sharedPrefs.getString(Const.KEY_LANG_LIST_PREFERENCE, "");
		if (Utils.isEmpty(str)) {
			return false;
		}
		return true;
	}

	public int getLanguage() {
		if (sharedPrefs != null) {
			String str = sharedPrefs.getString(Const.KEY_LANG_LIST_PREFERENCE,
					"");
			String armLang = res.getStringArray(R.array.languageEntries)[0];
			String enLang = res.getStringArray(R.array.languageEntries)[1];
			String rusLang = res.getStringArray(R.array.languageEntries)[2];

			if (str.equals(armLang)) {
				return 0;
			} else if (str.equals(enLang)) {
				return 1;
			} else if (str.equals(rusLang)) {
				return 2;
			}
		}
		return -1;
	}

	protected void closeDialog(Dialog d) {
		if (d != null && d.isShowing()) {
			d.cancel();
		}
	}

	protected void finishActivitiesByExit() {
		for (int i = 0; i < activitiesList.size(); i++) {
			activitiesList.get(i).finish();
		}
		ProductsActivity.currentScreen = 0;
		ProductsActivity.isFirstRun = true;
	}

	public void goToSettings() {
		Intent intent = new Intent();
		intent.setClass(this, SettingsActivity.class);
		startActivity(intent);
	}

	private void addActivities(Activity activity) {
		activitiesList.add(activity);
	}

	private void setDisplayWidthHeight() {
		Display display = getWindowManager().getDefaultDisplay();
		displayWidth = display.getWidth();
		displayHeight = display.getHeight();
	}

	public int getDisplayWidth() {
		return displayWidth;
	}

	public int getDisplayHeight() {
		return displayHeight;
	}

	public static final Resources getAppResources() {
		return res;
	}

	protected void setMenusText() {
		settings.setTitle(words.get(Const.SETTINGS));
		exit.setTitle(words.get(Const.EXIT));
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.root_options_menu, menu);
		settings = menu.findItem(R.id.settingsMenu);
		exit = menu.findItem(R.id.exitMenu);
		setMenusText();
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.exitMenu:
			finishActivitiesByExit();
			return true;
		case R.id.settingsMenu:
			goToSettings();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	private void setLinkTextListener() {
		linkText.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				startActivity(new Intent(AppRootActivity.this,
						AboutUsActivity.class));
			}
		});
	}
}
