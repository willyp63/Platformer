package engine;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

public class MyPanel extends JPanel{
	
	private static final long serialVersionUID = 1L;
	
	MyGame game;
	
	BufferedImage buffer;
	
	public MyPanel(MyGame game){
		this.game = game;
		
		//create buffer image
		buffer = new BufferedImage(game.width, game.height, BufferedImage.TYPE_INT_RGB);
	}
	
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		
		//paint game to buffer
		Graphics2D g1 = (Graphics2D)buffer.getGraphics();
		game.draw(g1);
		
		//paint buffer to screen
		g.drawImage(buffer, 0, 0, null);
    }
}
