package am.teghut.market.api.dto;

import java.util.ArrayList;
import java.util.List;

public class DataHolder {

	private static List<ProductWrapper> wrappedProducts = new ArrayList<ProductWrapper>();

	public static List<ProductWrapper> getWrappedProducts() {
		return wrappedProducts;
	}

	public static void setWrappedProducts(List<ProductWrapper> wrappedProducts) {
		DataHolder.wrappedProducts = wrappedProducts;
	}
}
