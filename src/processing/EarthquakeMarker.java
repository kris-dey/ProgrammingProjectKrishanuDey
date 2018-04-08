//Brian Lynch - a marker made using PGraphics so we can customize it and it is clearer than an image 
package processing;
import processing.core.*;
import de.fhpotsdam.unfolding.geo.Location;
import de.fhpotsdam.unfolding.marker.SimplePointMarker;
 
public class EarthquakeMarker extends SimplePointMarker {
 
  public EarthquakeMarker(Location location) {
    super(location);
  }
 
  public void draw(PGraphics pg, float x, float y) {
    pg.pushStyle();
    pg.noStroke();
    pg.fill(0, 0, 0);
    pg.ellipse(x, y-18, 15, 15);
    pg.fill(255, 255,255,255);
    pg.ellipse(x, y-18, 7, 7);
    pg.popStyle();
    pg.noStroke();
    pg.fill(0,0,0);
    pg.triangle(x, y, x-7, y-15, x+7, y-15);
    
  }
}