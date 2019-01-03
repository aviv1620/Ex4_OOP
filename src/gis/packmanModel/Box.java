package gis.packmanModel;

import Geom.Point3D;
/**
 * store Box. see assignment description for more information.
 * @author Aviv Vexler
 *
 */
public class Box {		
	public final int id;
	public Point3D location1;
	public Point3D location2;

	/**
	 * make new Box.
	 * @param id - unique id.
	 * @param location1 - location.
	 * @param location2 - location.
	 */
	public Box(int id,Point3D location1, Point3D location2) {
		super();
		this.id = id;
		this.location1 = location1;
		this.location2 = location2;
	}

	@Override
	public String toString() {
		return "Box [id=" + id + ", location1=" + location1 + ", location2=" + location2 + "]";
	}
	
	/**
	 * pay attention the x and y axis is replaced. 
	 * @return - top
	 */
	public double getTop() {
		return location2.x();
	}
	
	/**
	 * pay attention the x and y axis is replaced. 
	 * @return - right
	 */
	public double getRight() {
		return location2.y();
	}
	
	/**
	 * pay attention the x and y axis is replaced. 
	 * @return - left
	 */
	public double getLeft() {
		return location1.y();
	}
	
	/**
	 * pay attention the x and y axis is replaced.
	 * buttom is spelling mistake. 
	 * @return - bottom.
	 */
	public double getButtom() {
		return location1.x();
	}

	



}//a-v-i-v-v-e-x-l-e-r
