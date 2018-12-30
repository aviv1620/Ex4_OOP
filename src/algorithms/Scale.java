package algorithms;

import javax.swing.JPanel;

import Geom.Point3D;
import gui.Map;

/**
 * polar point to image map,
 * image map to screen size
 * and back.
 * @author Aviv Vexler
 *
 */
public class Scale {

	// polar bound points.
	public static final double TOP_LEFT_X = 35.2025;
	public static final double TOP_LEFT_Y = 32.1057;
	public static final double BOTTOM_RIGHT_X = 35.2124; 
	public static final double BOTTOM_RIGHT_Y = 32.1019;

	//image detail
	public static final int IMAGE_WIDTH = 1433;
	public static final int IMAGE_HEIGHT = 642;
	
	/**
	 * convert polar point to point on the image.
	 * @param point - point in polar coordinate.
	 * @return - point on image.
	 */
	public static Point3D polarPointToImage(Point3D point) {
		//x
		double dx = (point.y() - TOP_LEFT_X)/(BOTTOM_RIGHT_X - TOP_LEFT_X);
		double x = dx*IMAGE_WIDTH;

		//y
		double dy = (point.x() - BOTTOM_RIGHT_Y)/(TOP_LEFT_Y - BOTTOM_RIGHT_Y);
		dy = 1 - dy;
		double y = dy*IMAGE_HEIGHT;

		//return a-v-i-v-v-e-x-l-e-r
		return new Point3D(x,y,point.z());
	}

	/**
	 * scale point to window size.
	 * 
	 * @param point - point in image location.
	 * @param wWidth - window width
	 * @param wHeight - window height
	 * @return point on window location.
	 */
	public static Point3D ImageToScreen(Point3D point,int wWidth,int wHeight) {
		double dx = point.x() / IMAGE_WIDTH;
		double x = dx*wWidth;

		double dy = point.y() / IMAGE_HEIGHT;
		double y = dy*wHeight;

		return new Point3D(x,y,0);
	}
	

	/**
	 * scale x,y point on screen to map Ariel image size.
	 * @param p - point that user click on the screen
	 * @return point in image
	 */
	public static Point3D screenToImg(Point3D p,JPanel map) {
		double dx = p.x() / map.getWidth();
		dx = dx*IMAGE_WIDTH;

		double dy = p.y() / map.getHeight();
		dy = dy*IMAGE_HEIGHT;
		return new Point3D(dx,dy,0);
	}

	/**
	 * scale x,y point on image to polar gps locatin.
	 * @param p - point that user click on the screen
	 * @return point in image
	 */
	public static Point3D ImageToPolar(Point3D p,JPanel map) {
		double dx = p.x() / IMAGE_WIDTH;
		dx = dx*(BOTTOM_RIGHT_X - TOP_LEFT_X) + TOP_LEFT_X;

		double dy = p.y() / IMAGE_HEIGHT;
		dy = 1 - dy;
		dy = dy*(TOP_LEFT_Y - BOTTOM_RIGHT_Y) + BOTTOM_RIGHT_Y;
		return new Point3D(dy,dx,0);
	}
}
