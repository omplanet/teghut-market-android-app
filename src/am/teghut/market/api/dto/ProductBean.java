package am.teghut.market.api.dto;


public class ProductBean {

	private int id;
	private String name;
	private String imageURL;
	private String unit;
	private String description;
	private String currency;
	private int pricePerUnit;
	private int availabelInStock;
	private boolean deliveryService;
	private String locationName;
	private String locationURL;
	private int producerId;
	private String producerName;
	private String producerURL;
	private String category;

	public ProductBean() {
	}

	public ProductBean(int id, String name, String imageURL, String unit,
			String currency, int pricePerUnit, int availabelInStock,
			boolean deliveryService, String locationName, String locationURL,
			int producerId, String producerName, String producerURL,
			String description, String category) {
		this.id = id;
		this.name = name;
		this.imageURL = imageURL;
		this.unit = unit;
		this.currency = currency;
		this.pricePerUnit = pricePerUnit;
		this.availabelInStock = availabelInStock;
		this.deliveryService = deliveryService;
		this.locationName = locationName;
		this.locationURL = locationURL;
		this.producerId = producerId;
		this.producerName = producerName;
		this.producerURL = producerURL;
		this.description = description;
		this.category = category;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getImageURL() {
		return imageURL;
	}

	public void setImageURL(String imageURL) {
		this.imageURL = imageURL;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public String getCurrency() {
		return currency;
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

	public int getAvailabelInStock() {
		return availabelInStock;
	}

	public void setAvailabelInStock(int availabelInStock) {
		this.availabelInStock = availabelInStock;
	}

	public boolean isDeliveryService() {
		return deliveryService;
	}

	public void setDeliveryService(boolean deliveryService) {
		this.deliveryService = deliveryService;
	}

	public String getLocationName() {
		return locationName;
	}

	public void setLocationName(String locationName) {
		this.locationName = locationName;
	}

	public String getLocationURL() {
		return locationURL;
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

	public String getProducerName() {
		return producerName;
	}

	public void setProducerName(String producerName) {
		this.producerName = producerName;
	}

	public String getProducerURL() {
		return producerURL;
	}

	public void setProducerURL(String producerURL) {
		this.producerURL = producerURL;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	@Override
	public String toString() {
		return "ProductBean [id=" + id + ", name=" + name + ", imageURL="
				+ imageURL + ", unit=" + unit + ", description=" + description
				+ ", currency=" + currency + ", pricePerUnit=" + pricePerUnit
				+ ", availabelInStock=" + availabelInStock
				+ ", deliveryService=" + deliveryService + ", locationName="
				+ locationName + ", locationURL=" + locationURL
				+ ", producerId=" + producerId + ", producerName="
				+ producerName + ", producerURL=" + producerURL + ", category="
				+ category + "]";
	}

}
