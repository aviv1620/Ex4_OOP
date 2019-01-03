package algorithms;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.baeldung.algorithms.ga.dijkstra.Dijkstra;
import com.baeldung.algorithms.ga.dijkstra.Graph;
import com.baeldung.algorithms.ga.dijkstra.Node;

import Geom.Point3D;
import coords.MyCoords;
import gis.packmanModel.Box;
import gis.packmanModel.Fruit;
import gis.packmanModel.Game;
import gis.packmanModel.Ghost;
import gis.packmanModel.Packman;
import gis.packmanModel.PathPoint;
import gui.MyFrame;
/**
 * algorithm player.
 * please see the document "the algorithm" in github wiki..
 * @author Aviv Vexler
 *
 */
public class PlayAuto {
	/** distance Me need save between the ghost*/
	public static double ghostD = 0.00009;
	/** get out of ghost*/
	public static boolean  ghost = true;

	/**
	 * execute all the algorithm and return the azimuth player need to go.
	 * @param game - game data structures
	 * @return angle when Me go.
	 */
	public static double execute(Game game) {
		synchronized (game) {
			//makeNodes
			ArrayList<Node> nodes = makeNodes(game);
			
			//see node on map and add to graph
			Graph graph = new Graph();		
			for(Node n:nodes) {
				game.addDeveloperPoint(n.point);
				graph.addNode(n);
			}
			
			//Dijkstra
			graph = Dijkstra.calculateShortestPathFromSource(graph, nodes.get(0));

			//find shorted Path.
			int distance = Integer.MAX_VALUE;
			double angle = 0;

			Iterator<Node> nodesIter =  graph.getNodes().iterator();
			while(nodesIter.hasNext()) {
				Node node = nodesIter.next();

				//need go when flag is true.
				if(node.flag) {
					if(node.getDistance() <= distance) {
						distance = node.getDistance();

						//inform case not need to happen.
						if(node.getShortestPath().size() == 0)
							System.out.println("zero nodes");
						
						//calculate azimuth
						Point3D pa = game.getMe().location;
						Point3D pb = firstPoint(node);

						angle = MyCoords.GET_MY_COORDS().azimuth(pa, pb);

						if(MyFrame.CHEATS_DEVELOPER_RED)
							drawPath(node,game);
					}
				}
			}	
			
			return angle;
		}
	}

	/**
	 * if cheat enable draw patch on the map.
	 * @param node - node
	 * @param game - game
	 */
	private static void drawPath(Node node,Game game) {				
		List<Node> list = node.getShortestPath();
		
		//Special case
		if(list.size() == 0) 			
			return;
		
		//insert points
		game.clearPathPoint();
		for (int i = 1; i < list.size(); i++) {
			Point3D p1 = list.get(i-1).point;
			Point3D p2 = list.get(i).point;
			PathPoint pathPoint = new PathPoint(p1, p2);
			game.addPathPoint(pathPoint);
		}
		
		//lest point
		Point3D p1 = list.get( list.size() - 1 ).point;
		Point3D p2 = node.point;
		PathPoint pathPoint = new PathPoint(p1, p2);
		game.addPathPoint(pathPoint);

	}

	/**
	 * return the first point.
	 * see the document "the algorithm" in Github Wiki for more detail.
	 * @param node some Node
	 * @return the first point
	 */
	private static Point3D firstPoint(Node node) {
		List<Node> list = node.getShortestPath();
		if(list.size() > 1)
			return list.get(1).point;
		else 
			return node.point;

	}

	/**
	 * make the nodes.
	 * see the document "the algorithm" in Github Wiki for more detail.
	 * @param game - some game
	 * @return Nodes
	 */
	private static ArrayList<Node> makeNodes(Game game) {
		ArrayList<Node> nodes = new ArrayList<Node>();
		buildNodes(nodes,game);
		removeBoxNodes(nodes,game);
		destinationNodes(nodes,game);
		return nodes;
	}	
	
	/**
	 * build the nodes
	 * see the document "the algorithm" in Github Wiki for more detail.
	 * @param nodes - empty array list.
	 * @param game - some game
	 */
	private static void buildNodes(ArrayList<Node> nodes, Game game) {
		//me
		nodes.add(new Node("Me",false,game.getMe().location));

		//fruits
		Iterator<Fruit> iteratorF = game.iteratorFruit();
		while(iteratorF.hasNext()) {
			nodes.add(new Node("fruit", true, iteratorF.next().location));
		}

		//packman
		Iterator<Packman> iteratorP = game.iteratorPackmen();
		while(iteratorP.hasNext()) {
			nodes.add(new Node("Packman", true, iteratorP.next().location));
		}

		//builde 4 nodes to any box
		Iterator<Box> iteratorB = game.iteratorBox();
		while(iteratorB.hasNext()) {
			Box box = iteratorB.next();
			double left = box.getLeft();
			double right = box.getRight();
			double top = box.getTop();
			double buttom = box.getButtom();

			double d = 0.00001;

			nodes.add(new Node("box ", false, new Point3D(top + d,left - d)));
			nodes.add(new Node("box ", false, new Point3D(buttom - d,left - d)));
			nodes.add(new Node("box ", false, new Point3D(top + d, right + d)));
			nodes.add(new Node("box ", false, new Point3D(buttom - d, right + d)));
		}

		//builde 4 nodes to any ghost
		if(ghost) {
			Iterator<Ghost> iteratorG = game.iteratorGhost();
			while(iteratorG.hasNext()) {
				Ghost ghost = iteratorG.next();

				double x = ghost.location.x();
				double y = ghost.location.y();								

				double d = ghostD+0.00001;
				
				double left = y - d;
				double right = y + d;
				double top = x + d;
				double bottom = x - d;				
				
				nodes.add(new Node("ghost", false, new Point3D(top,left)));
				nodes.add(new Node("ghost", false, new Point3D(top,right)));
				nodes.add(new Node("ghost", false, new Point3D(bottom,left)));
				nodes.add(new Node("ghost", false, new Point3D(bottom,right)));
			}
		}


	}

	/**
	 * remove nodes inside box.
	 * see the document "the algorithm" in Github Wiki for more detail.
	 * @param nodes - empty array list.
	 * @param game - some game.
	 */
	private static void removeBoxNodes(ArrayList<Node> nodes, Game game) {
		for (int i = 0; i < nodes.size(); i++) {
			//don't delete Me
			if(nodes.get(i).getName().equals("Me"))
				continue;

			double x = nodes.get(i).point.y();
			double y = nodes.get(i).point.x();

			//out map
			if(x <= Scale.TOP_LEFT_X || x >= Scale.BOTTOM_RIGHT_X
					|| y <= Scale.BOTTOM_RIGHT_Y	|| y >= Scale.TOP_LEFT_Y) {				
				nodes.remove(i--);
				continue;
			}

			//inside box
			Iterator<Box> iteratorB = game.iteratorBox();
			while(iteratorB.hasNext()) {
				Box box = iteratorB.next();

				double left = box.getLeft();
				double right = box.getRight();
				double top = box.getTop();
				double buttom = box.getButtom();

				if(left <= x && x <= right && buttom <= y && y <= top) {
					nodes.remove(i--);
					break;
				}
			}		

			//inside ghost
			if(ghost) {
				Iterator<Ghost> iteratorG = game.iteratorGhost();
				while(iteratorG.hasNext()) {
					Ghost ghost = iteratorG.next();

					double left = ghost.location.y() - ghostD ;
					double right = ghost.location.y() + ghostD;
					double top = ghost.location.x() + ghostD;
					double bottom = ghost.location.x() - ghostD;

					if(left <= x && x <= right && bottom <= y && y <= top) {
						nodes.remove(i--);
						break;
					}
				}
			}

		}

	}

	/**
	 * destination the nodes.
	 * see the document "the algorithm" in Github Wiki for more detail.
	 * @param nodes - empty array list.
	 * @param game - some game.
	 */
	private static void destinationNodes(ArrayList<Node> nodes, Game game) {

		for (int i = 0; i < nodes.size(); i++) {
			for (int j = 0; j < nodes.size(); j++) {

				if(i == j)
					continue;

				Point3D pa = nodes.get(i).point;
				Point3D pb = nodes.get(j).point;

				//debug test 
				/*if(nodes.get(i).getName().equals("Me") &&
						nodes.get(j).getName().equals("fruit"))
					System.out.println("debug");*/

				//destination if not have Box in the middle.
				if( !isBoxMiddle( pa,pb,game ) ) {
					double distance = MyCoords.GET_MY_COORDS().distance3d(pa, pb);
					int disInt = (int)(distance * 100);
					
					nodes.get(i).addDestination(nodes.get(j), disInt);
					///debug test a-v-i-v-v-e-x-l-e-r
					//System.out.println(nodes.get(i).getName() + " to " + 
					//	nodes.get(j).getName() + " dis "+disInt + " distance " + distance);
				}
			}
		}

	}

	/**
	 * inform if have box in the middle.
	 * see the document "the algorithm" in Github Wiki for more detail.
	 * @param pa - some polar point.
	 * @param pb - some polar point.
	 * @param game - some game.
	 * @return true if have box in the middle.
	 */
	private static boolean isBoxMiddle(Point3D pa, Point3D pb, Game game) {
		double ax = pa.x();
		double ay = pa.y();
		double bx = pb.x();
		double by = pb.y();

		//box
		Iterator<Box> iteratorB = game.iteratorBox();
		while(iteratorB.hasNext()) {
			Box box = iteratorB.next();

			double left = box.getLeft();
			double right = box.getRight();
			double top = box.getTop();
			double bottom = box.getButtom();
			
			if(isRectangleMiddle(pa, pb, game, ax, ay, bx, by, left, right, top, bottom))
				return true;

		}

		//ghost
		if(ghost) {
			Iterator<Ghost> iteratorG = game.iteratorGhost();
			while(iteratorG.hasNext()) {
				Ghost ghost = iteratorG.next();


				double left = ghost.location.y() - ghostD ;
				double right = ghost.location.y() + ghostD;
				double top = ghost.location.x() + ghostD;
				double bottom = ghost.location.x() - ghostD;

				//if me inside ghost
				double MeX = game.getMe().location.x();
				double MeY = game.getMe().location.y();
				if(left < MeY && MeY <right && bottom < MeX && MeX < top)
					continue;
				
				if(isRectangleMiddle(pa, pb, game, ax, ay, bx, by, left, right, top, bottom))
					return true;

			}
		}

		return false;
	}

	/**
	 * inform if have box in the middle.
	 * part of isBoxMiddle().
	 * see the document "the algorithm" in Github Wiki for more detail.
	 * @param pa - some point.
	 * @param pb - some point.
	 * @param game - some game.
	 * @param ax - x of pa.
	 * @param ay - y of pa.
	 * @param bx - x of pb.
	 * @param by - y of pa.
	 * @param left - left box
	 * @param right - right box.
	 * @param top - top box.
	 * @param bottom - bottom box.
	 * @return true if have box in the middle.
	 */
	private static boolean isRectangleMiddle(Point3D pa, Point3D pb, Game game,
			double ax ,
			double ay ,
			double bx,
			double by ,
			double left ,
			double right ,
			double top ,
			double bottom) {

		double m = (ay - by) / (ax - bx);

		game.addVerticaLine(left);
		game.addVerticaLine(right);
		game.addHorizontalLine(top); 
		game.addHorizontalLine(bottom);

		//left
		double hitLeft = -1;
		if( (ay < left && by > left)|| (ay > left && by < left) ) 
			hitLeft = ax + (left/m)-ay/m;

		//right
		double hitRight = -1;
		if( (ay < right && by > right)|| (ay > right && by < right) ) 
			hitRight = ax + (right/m)-ay/m;

		//top
		double hitTop = -1;
		if( (ax > top && bx < top) || (ax < top && bx > top) )
			hitTop = m*(top - ax) + ay;

		//bottom
		double hitBottom = -1;
		if( (ax > bottom && bx < bottom) || (ax < bottom && bx > bottom) )
			hitBottom = m*(bottom - ax) + ay;

		if(MyFrame.CHEATS_DEVELOPER_LINE) {
			game.addDeveloperPoint(new Point3D(hitLeft,left));
			game.addDeveloperPoint(new Point3D(hitRight,right));
			game.addDeveloperPoint(new Point3D(top,hitTop));
			game.addDeveloperPoint(new Point3D(bottom,hitBottom));
		}

		//Check
		if( bottom < hitLeft && hitLeft < top)
			return true;
		if( bottom < hitRight && hitRight < top)
			return true;			
		if(left < hitBottom && hitBottom < right)
			return true;
		if(left < hitTop && hitTop < right)
			return true;


		return false;

	}
}
