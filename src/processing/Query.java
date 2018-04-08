//David Burke
package processing;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ArrayList;
import java.util.Collections;

public class Query {
	String type;

	Query() {
	}

	// Takes a list of Review objects and returns a list within given dates
	public ArrayList<Review> reviewsWithinDates(ArrayList<Review> reviews, String date1, String date2) {
		if (reviews != null) {
			ArrayList<Review> r = new ArrayList<Review>();
			DateFormat sourceFormat = new SimpleDateFormat("dd/MM/yyyy");
			Date currentDate;
			String currentDateString;
			try {
				Date firstDate = sourceFormat.parse(date1);
				Date secondDate = sourceFormat.parse(date2);
				for (int i = 0; i < reviews.size(); i++) {
					currentDateString = sourceFormat.format(reviews.get(i).getDate());
					currentDate = sourceFormat.parse(currentDateString);
					if (!firstDate.after(currentDate) && !secondDate.before(currentDate)) {
						r.add(reviews.get(i));
					}
				}
				return r;
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return null;
	}

	// Takes a list of Review objects and returns a list within given stars
	public ArrayList<Review> reviewsWithinStars(ArrayList<Review> reviews, int minStars, int maxStars) {
		if (reviews != null) {
			ArrayList<Review> r = new ArrayList<Review>();
			try {
				for (int i = 0; i < reviews.size(); i++) {
					if (reviews.get(i).getStars() >= minStars && reviews.get(i).getStars() <= maxStars) {
						r.add(reviews.get(i));
					}
				}
				return r;
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return null;

	}

	// Takes a list of Review objects and puts list in descending order for useful,
	// funny, and cool variables
	public ArrayList<Review> mostUsefulReviews(ArrayList<Review> reviews) {
		if (reviews != null) {
			ArrayList<Review> r = new ArrayList<Review>();
			r = reviews;
			try {
				for (int i = 0; i < r.size(); i++) {
					for (int j = i; j > 0; j--) {
						if (r.get(j).getUseful() > r.get(j - 1).getUseful()) {
							Collections.swap(r, j, j - 1);
						} else {
							break;
						}
					}
				}
				return r;
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return null;

	}

	public ArrayList<Review> mostFunnyReviews(ArrayList<Review> reviews) {
		if (reviews != null) {
			ArrayList<Review> r = new ArrayList<Review>();
			r = reviews;
			try {
				for (int i = 0; i < r.size(); i++) {
					for (int j = i; j > 0; j--) {
						if (r.get(j).getFunny() > r.get(j - 1).getFunny()) {
							Collections.swap(r, j, j - 1);
						} else {
							break;
						}
					}
				}
				return r;
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return null;

	}

	public ArrayList<Review> mostCoolReviews(ArrayList<Review> reviews) {
		if (reviews != null) {
			ArrayList<Review> r = new ArrayList<Review>();
			r = reviews;
			try {
				for (int i = 0; i < r.size(); i++) {
					for (int j = i; j > 0; j--) {
						if (r.get(j).getCool() > r.get(j - 1).getCool()) {
							Collections.swap(r, j, j - 1);
						} else {
							break;
						}
					}
				}
				return r;
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return null;

	}

	// Takes a list of reviews and returns lists of user names, text, dates, stars,
	// and useful/funny/cool review ratings
	public ArrayList<String> reviewNames(ArrayList<Review> reviews, MySQLAccess user_get) {
		if (reviews != null) {
			ArrayList<String> n = new ArrayList<String>();
			String currentName;
			try {
				for (int i = 0; i < reviews.size(); i++) {
					reviews.get(i).getUserName(user_get);
					currentName = reviews.get(i).getName();
					n.add(currentName);
				}
				return n;
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return null;
	}

	public ArrayList<String> reviewText(ArrayList<Review> reviews) {
		if (reviews != null) {
			ArrayList<String> t = new ArrayList<String>();
			String currentText;
			try {
				for (int i = 0; i < reviews.size(); i++) {
					currentText = reviews.get(i).getText();
					t.add(currentText);
				}
				return t;
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return null;
	}

	public ArrayList<String> reviewDates(ArrayList<Review> reviews) {
		if (reviews != null) {
			ArrayList<String> d = new ArrayList<String>();
			DateFormat sourceFormat = new SimpleDateFormat("dd/MM/yyyy");
			String currentDateString;
			try {
				for (int i = 0; i < reviews.size(); i++) {
					currentDateString = sourceFormat.format(reviews.get(i).getDate());
					d.add(currentDateString);
				}
				return d;
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return null;
	}

	public ArrayList<Integer> reviewStars(ArrayList<Review> reviews) {
		if (reviews != null) {
			ArrayList<Integer> s = new ArrayList<Integer>();
			Integer currentStars;
			try {
				for (int i = 0; i < reviews.size(); i++) {
					currentStars = reviews.get(i).getStars();
					s.add(currentStars);
				}
				return s;
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return null;
	}

	public ArrayList<Integer> reviewUsefuls(ArrayList<Review> reviews) {
		if (reviews != null) {
			ArrayList<Integer> u = new ArrayList<Integer>();
			Integer currentUsefuls;
			try {
				for (int i = 0; i < reviews.size(); i++) {
					currentUsefuls = reviews.get(i).getUseful();
					u.add(currentUsefuls);
				}
				return u;
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return null;
	}

	public ArrayList<Integer> reviewCools(ArrayList<Review> reviews) {
		if (reviews != null) {
			ArrayList<Integer> c = new ArrayList<Integer>();
			Integer currentCools;
			try {
				for (int i = 0; i < reviews.size(); i++) {
					currentCools = reviews.get(i).getCool();
					c.add(currentCools);
				}
				return c;
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return null;
	}

	public ArrayList<Integer> reviewFunnys(ArrayList<Review> reviews) {
		if (reviews != null) {
			ArrayList<Integer> f = new ArrayList<Integer>();
			Integer currentFunnys;
			try {
				for (int i = 0; i < reviews.size(); i++) {
					currentFunnys = reviews.get(i).getFunny();
					f.add(currentFunnys);
				}
				return f;
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return null;
	}

	// Takes a list of businesses and returns a list of business names,
	// neighborhoods,addresses, cities, states, etc.
	public ArrayList<String> businessNames(ArrayList<Business> businesses) {
		if (businesses != null) {
			ArrayList<String> n = new ArrayList<String>();
			String currentName;
			try {
				for (int i = 0; i < businesses.size(); i++) {
					currentName = businesses.get(i).getName();
					n.add(currentName);
				}
				return n;
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return null;
	}

	public ArrayList<String> businessNeighborhoods(ArrayList<Business> businesses) {
		if (businesses != null) {
			ArrayList<String> n = new ArrayList<String>();
			String currentNeighborhood;
			try {
				for (int i = 0; i < businesses.size(); i++) {
					currentNeighborhood = businesses.get(i).getNeighborhood();
					if (currentNeighborhood != null) {
						n.add(currentNeighborhood);
					} else {
						n.add("Neighborhood unavailable");
					}
				}
				return n;
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return null;
	}

	public ArrayList<String> businessAddresses(ArrayList<Business> businesses) {
		if (businesses != null) {
			ArrayList<String> a = new ArrayList<String>();
			String currentAddress;
			try {
				for (int i = 0; i < businesses.size(); i++) {
					currentAddress = businesses.get(i).getAddress();
					if (currentAddress != null) {
						a.add(currentAddress);
					} else {
						a.add("Address unavailable");
					}
				}
				return a;
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return null;
	}

	public ArrayList<String> businessCities(ArrayList<Business> businesses) {
		if (businesses != null) {
			ArrayList<String> c = new ArrayList<String>();
			String currentCity;
			try {
				for (int i = 0; i < businesses.size(); i++) {
					currentCity = businesses.get(i).getCity();
					if (currentCity != null) {
						c.add(currentCity);
					} else {
						c.add("City unavailable");
					}
				}
				return c;
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return null;
	}

	public ArrayList<String> businessIds(ArrayList<Business> businesses) {
		if (businesses != null) {
			ArrayList<String> id = new ArrayList<String>();
			String currentid;
			try {
				for (int i = 0; i < businesses.size(); i++) {
					currentid = businesses.get(i).getId();
					id.add(currentid);
				}
				return id;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	public ArrayList<String> businessStates(ArrayList<Business> businesses) {
		if (businesses != null) {
			ArrayList<String> s = new ArrayList<String>();
			String currentState;
			try {
				for (int i = 0; i < businesses.size(); i++) {
					currentState = businesses.get(i).getState();
					if (currentState != null) {
						s.add(currentState);
					} else {
						s.add("State unavailable");
					}
				}
				return s;
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return null;
	}

	public ArrayList<String> businessPostalCodes(ArrayList<Business> businesses) {
		if (businesses != null) {
			ArrayList<String> pc = new ArrayList<String>();
			String currentPostalCode;
			try {
				for (int i = 0; i < businesses.size(); i++) {
					currentPostalCode = businesses.get(i).getPostal_code();
					if (currentPostalCode != null) {
						pc.add(currentPostalCode);
					} else {
						pc.add("PostalCode unavailable");
					}
				}
				return pc;
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return null;
	}

	public ArrayList<Float> businessLatitudes(ArrayList<Business> businesses) {
		if (businesses != null) {
			ArrayList<Float> l = new ArrayList<Float>();
			Float currentLatitude;
			try {
				for (int i = 0; i < businesses.size(); i++) {
					currentLatitude = businesses.get(i).getLatitude();

					l.add(currentLatitude);

				}

				return l;
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return null;
	}

	public ArrayList<Float> businessLongitudes(ArrayList<Business> businesses) {
		if (businesses != null) {
			ArrayList<Float> l = new ArrayList<Float>();
			Float currentLongitude;
			try {
				for (int i = 0; i < businesses.size(); i++) {
					currentLongitude = businesses.get(i).getLongitude();

					l.add(currentLongitude);

				}

				return l;
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return null;
	}

	public ArrayList<Integer> businessStars(ArrayList<Business> businesses) {
		if (businesses != null) {
			ArrayList<Integer> s = new ArrayList<Integer>();
			Integer currentLatitude;
			try {
				for (int i = 0; i < businesses.size(); i++) {
					currentLatitude = businesses.get(i).getStars();
					s.add(currentLatitude);
				}
				return s;
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return null;
	}

	public ArrayList<Integer> businessReviewCounts(ArrayList<Business> businesses) {
		if (businesses != null) {
			ArrayList<Integer> rc = new ArrayList<Integer>();
			Integer currentReviewCount;
			try {
				for (int i = 0; i < businesses.size(); i++) {
					currentReviewCount = Math.abs(businesses.get(i).getReview_count());
					rc.add(currentReviewCount);
				}
				return rc;
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return null;
	}

	public ArrayList<String> businessIsOpens(ArrayList<Business> businesses) {
		if (businesses != null) {
			ArrayList<String> o = new ArrayList<String>();
			Boolean currentIsOpen;
			try {
				for (int i = 0; i < businesses.size(); i++) {
					currentIsOpen = businesses.get(i).isIs_open();
					if (currentIsOpen) {
						o.add("Open");
					} else {
						o.add("Closed");
					}
				}
				return o;
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return null;
	}

	public ArrayList<String> businessCategories(ArrayList<Business> businesses) {
		if (businesses != null) {
			ArrayList<String> c = new ArrayList<String>();
			String currentCategory;
			try {
				for (int i = 0; i < businesses.size(); i++) {
					currentCategory = businesses.get(i).getCategory();
					c.add(currentCategory);
				}
				return c;
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return null;
	}

}
