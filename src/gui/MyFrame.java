package gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.MouseMotionListener;
import java.io.File;
import java.io.IOException;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.filechooser.FileNameExtensionFilter;

import Geom.Point3D;
import Robot.Play;
import algorithms.ParsePlay;
import algorithms.Scale;
import gis.packmanModel.Game;
import threads.PlayRunnable;
import threads.SimplePlayer;



/**
 * @author aviv vexler
 * have some change from ex_3
 */
public class MyFrame extends JFrame {

	private JMenuBar menuBar;
	private JMenuItem itemLoad;
	private JMenuItem itemPlayManual;
	private JMenuItem itemPlayAuto;

	private boolean gameEnd = false;
	private Play play;	
	private Game game;
	private PlayRunnable playManualRunnable;
	private Map map = new Map(); 
	private JFileChooser fc = new JFileChooser();
	private FileNameExtensionFilter filterCSV;
	
	public static boolean CHEATS_DEVELOPER_BLUE = false;
	public static boolean CHEATS_DEVELOPER_RED = false;

	/**
	 * configured all the buttons and gui setting.
	 *  all the  buttons organize in method itemXXX.
	 * @param w - Width screen.
	 * @param h - Height screen.
	 * @throws IOException - Signals that an I/O exception of some sort has occurred.
	 */
	public MyFrame(int w,int h) throws IOException{
		//play music
		//SimplePlayer player = new SimplePlayer("resources/Pokemon Theme Song (8-Bit).mp3");
		//Thread t = new Thread(player);
		//t.start();				

		//menu
		menuBar = new JMenuBar();								
		itemLoad = new JMenuItem("Load");		
		itemPlayManual = new JMenuItem("play manual");	
		itemPlayAuto = new JMenuItem("play auto");	

		menuBar.add(itemLoad);
		menuBar.add(itemPlayManual);
		menuBar.add(itemPlayAuto);		
		
		itemLoad.addActionListener( (ActionEvent e) -> itemLoad() );
		itemPlayManual.addActionListener((ActionEvent e) -> itemPlayManual() );
		itemPlayAuto.addActionListener((ActionEvent e) -> itemPlayAuto());	

		//gui
		setJMenuBar(menuBar);
		add(map);

		setSize(w,h); 
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);

		//file open 
		filterCSV = new FileNameExtensionFilter("csv onely","csv");
		fc.setCurrentDirectory(new File("data"));
		fc.addActionListener((ActionEvent e) -> fileOpen());

		//Mouse
		map.addMouseListener(new MouseAdapter() {

			@Override
			public void mousePressed(MouseEvent e) {				
				click(e.getX(),e.getY());
			}			

		});
				
		map.addMouseMotionListener(new MouseMotionAdapter() {			
			@Override
			public void mouseMoved(MouseEvent e) {
				MyFrame.this.mouseMoved(e.getX(),e.getY());
				
			}
			
		});


		//open dialog 
		Runnable runnable = () -> itemLoad();
		new Thread(runnable).start();
		
		//key
		addKeyListener(new KeyAdapter() {					
			@Override
			public void keyTyped(KeyEvent e) {
				MyFrame.this.keyTyped(e.getKeyChar() );		
			}			
		});

	}	

	/** update the map in new patch and game and repaint*/
	@Override
	public void repaint() {
		map.setGameAndPatch(game);
		super.repaint();
	}
	
	/**
	 * cheats allow to control the speed of the game.
	 * and more
	 * @param keyChar - key from keyboard.
	 */
	private void keyTyped(char keyChar) {
		switch (keyChar) {
		case '0':
			PlayRunnable.SLEEP = 15;
			break;
		case '1':
			PlayRunnable.SLEEP = 1;
			break;
		case '2':
			PlayRunnable.SLEEP = 30;
			break;
		case '3':
			PlayRunnable.SLEEP = 100;
			break;
		case '4':
			PlayRunnable.SLEEP = 1000;
			break;
		case '5':
			CHEATS_DEVELOPER_BLUE = !CHEATS_DEVELOPER_BLUE;
			break;
		case '6':
			CHEATS_DEVELOPER_RED = !CHEATS_DEVELOPER_RED;
			break;
		}
	}

	/**
	 * called when user click on the screen.
	 * Add packman, fruit or do not do nothing if user not select something to add.
	 * @param x - x axis in mouse.
	 * @param y - y axis in mouse.
	 */
	private void click(int x, int y) {
		//game is not runnig
		if(game != null && play != null && !play.isRuning() && !gameEnd) {

			Point3D point3d = new Point3D(x,y,0);
			point3d = Scale.screenToImg(point3d, map);
			point3d = Scale.ImageToPolar(point3d, map);
			game.getMe().location = point3d;			
			repaint();
		}
		
		//reset board
		if(gameEnd)
			reset();

	}
	
	private void reset() {
		gameEnd = false;
		
		play = null;
		game = null;
		playManualRunnable = null;
		repaint();
		
		itemLoad.setEnabled(true);
		itemPlayAuto.setEnabled(true);
		itemPlayManual.setEnabled(true);
	}
	
	/**
	 * called when Moved mouse on the screen.
	 * update PlayManualRunnable class.
	 *  @param x - x axis in mouse.
	 *  @param y - y axis in mouse.
	 */
	private void mouseMoved(int x,int y) {		
		if(playManualRunnable != null)
			playManualRunnable.mouseMoved(x,y);

	}

	/**
	 * Celled when user open file.
	 * Filter the option:
	 * user cancel,user selected one CSV file,user save kml.
	 * And called the right method.
	 */
	private void fileOpen() {
		File file = fc.getSelectedFile();
		if(file == null)//user cancel
			return;
		else {			
			game = new Game();
			play = new Play(file.getPath());
			play.setIDs(312129331);
			ParsePlay.parseBoard(play, game);
			repaint();
		}


	}

	/** called when user press "Load CSV" button. */
	private void itemLoad() {
		fc.setFileFilter(filterCSV);
		fc.setFileSelectionMode(JFileChooser.FILES_ONLY);		
		fc.showOpenDialog(this);		
		//a>v>i>v>v>e>x>l>e>r.
	}

	private void itemPlayAuto() {
		play(false);
	}

	private void itemPlayManual() {		
		play(true);
	}
	
	private void play(boolean manual) {
		if(game == null || play == null) {
			System.out.println("please load example");
			return;
		}
		
		//"me" location not set.
		if(game.getMe().location.x() == 0) {
			System.out.println("please click on the map to select location");
			return;
		}
		
		//start game
		playManualRunnable = new PlayRunnable(this, game, play,manual);
		Thread t = new Thread(playManualRunnable);
		t.setName("play_manual");
		t.start();
	}

	public void startGame() {
		itemLoad.setEnabled(false);
		itemPlayAuto.setEnabled(false);
		itemPlayManual.setEnabled(false);
	}

	public void stopGame() {
		gameEnd = true;
		System.out.println("End.\npress on the map to reset.");
	}
	
	public JPanel getMap() {
		return map;
	}


	/** Version 1 maybe forever. */
	private static final long serialVersionUID = 1L;

	/** Open the software.
	 * @param args - not use
	 * @throws IOException - {@link IOException}
	 */
	public static void main(String[] args) throws IOException {
		new MyFrame((int)(1433/1.5),(int)(642/1.5));
	}


}
