package am.teghut.market.api.dto;


public class PreOrderBean {

	private String productId;
	private String contact;
	private String message;
	private String customerName;
	private boolean deliver;

	public PreOrderBean() {
	}

	public PreOrderBean(String productId, String contact, String message,
			String customerName, boolean deliver) {

		this.productId = productId;
		this.contact = contact;
		this.message = message;
		this.deliver = deliver;
		this.customerName = customerName;
	}

	public String getProductId() {
		return productId;
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}

	public String getContact() {
		return contact;
	}

	public void setContact(String contact) {
		this.contact = contact;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public boolean isDeliver() {
		return deliver;
	}

	public void setDeliver(boolean deliver) {
		this.deliver = deliver;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	@Override
	public String toString() {
		return "PreOrderBean [productId=" + productId + ", contact=" + contact
				+ ", message=" + message + ", customerName=" + customerName
				+ ", deliver=" + deliver + "]";
	}

}
