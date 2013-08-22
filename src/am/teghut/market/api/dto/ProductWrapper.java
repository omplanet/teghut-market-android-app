package am.teghut.market.api.dto;

import am.teghut.market.constant.Const;
import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

public class ProductWrapper implements Parcelable {

	private int id;
	private String name;
	private Bitmap image;
	private String unit;
	private String description;
	private String currency;
	private int pricePerUnit;
	private int inStock;
	private boolean deliveryService;
	private String locationName;
	private String locationURL;
	private int producerId;
	private String producerName;
	private String producerURL;
	private String category;

	public ProductWrapper() {
	}

	public ProductWrapper(int id, String name, Bitmap image, String unit,
			String description, String currency, int pricePerUnit, int inStock,
			boolean deliveryService, String locationName, String locationURL,
			int producerId, String producerName, String producerURL,
			String category) {
		this.id = id;
		this.name = name;
		this.image = image;
		this.unit = unit;
		this.description = description;
		this.currency = currency;
		this.pricePerUnit = pricePerUnit;
		this.inStock = inStock;
		this.deliveryService = deliveryService;
		this.locationName = locationName;
		this.locationURL = locationURL;
		this.producerId = producerId;
		this.producerName = producerName;
		this.producerURL = producerURL;
		this.category = category;
	}

	public ProductWrapper(Parcel in) {
		id = in.readInt();
		name = in.readString();
		image = in.readParcelable(ProductWrapper.class.getClassLoader());
		unit = in.readString();
		description = in.readString();
		currency = in.readString();
		pricePerUnit = in.readInt();
		inStock = in.readInt();
		deliveryService = in.readByte() == 1;
		locationName = in.readString();
		locationURL = in.readString();
		producerId = in.readInt();
		producerName = in.readString();
		producerURL = in.readString();
		category = in.readString();
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(id);
		dest.writeString(name);
		dest.writeParcelable(image, 0);
		dest.writeString(unit);
		dest.writeString(description);
		dest.writeString(currency);
		dest.writeInt(pricePerUnit);
		dest.writeInt(inStock);
		dest.writeByte((byte) (deliveryService ? 1 : 0));
		dest.writeString(locationName);
		dest.writeString(locationURL);
		dest.writeInt(producerId);
		dest.writeString(producerName);
		dest.writeString(producerURL);
		dest.writeString(category);
	}

	public static final Parcelable.Creator<ProductWrapper> CREATOR = new Creator<ProductWrapper>() {

		@Override
		public ProductWrapper[] newArray(int size) {
			return new ProductWrapper[size];
		}

		@Override
		public ProductWrapper createFromParcel(Parcel source) {
			return new ProductWrapper(source);
		}
	};

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName(int lngIndex) {
		return getSubstringed(name, lngIndex);
	}

	public void setName(String name) {
		this.name = name;
	}

	public Bitmap getImage() {
		return image;
	}

	public void setImage(Bitmap image) {
		this.image = image;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public String getDescription(int lngIndex) {
		return getSubstringed(description, lngIndex);
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getCurrency(int lngIndex) {
		return getSubstringed(currency, lngIndex);
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public int getPricePerUnit() {
		return pricePerUnit;
	}

	public void setPricePerUnit(int pricePerUnit) {
		this.pricePerUnit = pricePerUnit;
	}

	public int getInStock() {
		return inStock;
	}

	public void setInStock(int inStock) {
		this.inStock = inStock;
	}

	public boolean isDeliveryService() {
		return deliveryService;
	}

	public void setDeliveryService(boolean deliveryService) {
		this.deliveryService = deliveryService;
	}

	public String getLocationName(int lngIndex) {
		return getSubstringed(locationName, lngIndex);
	}

	public void setLocationName(String locationName) {
		this.locationName = locationName;
	}

	public String getLocationURL(int lngIndex) {
		return getSubstringed(locationURL, lngIndex);
	}

	public void setLocationURL(String locationURL) {
		this.locationURL = locationURL;
	}

	public int getProducerId() {
		return producerId;
	}

	public void setProducerId(int producerId) {
		this.producerId = producerId;
	}

	public String getProducerName(int lngIndex) {
		return getSubstringed(producerName, lngIndex);
	}

	public void setProducerName(String producerName) {
		this.producerName = producerName;
	}

	public String getProducerURL(int lngIndex) {
		return getSubstringed(producerURL, lngIndex);
	}

	public void setProducerURL(String producerURL) {
		this.producerURL = producerURL;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	private boolean isContainsLngKey(String str) {
		return str.contains(Const.KEY_LNG_AM) || str.contains(Const.KEY_LNG_EN)
				|| str.contains(Const.KEY_LNG_RU);
	}

	private String getSubstringed(String str, int lngIndex) {
		if (isContainsLngKey(str)) {
			switch (lngIndex) {
			case 0:
				return getAm(str);
			case 1:
				return getEn(str);
			case 2:
			 return	getRu(str);				
			default:
				return str;
			}
		}
		return str;
	}

	private String getAm(String str) {
		int start = str.indexOf(Const.KEY_LNG_AM) + Const.KEY_LNG_AM.length();
		if (str.contains(Const.KEY_LNG_RU)) {
			int end = str.indexOf(Const.KEY_LNG_RU);
			return str.substring(start, end);
		} else {
			return str.substring(start);
		}
	}

	private String getEn(String str) {
		int start = Const.KEY_LNG_EN.length();
		int end = str.indexOf(Const.KEY_LNG_AM);
		return str.substring(start, end);
	}

	private String getRu(String str) {
		if (str.contains(Const.KEY_LNG_RU)) {
			int start = str.indexOf(Const.KEY_LNG_RU);
			return str.substring(start);
		} else {
			return getEn(str);
		}
	}

}
