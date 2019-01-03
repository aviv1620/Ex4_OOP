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
import gui.MyFrame;

public class PlayAuto {
	public static int nodeID;
	public static double ghostD = 0.00009;
	public static boolean  ghost = false;

	public static double execute(Game game) {
		synchronized (game) {



			nodeID = 0;
			ArrayList<Node> nodes = makeNodes(game);
			//see node on map and add to graph
			Graph graph = new Graph();		
			for(Node n:nodes) {
				game.addDeveloperPoint(n.point);
				graph.addNode(n);
			}

			graph = Dijkstra.calculateShortestPathFromSource(graph, nodes.get(0));

			int distance = Integer.MAX_VALUE;

			double angle = 0;//FIXME

			Iterator<Node> nodesIter =  graph.getNodes().iterator();
			while(nodesIter.hasNext()) {
				Node node = nodesIter.next();

				if(node.flag) {
					if(node.getDistance() <= distance) {
						distance = node.getDistance();

						Point3D pa = game.getMe().location;
						Point3D pb = firstPoint(node);

						angle = MyCoords.GET_MY_COORDS().azimuth(pa, pb);			
					}								
				}
			}			
			return angle;
		}
	}

	private static Point3D firstPoint(Node node) {
		List<Node> list = node.getShortestPath();
		if(list.size() > 1)
			return list.get(1).point;
		else 
			return node.point;

	}

	private static ArrayList<Node> makeNodes(Game game) {
		ArrayList<Node> nodes = new ArrayList<Node>();
		buildNodes(nodes,game);
		removeBoxNodes(nodes,game);
		destinationNodes(nodes,game);
		return nodes;
	}	

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

			nodes.add(new Node("box "+nodeID, false, new Point3D(top + d,left - d)));
			nodes.add(new Node("box "+nodeID, false, new Point3D(buttom - d,left - d)));
			nodes.add(new Node("box "+nodeID, false, new Point3D(top + d, right + d)));
			nodes.add(new Node("box "+nodeID, false, new Point3D(buttom - d, right + d)));
		}

		//builde 4 nodes to any ghost
		if(ghost) {
			Iterator<Ghost> iteratorG = game.iteratorGhost();
			while(iteratorG.hasNext()) {
				Ghost ghost = iteratorG.next();

				double x = ghost.location.x();
				double y = ghost.location.y();

				double d = ghostD+0.00001;
				nodes.add(new Node("ghost", false, new Point3D(x - d,y - d)));
				nodes.add(new Node("ghost", false, new Point3D(x + d,y - d)));
				nodes.add(new Node("ghost", false, new Point3D(x - d,y + d)));
				nodes.add(new Node("ghost", false, new Point3D(x + d,y + d)));
			}
		}


	}

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

	private static void destinationNodes(ArrayList<Node> nodes, Game game) {

		for (int i = 0; i < nodes.size(); i++) {
			for (int j = 0; j < nodes.size(); j++) {

				if(i == j)
					continue;

				Point3D pa = nodes.get(i).point;
				Point3D pb = nodes.get(j).point;

				//debug test 
				/*				if(nodes.get(i).getName().equals("Me") &&
						nodes.get(j).getName().equals("fruit"))
					System.out.println("debug");*/

				if( !isBoxMiddle( pa,pb,game ) ) {
					double distance = MyCoords.GET_MY_COORDS().distance3d(pa, pb);
					int disInt = (int)(distance * 1000000);
					//System.out.println(nodes.get(i).getName() + " to " +
					//	nodes.get(j).getName() + " dis "+disInt + " distance " + distance);
					nodes.get(i).addDestination(nodes.get(j), disInt);
				}
			}
		}

	}

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

				if(isRectangleMiddle(pa, pb, game, ax, ay, bx, by, left, right, top, bottom))
					return true;

			}
		}

		return false;
	}

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
