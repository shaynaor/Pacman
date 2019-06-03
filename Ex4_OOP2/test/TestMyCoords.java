import static org.junit.jupiter.api.Assertions.*;

import java.util.concurrent.ThreadLocalRandom;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import Coords.ConvertGpsToXyz;
import Coords.MyCoords;
import Geom.Point3D;

class TestMyCoords {

	@Test
	/**
	 * this function moves a point in the x or y or z direction, and then checks the
	 * distance that the point moved in that direction compared to what it was told
	 * to move. we check 1000 points and move them between 0 to 2000km accuracy
	 * offset allowed for x is 0.001% accuracy offset allowed for y is 0.001%
	 */
	void AddMetersToPoint() {
		double lat;
		double lon;
		double alt;
		MyCoords add = new MyCoords();

		for (int i = 0; i < 10000; i++) {
			lat = ThreadLocalRandom.current().nextInt(-90, 90 + 1);
			lon = ThreadLocalRandom.current().nextInt(-90, 90 + 1);
			alt = ThreadLocalRandom.current().nextInt(-450, 20000 + 1);
			double x = ThreadLocalRandom.current().nextInt(0, 2000000 + 1);
			double y = ThreadLocalRandom.current().nextInt(0, 2000000 + 1);
			Point3D gpsX = new Point3D(lat, lon, alt);
			Point3D gpsY = new Point3D(lat, lon, alt);
			Point3D moveX = new Point3D(x, 0, 0);
			Point3D moveY = new Point3D(0, y, 0);
			Point3D gpsXmoved = add.add(gpsX, moveX);
			Point3D gpsYmoved = add.add(gpsY, moveY);
			double distanceX = add.distance3d(gpsX, gpsXmoved);
			double distanceY = add.distance3d(gpsY, gpsYmoved);
			

			if (distanceX >= 1.0001 * x && x > 0.01 || distanceX <= 0.9999 * x || distanceY >= 1.0001 * y && y > 0.01) {
				fail("Moved to much or to liltle");
			}
		}
	}

	@Test
	/**
	 * testing distance function with two known distances and comparing them to the
	 * result.
	 */
	void Distance() {
		Point3D p1 = new Point3D(32.332, 35, 20);
		Point3D p2 = new Point3D(32.33, 35, 20);
		MyCoords distance = new MyCoords();
		double dp1p2 = 222;
		double checkd = distance.distance3d(p1, p2);
		double delta = dp1p2 - checkd;
		if (delta > 1 || delta < -1) {
			fail("Moved to much or to liltle");
		}

		Point3D p3 = new Point3D(32.85, 35.6, 20);
		Point3D p4 = new Point3D(32.85, 35.55, 20);

		double dp3p4 = 4671;
		checkd = distance.distance3d(p3, p4);
		delta = dp3p4 - checkd;
		if (delta > 10 || delta < -10) {
			fail("Moved to much or to liltle");
		}

	}

	@Test
	/**
	 * testing to see if function return currect azimuth and elevation compared to
	 * known result.
	 */
	void elevation_azimuth() {
		Point3D p1 = new Point3D(32.10332, 35.20904, 670);
		Point3D p2 = new Point3D(32.10635, 35.20523, 650);
		MyCoords azi = new MyCoords();
		double[] ans;
		ans = azi.azimuth_elevation_dist(p1, p2);
		double elevation = ans[1];
		double azimuth = ans[0];
		double curectELE = -2.3;
		double curectAZI = 313;
		double deltaELE = elevation - curectELE;
		double deltaAZI = azimuth - curectAZI;

		if (deltaELE > 1 || deltaELE < -1 || deltaAZI > 1 || deltaAZI < -1) {
			fail("to big of an error");
		}
	}

	@Test
	/**
	 * testing isValid_GPS_Point function.
	 */
	void isValid_GPS_Point() {
		double lat = 0;
		double lon = 0;
		double alt = 0;
		Point3D correctGpsPoint = new Point3D(lat, lon, alt);
		Point3D inCorrectGpsPoint = new Point3D(lat, lon, alt);
		MyCoords test = new MyCoords();

		for (int i = 0; i < 100; i++) {
			lat = ThreadLocalRandom.current().nextInt(-90, 90 + 1);
			lon = ThreadLocalRandom.current().nextInt(-180, 180 + 1);
			alt = ThreadLocalRandom.current().nextInt(-450, 20000 + 1);

			correctGpsPoint = new Point3D(lat, lon, alt);

			if (!test.isValid_GPS_Point(correctGpsPoint))
				fail("this is correct point shoud not fail!");
		}

		for (int i = 0; i < 1000; i++) {
			lat = ThreadLocalRandom.current().nextInt(-180, -92 + 1);
			lon = ThreadLocalRandom.current().nextInt(-500, -182 + 1);
			alt = ThreadLocalRandom.current().nextInt(-2000, -452 + 1);

			inCorrectGpsPoint = new Point3D(lat, lon, alt);

			if (test.isValid_GPS_Point(inCorrectGpsPoint))
				fail("this is correct point shoud not fail!");

			lat = ThreadLocalRandom.current().nextInt(91, 200 + 1);
			lon = ThreadLocalRandom.current().nextInt(181, 300 + 1);

			inCorrectGpsPoint = new Point3D(lat, lon, alt);

			if (test.isValid_GPS_Point(inCorrectGpsPoint))
				fail("this is correct point shoud not fail!");

		}

	}
}
