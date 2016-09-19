package bodies.projectiles;

import java.awt.geom.Rectangle2D;

import shrooms.Body;
import shrooms.Shrooms;
import bodies.walls.Wall;

public class Apple extends Projectile {
	
	//animation constants
	private static final String SPRITE_KEY = "apple_projectile";
	private static final int NUM_FRAMES = 4;
	private static final int ANIMATION_DELAY = 3;
	
	//bounds and frame
	private static final Rectangle2D.Float FRAME = new Rectangle2D.Float(0, 0, 24, 24);
	
	//animation frame
	public int frame;

	public Apple() {
		super((Rectangle2D.Float)FRAME.clone(), (Rectangle2D.Float)FRAME.clone(), SPRITE_KEY);
		
		frame = 0;
	}
	
	public void update(Shrooms game){
		frame++;
		if(frame >= NUM_FRAMES*ANIMATION_DELAY){
			frame = 0;
		}
		
		spriteFrame = frame / ANIMATION_DELAY;
	}
	
	public void collisionWith(Body b, Shrooms game) {
		if(b instanceof Wall){
			alive = false;
		}
	}
}
