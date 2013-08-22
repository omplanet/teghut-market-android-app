package am.teghut.market.ui;

import am.teghut.market.R;
import am.teghut.market.constant.Const;
import am.teghut.market.util.LanguageManager;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.content.res.Resources;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceCategory;
import android.preference.PreferenceScreen;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

public class SettingsActivity extends PreferenceActivity implements
		OnSharedPreferenceChangeListener {

	private static final String TAG = "SettingsActivity: ";
	private Resources res;

	private LanguageManager langManager;
	private PreferenceCategory prefCategory;
	private ListPreference langListPreference;
	private Spannable summary;
	private MenuItem exit;
	private int color;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.preference_screen);
		setContentView(R.layout.activity_settings);
		res = getResources();
		initLangListPreference();

	}

	@Override
	protected void onResume() {
		super.onResume();
		setPrefsSummaries(getPreferenceScreen());
		getPreferenceScreen().getSharedPreferences()
				.registerOnSharedPreferenceChangeListener(this);

	}

	public void finishActivitiesByExit() {
		for (int i = 0; i < AppRootActivity.activitiesList.size(); i++) {
			AppRootActivity.activitiesList.get(i).finish();
		}
		this.finish();
	}

	private void initLangListPreference() {
		prefCategory = (PreferenceCategory) findPreference(Const.KEY_PREFERENCE_CATEGORY);
		langListPreference = (ListPreference) findPreference(Const.KEY_LANG_LIST_PREFERENCE);
	}

	@Override
	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
			String key) {
		Preference preference = findPreference(key);
		if (isListPreference(preference)) {
			setLangListSummary(preference);
			if (exit != null) {
				exit.setTitle(AppRootActivity.words.get(Const.EXIT));
			}
		}
	}

	private void setPrefsSummaries(PreferenceScreen screen) {
		for (int i = 0; i < screen.getPreferenceCount(); i++) {
			Preference preference = screen.getPreference(i);
			if (isListPreference(preference)) {
				setLangListSummary(preference);
			}
		}
	}

	private boolean isListPreference(Preference preference) {
		return preference instanceof ListPreference;
	}

	private void setTextColor(Spannable summary) {
		if (summary != null && summary.length() > 0) {
			color = res.getColor(R.color.white);
			summary.setSpan(new ForegroundColorSpan(color), 0,
					summary.length(), 0);
		}
	}

	private void setLangListSummary(Preference preference) {
		String preferenceKey = preference.getKey();
		langListPreference = (ListPreference) preference;
		try {
			if (preferenceKey.equals(Const.KEY_LANG_LIST_PREFERENCE)) {
				CharSequence entry = getListEntry(langListPreference);
				summary = new SpannableString(entry);
				setTextColor(summary);
				langListPreference.setSummary(summary);
				int index = langListPreference.findIndexOfValue((String) entry);
				if (index != -1) {
					langListPreference.setValueIndex(index);
				}
				langManager = new LanguageManager(this, index);
				AppRootActivity.words = langManager.getWords();
				prefCategory.setSummary(AppRootActivity.words
						.get(Const.LANG_PRE_TITLE));
			}
		} catch (Exception e) {
			Log.e(TAG + "initListSummary() ", e.toString());
		}
	}

	private CharSequence getListEntry(ListPreference listPreference) {
		return listPreference.getEntry() == null ? "" : listPreference
				.getEntry();
	}

	public CharSequence getLangListSummary() {
		return langListPreference.getSummary();
	}

	private void finishCurrent() {
		getPreferenceScreen().getSharedPreferences()
				.unregisterOnSharedPreferenceChangeListener(this);
		this.finish();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			finishCurrent();
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.preference_options_menu, menu);
		exit = menu.findItem(R.id.exitMenu);
		exit.setTitle(AppRootActivity.words.get(Const.EXIT));
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.exitMenu:
			finishActivitiesByExit();
			finishCurrent();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

}
