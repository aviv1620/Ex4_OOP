package gis.packmanModel;

import Geom.Point3D;
/**
 * store Me, the pink packman.
 * @author Aviv Vexler
 *
 */
public class Me {
	public final int id;
	public Point3D location;
	public double angle;
	public int speed;
	public int radius;
	
	/**
	 * make me
	 * @param id - recommended uniqueidentifier
	 * @param location - location polar point.
	 * @param speed - speed me.
	 * @param radius - radius me.
	 */
	public Me(int id,Point3D location, int speed, int radius) {
		this.id = id;
		this.location = location;
		this.angle = 90;
		this.speed = speed;
		this.radius = radius;
	}

	@Override
	public String toString() {
		return "Me [id=" + id + ", location=" + location + ", angle=" + angle + ", speed=" + speed + ", radius="
				+ radius + "]";
	}
	
	
}
