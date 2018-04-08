//Matthew Henry

//All SQL calls are made to stored procedures on the dataBase, however each SQL query is commented above the method for transparency 
//Each stored procedure takes various inputs which are made obvious by the methods input variable names
package processing;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.ArrayList;
import java.util.Date;
//Opens the SQL connection in through the Object below, this connection is closed upon closing the program
public class MySQLAccess {
	private Connection dbConnection = null;
	private PreparedStatement preparedStatement = null;
	private ResultSet resultSet = null;

	// SetUp a connection to the database 
	public void connectToDB(String dataBaseName, String user, String pass) throws Exception {
		Class.forName("com.mysql.jdbc.Driver");
		dbConnection = DriverManager
				.getConnection("jdbc:mysql://programmingproj.cb1l7vftoytz.eu-west-1.rds.amazonaws.com/" + dataBaseName
						+ "?user=" + user + "&password=" + pass + "&userSSL=false");
		if (dbConnection.isValid(15)) {
			System.out.println("Connection to " + dataBaseName + " as " + user + " successful.");
		} else {
			System.out.println("Connection to " + dataBaseName + " as " + user + " failed, Time out");
		}

	}

	// Process the resultSet taken by any given SQL query into a set of bussiness
	// objects
	private ArrayList<Business> processBusinessResults(ResultSet results) throws Exception {
		ArrayList<Business> b = new ArrayList<Business>();
		results.next();
		while (!resultSet.isAfterLast()) {
			Business newBusiness = new Business(resultSet.getString("id"), resultSet.getString("name"),
					resultSet.getString("neighborhood"), resultSet.getString("address"), resultSet.getString("city"),
					resultSet.getString("state"), resultSet.getString("postal_Code"), resultSet.getFloat("latitude"),
					resultSet.getFloat("longitude"), resultSet.getInt("stars"), resultSet.getInt("review_count"),
					resultSet.getBoolean("is_open"), resultSet.getString("category"));
			b.add(newBusiness);
			results.next();
		}
		return b;
	}
	//Processs a given resultSet into  a set of Review objects to obe later processed by a query object
	private ArrayList<Review> processReviewResults(ResultSet results) throws Exception {
		ArrayList<Review> r = new ArrayList<Review>();
		results.next();
		while (!resultSet.isAfterLast()) {
			Review newReview = new Review(resultSet.getString("id"), resultSet.getString("business_id"),
					resultSet.getString("user_id"), resultSet.getString("text"), resultSet.getDate("date"),
					resultSet.getInt("stars"), resultSet.getInt("useful"), resultSet.getInt("funny"),
					resultSet.getInt("cool"));
			r.add(newReview);
			resultSet.next();
		}
		return r;
	}
	//Close the SQL connection
	public void close() {
		try {
			dbConnection.close();
			resultSet = null;
			preparedStatement = null;
			System.out.println("Connection to dataBase closed");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// Gets the businesses by name (simple search Query)
	//SELECT DISTINCT yelp_db.business.*,(select category  from yelp_db.category where business_id = yelp_db.business.id LIMIT 1) 
	//AS category FROM yelp_db.business 
	//WHERE name like CONCAT('%',business_name,'%') LIMIT max;
	public ArrayList<Business> getBusinessesByName(String name, int maxReturn) {
		ArrayList<Business> businesses = new ArrayList<Business>();
		try {
			preparedStatement = dbConnection
					.prepareStatement("CAll getBusinesses(\"" + name + "\"," + maxReturn + ");");
			resultSet = preparedStatement.executeQuery();
			if (resultSet != null) {
				businesses = processBusinessResults(resultSet);
				return businesses;
			}
			return null;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	// Gets by category 
	//SELECT business.*, c.category FROM business 
	//JOIN category c on c.business_id = business.id 
	//where c.category =category_name LIMIT max_return;
	public ArrayList<Business> getBusinessesByCategory(String category, int maxReturn) {
		ArrayList<Business> businesses = new ArrayList<Business>();
		try {
			preparedStatement = dbConnection
					.prepareStatement("CALL getBusinessesByCategory(\"" + category + "\"," + maxReturn + ");");
			resultSet = preparedStatement.executeQuery();
			if (resultSet != null) {
				businesses = processBusinessResults(resultSet);
				return businesses;
			}
			return null;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	// Gets both by category and by name 
	//SELECT business.*, c.category FROM business 
	//JOIN category c on c.business_id = business.id 
	//where c.category =category_name AND business.name LIKE CONCAT('%',business_name,'%') LIMIT max_return;
	public ArrayList<Business> getBusinessByCategoryAndName(String name, String category, int max_return) {
		ArrayList<Business> businesses = new ArrayList<Business>();
		try {
			preparedStatement = dbConnection.prepareStatement(
					"CALL getBusinessesByCategoryAndName(\"" + name + "\",\"" + category + "\"," + max_return + ");");
			resultSet = preparedStatement.executeQuery();
			if (resultSet != null) {
				businesses = processBusinessResults(resultSet);
				return businesses;
			}
			return null;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	/*
	 * SELECT DISTINCT yelp_db.business.*,(select category  from yelp_db.category where business_id = yelp_db.business.id LIMIT 1) 
		as category FROM yelp_db.business 
		order by stars DESC LIMIT max_Return;
	 */
	public ArrayList<Business> getBusinessesByStars(int max_return) {
		ArrayList<Business> businesses = new ArrayList<Business>();
		try {
			preparedStatement = dbConnection.prepareStatement("CALL getBusinessesByStars(" + max_return + ");");
			resultSet = preparedStatement.executeQuery();
			if (resultSet != null) {
				businesses = processBusinessResults(resultSet);
				return businesses;
			}
			return null;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	/*SELECT DISTINCT yelp_db.business.*,(select category  from yelp_db.category where business_id = yelp_db.business.id LIMIT 1) 
			as category FROM yelp_db.business 
			order by review_count DESC LIMIT maxReturn;
			*/
	public ArrayList<Business> getBusinessesByReviews(int max_return) {
		ArrayList<Business> businesses = new ArrayList<Business>();
		try {
			preparedStatement = dbConnection.prepareStatement("CALL getBusinessesByReviews(" + max_return + ");");
			resultSet = preparedStatement.executeQuery();
			if (resultSet != null) {
				businesses = processBusinessResults(resultSet);
				return businesses;
			}
			return null;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/*
	 SELECT DISTINCT yelp_db.business.*,(select category  from yelp_db.category where business_id = yelp_db.business.id LIMIT 1) 
	AS category FROM yelp_db.business where(
		latitude
	      BETWEEN originLat - (radius / 69) 
	      AND originLat + (radius / 69)
	    AND longitude
	      BETWEEN originLong - (radius / (69 * COS(RADIANS(originLong)))) 
	      AND originLong + (radius / (69* COS(RADIANS(originLong)))))
	    LIMIT max_return;
	    
	*/
	public ArrayList<Business> getBusinessesByLocation(double originLat, double originLong, int radius, int max_return) {
		ArrayList<Business> businesses = new ArrayList<Business>();
		try {
			preparedStatement = dbConnection.prepareStatement("CALL getBusinessesByLocation("+originLat+","+originLong+","+radius+"," + max_return + ");");
			resultSet = preparedStatement.executeQuery();
			if (resultSet != null) {
				businesses = processBusinessResults(resultSet);
				return businesses;
			}
			return null;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	

	//Select business.*, c.category from business 
	//join category c on c.business_id = business.id 
	//where c.category = category_name order by stars DESC LIMIT max_return;
	
	public ArrayList<Business> getBusinessesByStarsAndCategory(String category, int max_return) {
		ArrayList<Business> businesses = new ArrayList<Business>();
		try {
			preparedStatement = dbConnection
					.prepareStatement("Call getBusinessesByStarsAndCategory(\"" + category + "\"," + max_return + ");");
			resultSet = preparedStatement.executeQuery();
			if (resultSet != null) {
				businesses = processBusinessResults(resultSet);
				return businesses;
			}
			return null;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	//SELECT DISTINCT yelp_db.business.*,(select category  from yelp_db.category where business_id = yelp_db.business.id LIMIT 1) 
	//as category FROM yelp_db.business 
	//LIMIT rowOffset, maxReturn;
	public ArrayList<Business> getBusinessesByOffset(int offset, int maxReturn){
		ArrayList<Business> businesses= new ArrayList<Business>();
		try {
			preparedStatement = dbConnection.prepareStatement("Call getBusinessesOffset("+offset+","+maxReturn+");");
			resultSet = preparedStatement.executeQuery();
			if (resultSet != null) {
				businesses = processBusinessResults(resultSet);
				return businesses;
			}
			return null;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	//SELECT yelp_db.review.stars from yelp_db.review USE INDEX(BUSINESS_INDEX)
	//WHERE yelp_db.review.business_id= input 
	//LIMIT max_return;
	public ArrayList<Integer> getBusinessStars(String businessID, int max_return) {
		ArrayList<Integer> businessesStars = new ArrayList<Integer>();
		try {
			preparedStatement = dbConnection
					.prepareStatement("CALL getBusinessStars(\"" + businessID + "\"," + max_return + ");");
			resultSet = preparedStatement.executeQuery();
			resultSet.next();
			int[] data = new int[5];
			while (!resultSet.isAfterLast()) {
				int stars = resultSet.getInt("stars");
				switch (stars) {
				case (1):
					data[0] = data[0] + 1;
					break;
				case (2):
					data[1] = data[1] + 1;
					break;
				case (3):
					data[2] = data[2] + 1;
					break;
				case (4):
					data[3] = data[3] + 1;
					break;
				case (5):
					data[4] = data[4] + 1;
				}
				resultSet.next();
			}
			for (Integer star : data) {
				businessesStars.add(star);
			}
			return businessesStars;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	//SELECT yelp_db.review.* FROM yelp_db.review USE INDEX (BUSINESS_INDEX) WHERE business_id = b_id LIMIT max_return;
	public ArrayList<Review> getReviewsByBusiness(String businessID, int max_return) {
		ArrayList<Review> businessReviews = new ArrayList<Review>();
		try {
			preparedStatement = dbConnection
					.prepareStatement("CAll getBusinessReviews(\"" + businessID + "\"," + max_return + ");");
			resultSet = preparedStatement.executeQuery();
			if (resultSet != null) {
				businessReviews = processReviewResults(resultSet);
				return businessReviews;
			}
			return null;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	//SELECT * FROM review where business_id = b_id ORDER BY date DESC LIMIT max_return;
	public ArrayList<Review> getReviewsByDate(String businessID, int max_return) {
		ArrayList<Review> businessReviews = new ArrayList<Review>();
		try {
			preparedStatement = dbConnection
					.prepareStatement("CAll getReviewsByDate(\"" + businessID + "\"," + max_return + ");");
			resultSet = preparedStatement.executeQuery();
			if (resultSet != null) {
				businessReviews = processReviewResults(resultSet);
				return businessReviews;
			}
			return null;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	//SELECT * FROM review WHERE business_id = b_id ORDER BY stars DESC LIMIT max_return;
	public ArrayList<Review> getReviewsByStars(String businessID, int max_return) {
		ArrayList<Review> businessReviews = new ArrayList<Review>();
		try {
			preparedStatement = dbConnection
					.prepareStatement("CAll getReviewsByStars(\"" + businessID + "\"," + max_return + ");");
			resultSet = preparedStatement.executeQuery();
			if (resultSet != null) {
				businessReviews = processReviewResults(resultSet);
				return businessReviews;
			}
			return null;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	//SELECT * FROM review WHERE date BETWEEN startDate AND endDate AND business_id = b_id LIMIT max_return; 
	public ArrayList<Review> getReviewsBetweenDates(String businessID, String startDate, String endDate, int max_return) {
		ArrayList<Review> businessReviews = new ArrayList<Review>();
		try {
			preparedStatement = dbConnection
					.prepareStatement("CAll getReviewsByDates(\"" + businessID + "\",'"+startDate+"','"+endDate+"'," + max_return + ");");
			resultSet = preparedStatement.executeQuery();
			if (resultSet != null) {
				businessReviews = processReviewResults(resultSet);
				return businessReviews;
			}
			return null;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	//SELECT yelp_db.user.name FROM yelp_db.user USE INDEX(PRIMARY) WHERE id = u_id LIMIT 1;
	public String getUserNameByID(String userID) {
		try {
			preparedStatement = dbConnection.prepareStatement("CALL getUserByID(\"" + userID + "\");");
			resultSet = preparedStatement.executeQuery();
			resultSet.next();
			if (resultSet != null) {
				String userName = resultSet.getString("name");
				return userName;
			}
			return null;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
}
