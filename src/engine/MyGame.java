package engine;

import java.awt.Graphics2D;
import java.awt.event.KeyEvent;

public abstract class MyGame{
	
	public static final int DEFAULT_REFRESH_RATE = 16; //updates per second
	
	MyFrame window;
	boolean running;
	
	public String title;
	public int width, height;
	public int maxSleepTime;
	
	public MyGame(String title, int width, int height){
		this.title = title;
		this.width = width;
		this.height = height;
		
		maxSleepTime = (int)1000/DEFAULT_REFRESH_RATE; //1 sec divided by updates per second
		
		window = new MyFrame(title, width, height, this);
		running = false;
	}
	
	public void reset(){
		running = false;
	}
	
	public void gameLoop(){
		long systemTime, sleepTime, timeElapsed;
		
		while(true){
			setup();
			running = true;
			
			while(running){
				systemTime = System.currentTimeMillis();
				
				//update game
				update();
				window.repaint();
				
				//sleep
				timeElapsed = System.currentTimeMillis() - systemTime;
				sleepTime = maxSleepTime - timeElapsed;
				try {
					if(sleepTime > 0)
						Thread.sleep(sleepTime);
				} catch(InterruptedException ex) {
				    Thread.currentThread().interrupt();
				}
			}
		}
	}
	
	public void setRefreshRate(int rate){
		maxSleepTime = (int)1000/rate; //1 sec divided by updates per second
	}
	
	public abstract void setup();
	public abstract void update();
	public abstract void draw(Graphics2D g);
	public abstract void keyPressed(KeyEvent e);
	public abstract void keyReleased(KeyEvent e);
}
