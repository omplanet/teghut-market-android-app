package am.teghut.market.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import am.teghut.market.api.dto.PreOrderBean;
import am.teghut.market.api.dto.ProductBean;
import am.teghut.market.constant.Const;
import android.util.Log;

public class DataManager {
	private final static String PRODUCT_BASE_URL = "http://hamaspyur.am:8080/api/market/";
	private final static String TAG = "DataManager";
	private static String getAllProductUrl = "products/all";

	public static int retrievingState = 0;

	public static final List<ProductBean> getProducts() {
		return productBeansResponseParser(httpClientManager(getAllProductUrl));
	}

	private static ArrayList<ProductBean> productBeansResponseParser(
			String response) {
		ArrayList<ProductBean> productBeans = new ArrayList<ProductBean>();

		try {
			JSONObject jsonObject = new JSONObject(response);
			JSONArray jArray = jsonObject.getJSONArray("products");

			for (int i = 0; i < jArray.length(); i++) {

				JSONObject jObject = jArray.getJSONObject(i);
				ProductBean productBean = new ProductBean();
				productBean.setId(jObject.getInt(Const.ID));
				productBean.setName(jObject.getString(Const.NAME));
				productBean.setUnit(jObject.getString(Const.UNIT));
				productBean.setImageURL(jObject.getString(Const.IMAGE));
				productBean.setCurrency(jObject.getString(Const.CURRENCY));
				productBean.setPricePerUnit(jObject
						.getInt(Const.PRICE_PER_UNIT));
				productBean.setAvailabelInStock(jObject
						.getInt(Const.AVAILABLE_IN_STOCK));
				productBean.setDeliveryService(jObject
						.getBoolean(Const.DELIVERY_SERVICE));
				productBean.setLocationName(jObject
						.getString(Const.LOCATION_NAME));
				productBean.setLocationURL(jObject
						.getString(Const.LOCATION_URL));
				productBean.setProducerId(jObject.getInt(Const.PRODUCER_ID));
				productBean.setProducerName(jObject
						.getString(Const.PRODUCER_NAME));
				productBean.setProducerURL(jObject
						.getString(Const.PRODUCER_URL));
				productBean
						.setDescription(jObject.getString(Const.DESCRIPTION));
				productBean.setCategory(jObject.getString(Const.CATEGORY));
				productBeans.add(productBean);
			}
			if(productBeans.size() > 0){
				retrievingState = 1;
			}
		} catch (JSONException e) {
			Log.e(TAG, e.getMessage());
			return null;
		}
		return productBeans;
	}

	private static String httpClientManager(String url) {

		BufferedReader bufferedReader = null;
		String NL = System.getProperty("line.separator");
		String line = "";

		HttpClient client = new DefaultHttpClient();
		HttpGet request = new HttpGet(PRODUCT_BASE_URL + url);
		try {
			HttpResponse response = client.execute(request);
			bufferedReader = new BufferedReader(new InputStreamReader(response
					.getEntity().getContent()));
			StringBuilder strBuilder = new StringBuilder();
			while ((line = bufferedReader.readLine()) != null) {
				strBuilder.append(line + NL);
			}
			bufferedReader.close();			
			return strBuilder.toString();
		} catch (Exception e) {
			Log.e(TAG, e.getMessage());
			return null;
		} finally {
			try {
				bufferedReader.close();
			} catch (IOException e) {
				Log.e(TAG, e.getMessage());
			}
		}

	}

	public static boolean sendPreOrder(PreOrderBean preOrderBean) {
		HttpClient client = new DefaultHttpClient();
		HttpPost request = new HttpPost(PRODUCT_BASE_URL + "sendPreOrder");
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

		nameValuePairs.add(new BasicNameValuePair("contact", preOrderBean
				.getContact()));
		nameValuePairs.add(new BasicNameValuePair("message", preOrderBean
				.getMessage()));
		nameValuePairs.add(new BasicNameValuePair("productId", preOrderBean
				.getProductId()));
		nameValuePairs.add(new BasicNameValuePair("delivery", preOrderBean
				.isDeliver() + ""));
		nameValuePairs.add(new BasicNameValuePair("customerName", preOrderBean
				.getCustomerName()));

		try {
			request.setEntity(new UrlEncodedFormEntity(nameValuePairs));
			client.execute(request);
			return true;
		} catch (Exception e) {
			Log.e(TAG, e.getMessage());
			return false;
		}
	}
}
