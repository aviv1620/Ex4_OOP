package com.baeldung.algorithms.ga.dijkstra;

import java.util.Iterator;
import java.util.List;

import Geom.Point3D;


/**
 * 
 * @author Aviv Vexler
 * 
 * Test class not use in my code
 *
 */
public class Test {

	public static void main(String[] args) {
		mtGraphExemple();
	}

	public static void mtGraphExemple() {
		Node nodeFA = new Node("FA",true,new Point3D(3, 3));//fruit a
		Node nodeFB = new Node("FB",true,new Point3D(6, 3));//fruit b
		Node nodeP = new Node("P",false,new Point3D(1, 5));//fruit packman
		Node nodeM1 = new Node("M1",false,new Point3D(4, 1));//point M1
		Node nodeM2 = new Node("M1",false,new Point3D(5, 1));//point M1

		nodeFA.addDestination(nodeP, 2);
		nodeFA.addDestination(nodeM2, 2);		
		nodeFB.addDestination(nodeM2, 2);		
		nodeP.addDestination(nodeFA, 2);
		nodeP.addDestination(nodeM1, 4);		
		nodeM1.addDestination(nodeFA, 2);
		nodeM1.addDestination(nodeP, 4);
		nodeM1.addDestination(nodeM2, 1);		
		nodeM2.addDestination(nodeM1, 1);
		nodeM2.addDestination(nodeFB, 2);


		Graph graph = new Graph();

		graph.addNode(nodeFA);
		graph.addNode(nodeFB);
		graph.addNode(nodeP);
		graph.addNode(nodeM1);
		graph.addNode(nodeM2);

		graph = Dijkstra.calculateShortestPathFromSource(graph, nodeP);

		Iterator<Node> nodesIter =  graph.getNodes().iterator();
		while(nodesIter.hasNext()) {
			Node node = nodesIter.next();
			if(node.flag) {
				Node fisrtNode = null;
				List<Node> shortestPath = node.getShortestPath();
				if(shortestPath.size() > 1)
					fisrtNode = shortestPath.get(1);


				System.out.println(node + " from "+fisrtNode);
			}
		}

	}

	public static void webExemple() {
		//https://www.baeldung.com/java-dijkstra
		//https://www.baeldung.com/wp-content/uploads/2017/01/initial-graph.png

		Node nodeA = new Node("A");
		Node nodeB = new Node("B");
		Node nodeC = new Node("C");
		Node nodeD = new Node("D"); 
		Node nodeE = new Node("E");
		Node nodeF = new Node("F");

		nodeA.addDestination(nodeB, 10);
		nodeA.addDestination(nodeC, 15);

		nodeB.addDestination(nodeD, 12);
		nodeB.addDestination(nodeF, 15);

		nodeC.addDestination(nodeE, 10);

		nodeD.addDestination(nodeE, 2);
		nodeD.addDestination(nodeF, 1);

		nodeF.addDestination(nodeE, 5);

		Graph graph = new Graph();

		graph.addNode(nodeA);
		graph.addNode(nodeB);
		graph.addNode(nodeC);
		graph.addNode(nodeD);
		graph.addNode(nodeE);
		graph.addNode(nodeF);

		graph = Dijkstra.calculateShortestPathFromSource(graph, nodeA);

		//use
		Iterator<Node> nodesIter =  graph.getNodes().iterator();
		while(nodesIter.hasNext()) {
			Node node = nodesIter.next();
			System.out.println(node);
			if(node.getName().equals("E")) {
				System.out.print("E start goint to ");

				Node fisrtNode = node.getShortestPath().get(1);
				System.out.println(fisrtNode);

				System.out.println();
			}

		}

	}



}
