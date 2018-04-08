//Matthew henry -- generates A business object in order to store business data for either display or further query
package processing;

public class Business {
	private String id;
	private String name;
	private String neighborhood;
	private String address;
	private String city;
	private String state;
	private String postal_code;
	private float latitude;
	private float longitude;
	private int stars;
	private int review_count;
	private boolean is_open;
	private String category;

	public Business(String id, String name, String neighborhood, String address, String city, String state,
			String postal_code, float latitude, float longitude, int stars, int review_count, boolean is_open,
			String category) {
		super();
		this.id = id;
		this.name = name;
		this.neighborhood = neighborhood;
		this.address = address;
		this.city = city;
		this.state = state;
		this.postal_code = postal_code;
		this.latitude = latitude;
		this.longitude = longitude;
		this.stars = stars;
		this.review_count = review_count;
		this.is_open = is_open;
		this.category = category;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getNeighborhood() {
		return neighborhood;
	}

	public void setNeighborhood(String neighborhood) {
		this.neighborhood = neighborhood;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getPostal_code() {
		return postal_code;
	}

	public void setPostal_code(String postal_code) {
		this.postal_code = postal_code;
	}

	public float getLatitude() {
		return latitude;
	}

	public void setLatitude(float latitude) {
		this.latitude = latitude;
	}

	public float getLongitude() {
		return longitude;
	}

	public void setLongitude(float longitude) {
		this.longitude = longitude;
	}

	public int getStars() {
		return stars;
	}

	public void setStars(int stars) {
		this.stars = stars;
	}

	public int getReview_count() {
		return review_count;
	}

	public void setReview_count(int review_count) {
		this.review_count = review_count;
	}

	public boolean isIs_open() {
		return is_open;
	}

	public void setIs_open(boolean is_open) {
		this.is_open = is_open;
	}

}
