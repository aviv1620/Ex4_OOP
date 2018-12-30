package threads;




import java.io.FileInputStream;

import javazoom.jl.player.Player;
/**
 * this just to sound.
 * is not part of the assignment.
 * i take it from model.
 * @author Yael.
 *
 */
public class SimplePlayer implements Runnable{

    private String path;

	public SimplePlayer(String path){
    	this.path = path;
    }
	
    public void play()
    {
        try{

             FileInputStream fis = new FileInputStream(path);
             Player playMP3 = new Player(fis);

             playMP3.play();

        }  catch(Exception e){
             System.out.println(e);
        }
    }

	@Override
	public void run() {
		play();
	} 
}