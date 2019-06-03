package Coords;

import Geom.Pixel;
import Geom.Point3D;
import Gui.Map;
/**
 * This class receives a map with gps cords on the top left and bot right side of the map.
 * and will convert a gps point to a pixel depending on map pixel size.
 * and it can convert pixel point back to gps point.
 * @author Alex vaisman ,Shay naor.
 */
public class Convert_pixel_gps {


	private Map map;
	private double ratioHeight;
	private double ratioWidth;

	public Convert_pixel_gps(Map map) {
		this.map = map;
		RpixelMeters();
	}


    /**
     * This function finds the ration between one pixel to meters.
     * it will find the ratio for map height and map width.
     */
	private void RpixelMeters() {

		Point3D botLeft = new Point3D(map.getBotRight().x(), map.getTopLeft().y());
		MyCoords coords = new MyCoords();
		double distanceHeight;
		double distanceWidth;

		/* Finding Height in meters */
		distanceHeight = coords.distance3d(map.getTopLeft(), botLeft);
		distanceHeight /= this.map.getHeight();
		
		/* Finding Width in meters */
		distanceWidth = coords.distance3d(botLeft, map.getBotRight());
		distanceWidth /= this.map.getWidth();

		this.ratioHeight =distanceWidth; 
		this.ratioWidth = distanceHeight;
	}
 
	/**
	 * This function receives a gps point and converts it to pixel.
	 * It will convert it to pixel using the ratios for pixel to meter.
	 * @param gps the gps point the function needs to convert.
	 * @return a pixel.
	 */
	public Pixel convertGPStoPixel(Point3D gps) {
		if (!isIn(gps)) {
			System.err.println("Ivalid gps point, the gps point is outside the map!");
			throw new RuntimeException();
		}
		MyCoords coords = new MyCoords();

		double[] info = coords.azimuth_elevation_dist(gps, map.getTopLeft());
		double azi = info[0];
		double distance = coords.distance3d(gps, map.getTopLeft());
		double dy;
		double dx;
		int pixelX;
		int pixelY;
		/* Converting azi to degrees and then to radians */
		azi = azi % 90;
		azi = Math.toRadians(azi);

		/* Finding right triangle a side */
		dy = distance * Math.sin(azi);
		/* Finding right triangle b side */
		dx = distance * Math.cos(azi);

		pixelX = (int) (dx / ratioHeight);
		pixelY = (int) (dy / ratioWidth);

		Pixel pixel = new Pixel(pixelX, pixelY);

		return pixel;
	}

	
	/**
	 * This function receives a pixel and converts it to a gps point .
	 * @param pixel , the pixel the function received.
	 * @return a gps point.
	 */
	public Point3D convertPixeltoGPS(Pixel pixel) {
		
		double pixelX = pixel.getX();
		double pixelY = pixel.getY();

		/* Calculate lon */
		double deltaLon = map.getBotRight().y() - map.getTopLeft().y();
		double y = pixelX * deltaLon;
		y /= this.map.getWidth();
		y += map.getTopLeft().y();

		/* Calculate lat */
		double deltaLat = map.getTopLeft().x() - map.getBotRight().x();
		double x = pixelY * deltaLat;
		x /= this.map.getHeight();
		x -= map.getTopLeft().x();
		x = Math.abs(x);

		Point3D gps = new Point3D(x, y);

		return gps;
	}
  
	
	/**
	 * The function checks for a given gps point p if its in the map limit.
	 * @param p the gps point the function checks.
	 * @return if its in or not.
	 */
	public boolean isIn(Point3D p) {
		if (p == null)
			return false;

		double lat = p.x(), lon = p.y();

		if (lat > map.getTopLeft().x() || lat < map.getBotRight().x())
			return false;

		if (lon > map.getBotRight().y() || lon < map.getTopLeft().y())
			return false;

		return true;
	}
	
	
	//======Getters======
	public double getRatioHeight() {
		return ratioHeight;
	}

	public double getRatioWidth() {
		return ratioWidth;
	}

}
