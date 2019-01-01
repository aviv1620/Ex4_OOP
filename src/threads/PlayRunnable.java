package threads;

import Geom.Point3D;
import Robot.Play;
import algorithms.ParsePlay;
import algorithms.PlayAuto;
import algorithms.Scale;
import coords.MyCoords;
import gis.packmanModel.Game;
import gui.MyFrame;
/**
 * in this way i succeed to get 15 score in exemple 8.
 * and in slow motion cheat
 * @author Aviv Vexler
 *
 */
public class PlayRunnable implements Runnable {

	public static int SLEEP = 15;

	private MyFrame frame;//to repaint,start and stop.
	private Game game;
	private Play play;
	private double angale;
	private boolean manual;
	private MyCoords myCoords = MyCoords.GET_MY_COORDS();

	private Point3D mouse;

	public PlayRunnable(MyFrame frame, Game game, Play play,boolean manual) {
		this.frame = frame;
		this.game = game;
		this.play = play;
		this.manual = manual;
	}

	public void mouseMoved(int x,int y) {		
		mouse = new Point3D(x,y,0);		
	}

	private void angaleManualCalculate() {
		if(game.getMe() == null || mouse == null)
			return;
		
		//calculate azimuth between player and mouse
		Point3D p1 = game.getMe().location;

		Point3D p2 = mouse;
		p2 = Scale.screenToImg(p2,frame.getMap() );
		p2 = Scale.ImageToPolar(p2, frame.getMap());
		try {
			double []azimuth_elevation_dist = myCoords.azimuth_elevation_dist(p1, p2);
			angale = azimuth_elevation_dist[0];
			
			if(azimuth_elevation_dist[2] > 3)//fix bug that make me pacman dance.
				game.getMe().angle = angale;			
		}catch (Exception e) {
			//distance to much short
		}
	}

	@Override
	public void run() {				
		//start
		frame.startGame();
		Point3D initLocation =  game.getMe().location;
		play.setInitLocation(initLocation.x(), initLocation.y());
		play.start();

		//run the game
		while(play.isRuning()) {			
			play.rotate(angale);

			// 7.2) get the current score of the game
			ParsePlay.parseStatistics(play, game);			

			ParsePlay.parseBoard(play, game);
			if(manual)
				angaleManualCalculate();//must called after parseBoard
			else
				PlayAuto.execute(game);
			frame.repaint();

			try {
				Thread.sleep(SLEEP);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}		

		//stop
		frame.stopGame();

		ParsePlay.parseStatistics(play, game);
		frame.repaint();

	}

}
