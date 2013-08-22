package am.teghut.market.api.dto;

public class ProducerBean {

	private long id;
	private String firstName;
	private String lastName;
	private String contactInfo;
	private ProducerBean producer;

	public ProducerBean() {
	}

	public ProducerBean(long id, String firstName, String lastName,
			String contactInfo, ProducerBean producer) {
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
		this.contactInfo = contactInfo;
		this.producer = producer;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getContactInfo() {
		return contactInfo;
	}

	public void setContactInfo(String contactInfo) {
		this.contactInfo = contactInfo;
	}

	public ProducerBean getProducer() {
		return producer;
	}

	public void setProducer(ProducerBean producer) {
		this.producer = producer;
	}

	@Override
	public String toString() {
		return "Producer [id=" + id + ", firstName=" + firstName
				+ ", lastName=" + lastName + ", contactInfo=" + contactInfo
				+ ", producer=" + producer + "]";
	}

}
