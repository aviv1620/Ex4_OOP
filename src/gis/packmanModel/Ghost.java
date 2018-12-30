package gis.packmanModel;

import Geom.Point3D;
/**
 * store Packman. see assignment description for more information.
 * @author Aviv Vexler
 *
 */
public class Ghost {		
	public final int id;
	public Point3D location;
	public int speed;
	public int radius;

	/**
	 * make new Packman.
	 * @param id - unique id.
	 * @param location - location.
	 * @param angale - angale.
	 * @param speed - speed.
	 * @param radius - radius.
	 */
	public Ghost(int id,Point3D location, int speed,int radius) {
		super();
		this.id = id;
		this.location = location;
		this.radius = radius;
	}

	@Override
	public String toString() {
		return "ghost [id=" + id + ", location=" + location + ", speed=" + speed + "]";
	}





}
