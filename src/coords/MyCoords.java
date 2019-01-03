package coords;
import Geom.Point3D;

/**
 * @author Aviv Vexler
 * hold method to calculate in polar coordinates.
 */
public class MyCoords implements coords_converter {

	private final int radius = 6371000;//earth radius
	//private final double lonNorm = 0.847091174;
	private final double oneMeterInMile = 0.000621371192;
	
	private static MyCoords MY_COORDS;
	public static MyCoords GET_MY_COORDS() {
		if(MY_COORDS == null)
			MY_COORDS = new MyCoords();
		
		return MY_COORDS;
	}
	
	/**convert polar coordinate degrees to radian.
	 * @param p - some point in degrees.
	 * @return - the point in radian.*/
	private Point3D degreesToRadian(Point3D p) {
		return new Point3D(
				Point3D.d2r(p.x()),
				Point3D.d2r(p.y()),
				p.z()
				);
	}
	
	/**convert polar coordinate radian to degrees.
	 * @param p - some point in radian.
	 * @return - the point in degrees.*/
	private Point3D radianToDegrees(Point3D p) {
		return new Point3D(
				Point3D.r2d(p.x()),
				Point3D.r2d(p.y()),
				p.z()
				);
	}
	
	/** @param radianLatitude - Latitude in radian.
	 * @return lonNorm*/
	private double lonNorm(double radianLatitude) {
		return Math.cos(radianLatitude);
	}
	
	/**@see Coords.coords_converter#add(Geom.Point3D, Geom.Point3D) */
	@Override
	public Point3D add(Point3D gps, Point3D local_vector_in_meter) {
		
		//gps in radian
		Point3D gpsR = degreesToRadian(gps);// gps in radian
		
		double x = gpsR.x() - Math.asin( local_vector_in_meter.x()/radius );
		double y = gpsR.y() - Math.asin(local_vector_in_meter.y()/(radius*lonNorm(gpsR.x())));
		double z = gpsR.z() - local_vector_in_meter.z();
		return radianToDegrees( new Point3D(x,y,z) );
	}

	/** @see Coords.coords_converter#distance3d(Geom.Point3D, Geom.Point3D)*/
	@Override
	public double distance3d(Point3D gps0, Point3D gps1) {
		Point3D gps0R = degreesToRadian(gps0);// gps in radian
		Point3D gps1R = degreesToRadian(gps1);// gps in radian
		
		double diffX = gps1R.x() - gps0R.x();
		double diffY = gps1R.y() - gps0R.y();
		
		double materX = Math.sin(diffX)*radius;
		double lonNorm = lonNorm( gps0R.x() );
		double materY = Math.sin(diffY)*radius*lonNorm;
		
		return Math.sqrt( Math.pow(materX, 2) + Math.pow(materY, 2) );
	}

	/** @see Coords.coords_converter#vector3D(Geom.Point3D, Geom.Point3D)*/
	@Override
	public Point3D vector3D(Point3D gps0, Point3D gps1) {
		double x = (gps0.x() + gps1.x())/2;
		double y = (gps0.y() + gps1.y())/2;
		double z = (gps0.z() + gps1.z())/2;
		return new Point3D(x,y,z);
	}

	/** @see Coords.coords_converter#azimuth_elevation_dist(Geom.Point3D, Geom.Point3D)
	 * formulas from hare:
	 * http://tchester.org/sgm/analysis/peaks/how_to_get_view_params.html*/
	@Override
	public double[] azimuth_elevation_dist(Point3D gps0, Point3D gps1) {
		double []ans = new double[3];
		
		//distance
		double distance = distance3d(gps0, gps1);
		if(distance > 100000 || distance < 1)
			throw new RuntimeException(distance + "is distance to much long or to much short");
		
		//azimuth				
		Point3D gps0R = degreesToRadian(gps0);// gps in radian
		Point3D gps1R = degreesToRadian(gps1);// gps in radian
		
		double d =  distance*oneMeterInMile;
		d = d/(radius*oneMeterInMile);
			
		double x = Math.acos( (Math.sin(gps1R.x()) - Math.sin(gps0R.x())*Math.cos(d) )/
				(Math.sin(d)*Math.cos(gps0R.x())) );
		
		if(Math.sin(gps1R.y() - gps0R.y()) > 0)
			ans[0] = x;
		else
			ans[0] = 2*Math.PI - x;
		
		ans[0] = Math.toDegrees(ans[0]);		
		//elevation
		x = Math.asin( (gps1R.z() - gps0R.z())/distance );
		ans[1] = Math.toDegrees(x); 
		
		//distance a-v-i-v-v-e-x-l-e-r
		ans[2] = distance;
		
		
		return ans;
	}
	
	public double azimuth(Point3D a,Point3D b) {
		
		try {
			return azimuth_elevation_dist(a, b)[0];
		}catch (Exception e) {
			return 0;
		}
	}

	/** @see Coords.coords_converter#isValid_GPS_Point(Geom.Point3D) */
	@Override
	public boolean isValid_GPS_Point(Point3D p) {
		
		return ( -180 <= p.x() && p.x() <= 180 &&
				-90 <= p.y() && p.y() <= 90 &&
				-450 <= p.z());
	}
//a-v-i-v-v-e-x-l-e-r
}
