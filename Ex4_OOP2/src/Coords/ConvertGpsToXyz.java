package Coords;

import java.util.Arrays;
import Geom.Point3D;

/**
 * This class converts a Decimal degrees latitude longitude altitude point, into
 * a ECEF(earth-centered, earth-fixed) x,y,z point and the other way around.
 * ECEF wiki link: https://en.wikipedia.org/wiki/ECEF
 *
 * @author Alex Vaisman , Shay naor
 *
 */

public class ConvertGpsToXyz {

	public static final double a = 6378137; // the equatorial earth radius.
	public static final double f = 1 / 298.257223563; // WGS84 flattening wiki link:
														// https://en.wikipedia.org/wiki/Flattening
	public static final double b = a * (1 - f);
	public static final double e = Math.sqrt((Math.pow(a, 2) - Math.pow(b, 2)) / Math.pow(a, 2));
	public static final double e2 = Math.sqrt((Math.pow(a, 2) - Math.pow(b, 2)) / Math.pow(b, 2));

	/**
	 * converting a ECEF x,y,z point in meters to lla (latitude longitude
	 * altitude)point. The formula taken from :
	 * https://stackoverflow.com/questions/18253546/ecef-to-lla-lat-lon-alt-in-java.
	 * 
	 * @param vec the x,y,z point the function receives.
	 * @return lla point
	 */
	public static Point3D ecef2lla(Point3D vec) {

		double lat, lon, height, N, theta, p;
		double x = vec.x(), y = vec.y(), z = vec.z();

		p = Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2));
		theta = Math.atan((z * a) / (p * b));
		lon = Math.atan(y / x);
		lat = Math.atan(((z + Math.pow(e2, 2) * b * Math.pow(Math.sin(theta), 3))
				/ ((p - Math.pow(e, 2) * a * Math.pow(Math.cos(theta), 3)))));
		N = a / (Math.sqrt(1 - (Math.pow(e, 2) * Math.pow(Math.sin(lat), 2))));

		double m = (p / Math.cos(lat));
		height = m - N;

		lon = lon * 180 / Math.PI;
		lat = lat * 180 / Math.PI;

		Point3D point = new Point3D(lat, lon, height);
		return point;
	}

	/**
	 * Converts a lla point into a ECEF x,y,z point. The formula taken from :
	 * http://mathforum.org/library/drmath/view/51832.html.
	 * 
	 * @param vec The lla point the function receives.
	 * @return ECEF point.
	 */
	public static Point3D lla2ecef(Point3D vec) {

		double lat = vec.x(), lon = vec.y(), h = vec.z();

		double cosLat = Math.cos(lat * Math.PI / 180.0);
		double sinLat = Math.sin(lat * Math.PI / 180.0);
		double cosLon = Math.cos(lon * Math.PI / 180.0);
		double sinLon = Math.sin(lon * Math.PI / 180.0);

		double C = 1.0 / Math.sqrt(cosLat * cosLat + (1 - f) * (1 - f) * sinLat * sinLat);
		double S = (1.0 - f) * (1.0 - f) * C;
		double x = (a * C + h) * cosLat * cosLon;
		double y = (a * C + h) * cosLat * sinLon;
		double z = (a * S + h) * sinLat;

		Point3D point = new Point3D(x, y, z);

		return point;
	}

}