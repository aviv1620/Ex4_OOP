package gis.packmanModel;

import java.util.ArrayList;
import java.util.Iterator;

import Geom.Point3D;
/**
 * store packmans and fruits
 * @author Aviv Vexler
 *
 */
public class Game {
	private ArrayList<Packman> packmens;
	private ArrayList<Fruit> fruits;
	private ArrayList<Ghost> ghosts;
	private ArrayList<Box> boxs;
	private ArrayList<Point3D> developerPoint;
	private Me me;
	
	private int time;
	private int score;
	private int kbg;//kill by ghosts
	private int oob;//out of box
	public enum Feedback{
		good,bad
	}
	private Feedback feedback;
	private int tftl;//time feedback to leave.
	
	public Game() {
		clear();		
	}		

	/**
	 * make new packman with: angle=0,speed=1,radius=1, id=lest packmen + 1.  
	 * @param location - location to new packman.
	 */
	public void addDefultPackmen(Point3D location) {
		//find id.
		int id = 0;
		if(packmens.size() > 0) {
			Packman packmen = packmens.get(packmens.size() - 1);//lest packmen
			id = packmen.id + 1;
		}
		
		//make the new packmen and add to list.
		Packman packmen = new Packman(id, location, 1, 1);
		packmens.add(packmen);
		////a-v-i-v-v-e-x-l-e-r.
	}
	
	/**@param packmen - packmen to add.*/
	public void addPackman(Packman packmen) {
		packmens.add(packmen);
	}
	
	/**
	 * add fruit with weight=1 and id=lest fruit + 1.
	 * @param location - loaction to new fruit.
	 */
	public void addDefultFruit(Point3D location) {
		//find id
		int id = 0;
		if(fruits.size() > 0) {
			Fruit fruit = fruits.get( fruits.size() - 1 );
			id = fruit.id + 1;
		}
		
		//make new fruit
		Fruit fruit = new Fruit(id, location, 1);
		fruits.add(fruit);
	}
	
	/** @param fruit - fruit to add.*/
	public void addFruit(Fruit fruit) {
		fruits.add(fruit);
	}
	
	/** @param ghost - ghost to add.*/
	public void addGhost(Ghost ghost) {
		ghosts.add(ghost);
	}
	
	/** @param box - box to add.*/
	public void addBox(Box box) {
		boxs.add(box);
	}
	
	/** @param developerPoint - developerPoint to add.*/
	public void addDeveloperPoint(Point3D point) {
		developerPoint.add(point);
	}
		
	
	/**@param me - me to add.*/
	public void setMe(Me me) {
		this.me = me;
	}
	
	/** @return me*/
	public Me getMe() {
		return me;
	}
	
	
	/** @param index - index to packmans.
	 * @return - Packman.*/
	public Packman getPackmen(int index) {
		return packmens.get(index);
	}
	
	
	
	/**
	 * @param score the score to set
	 */
	public void setScore(int score) {
		//feedback
		if(score > this.score) {
			feedback = Feedback.good;
			tftl = 100;
		}else if(score < this.score) {
			feedback = Feedback.bad;
			tftl = 100;
		}
		
		//set score
		this.score = score;
	}

	/**
	 * @param time the time to set
	 */
	public void setTime(int time) {
		this.time = time;
	}

	/**
	 * @param kbg the kill by ghosts to set
	 */
	public void setKbg(int kbg) {
		this.kbg = kbg;
	}

	/**
	 * @param oob the out of box to set
	 */
	public void setOob(int oob) {
		this.oob = oob;
	}
	
	/**
	 * @return statistics
	 */
	public String getStatistics() {
		return "Time: "+time+"/1000 Score: "+score+" kill by ghosts: "+kbg+" Out of box: "+oob;
	}
	
	
	
	/**
	 * @return the feedback
	 */
	public Feedback getFeedback() {
		return feedback;
	}

	/**
	 * @param feedback the feedback to set
	 */
	public void setFeedback(Feedback feedback) {
		this.feedback = feedback;
	}

	public boolean drawFeedback() {
		return tftl > 0;
	}
	
	/**
	 * tdtl = time feedback to leave
	 */
	public void subTftl() {
		tftl--;
	}

	/**@return Iterator to Fruit.*/
	public Iterator<Fruit> iteratorFruit(){
		return fruits.iterator();
	}
	
	/**@return Iterator to packmans.*/
	public Iterator<Packman> iteratorPackmen(){
		return packmens.iterator();
	}
	
	/**@return Iterator to ghost.*/
	public Iterator<Ghost> iteratorGhost(){
		return ghosts.iterator();
	}
	
	/**@return Iterator to box.*/
	public Iterator<Box> iteratorBox(){
		return boxs.iterator();
	}
	
	/**@return Iterator to developer point.*/
	public Iterator<Point3D> iteratorDeveloperPoint(){
		return developerPoint.iterator();
	}
	
	/**clear the data structures*/
	public void clear() {
		packmens = new ArrayList<Packman>();
		fruits = new ArrayList<Fruit>();
		ghosts = new ArrayList<Ghost>();
		boxs = new ArrayList<Box>();
		developerPoint = new ArrayList<Point3D>();
		me = null;
		
	}
	
	/** @return size packman*/
	public int countPackmens() {
		return packmens.size();
	}
	
	/*** @return ArrayList fruit with another Pointer but same object. */
	public ArrayList<Fruit> CopyPointerFruit(){
		ArrayList<Fruit> arrayList = new ArrayList<Fruit>();
		arrayList.addAll(fruits);
		return arrayList;
	}

	@Override
	public String toString() {
		return "Game [packmens=" + packmens + ", fruits=" + fruits + "]";
	}
	
	
	
}
