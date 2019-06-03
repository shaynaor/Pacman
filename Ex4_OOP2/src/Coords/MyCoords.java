package Coords;

import Geom.Point3D;

public class MyCoords implements coords_converter {
	/**
	 * this function receive a gps point and point in meters. transforms the gps
	 * point to xyz ecef and moves it by the values recived in the meters point. If
	 * the gps point is invalid the function throws RuntimeException.
	 */
	public Point3D add(Point3D gps, Point3D local_vector_in_meter) {
		if (!isValid_GPS_Point(gps)) {
			System.err.println("Ivalid gps point, the values must be: "
					+ " lat, lon , lat coordinate: [-90,+90],[-180,+180],[-450, +inf]");
			throw new RuntimeException();
		}
		Point3D p = new Point3D(ConvertGpsToXyz.lla2ecef(gps));
		p.add(local_vector_in_meter);
		p = ConvertGpsToXyz.ecef2lla(p);
		return p;
	}

	/**
	 * this function finds the 3d distance between 2 gps points by converting the
	 * points into ecef . If the gps point is invalid the function throws
	 * RuntimeException.
	 */
	public double distance3d(Point3D gps0, Point3D gps1) {
		if (!isValid_GPS_Point(gps0)) {
			System.err.println("Ivalid gps point, the values must be: "
					+ " lat, lon , lat coordinate: [-90,+90],[-180,+180],[-450, +inf]");
			throw new RuntimeException();
		}
		Point3D p0 = new Point3D(ConvertGpsToXyz.lla2ecef(gps0));
		Point3D p1 = new Point3D(ConvertGpsToXyz.lla2ecef(gps1));
		double distance = p0.distance3D(p1);
		return distance;
	}

	/**
	 * this function returns a vector between 2 gps points. If the gps point is
	 * invalid the function throws RuntimeException.
	 */
	public Point3D vector3D(Point3D gps0, Point3D gps1) {
		if (!isValid_GPS_Point(gps0) || !isValid_GPS_Point(gps1)) {
			System.err.println("Ivalid gps point, the values must be: "
					+ " lat, lon , lat coordinate: [-90,+90],[-180,+180],[-450, +inf]");
			throw new RuntimeException();
		}

		Point3D p0 = new Point3D(ConvertGpsToXyz.lla2ecef(gps0));
		Point3D p1 = new Point3D(ConvertGpsToXyz.lla2ecef(gps1));

		double x = p1.x() - p0.x();
		double y = p1.y() - p0.y();
		double z = p1.z() - p0.z();

		Point3D p0Top1 = new Point3D(x, y, z);

		return p0Top1;
	}

	/**
	 * this functions calculates the azimuth , elevation and distance between 2
	 * points. returns an array with answers. If the gps point is invalid the
	 * function throws RuntimeException.
	 */
	public double[] azimuth_elevation_dist(Point3D gps0, Point3D gps1) {
		if (!isValid_GPS_Point(gps0) || !isValid_GPS_Point(gps1)) {
			System.err.println("Ivalid gps point, the values must be: "
					+ " lat, lon , lat coordinate: [-90,+90],[-180,+180],[-450, +inf]");
			throw new RuntimeException();
		}

		double azimuth, elevation, distance, h;
		double ans[] = { 0, 0, 0 };
		MyCoords cord = new MyCoords();

		// * calculation azimuth *
		double longitude1 = gps0.y();
		double longitude2 = gps1.y();
		double latitude1 = Math.toRadians(gps0.x());
		double latitude2 = Math.toRadians(gps1.x());
		double longDiff = Math.toRadians(longitude2 - longitude1);
		double y = Math.sin(longDiff) * Math.cos(latitude2);
		double x = Math.cos(latitude1) * Math.sin(latitude2)
				- Math.sin(latitude1) * Math.cos(latitude2) * Math.cos(longDiff);

		azimuth = (Math.toDegrees(Math.atan2(y, x)) + 360) % 360;
		// * calculating distance *
		distance = cord.distance3d(gps0, gps1);

		// * calculating elevation*
		h = gps1.z() - gps0.z();

		double temp = Math.asin(h / distance);
		elevation = Math.toDegrees(temp);

		ans[0] = azimuth;
		ans[1] = elevation;
		ans[2] = distance;
		return ans;
	}

	/**
	 * this function checks if the gps point is valid.
	 */
	public boolean isValid_GPS_Point(Point3D p) {
		if (p == null)
			return false;

		double lat = p.x(), lon = p.y(), alt = p.z();

		if (lat < -90 && lat > 90)
			return false;

		if (lon < -180 && lon > 180)
			return false;

		if (alt < -450)
			return false;

		return true;
	}

}