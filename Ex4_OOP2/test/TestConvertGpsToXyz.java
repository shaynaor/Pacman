import static org.junit.jupiter.api.Assertions.*;

import java.util.concurrent.ThreadLocalRandom;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import Coords.ConvertGpsToXyz;
import Geom.Point3D;

class TestConvertGpsToXyz {

	@Test
	/**
	 * this function creates 10000 random gps points creates a copy of that point. 
	 * * converts it to ecef and back to lla and checks if the result is the same to 0.0001 accuracy
	 */
	void ConvertPoints() {
		double lat;
		double lon;
		double alt;
		ConvertGpsToXyz convert = new ConvertGpsToXyz();

		for(int i = 0;i < 10000;i++) {
			lat = ThreadLocalRandom.current().nextInt(-90, 90 + 1);
			lon = ThreadLocalRandom.current().nextInt(-90, 90 + 1);
			alt = ThreadLocalRandom.current().nextInt(-450, 20000 + 1);
			Point3D gps = new Point3D(lat,lon,alt);
			Point3D tempecef = convert.lla2ecef(gps);
			Point3D changed = convert.ecef2lla(tempecef);
			double deltaX = gps.x()-changed.x();
			double deltaY = gps.y()-changed.y();
			double deltaZ = gps.z()-changed.z();

			if(deltaX>0.0001||deltaX<-0.0001||deltaZ>0.0001||deltaZ<-0.0001||deltaY>0.0001||deltaY<-0.0001) {
				fail("Not yet implemented");
			}

		}



	}


}
