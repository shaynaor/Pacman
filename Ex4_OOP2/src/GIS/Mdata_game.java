package GIS;

import java.text.ParseException;

/**
 * This class represents the Meta data of pacman and fruit . for pacman it will
 * save his id , speed and eating radius. for fruit it will save its id and
 * Weight.
 * 
 * @author Alex vaisman, Shay naor.
 *
 */
public class Mdata_game implements Meta_data {
	private int id;
	private double speedWeight;
	private double radius;

	/**
	 * Constructor , receives a string. will input the correct value depending on if
	 * the line is a pacman, fruit or ghost.
	 * 
	 * @param meta
	 * @throws ParseException
	 */
	public Mdata_game(String[] meta) throws ParseException {
		this.id = Integer.parseInt(meta[1]);
		this.speedWeight = Double.parseDouble(meta[5]);
		/* If the line represent ghost or pacman getradius from meta[6] */
		if (meta[0].contains("P") || meta[0].contains("G")) {
			this.radius = Double.parseDouble(meta[6]);
		}
	}


	/**
	 * Constructor, creates a default pacman or fruit with default values of
	 * speedWeight = 1, radius - 1.
	 * 
	 * @param id
	 * @param isPacman
	 */
	public Mdata_game(int id, boolean isPacman) {
		this.id = id;
		this.speedWeight = 1;
		if (isPacman)
			this.radius = 1;

	}
	

	public String toString() {
		return "Mdata_game: id=" + id + ", speedWeight=" + speedWeight + ", radius=" + radius ;
	}

	/* Getters */
	public int getId() {
		return id;
	}

	public double getSpeedWeight() {
		return speedWeight;
	}

	public double getRadius() {
		return radius;
	}
}
