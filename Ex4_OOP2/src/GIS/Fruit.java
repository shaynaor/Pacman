package GIS;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.ParseException;

import javax.imageio.ImageIO;

import Geom.Point3D;

/**
 * This class represents a Fruit. A fruit is an object with a gps point and an
 * id.
 * 
 * 
 * @author Alex Vaisman ,Shay naor.
 *
 */
public class Fruit {


	private Point3D gps;
	private Mdata_game metaData;
	private int fruitsNearMe;
	private BufferedImage myImage;

	/**
	 * Fruit Constructor receives a line from a CSV file and creates a fruit from
	 * the given data.
	 * 
	 * @param line , a line from CSV file.
	 * @throws ParseException , Throws ParseException if CSV data is incorrect.
	 */
	public Fruit(String[] line) throws ParseException {
		double lat = 0, lon = 0, alt = 0;
		lat = Double.parseDouble(line[2]);
		lon = Double.parseDouble(line[3]);
		alt = Double.parseDouble(line[4]);

		this.gps = new Point3D(lat, lon, alt);
		this.metaData = new Mdata_game(line);
		this.fruitsNearMe = 0;
		
		/* Fruit GUI image */
		try {
			this.myImage = ImageIO.read(new File("images\\cherry.png"));
		} catch (IOException e) {
			System.err.println("ERROR: incorrect path for picture!");
			e.printStackTrace();
		}
		
	}

	/**
	 * Fruit constructor , creates a fruit from gps lat and lon.
	 * 
	 * @param x  , the lat value.
	 * @param y  , the lon value.
	 * @param id , the id of the fruit.
	 */
	public Fruit(double x, double y, int id) {
		double lat = x, lon = y, alt = 0;
		this.gps = new Point3D(lat, lon, alt);

		this.metaData = new Mdata_game(id, false);
	}

	/* Getters */
	public Point3D getGps() {
		return gps;
	}

	public Mdata_game getMetaData() {
		return metaData;
	}

	public Meta_data getData() {
		return this.metaData;
	}

	public int getFruitsNearMe() {
		return fruitsNearMe;
	}

	public void setFruitsNearMe() {
		this.fruitsNearMe ++;
	}

	public BufferedImage getMyImage() {
		return myImage;
	}
}