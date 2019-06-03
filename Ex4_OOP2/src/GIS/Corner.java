package GIS;

import java.util.ArrayList;

import Geom.Point3D;

/**
 * This function represents a corner.
 * a corner has a gps point , and a list of what it sees,one for fruits
 * and one for corners.
 * it has a unique id as well.
 * @author Alex vaisman, Shay naor
 *
 */
public class Corner {

	private Point3D gps;
	private ArrayList<Corner> whatISee;
	private ArrayList<Fruit> whatFruitIsee;
	private static int NextId = 0;
	private final int myId;

	public Corner (Point3D corner) {
		this.gps = corner;
		this.myId = NextId++;
		this.whatISee = new ArrayList<Corner>();
		this.whatFruitIsee = new ArrayList<Fruit>();
	}



	/* Getters */

	public Point3D getGps() {
		return gps;
	}

	public ArrayList<Corner> getWhatISee() {
		return whatISee;
	}

	public int getMyId() {
		return myId;
	}

	public ArrayList<Fruit> getWhatFruitIsee() {
		return whatFruitIsee;
	}


}
