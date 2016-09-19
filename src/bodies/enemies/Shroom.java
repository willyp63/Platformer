package bodies.enemies;

import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import shrooms.Shrooms;

public class Shroom extends Enemy {
	
	private static final String SPRITE_KEY = "shroom";
	private static final int FORWARD_FRAME = 1, BACKWARD_FRAME = 0;
	private static final Rectangle2D.Float FRAME = new Rectangle2D.Float(0, 0, 48, 48);
	private static final Rectangle2D.Float BOUNDS = new Rectangle2D.Float(4, 8, 40, 40);
	
	private static final int SPEED = 5;
	private static final int JUMP = 25;
	private static final int UPDATE_PERIOD = 30;
	
	public int updateCount;

	public Shroom(Point2D.Float p) {
		super((Rectangle2D.Float)FRAME.clone(), (Rectangle2D.Float)BOUNDS.clone(), SPRITE_KEY);
		shift(p);
		
		updateCount = 0;
	}
	
	public void update(Shrooms game){
		super.update(game);
		
		updateCount++;
		
		if(updateCount > UPDATE_PERIOD){
			updateCount = 0;
			
			//determine direction based off of sams x position
			float samCenterX = game.sam.bounds.x + game.sam.bounds.width/2;
			float shroomCenterX = bounds.x + bounds.width/2;
			
			//set velocity and frame
			if(samCenterX > shroomCenterX){
				velocity.x = SPEED;
				spriteFrame = FORWARD_FRAME;
			}else{
				velocity.x = -SPEED;
				spriteFrame = BACKWARD_FRAME;
			}
			
			//jump
			if(grounded)
				velocity.y -= JUMP;
		}
	}
}
