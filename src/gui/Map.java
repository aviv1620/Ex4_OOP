package gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;

import javax.imageio.ImageIO;
import javax.swing.JPanel;


import Geom.Point3D;
import algorithms.Scale;
import gis.packmanModel.Box;
import gis.packmanModel.Fruit;
import gis.packmanModel.Game;
import gis.packmanModel.Ghost;
import gis.packmanModel.Me;
import gis.packmanModel.Packman;

/**
 * 
 * shoe the game {@link GIS.packmanModel.Game} data structure and 
 * patch {@link GIS.packmanModel.Patch} data structure on screen.
 * Method setPatchAndGame(Game,Patch) must celled before repaint.
 * @author Aviv Vexler
 */
public class Map extends JPanel{

	/** version 1 maybe forever*/
	private static final long serialVersionUID = 1L;



	//varible
	private Game game;
	private BufferedImage imgMap;
	private BufferedImage imgPackman;
	private BufferedImage imgMe;
	private BufferedImage imgBlinky;
	private BufferedImage imgClyde;
	private BufferedImage imgInky;
	private BufferedImage imgPinky;

	/**
	 * load image to draw.
	 * @throws IOException - {@link IOException}.
	 */
	public Map() throws IOException{
		//image		
		File imgMapFile = new File("resources/Ariel1.png");
		imgMap = ImageIO.read(imgMapFile);

		File imgPackmanFile = new File("resources/Packman.png");
		imgPackman= ImageIO.read(imgPackmanFile);

		File imgMeFile = new File("resources/me.png");
		imgMe= ImageIO.read(imgMeFile);

		File imgBlinkyFile = new File("resources/Blinky.gif");
		imgBlinky = ImageIO.read(imgBlinkyFile);

		File imgClydeFile = new File("resources/Clyde.gif");
		imgClyde= ImageIO.read(imgClydeFile);

		File imgInkyFile = new File("resources/Inky.gif");
		imgInky = ImageIO.read(imgInkyFile);

		File imgPinkyFile = new File("resources/Pinky.gif");
		imgPinky = ImageIO.read(imgPinkyFile);
	}

	/**@param game - game data structure to draw.
	 * @param patch - patch data structure to draw.*/
	public void setGameAndPatch(Game game) {
		this.game = game;
	}

	/**Calls the UI delegate's paint method, if the UI delegate is non-null.
	 * @param g - the Graphics object to protect
	 */
	@Override
	protected void paintComponent(Graphics g) {						
		int width = getWidth();
		int Height = getHeight();

		//map		
		g.drawImage(imgMap, 0, 0,width,Height, null);				

		//game and patch
		paintGame(g, game,width,Height);

	}

	/**
	 * draw game {@link GIS.packmanModel.Game} data structure on screen
	 * @param g - Graphics from JFrame
	 * @param game - game {@link GIS.packmanModel.Game} 
	 * @param wWidth - window width.
	 * @param wHeight - window height.
	 */
	private synchronized void paintGame(Graphics g,Game game,int wWidth,int wHeight){	

		if(game == null)
			return;

		synchronized (game) {
			paintBox(g, game, wWidth, wHeight);
			paintFruit(g, game, wWidth, wHeight);
			paintPackman(g, game, wWidth, wHeight);		
			paintGhost(g, game, wWidth, wHeight);
			paintMe(g, game, wWidth, wHeight);
			if(MyFrame.CHEATS_DEVELOPER_BLUE)
				paintDeveloperBlue(g, game, wWidth, wHeight);
			if(MyFrame.CHEATS_DEVELOPER_RED)
				paintDeveloperRed(g, game, wWidth, wHeight);

			//statistics						
			paintStatistics(g,game,wWidth,wHeight);
		}

	}



	private void paintFruit(Graphics g,Game game,int wWidth,int wHeight){
		//fruit
		Iterator<Fruit> fruitIterator = game.iteratorFruit();
		while(fruitIterator.hasNext()) {
			//get point ans scale
			Fruit fruit= fruitIterator.next();
			Point3D point = Scale.polarPointToImage(fruit.location);
			point = Scale.ImageToScreen(point,wWidth,wHeight);
			int radius = 25;

			//draw (maybe later is be image).
			g.setColor(Color.GREEN);
			g.drawOval(((int)point.x() - radius/2), (int)(point.y() - radius/2), radius, radius);
			g.fillOval((int)point.x() - radius/2, (int)point.y() - radius/2, radius, radius);

		}
	}

	private void paintPackman(Graphics g,Game game,int wWidth,int wHeight){
		//packman
		Iterator<Packman> packmanIterator = game.iteratorPackmen();
		while(packmanIterator.hasNext()) {
			//get point and scale
			Packman packman = packmanIterator.next();
			Point3D point = Scale.polarPointToImage(packman.location);
			point = Scale.ImageToScreen(point,wWidth,wHeight);
			int radius = packman.radius*50;


			//draw.
			double x = point.x() - radius/2 ;
			double y = point.y() - radius/2;
			Graphics2D g2d=(Graphics2D)g;		
			AffineTransform at = AffineTransform.getTranslateInstance(x, y);
			at.rotate(Math.toRadians(packman.angle),radius/2,radius/2);
			g2d.drawImage(imgPackman, at, null);						

		}
	}

	private void paintMe(Graphics g,Game game,int wWidth,int wHeight){
		//get point and scale
		Me me = game.getMe();

		Point3D point = Scale.polarPointToImage(me.location);
		point = Scale.ImageToScreen(point,wWidth,wHeight);
		int radius = me.radius*50;


		//draw.
		double x = point.x() - radius/2 ;
		double y = point.y() - radius/2;
		Graphics2D g2d=(Graphics2D)g;		
		AffineTransform at = AffineTransform.getTranslateInstance(x, y);
		at.rotate(Math.toRadians(me.angle),radius/2,radius/2);
		g2d.drawImage(imgMe, at, null);	
	}

	private void paintGhost(Graphics g,Game game,int wWidth,int wHeight){

		Iterator<Ghost> packmanIterator = game.iteratorGhost();
		int num = 0;//some num to get different ghost.
		while(packmanIterator.hasNext()) {
			//get point and scale
			Ghost ghost = packmanIterator.next();
			Point3D point = Scale.polarPointToImage(ghost.location);
			point = Scale.ImageToScreen(point,wWidth,wHeight);
			int radius = ghost.radius*30;


			//draw.
			double x = point.x() - radius/2 ;
			double y = point.y() - radius/2;
			g.drawImage(getGhost(num), (int)x,(int)y,radius,radius, null);	
			num++;
		}
	}

	private void paintBox(Graphics g,Game game,int wWidth,int wHeight){
		Iterator<Box> packmanIterator = game.iteratorBox();
		while(packmanIterator.hasNext()) {
			//get points
			Box box = packmanIterator.next();
			Point3D point1 = Scale.polarPointToImage(box.location1);
			point1 = Scale.ImageToScreen(point1,wWidth,wHeight);

			Point3D point2 = Scale.polarPointToImage(box.location2);
			point2 = Scale.ImageToScreen(point2,wWidth,wHeight);	

			double left = point1.x();
			double right = point2.x();
			double top = point2.y();
			double buttom = point1.y();
			double width = right - left;
			double height = buttom - top;

			g.fillRect((int)left, (int)top , (int)width, (int)height);

		}
	}

	private void paintStatistics(Graphics g, Game game2, int wWidth, int wHeight) {
		//font not exit the screen.
		Font f1 = new Font("Consolas", Font.BOLD, wWidth / 35);
		g.setFont(f1);

		//color by feedback
		if(game.drawFeedback()) {
			game.subTftl();
			switch (game.getFeedback()) {
			case good:
				g.setColor(Color.GREEN);
				break;

			default:
				g.setColor(Color.RED);
				break;
			}
		}else
			g.setColor(Color.WHITE);

		//draw text
		g.drawString(game.getStatistics(),0,wHeight);

	}

	private void paintDeveloperBlue(Graphics g, Game game, int wWidth, int wHeight) {
		//me
		paintDeveloperPoint(g, wWidth, wHeight, game.getMe().location, Color.BLUE);;

		//fruit
		Iterator<Fruit> fruitIterator = game.iteratorFruit();
		while(fruitIterator.hasNext()) {
			//get point ans scale
			Fruit fruit= fruitIterator.next();
			paintDeveloperPoint(g, wWidth, wHeight, fruit.location, Color.BLUE);		
		}

		//packman
		Iterator<Packman> packmanIterator = game.iteratorPackmen();
		while(packmanIterator.hasNext()) {
			//get point and scale
			Packman packman = packmanIterator.next();
			paintDeveloperPoint(g, wWidth, wHeight, packman.location, Color.BLUE);		
		}
		
		//ghost
		Iterator<Ghost> ghostIterator = game.iteratorGhost();
		while(ghostIterator.hasNext()) {
			//get point and scale
			Ghost ghost = ghostIterator.next();
			paintDeveloperPoint(g, wWidth, wHeight, ghost.location, Color.BLUE);		
		}
	}
	
	private void paintDeveloperRed(Graphics g, Game game, int wWidth, int wHeight) {
		Iterator<Point3D> iterator = game.iteratorDeveloperPoint();
		while(iterator.hasNext()) {
			Point3D point3D = iterator.next();
			paintDeveloperPoint(g, wWidth, wHeight, point3D, Color.RED);		
		}
	}

	private void paintDeveloperPoint(Graphics g,int wWidth,int wHeight,Point3D p,Color color) {
		Point3D point = Scale.polarPointToImage(p);
		point = Scale.ImageToScreen(point,wWidth,wHeight);		
		int radius = 5;

		//draw .
		g.setColor(color);
		g.drawOval(((int)point.x() - radius/2), (int)(point.y() - radius/2), radius, radius);
		g.fillOval((int)point.x() - radius/2, (int)point.y() - radius/2, radius, radius);
	}


	private BufferedImage getGhost(int num) {

		switch (num % 4) {
		case 0:
			return imgBlinky;
		case 1:
			return imgClyde;
		case 2:
			return imgInky;
		default:
			return imgPinky;
		}
	}





}
