package Gui;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;

import javax.imageio.ImageIO;

import Geom.Point3D;

/**
 * This class represents a map. a map is an object that receives an image of an
 * area, and the gps points of the top left and bottom right corners of the
 * image. it will also same the value of how much pixels are in the image. as
 * heightXwidth.
 * 
 * @author Alex vaisman, Shay naor.
 *
 */
public class Map {
	private BufferedImage myImage;
	private int width;
	private int height;
	private Point3D topLeft;
	private Point3D botRight;

	/**
	 * This function loads an image and inputs the topleft and bot right gps values.
	 * @param bounds , the bounds of the map
	 */
	public Map(String bounds) {
		/* INIT myImge filed */
		try {
			this.myImage = ImageIO.read(new File("images\\Ariel1.png"));
		} catch (IOException e) {
			System.err.println("ERROR: incorrect path for picture!");
			e.printStackTrace();
		}

		String[] userInfo = {};
		userInfo = bounds.split(",");


		double lat = Double.parseDouble(userInfo[2]);
		double lon = Double.parseDouble(userInfo[3]);

		Point3D botLeft = new Point3D(lat, lon, 0);

		lat = Double.parseDouble(userInfo[5]);
		lon = Double.parseDouble(userInfo[6]);

		Point3D topRight = new Point3D(lat, lon, 0);



		this.width = myImage.getWidth();
		this.height = myImage.getHeight();
		this.topLeft = new Point3D(topRight.x(), botLeft.y());
		this.botRight = new Point3D(botLeft.x(), topRight.y());


	}

	/* Getters */
	public BufferedImage getMyImage() {
		return myImage;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public Point3D getTopLeft() {
		return topLeft;
	}

	public Point3D getBotRight() {
		return botRight;
	}

	public void setTopLeft(Point3D topLeft) {
		this.topLeft = topLeft;
	}

	public void setBotRight(Point3D botRight) {
		this.botRight = botRight;
	}


}