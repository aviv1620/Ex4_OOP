package gis.packmanModel;

import Geom.Point3D;
/**
 * store fruit. see assignment description for more information.
 * @author Aviv Vexler
 *
 */
public class Fruit {
	public Point3D location;
	public int Weight;
	public final int id;
	
	/**
	 * @param id = unique id.
	 * @param location - location.
	 * @param weight - weight
	 */
	public Fruit(int id,Point3D location, int weight) {
		super();
		this.location = location;
		Weight = weight;
		this.id = id;
	}

	@Override
	public String toString() {
		return "Fruit [location=" + location + ", Weight=" + Weight + ", id=" + id + "]";
	}
	
	
}
