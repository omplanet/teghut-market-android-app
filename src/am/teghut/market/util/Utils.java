package am.teghut.market.util;

import java.util.List;

public class Utils {

	public static final boolean isEmpty(String str) {
		return str == null || str.trim().length() == 0;
	}

	public static final boolean isEmpty(List<String> list) {
		for (String str : list) {
			if (isEmpty(str)) {
				return true;
			}
		}
		return false;
	}
}
