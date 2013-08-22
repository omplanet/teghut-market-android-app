package am.teghut.market.util;

import java.util.HashMap;

import am.teghut.market.R;
import am.teghut.market.constant.Const;
import android.content.Context;
import android.content.res.Resources;

public class LanguageManager {

	private HashMap<String, String> words;
	private Resources res;

	public LanguageManager(Context context, int index) {
		words = new HashMap<String, String>();
		this.res = context.getResources();
		setWords(index);
	}

	public void setWords(int langIndex) {
		String[] strings = null;
		switch (langIndex) {
		case 0:
			strings = res.getStringArray(R.array.am);
			break;
		case 1:
			strings = res.getStringArray(R.array.en);
			break;
		case 2:
			strings = res.getStringArray(R.array.ru);
			break;
		default:
			strings = res.getStringArray(R.array.am);
			break;
		}
		words.put(Const.SETTINGS, strings[0]);
		words.put(Const.EXIT, strings[1]);
		words.put(Const.APP_TITLE, strings[2]);
		words.put(Const.ORDER_TITLE, strings[3]);
		words.put(Const.CONTACT_TEXT_TITLE, strings[4]);
		words.put(Const.MESSAGE_TEXT_TITLE, strings[5]);
		words.put(Const.CHARS_REMAINING, strings[6]);
		words.put(Const.PREFER_DELIVERY, strings[7]);
		words.put(Const.SUBMIT_BUTTON, strings[8]);
		words.put(Const.CLOSE_BUTTON, strings[9]);
		words.put(Const.CONTACT_HINT, strings[10]);
		words.put(Const.MESSAGE_HINT, strings[11]);
		words.put(Const.CURRENCY, strings[12]);
		words.put(Const.PRE_ORDER, strings[13]);
		words.put(Const.LANG_PRE_TITLE, strings[14]);
		words.put(Const.WELCOME_TEXT, strings[15]);
		words.put(Const.ABOUT_TEGHUT, strings[16]);
		words.put(Const.BACK, strings[17]);
		words.put(Const.NEXT, strings[18]);
		words.put(Const.AM, strings[19]);
		words.put(Const.EN, strings[20]);
		words.put(Const.RU, strings[21]);
		words.put(Const.SET_DEFAULT_LANG, strings[22]);
		words.put(Const.MAKE_LENG_DEFAULT, strings[23]);
		words.put(Const.POWERED_BY, strings[24]);
		words.put(Const.MARKET_COMMUNITY, strings[25]);
		words.put(Const.THANK_YOU, strings[26]);
		words.put(Const.DOWNLOADED, strings[27]);
		words.put(Const.BUYER_NAME, strings[28]);
		words.put(Const.BUYER_NAME_HINT, strings[29]);
		words.put(Const.DESCRIPTION, strings[30]);
		words.put(Const.LOCATION, strings[31]);
		words.put(Const.PRODUCER, strings[32]);
		words.put(Const.DELIVERY, strings[33]);
		words.put(Const.PRICE, strings[34]);
		words.put(Const.IN_STOCK, strings[35]);
		words.put(Const.YES, strings[36]);
		words.put(Const.NO, strings[37]);
		words.put(Const.NO_CONNECTION, strings[38]);
		words.put(Const.NOT_IN_STOCK, strings[39]);
		words.put(Const.REQUIRED_FIELDS, strings[40]);
	}

	public HashMap<String, String> getWords() {
		return words;
	}
}
