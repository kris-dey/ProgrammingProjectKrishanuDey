package processing;

import java.awt.Dimension;

import java.awt.Toolkit;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import controlP5.Button;
import controlP5.ControlP5;
import controlP5.DropdownList;
import controlP5.ScrollableList;
import controlP5.Textfield;
import de.fhpotsdam.unfolding.UnfoldingMap;
import de.fhpotsdam.unfolding.geo.Location;
import de.fhpotsdam.unfolding.utils.MapUtils;
import grafica.GPlot;
import processing.Graphs.ScatterPlot;
import processing.core.PApplet;
import processing.core.PFont;
import processing.core.PImage;

public class Main extends PApplet {
	public final double TESTLAT =36.114647;
	public final double TESTLONG =-115.172813;
	Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	float screenHeight = (float) screenSize.getHeight();
	float screenWidth = (float) screenSize.getWidth();
	PImage backgroundImage;
	MySQLAccess db;
	Graphs starHist;
	ScatterPlot reviewsOverTime;
	Review selectedReview;
	PImage star;
	ArrayList<String> reviewsText, reviewsNames, reviewsDates;
	ArrayList<String> businessNames;
	ArrayList<String> businessAddress;
	ArrayList<Business> searchCache;
	ArrayList<Review> reviewCache;
	public static final int NULL_EVENT = 0;
	ArrayList<Review> reviews = new ArrayList<Review>();
	String queryResult;
	ControlP5 home;
	Textfield searchField;
	DropdownList categoryDrop;
	Button searchButton;
	ScrollableList businessesScrollList;
	ScrollableList resultPageScrollList;
	PFont AgencyFB12;
	PFont AgencyFB32;
	PFont textFont;
	String selectedCategory;
	Button recentReviews;
	Button nearMe;
	Button starsRating, nextGraph;
	Button mostReviewed;
	Button showMap;
	Button orderByDate;
	Button orderByStars;
	PImage logo;
	GPlot reviewPlot, plot;
	Boolean showStars, activateMap;
	Textfield startDate;
	Textfield endDate;
	Button dateSort;
	List<String> businessList, reviewsList;
	int currentScreen = 1;
	Button backButton;
	ControlP5 resultPage;
	String restaurantName;
	int boxIndex;
	Business selectedBusiness;
	ReviewPopup reviewSplash;
	int buttonColour = color(0, 0, 0, 180);
	int activeColour = color(255, 10, 10);
	Map map, businessMap;
	UnfoldingMap uMap, uBusinessMap;
	Location location;
	String[] userLocation;
	Button exitButtonHomePage;
	Button exitButtonResultsPage;
	PImage stars;
	boolean searchFailed = false;
	int run;

	public static void main(String[] args) {
		PApplet.main("processing.Main");
	}

	public void settings() {
		size((int) screenWidth, (int) screenHeight, P2D);
		fullScreen();
	}

	
	public void setup() {
		// Krishanu Dey and Ciara O'Sullivan - loads two screens, images, fonts and background
		
		home = new ControlP5(this);
		resultPage = new ControlP5(this);
		resultPage.setAutoDraw(false);

		stars = loadImage("data\\star.png");
		stars.resize((int) (screenWidth / 35), (int) (screenHeight / 20));

		star = loadImage("data\\star.png");
		star.resize((int) (20), (int) (20));

		AgencyFB32 = createFont("data\\AgencyFB-Reg-32.vlw", 32);
		AgencyFB12 = createFont("data\\AgencyFB-Reg-12.vlw", 12);

		home.setFont(AgencyFB12);
		resultPage.setFont(AgencyFB12);

		fill(5, 5, 5);
		backgroundImage = loadImage("data\\bg.jpg");
		backgroundImage.resize((int) screenWidth, (int) screenHeight);
		logo = loadImage("data\\group10icon.PNG");

		frameRate(100);
		resultPage.hide();
		run = 1;
	}

	public void draw() {
		if (run == 1) {
			// Krishanu Dey - Draws the loading screen on the first run while the database connection is made
			image(backgroundImage, 0, 0);
			textFont(AgencyFB32, screenHeight / 15);
			fill(0);
			text("Loading . . .", (float) (screenWidth / 2.55), screenHeight / 2);
			run++;
		} else if (run == 2) {
			initialiseScreen(home);
			// Matthew Henry - Making the database connection
			db = new MySQLAccess();
			makeConnections();
			// Brian Lynch - Setting up the maps
			float graphWidth = (float) (screenWidth / 1.7);
			float graphHeight = (float) (screenHeight / 2.2);
			plot = new GPlot(this, graphWidth, graphHeight);
			reviewPlot = new GPlot(this, graphWidth, graphHeight);
			starHist = new Graphs((float) (screenWidth / 3.3), (float) (screenHeight / 4.4), plot, this, db);
			reviewsOverTime = starHist.new ScatterPlot((float) (screenWidth / 3.3), (float) (screenHeight / 4.4),
					reviewPlot, star, this, db);
			// map
			location = new Location(0, 0);
			uMap = new UnfoldingMap(this, (float) (screenWidth / 1.815), (float) (screenHeight / 7.6),
					(screenWidth / 4), (float) (screenHeight / 4));
			map = new Map(location, uMap);
			MapUtils.createDefaultEventDispatcher(this, uMap);
			starsRating();
			initialiseResultPage(resultPage);
			run++;

		} else if (run == 3) {
			// Ciara O'Sullivan and Krishanu Dey - changes screens using if statements and global variables 
			image(backgroundImage, 0, 0);
			stroke(50);
			fill(15);
			if (currentScreen == 1) {
				home.show();
				home.draw();
				image(logo, (float) (screenWidth / 2.45), screenHeight / 35);

			} else if (currentScreen == 2) {
				resultPage.draw();
				fill(buttonColour);

				uMap.draw();
				rect(resultPageScrollList.getPosition()[0], (float) (screenHeight / 7.6), (float) (screenWidth / 2.5),
						screenHeight / 7);
				fill(255);
				textFont(AgencyFB32, screenHeight / 30);
				text(selectedBusiness.getName(), resultPageScrollList.getPosition()[0], (float) (screenHeight / 6.3));
				textFont(AgencyFB32, screenHeight / 40);
				text(selectedBusiness.getCity() + " : " + selectedBusiness.getAddress(),
						resultPageScrollList.getPosition()[0], (float) (screenHeight / 5));

				if (showStars) {
					starHist.draw(star);
				} else {
					reviewsOverTime.draw();
				}
				reviewSplash.draw(stars);
				if(searchFailed) {
					
				}
			}

			if (!reviewSplash.isDrawn()) {
				resultPageScrollList.unlock();
				for (int i = 0; i < resultPage.getAll(Button.class).size(); i++) {
					resultPage.getAll(Button.class).get(i).unlock();
				}

			}
			if (activateMap) {
				businessMap.draw();
			}
			businessesScrollList.setOpen(true);
			resultPageScrollList.setOpen(true);
		}
	}

	public void initialiseResultPage(ControlP5 screen2) {
		// Ciara O'Sullivan and Krishanu Dey - sets up elements of the Result Page(widgets, search field, drop down list, ScrollList and Map)
		//									   The positioning of the elements have been done such that the height and width are all relative
		//									   based the screen's height and width 
		
		exitButtonResultsPage = new Button(screen2, "exitButtonResultsPage").setImage(loadImage("data\\x-mark.png"));
		exitButtonResultsPage.setColorBackground(buttonColour).setColorActive(activeColour)
				.setColorForeground(activeColour);
		exitButtonResultsPage.setHeight((int) (screenWidth / 25)).setWidth((int) screenWidth / 25);
		exitButtonResultsPage.setPosition((float) (screenWidth - exitButtonResultsPage.getWidth()), (float) 0);

		backButton = new Button(screen2, "backButton").setCaptionLabel("Home");
		backButton.setColorBackground(buttonColour).setColorActive(activeColour).setColorForeground(color(255, 10, 10));
		backButton.setHeight((int) (screenHeight / 20)).setWidth((int) screenWidth / 10);
		backButton.setPosition((float) (screenWidth / 1.2), (float) (screenHeight / 13));
		backButton.getCaptionLabel().toUpperCase(false);

		nextGraph = new Button(screen2, "nextGraph").setCaptionLabel("Reviews per month");
		nextGraph.setColorBackground(buttonColour).setColorActive(activeColour).setColorForeground(activeColour);
		nextGraph.setHeight((int) (screenHeight / 20)).setWidth((int) screenWidth / 10);
		nextGraph.setPosition(plot.getPos()[0], plot.getPos()[1] + plot.getDim()[1] + screenHeight / 9);
		nextGraph.setPosition((float) (screenWidth / 1.2), (float) (screenHeight / 1.2));
		nextGraph.getCaptionLabel().toUpperCase(false);

		resultPageScrollList = new ScrollableList(screen2, "resultPageScrollList");
		resultPageScrollList.setPosition(screenWidth / 9, (float) (screenHeight / 2.5))
				.setSize((int) (screenWidth / 3), (int) screenHeight / 2).setBarHeight(20).setItemHeight(100)
				.setColorBackground(buttonColour).setType(ScrollableList.LIST).setColorActive(activeColour)
				.setColorForeground(activeColour).setBarVisible(false);
		reviewSplash = new ReviewPopup(this, (int) screenWidth / 10, (int) screenHeight / 10,
				(float) (screenWidth / 1.3), (float) (screenHeight / 1.3), (int) screenWidth, screenHeight);

		orderByDate = new Button(screen2, "orderByDate").setCaptionLabel("Order by date");
		orderByDate.setColorBackground(buttonColour).setColorActive(activeColour).setColorForeground(activeColour);
		orderByDate.setHeight((int) (screenHeight / 20)).setWidth((int) screenWidth / 10);
		orderByDate.setPosition(resultPageScrollList.getPosition()[0],
				resultPageScrollList.getPosition()[1] + resultPageScrollList.getHeight());
		orderByDate.getCaptionLabel().toUpperCase(false);

		orderByStars = new Button(screen2, "orderByStars").setCaptionLabel("Order by stars");
		orderByStars.setColorBackground(buttonColour).setColorActive(activeColour).setColorForeground(activeColour);
		orderByStars.setHeight((int) (screenHeight / 20)).setWidth((int) screenWidth / 10);
		orderByStars.setPosition(
				resultPageScrollList.getPosition()[0] + (resultPageScrollList.getWidth() - orderByStars.getWidth()),
				resultPageScrollList.getPosition()[1] + resultPageScrollList.getHeight());
		orderByStars.getCaptionLabel().toUpperCase(false);
		
		startDate = new Textfield(screen2, "Start Date");
		startDate.setLabelVisible(false);
		startDate.setColorBackground(color(201, 199, 187)).setColorForeground(color(255)).setColorLabel(255)
				.setColorActive(activeColour);
		startDate.setPosition(resultPageScrollList.getPosition()[0], resultPageScrollList.getPosition()[1] - (int) screenWidth / 30);
		startDate.setHeight((int) screenHeight / 35).setWidth((int) (screenWidth / 10)).setColorValueLabel(255);
		startDate.setSize(startDate.getWidth(), startDate.getHeight());
		
		endDate = new Textfield(screen2, "End Date");
		endDate.setLabelVisible(false);
		endDate.setColorBackground(color(201, 199, 187)).setColorForeground(color(255)).setColorLabel(255)
				.setColorActive(activeColour);
		endDate.setPosition(resultPageScrollList.getPosition()[0]+ (startDate.getWidth() + screenWidth / 140) 
				, resultPageScrollList.getPosition()[1]  - (int) screenWidth / 30);
		
		endDate.setHeight((int) screenHeight / 35).setWidth((int) ( screenWidth / 10)).setColorValueLabel(255);
		endDate.setSize(endDate.getWidth(), endDate.getHeight());
		
		dateSort = new Button(screen2, "dateSort").setCaptionLabel("Sort between dates");
		dateSort.getCaptionLabel().toUpperCase(false);
		dateSort.setColorBackground(buttonColour).setColorActive(activeColour).setColorForeground(activeColour);
		dateSort.setHeight((int) (screenHeight / 35)).setWidth((int) screenWidth / 10);
		dateSort.setPosition(resultPageScrollList.getPosition()[0] + (startDate.getWidth() + screenWidth / 8), resultPageScrollList.getPosition()[1] - (int) screenWidth / 30);
		dateSort.getCaptionLabel().toUpperCase(false);
		
	}

	
	public void initialiseScreen(ControlP5 screen) {
		// Ciara O'Sullivan and Krishanu Dey - sets up elements of the Home Page(widgets, search field, drop down list, ScrollList and Map)
		//		                               The positioning of the elements have been done such that the height and width are all relative
		//									   based the screen's height and width 

		showStars = true;
		String[] categoryList = { "Food", "Bars", "Hotels", "Shopping" };

		float LocationX = (float) (screenWidth / 3.3);
		float LocationY = screenHeight / 5;

		exitButtonHomePage = new Button(screen, "exitButtonHomePage").setImage(loadImage("data\\x-mark.png"));
		exitButtonHomePage.setColorBackground(buttonColour).setColorActive(activeColour).setColorForeground(activeColour);
		exitButtonHomePage.setHeight((int) (screenWidth / 25)).setWidth((int) screenWidth / 25);
		exitButtonHomePage.setPosition((float) (screenWidth - exitButtonHomePage.getWidth()), (float) 0);

		searchField = new Textfield(screen, "searchField").setLabel("Search");
		searchField.setColorBackground(color(201, 199, 187)).setColorForeground(color(255)).setColorLabel(255)
				.setColorActive(activeColour);
		searchField.setPosition(LocationX, LocationY);
		searchField.setHeight((int) screenHeight / 35).setWidth((int) (screenWidth / 4)).setColorValueLabel(0);

		searchButton = new Button(screen, "searchButton").setCaptionLabel("Search");
		searchButton.setColorBackground(buttonColour).setColorActive(activeColour).setColorForeground(activeColour);
		searchButton.setHeight(searchField.getHeight()).setWidth(searchField.getWidth() / 4);
		searchButton.setPosition(searchField.getPosition()[0] + searchField.getWidth() + screenWidth / 150,
				searchField.getPosition()[1]);
		searchButton.getCaptionLabel().toUpperCase(false);

		nearMe = new Button(screen, "nearMe").setCaptionLabel("Near Me");
		nearMe.setColorBackground(buttonColour).setColorActive(activeColour).setColorForeground(activeColour);
		nearMe.setHeight((int) screenHeight / 20).setWidth((int) screenWidth / 10);
		nearMe.setPosition((float) (screenWidth / 3.5) + screenWidth / 15, (float) (screenHeight / 3.5));
		nearMe.getCaptionLabel().toUpperCase(false);

		starsRating = new Button(screen, "starsRating").setCaptionLabel("Highest Rated");
		starsRating.setColorBackground(buttonColour).setColorActive(activeColour).setColorForeground(activeColour);
		starsRating.setHeight((int) (screenHeight / 20)).setWidth((int) screenWidth / 10);
		starsRating.setPosition(nearMe.getPosition()[0] + nearMe.getWidth() + screenWidth / 150,
				nearMe.getPosition()[1]);
		starsRating.getCaptionLabel().toUpperCase(false);

		mostReviewed = new Button(screen, "mostReviewed").setCaptionLabel("Most Reviewed");
		mostReviewed.setColorBackground(buttonColour).setColorActive(activeColour).setColorForeground(activeColour);
		mostReviewed.setHeight((int) (screenHeight / 20)).setWidth((int) screenWidth / 10);
		mostReviewed.setPosition(starsRating.getPosition()[0] + starsRating.getWidth() + screenWidth / 150,
				starsRating.getPosition()[1]);
		mostReviewed.getCaptionLabel().toUpperCase(false);

		categoryDrop = new DropdownList(home, "categoryDrop");
		categoryDrop.setColorBackground(buttonColour).setColorForeground(activeColour);
		categoryDrop.setBarHeight(searchField.getHeight())
				.setHeight(categoryDrop.getBarHeight() * (categoryList.length + 1)).setWidth(searchField.getWidth() / 4)
				.setItemHeight(categoryDrop.getBarHeight());
		categoryDrop.setPosition(searchButton.getPosition()[0] + searchButton.getWidth() + screenWidth / 150,
				searchButton.getPosition()[1]);
		categoryDrop.setLabelVisible(false).setCaptionLabel("Category").setOpen(false);
		categoryDrop.addItems(categoryList);
		categoryDrop.getCaptionLabel().toUpperCase(false);

		businessesScrollList = new ScrollableList(screen, "businessesScrollList");
		businessesScrollList.setPosition(screenWidth / 4, (float) (screenHeight / 2.5))
				.setSize((int) screenWidth / 2, (int) screenHeight / 2).setBarHeight(20).setItemHeight(100)
				.setColorBackground(buttonColour).setType(ScrollableList.LIST).setColorActive(activeColour)
				.setColorForeground(activeColour).setBarVisible(false);

		showMap = new Button(screen, "showMap");
		showMap.setHeight((int) (screenHeight / 20)).setWidth((int) screenWidth / 10).setPosition(
				(screenWidth / 2) - showMap.getWidth() / 2,
				businessesScrollList.getHeight() + businessesScrollList.getPosition()[1]);
		showMap.getCaptionLabel().toUpperCase(false);
		showMap.setCaptionLabel("Show Map");
		showMap.setColorBackground(buttonColour).setColorActive(activeColour).setColorForeground(activeColour);
		uBusinessMap = new UnfoldingMap(this, (int) screenWidth / 10, (int) screenHeight / 10,
				(float) (screenWidth / 1.3), (float) (screenHeight / 1.3));
		businessMap = new Map(location, uBusinessMap);
		MapUtils.createDefaultEventDispatcher(this, uBusinessMap);
		activateMap = false;
	}
	
	public void dateSort() {
		//Matthew henry And Krishanu Dey -- Runs a SQL query where reviews are returned based on input dates from the respective text boxes
		Query reviewQuery = new Query();
		reviewCache = db.getReviewsBetweenDates(searchCache.get(boxIndex).getId(),startDate.getText(), endDate.getText(), 15);
		if(reviewCache != null) {
		reviewsNames = reviewQuery.reviewNames(reviewCache, db);
		reviewsText = reviewQuery.reviewText(reviewCache);
		reviewsText = shortenReviewText(reviewsText);
		reviewsDates = reviewQuery.reviewDates(reviewCache);
		reviewsList = reviewsNames;
		resultPageScrollList.setItems(reviewsList);

		for (int i = 0; i < resultPageScrollList.getItems().size(); i++) {
			resultPageScrollList.getItem(i).put("text",
					reviewsNames.get(i) + "\n" + reviewsText.get(i) + "\n" + reviewsDates.get(i));
		}
		searchFailed = false;
		}else {
			searchFailed = true;
		}
	}

	public void searchField(String searchText) {
		// Matthew Henry -- Run when the enter key is pressed and searchField is in focus, runs a query which gets businesses based on the text entered
		//					handes some degree of predictive text as wildcards are used in the SQL query system
		Query searchQuery = new Query();
		searchCache = db.getBusinessesByName(searchText, 20);
			businessNames = searchQuery.businessNames((searchCache));
			businessAddress = searchQuery.businessAddresses(searchCache);
			businessList = businessNames;
			businessesScrollList.setItems(businessList);
			for (int i = 0; i < businessesScrollList.getItems().size(); i++) {
				businessesScrollList.getItem(i).put("text", businessNames.get(i) + "\n\n" + businessAddress.get(i));
			}
			ArrayList<Location> l = new ArrayList<Location>();
			for (Business bLocations : searchCache) {
				l.add(new Location(bLocations.getLatitude(), bLocations.getLongitude()));
			}
			businessMap.addMultipleMarkers(l);
		}

	public void exitButtonHomePage() {
		// Krishanu Dey - Exits the program when the 'X' button is pressed
		exit();
	}

	public void exitButtonResultsPage() {
		// Krishanu Dey - Exits the program when the 'X' button is pressed
		exit();
	}

	public void mostReviewed() {
		// Matthew Henry -- runs a SQL query where by businesses are ordered by highest number of reviews
		Query searchQuery = new Query();
		searchCache = db.getBusinessesByReviews(20);
			businessNames = searchQuery.businessNames((searchCache));
			businessAddress = searchQuery.businessAddresses(searchCache);
			businessList = businessNames;
			businessesScrollList.setItems(businessList);
			for (int i = 0; i < businessesScrollList.getItems().size(); i++) {
				businessesScrollList.getItem(i).put("text", businessNames.get(i) + "\n\n" + businessAddress.get(i));
			}
			ArrayList<Location> l = new ArrayList<Location>();
			for (Business bLocations : searchCache) {
				l.add(new Location(bLocations.getLatitude(), bLocations.getLongitude()));
			}
			businessMap.addMultipleMarkers(l);
		}

	public void starsRating() {
		// Matthew Henry -- runs a SQL query where by businesses are ordered by highest star rating
		Query searchQuery = new Query();
		searchCache = db.getBusinessesByStars(20);
			businessNames = searchQuery.businessNames((searchCache));
			businessAddress = searchQuery.businessAddresses(searchCache);
			businessList = businessNames;
			businessesScrollList.setItems(businessList);
			for (int i = 0; i < businessesScrollList.getItems().size(); i++) {
				businessesScrollList.getItem(i).put("text", businessNames.get(i) + "\n\n" + businessAddress.get(i));
			}
			ArrayList<Location> l = new ArrayList<Location>();
			for (Business bLocations : searchCache) {
				l.add(new Location(bLocations.getLatitude(), bLocations.getLongitude()));
			}
			businessMap.addMultipleMarkers(l);
		}

	public void businessesScrollList(int n) {
		// Matthew Henry -- moves the program to the results page while passing in the business ID in order to generate the results screens data
		home.hide();
		resultPage.show();
		Query starQuery = new Query();
		boxIndex = n;
		currentScreen = 2;
		ArrayList<Business> b = db.getBusinessesByName(businessesScrollList.getItem(n).get("name").toString(), 1);
		starHist.changePoints(starQuery.businessIds(b).get(0));
		reviewsOverTime.changePoints(starQuery.businessIds(b).get(0), starQuery);
		selectedBusiness = searchCache.get(n);
		location = new Location(selectedBusiness.getLatitude(), selectedBusiness.getLongitude());
		map = new Map(location, uMap);
		map.addMarker();
		businessReviews();

	}

	public void orderByDate() {
		//Matthew henry -- runs SQL query based on given business ID where reviews are ordered by most recent date
		Query reviewQuery = new Query();
		reviewCache = db.getReviewsByDate(searchCache.get(boxIndex).getId(), 15); //
		reviewsNames = reviewQuery.reviewNames(reviewCache, db);
		reviewsText = reviewQuery.reviewText(reviewCache);
		reviewsText = shortenReviewText(reviewsText);
		reviewsDates = reviewQuery.reviewDates(reviewCache);
		reviewsList = reviewsNames;
		resultPageScrollList.setItems(reviewsList);

		for (int i = 0; i < resultPageScrollList.getItems().size(); i++) {
			resultPageScrollList.getItem(i).put("text",
					reviewsNames.get(i) + "\n" + reviewsText.get(i) + "\n" + reviewsDates.get(i));
		}
	}

	public void orderByStars() {
		//Matthew henry -- runs a query based on the given business ID where reviews are ordered by highest star ratings
		Query reviewQuery = new Query();
		reviewCache = db.getReviewsByStars(searchCache.get(boxIndex).getId(), 15); //
		reviewsNames = reviewQuery.reviewNames(reviewCache, db);
		reviewsText = reviewQuery.reviewText(reviewCache);
		reviewsText = shortenReviewText(reviewsText);
		reviewsDates = reviewQuery.reviewDates(reviewCache);
		reviewsList = reviewsNames;
		resultPageScrollList.setItems(reviewsList);

		for (int i = 0; i < resultPageScrollList.getItems().size(); i++) {
			resultPageScrollList.getItem(i).put("text",
					reviewsNames.get(i) + "\n" + reviewsText.get(i) + "\n" + reviewsDates.get(i));
		}
	}

	public void resultPageScrollList(int n) {
		// Matthew Henry -- locks/hides all items on the results screen and displays the "splash" screen conatianing review atributes
		resultPageScrollList.lock();

		selectedReview = reviewCache.get(n);
		for (int i = 0; i < resultPage.getAll(Button.class).size(); i++) {
			resultPage.getAll(Button.class).get(i).lock();
		}
		reviewSplash.displayReview(selectedReview, AgencyFB32);
	}

	public void categoryDrop(int n) {
		// Matthew henry -- dropdown list functionality
		selectedCategory = categoryDrop.getItem(n).get("name").toString();
	}

	public void searchButton() {
		// MAtthew Henry -- uses both the searchbox text and category box to search for a business by name and category
		if (selectedCategory == null) {
			searchField(searchField.getText());
			searchField.clear();
			searchField.setText("");
		} else {
			Query categorySearchQuery = new Query();
			searchCache = db.getBusinessByCategoryAndName(searchField.getText(), selectedCategory, 20);
				businessNames = categorySearchQuery.businessNames((searchCache));
				businessAddress = categorySearchQuery.businessAddresses(searchCache);
				businessList = businessNames;
				businessesScrollList.setItems(businessList);
				for (int i = 0; i < businessesScrollList.getItems().size(); i++) {
					businessesScrollList.getItem(i).put("text", businessNames.get(i) + "\n\n" + businessAddress.get(i));
				}
				ArrayList<Location> l = new ArrayList<Location>();
				for (Business bLocations : searchCache) {
					l.add(new Location(bLocations.getLatitude(), bLocations.getLongitude()));
				}
				businessMap.addMultipleMarkers(l);
			}
		}

	public void nearMe() {
		// MAtthew henry -- uses SQL based algorithm to determine nearby businesses depending on the lat/long passed in and the radius given
				Query categorySearchQuery = new Query();
				// searchCache = db.getBusinessesByLocation(userLocation[0], userLocation[1],
				// 10, 10); ///No businesses in dublin so this is depreciated
				searchCache = db.getBusinessesByLocation(TESTLAT, TESTLONG, 15, 15);
				
				businessNames = categorySearchQuery.businessNames((searchCache));
				businessAddress = categorySearchQuery.businessAddresses(searchCache);
				businessList = businessNames;
				businessesScrollList.setItems(businessList);
				for (int i = 0; i < businessesScrollList.getItems().size(); i++) {
					businessesScrollList.getItem(i).put("text", businessNames.get(i) + "\n\n" + businessAddress.get(i));
				}
				ArrayList<Location> l = new ArrayList<Location>();
				for (Business bLocations : searchCache) {
					l.add(new Location(bLocations.getLatitude(), bLocations.getLongitude()));
				}
				businessMap.addMultipleMarkers(l);
		}

	public void backButton() {
		// MAtthew Henry -- switches screens by hiding the contoller object containing all of the screen elements on the result screen
		resultPage.hide();
		home.show();
		currentScreen = 1;

	}

	public void nextGraph() {
		// Matthew Henry -- switches betweeen the graphs
		if (showStars) {
			nextGraph.setLabel("Stars per review");
			showStars = false;
		} else {
			nextGraph.setLabel("Reviews per month");
			showStars = true;
		}
	}

	public void businessReviews() {
		// Matthew henrys -- functionality for getting and displaying data on the reviews dependant on a specific business (I.e the selected business)
		Query reviewQuery = new Query();
		reviewCache = db.getReviewsByBusiness(searchCache.get(boxIndex).getId(), 20); //
		reviewsNames = reviewQuery.reviewNames(reviewCache, db);
		reviewsText = reviewQuery.reviewText(reviewCache);
		reviewsText = shortenReviewText(reviewsText);
		reviewsDates = reviewQuery.reviewDates(reviewCache);
		reviewsList = reviewsNames;
		resultPageScrollList.setItems(reviewsList);

		for (int i = 0; i < resultPageScrollList.getItems().size(); i++) {
			resultPageScrollList.getItem(i).put("text",
					reviewsNames.get(i) + "\n" + reviewsText.get(i) + "\n" + reviewsDates.get(i));
		}
	}

	public ArrayList<String> shortenReviewText(ArrayList<String> reviewText) {
		// Matthew Henry -- shortens review text based on the current screen demensions so they fit correctly on the reivew scroll list
		ArrayList<String> shortenedTexts = new ArrayList<String>();
		int StringLength = (int) screenWidth / 24;
		for (String text : reviewText) {
			String shortened = text.substring(0, StringLength) + "...";
			shortenedTexts.add(shortened);
		}
		return shortenedTexts;
	}

	public void makeConnections() {
		// Matthew Henry -- Makes database connections as well as finding the users rough lat/long based on the current IP address
		URL ipapi;
		try {
			db.connectToDB("yelp_db", "pprojectroot", "QAZwsx4321!");
			ipapi = new URL("https://ipapi.co/latlong/");
			URLConnection c = ipapi.openConnection();
			c.setRequestProperty("User-Agent", "java-ipapi-client");
			BufferedReader reader = new BufferedReader(new InputStreamReader(c.getInputStream()));
			userLocation = reader.readLine().split(",");

			reader.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void showMap() {
		// Matthew Henry -- Functionality for hiding/showing the items on the home page once the Map is shown on screen
		if (activateMap == false) {
			activateMap = true;
			searchButton.hide();
			nearMe.hide();
		
			mostReviewed.hide();
			starsRating.hide();
			searchField.hide();
			businessesScrollList.hide();
			categoryDrop.hide();

		} else if (activateMap == true) {

			activateMap = false;
			searchButton.show();
			nearMe.show();
		
			mostReviewed.show();
			starsRating.show();
			searchField.show();
			businessesScrollList.show();
			categoryDrop.show();
		}
	}
}
