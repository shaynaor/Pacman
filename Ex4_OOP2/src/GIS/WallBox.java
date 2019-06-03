package GIS;

import java.util.ArrayList;

import Algo.Segment;
/**
 * This class represents a wallbox.
 * a wallbox has 4 segments and each represents a wall .
 * @author Alex vaisman, Shay naor.
 *
 */
public class WallBox {

	private ArrayList<Segment> walls;

	public WallBox(Box box) {
		this.walls = new ArrayList<Segment>();
		Segment leftWall = new Segment(box.getBotLeft(), box.getTopLeft());
		Segment rightWall = new Segment(box.getBotRight(), box.getTopRight());
		Segment topWall = new Segment(box.getTopLeft(), box.getTopRight());
		Segment botWall = new Segment(box.getBotLeft(), box.getBotRight());
		walls.add(botWall);
		walls.add(leftWall);
		walls.add(rightWall);
		walls.add(topWall);
	}

	public String toString() {
		return "WallBox [walls=" + walls + "]";
	}
	/* Getters */

	public ArrayList<Segment> getWalls() {
		return walls;
	}

}
