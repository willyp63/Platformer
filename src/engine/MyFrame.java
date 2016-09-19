package engine;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JFrame;

public class MyFrame extends JFrame{
	
	private static final long serialVersionUID = 1L;
	
	MyGame game;

	public MyFrame(String title, int width, int height, MyGame game){
		super(title);
		
		this.game = game;
		
        setContentPane(new MyPanel(game));

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(width, height);
        setResizable(false);
        
        addKeyListener(new KeyListener() {
			public void keyPressed(KeyEvent e) {
				game.keyPressed(e);
				e.consume();
			}

			public void keyReleased(KeyEvent e) {
				game.keyReleased(e);
				e.consume();
			}

			public void keyTyped(KeyEvent e) {
				e.consume();
			}
        });
        
        setVisible(true);
	}
}
