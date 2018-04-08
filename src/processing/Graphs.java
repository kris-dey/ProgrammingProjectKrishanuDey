/*Brian Lynch - sets up grafica so it can be easily used and extended for other graphs 
creates a histogram and dynamically takes in data fromt he SQL depending on which business is clicked.
				
				*/
package processing;
import processing.core.PApplet;
import processing.core.PImage;
import java.util.ArrayList;

import grafica.*;

public class Graphs extends Main {

	// Histogram bar colors
	int[] colours = { color(190, 0, 0), color(255, 120, 0), color(255, 170, 0), color(50, 255, 50), color(0, 190, 0), };
	// Histogram bar outline color
	int[] colour = { 250 };
	// dimensions of the chart array (filled by the graph parameters)
	float[] dimensions = new float[2];

	public GPlot plot;
	public PImage star;
	public PApplet p;
	public ArrayList<Integer> stars;
	public MySQLAccess connection;
	public int nPoints;
	public GPointsArray points;

	public Graphs(float x, float y, GPlot plot, PApplet p, MySQLAccess connection) {
		// 350 x 200
		this.plot = plot;
		this.star = star;
		this.connection = connection;
		// dimensions of graph
		dimensions[0] = x;
		dimensions[1] = y;
		points = new GPointsArray();
		// have to take PApplet as a parameter "p"
		this.p = p;

		// take in points SQL

		// Setting up histogram
		plot.setBgColor(color(0, 0, 0, 120));
		plot.setTitleText("Star Rating");
		plot.setDim(dimensions);

		plot.getTitle().setFontSize(24);
		plot.getTitle().setFontColor(255);
		plot.startHistograms(GPlot.HORIZONTAL);
		plot.getHistogram().setBgColors(colours);
		plot.getHistogram().setLineColors(colour);
		plot.getYAxis().setRotateTickLabels(false);
		plot.getYAxis().setLineColor(255);
		plot.getXAxis().setLineColor(255);
		plot.getYAxis().setFontColor(255);
		plot.getXAxis().setFontColor(255);
		plot.getYAxis().setDrawTickLabels(false);
		plot.getYAxis().setTicksSeparation(1);
		
	}

	public void changePoints(String businessID) {
		stars = connection.getBusinessStars(businessID, 1000);
		points = new GPointsArray(stars.size());
		int highestStars = 0;
		for (int i = 0; i < stars.size(); i++) {
			if (highestStars < stars.get(i)) {
				highestStars = stars.get(i);
			}
			points.add(stars.get(i), i + 1);
		}
		plot.setXLim(0, highestStars);
		plot.setPoints(points);
		System.out.println(stars.get(4));
	}

	public void draw(PImage star) {

		
		plot.beginDraw();
		p.noStroke();
		p.fill(0,0,0,155);
		p.rect((float) -(screenWidth/11.3833),(float)-(screenHeight/3.490),(float) (screenWidth/2.4),(float)(screenHeight/2.953));
		
		plot.setPoints(points);
		//plot.drawBackground();
		plot.drawYAxis();
		plot.drawXAxis();
		plot.drawTitle();
		plot.drawHistograms();


		// Draws the stars on the side of the graph

		//5 star 
		p.image(star,(float) -(screenWidth/45.53), (float) -(screenHeight / 4.517));
		p.image(star,(float) -(screenWidth/27.32), (float) -(screenHeight / 4.517)); 
		p.image(star,(float) -(screenWidth/19.51),(float) -(screenHeight / 4.517)); 
		p.image(star,(float) -(screenWidth/15.17), (float) -(screenHeight / 4.517)); 
		p.image(star,(float) -(screenWidth/12.418),(float) -(screenHeight / 4.517)); 
		//4 star
		p.image(star,(float) -(screenWidth/45.53), (float) -(screenHeight / 5.774));
		p.image(star,(float) -(screenWidth/27.32), (float) -(screenHeight / 5.774)); 
		p.image(star,(float) -(screenWidth/19.51),(float) -(screenHeight / 5.774)); 
		p.image(star,(float) -(screenWidth/15.17), (float) -(screenHeight / 5.774)); 

		//3 star 
		p.image(star,(float) -(screenWidth/45.53), (float) -(screenHeight / 7.8367));
		p.image(star,(float) -(screenWidth/27.32), (float) -(screenHeight / 7.8367)); 
		p.image(star,(float) -(screenWidth/19.51),(float) -(screenHeight / 7.8367)); 
		//2 star 
		p.image(star,(float) -(screenWidth/45.53), (float) -(screenHeight / 12.19));
		p.image(star,(float) -(screenWidth/27.32), (float) -(screenHeight / 12.19)); 
		//1 star 
		p.image(star,(float) -(screenWidth/45.53), (float) -(screenHeight / 28.44));


		plot.endDraw();
	}
	//Matthew henry -- generates a scatterplot extending the graphs class and using the graphica library.
	//					scatterplot displays reviews by month through the results on a SQL query. Points are changed dynamically based on the business that is selected
	//					to be displayed
	public class ScatterPlot extends Graphs{

		private final String[] MONTHS = {"January", "February", "March", "April","May", "June", "July", "August", "September", "October", "November", "December"};
		public ScatterPlot(float x, float y, GPlot plot, PImage star, PApplet p, MySQLAccess connection) {
			super(x, y, plot, p, connection);
			plot.setHistVisible(false);
			plot.setTitleText("Reviews By Month");
			plot.setLineColor(color(255));
			plot.getXAxis().setFixedTicks(true);
			plot.getXAxis().setDrawTickLabels(true);
			plot.getYAxis().setDrawTickLabels(true);
			float[] xAxisTick = {1,2,3,4,5,6,7,8,9,10,11,12};
			plot.activatePointLabels();
			plot.getYAxis().setTicksSeparation(1);
			plot.getXAxis().setTicksSeparation(1);
			plot.getXAxis().setTicks(xAxisTick);
			plot.getXAxis().setTickLabels(MONTHS);
			plot.getXAxis().setRotateTickLabels(true);
			
		}
		public void draw() {
			plot.beginDraw();
			plot.setPoints(points);
			plot.drawBackground();
			plot.drawYAxis();
			
			plot.drawXAxis();
			plot.drawTitle();
			plot.drawPoints();
			plot.drawLines();
			plot.drawLabels();
			plot.endDraw();
		}
		
		public void changePoints(String businessID, Query reviewQuery) {
			reviews = connection.getReviewsByBusiness(businessID, 1000);
			ArrayList<String> r = reviewQuery.reviewDates(reviews);
			int[] months = new int[12];
			for(String rMonth : r) {
				
				int currentMonth = Integer.parseInt(rMonth.split("/")[1]);
				switch(currentMonth) {
				case(1):months[0] = months[0]+1;
						break;
				case(2):months[1] = months[1]+1;
						break;
				case(3):months[2] = months[2]+1;
						break;
				case(4):months[3] = months[3]+1;
						break;
				case(5):months[4] = months[4]+1;
						break;
				case(6):months[5] = months[5]+1;
						break;
				case(7):months[6] = months[6]+1;
						break;
				case(8):months[7] = months[7]+1;
						break;
				case(9):months[8] = months[8]+1;
						break;
				case(10):months[9] = months[9]+1;
						break;
				case(11):months[10] = months[10]+1;
						break;
				case(12):months[11] = months[11]+1;
				}
				
			}
			points = new GPointsArray(months.length);
			
			for(int i = 0; i < months.length;i++) {
				points.add(i+1, months[i], MONTHS[i]);
			}
			plot.setPoints(points);
		}
		
	}
}
