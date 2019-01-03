package algorithms;

import java.util.ArrayList;
import java.util.StringTokenizer;

import Robot.Play;
import gis.packmanModel.Box;
import gis.packmanModel.Fruit;
import gis.packmanModel.Game;
import gis.packmanModel.Ghost;
import gis.packmanModel.Me;
import gis.packmanModel.Packman;
import Geom.Point3D;
/**
 * Parse string from class play.
 * @author Aviv Vexler
 *
 */
public class ParsePlay {

	/**
	 * parse game-board from server to game.
	 * @param play - the server that run the game.
	 * @param game - my game data data structures.
	 */
	public static synchronized void parseBoard(Play play,Game game) {
		synchronized (game) {
			//clear
			game.clear();

			//build
			ArrayList<String> board_data = play.getBoard();
			for(int i=0;i<board_data.size();i++) {

				String record = board_data.get(i);
				StringTokenizer fields = new StringTokenizer(record, ","); 
				char type = fields.nextToken().charAt(0);

				int id = Integer.parseInt( fields.nextToken() );			
				double lat = Double.parseDouble( fields.nextToken() );
				double lon = Double.parseDouble( fields.nextToken() );
				double alt = Double.parseDouble( fields.nextToken() );
				Point3D location = new Point3D(lat,lon,alt);

				double speed_weight_lat = Double.parseDouble( fields.nextToken() );
				double radius_lon = 0;
				if(fields.hasMoreTokens())//fruit case
					radius_lon = Double.parseDouble( fields.nextToken() );

				switch (type) {

				case 'M':
					Me me = new Me(id, location, (int)speed_weight_lat, (int)radius_lon);
					game.setMe(me);
					break;

				case 'F':
					Fruit fruit = new Fruit(id, location, (int)speed_weight_lat);
					game.addFruit(fruit);
					break;

				case 'G':
					Ghost ghost = new Ghost(id, location, (int)speed_weight_lat,(int)radius_lon);
					game.addGhost(ghost);
					break;

				case 'P':
					Packman packman = new Packman(id, location, (int)speed_weight_lat, (int)radius_lon);
					game.addPackman(packman);
					break;

				case 'B':
					Point3D location2 = new Point3D(speed_weight_lat,radius_lon,0);
					Box box = new Box(id, location, location2);
					game.addBox(box);
					break;

				default:
					break;
				}

			}
		}
	}


	/**
	 * get statistics from play class and parse to string that show on the screen.
	 * @param play - play class.
	 * @param game - game class.
	 */
	public static synchronized void parseStatistics(Play play,Game game) {
		String info = play.getStatistics();			
		StringTokenizer fields = new StringTokenizer(info, ","); 

		//skip date
		fields.nextToken();

		//time
		String tamp = fields.nextToken().split(":")[1];//take after':'	
		int time = (int)Double.parseDouble(tamp) / 100;

		//score
		tamp = fields.nextToken().split(":")[1];//take after':'	
		int score = (int)Double.parseDouble(tamp);

		//skip Time left
		fields.nextToken();

		//kill by ghosts(kbg)
		tamp = fields.nextToken().split(":")[1];//take after':'	
		int kbg = Integer.parseInt(tamp);

		//out of box(oob)
		tamp = fields.nextToken().split(":")[1];//take after':'	
		int oob = Integer.parseInt(tamp);

		//insert to game
		game.setTime(time);
		game.setScore(score);
		game.setKbg(kbg);
		game.setOob(oob);


	}
}
