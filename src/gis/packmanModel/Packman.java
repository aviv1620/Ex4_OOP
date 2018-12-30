package gis.packmanModel;

import Geom.Point3D;
/**
 * store Packman. see assignment description for more information.
 * @author Aviv Vexler
 *
 */
public class Packman {		
	public final int id;
	public Point3D location;
	public double angle;
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
	public Packman(int id,Point3D location, int speed, int radius) {
		super();
		this.id = id;
		this.location = location;
		this.angle = 90;
		this.speed = speed;
		this.radius = radius;
	}

	@Override
	public String toString() {
		return "Packman [speed=" + speed + ", id=" + id + ", location=" + location + ", angle=" + angle + ", radius="
				+ radius + "]";
	}



}
