//Krishanu Dey - Creates the review class which stores the all the data for each review 
package processing;

import java.util.Date;

public class Review {

	private String name, id, business_id, user_id, text;
	private Date date;
	private int stars, useful, funny, cool;

	public Review(String id, String business_id, String user_id, String text, Date date, int stars, int useful,
			int funny, int cool) {
		super();
		this.id = id;
		this.business_id = business_id;
		this.user_id = user_id;
		this.text = text;
		this.date = date;
		this.stars = stars;
		this.useful = useful;
		this.funny = funny;
		this.cool = cool;
		this.name = null;
	}
	//Matthew Henry -- finds the user name dynamically as needed
	public void getUserName(MySQLAccess user_get) {
		if (this.name == null) {
			String n = user_get.getUserNameByID(this.user_id);
			if (n != null) {
				this.name = n;
			} else {
				System.out.println("Error getting name for " + this.user_id);
			}
		}
	}

	public String getName() {
		return name;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getBusiness_id() {
		return business_id;
	}

	public void setBusiness_id(String business_id) {
		this.business_id = business_id;
	}

	public String getUser_id() {
		return user_id;
	}

	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public int getStars() {
		return stars;
	}

	public void setStars(int stars) {
		this.stars = stars;
	}

	public int getUseful() {
		return useful;
	}

	public void setUseful(int useful) {
		this.useful = useful;
	}

	public int getFunny() {
		return funny;
	}

	public void setFunny(int funny) {
		this.funny = funny;
	}

	public int getCool() {
		return cool;
	}

	public void setCool(int cool) {
		this.cool = cool;
	}

}