package algorithms;

import java.util.ArrayList;
import java.util.Iterator;

import com.baeldung.algorithms.ga.dijkstra.Node;

import Geom.Point3D;
import gis.packmanModel.Box;
import gis.packmanModel.Fruit;
import gis.packmanModel.Game;
import gis.packmanModel.Packman;

public class PlayAuto {
	public static double execute(Game game) {

		ArrayList<Node> nodes = makeNodes(game);
		//see node on map
		for(Node n:nodes) 
			game.addDeveloperPoint(n.point);





		return 0;
	}

	private static ArrayList<Node> makeNodes(Game game) {
		ArrayList<Node> nodes = new ArrayList<Node>();
		buildNodes(nodes,game);
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
			//double left = box.getLeft();
			//double right = box.getRight();
			//double top = box.getTop();
			//double buttom = box.getButtom();
			
			double d = 0.0000;
			
			//nodes.add(new Node("box", false, new Point3D(left - d, top - d)));
			//nodes.add(new Node("box", false, new Point3D(right + d, top - d)));
			//nodes.add(new Node("box", false, new Point3D(right + d, buttom + d)));
			//nodes.add(new Node("box", false, new Point3D(left - d, buttom + d)));
		}

	}
}
