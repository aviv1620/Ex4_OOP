package com.baeldung.algorithms.ga.dijkstra;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import Geom.Point3D;

/**
 * 
 * @author baeldung
 *
 */
public class Node {

    private String name;

    /** @author Aviv Vexler*/
    public boolean flag;
    /** @author Aviv Vexler*/
    public Point3D point;
    
    

	/**
	 *  @author Aviv Vexler
	 */
	@Override
	public String toString() {
		return "Node [name=" + name + ", distance=" + distance + "]";
	}

	private LinkedList<Node> shortestPath = new LinkedList<>();

    private Integer distance = Integer.MAX_VALUE;

    private Map<Node, Integer> adjacentNodes = new HashMap<>();

    public Node(String name,boolean flag,Point3D point) {
        this.name = name;
        this.flag = flag;
        this.point = point;
    }
    
    public Node(String name) {
        this.name = name;
    }

    public void addDestination(Node destination, int distance) {
        adjacentNodes.put(destination, distance);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Map<Node, Integer> getAdjacentNodes() {
        return adjacentNodes;
    }

    public void setAdjacentNodes(Map<Node, Integer> adjacentNodes) {
        this.adjacentNodes = adjacentNodes;
    }

    public Integer getDistance() {
        return distance;
    }

    public void setDistance(Integer distance) {
        this.distance = distance;
    }

    public List<Node> getShortestPath() {
        return shortestPath;
    }

    public void setShortestPath(LinkedList<Node> shortestPath) {
        this.shortestPath = shortestPath;
    }
    
    

}