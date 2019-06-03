package GIS;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import Coords.MyCoords;
import Geom.Point3D;

/**
 * This class represents a player.
 * player has a gps point ,radius,speed,orientation and an image associated with it.
 * it also has a list of what the player sees.
 * @author Alex vaisman, Shay naor.
 *
 */
public class Player {
	private int id;
	private double radius;
	private double speed;
	private Point3D gps;
	private double orientation;
	private ArrayList<Corner> whatISee;
	private BufferedImage myImage;

	public Player(String[] line) throws ParseException {
		id = Integer.parseInt(line[1]);
		
		double lat = Double.parseDouble(line[2]);
		double lon = Double.parseDouble(line[3]);
		double alt = Double.parseDouble(line[4]);
		this.gps = new Point3D(lat, lon, alt);
		
		this.speed = Double.parseDouble(line[5]);
		this.radius = Double.parseDouble(line[6]);
		this.orientation = 0;
		this.whatISee = new ArrayList<Corner>();
		
		/* Player GUI image */
		try {
			this.myImage = ImageIO.read(new File("images\\player70x70.png"));
		} catch (IOException e) {
			System.err.println("ERROR: incorrect path for picture!");
			e.printStackTrace();
		}
		
		
	}
    /**
     * This function finds the azimuth from the player to a gps point.
     * @param gps
     */
	public void findOrientation(Point3D gps) {
		MyCoords coords = new MyCoords();
		double arr[] = new double[3];
		arr = coords.azimuth_elevation_dist(this.gps, gps);
		orientation = arr[0];
	}
	
	/* Getters */
	public Point3D getGps() {
		return gps;
	}

	public double getOrientation() {
		return orientation;
	}
	

	public int getId() {
		return id;
	}

	public double getRadius() {
		return radius;
	}

	public double getSpeed() {
		return speed;
	}

	public void setOrientation(double orientation) {
		this.orientation = orientation;
	}

	public ArrayList<Corner> getWhatISee() {
		return whatISee;
	}

	public BufferedImage getMyImage() {
		return myImage;
	}

}
