//Brian Lynch - A map class which can create a map object
package processing;

import java.util.ArrayList;

import de.fhpotsdam.unfolding.UnfoldingMap;
import de.fhpotsdam.unfolding.geo.Location;
import de.fhpotsdam.unfolding.marker.SimplePointMarker;
import processing.core.PApplet;

public class Map {
	UnfoldingMap map;
	Location location;
	PApplet canvas;
	public Map(Location location, UnfoldingMap map) {
		this.map = map;
		this.location = location;
		
		
	}
	public void draw() {
		map.draw();
	}
	public void addMarker() {
		SimplePointMarker marker = new EarthquakeMarker(location);
		map.zoomAndPanTo(15, new Location(location));
		map.addMarker(marker);
	}
	//MAtthew Henry
	public void addMultipleMarkers(ArrayList<Location> locations) {
		SimplePointMarker marker = null;
		for(Location l : locations) {
			marker = new EarthquakeMarker(l);
			map.addMarker(marker);
		}
		map.zoomAndPanToFit(locations);
	}
	
}
