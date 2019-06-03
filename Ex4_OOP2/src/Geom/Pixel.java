package Geom;
/**
 * This class represents a pixel
 * This class has method to find distance between two pixels and a method
 * to find the angle in degrees between them.
 * @author Alex vaisman, Shay naor.
 */
public class Pixel {
	private int x;
	private int y;

	public Pixel(int x, int y) {
		this.x = x;
		this.y = y;
	}


	/**
	 * This function finds the distance between two pixels.
	 * @param a , first pixel
	 * @param b , second pixel
	 * @return , the distance between the two in pixels.
	 */
	public double distancePixel(Pixel a, Pixel b) {
		double x1 = a.getX();
		double x2 = b.getX();
		double y1 = a.getY();
		double y2 = b.getY();
		double distance;

		distance = Math.sqrt(Math.pow((x2 - x1), 2) + Math.pow((y2 - y1), 2));
		return distance;
	}
	/**
	 * This function find the angle between pixel a to pixel b in degrees.
	 * @param a , the first pixel
	 * @param b , the second pixel
	 * @return the angel in degrees between a and b.
	 */
	public double anglePixel(Pixel a, Pixel b) {
		double dist = distancePixel(a, b);
		double dx;
		double angle;
		dx = b.getX() - a.getX();
		angle = Math.acos(dx / dist);
		angle = Math.toDegrees(angle);
		return angle;
	}

	public String toString() {
		return "("+ x + "," + y + ")";
	}

	//=======Getters=========
	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}
}
