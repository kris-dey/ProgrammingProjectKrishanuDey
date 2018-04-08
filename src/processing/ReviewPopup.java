//Matthew Henry -- creates the popUP object which displays review data

package processing;

import controlP5.Button;
import controlP5.ControlP5;
import controlP5.ControlP5Constants;
import controlP5.Textarea;
import processing.core.PApplet;
import processing.core.PFont;
import processing.core.PImage;

public class ReviewPopup {
	private String reviewerName, reviewDate, reviewText;
	private int reviewStars, reviewUseful, reviewCool, reviewFunny;
	private ControlP5 screen;
	private PApplet canvas;
	private Button returnButton;
	private int x;
	private int y;
	private float width;
	private float height;
	private float screenHeight;
	private float screenWidth;
	private PFont font;
	private Textarea reviewTextSpace;
	private boolean drawSplash = false;
	
	//Constructs the required button and interaction required for the popup to function
	//also hides the underlying screen to prevent overDrawing
	public ReviewPopup(PApplet canvas, int x, int y, float width, float height, float screenWidth, float screenHeight) {
		super();
		screen = new ControlP5(canvas);
		this.canvas = canvas;
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.screenHeight = screenHeight;
		this.screenWidth = screenWidth;

		returnButton = screen.addButton("returnButton");
		returnButton.setWidth((int) (screenWidth / 10)).setHeight((int) (screenHeight / 20));
		returnButton.setPosition(this.x + (width - returnButton.getWidth()),
				(this.y + this.height) - returnButton.getHeight());
		returnButton.setColorBackground(canvas.color(0, 0, 0, 210)).setColorActive(canvas.color(255, 10, 10))
				.setColorForeground(canvas.color(255, 10, 10)).setLabel("Return");
		reviewTextSpace = screen.addTextarea("reviewTextSpace");
		reviewTextSpace.setWidth((int) width).setHeight((int) height);
		reviewTextSpace.setPosition((float) this.x, this.y + screenHeight / 25);
		reviewTextSpace.hideArrow().hideScrollbar().hideBar().setScrollActive(ControlP5Constants.INACTIVE);

		screen.hide();
	}
	//Draws the various atributes and icons which allow the user to see the various pieces of review data and text
	public void draw(PImage stars) {
		if (drawSplash) {
			canvas.fill(0, 0, 0, 210);
			canvas.rect(0, 0, (int) screenWidth, (int) screenHeight);
			canvas.pushStyle();
			canvas.fill(canvas.color(0, 0, 0, 180));
			canvas.noStroke();
			canvas.rect(x, y, width, height);
			canvas.popStyle();
			canvas.fill(255);
			canvas.textFont(font, screenHeight / 30);
			canvas.text(reviewerName, x, y + screenHeight / 30);
			canvas.textFont(font, screenHeight / 50);
			reviewTextSpace.setText(reviewText);
			canvas.textFont(font, screenHeight / 30);
			canvas.text("Stars", x, y + height - returnButton.getHeight() - screenHeight / 25);
			for (int i = 0; i < reviewStars; i++) {
				canvas.image(stars, x + (i * (int) (screenWidth / 34)),
						y + height - returnButton.getHeight() - screenHeight / 30);
			}
			canvas.text("Useful", x + screenWidth / 5, y + height - returnButton.getHeight() - screenHeight / 25);
			canvas.text(reviewUseful, x + screenWidth / 5, y + height - returnButton.getHeight());
			canvas.text("Cool", x + 2 * screenWidth / 5, y + height - returnButton.getHeight() - screenHeight / 25);
			canvas.text(reviewCool, x + 2 * screenWidth / 5, y + height - returnButton.getHeight());
			canvas.text("Funny", x + 3 * screenWidth / 5, y + height - returnButton.getHeight() - screenHeight / 25);
			canvas.text(reviewFunny, x + 3 * screenWidth / 5, y + height - returnButton.getHeight());
			screen.draw();
		} else {
			screen.hide();
		}

		if (returnButton.isMousePressed()) {
			returnButton();
		}
	}
	//uses the review obect selected to generate the required data for the review
	public void displayReview(Review selectedReview, PFont font) {
		PFont buttonFont = canvas.createFont("data\\AgencyFB-Reg-12.vlw", 12);
		screen.setFont(buttonFont);
		reviewTextSpace.setFont(font = canvas.createFont("data\\AgencyFB-Reg-32.vlw", screenHeight / 40));
		drawSplash = true;
		screen.show();
		reviewerName = selectedReview.getName();
		reviewDate = selectedReview.getDate().toString();
		reviewText = "\n\n" + selectedReview.getText();
		reviewStars = selectedReview.getStars();
		reviewUseful = selectedReview.getUseful();
		reviewCool = selectedReview.getCool();
		reviewFunny = selectedReview.getFunny();
		this.font = font;
	}
	//returns the underlying screen
	public void returnButton() {
		screen.hide();
		drawSplash = false;
	}
	//Lets the mainline UI know when a splash screen is drawn in order to hide non widget primitives 
	public boolean isDrawn() {
		return drawSplash;
	}

}
