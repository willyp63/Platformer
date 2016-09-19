package bodies.walls;

import java.awt.geom.Rectangle2D;

import shrooms.Shrooms;

public class RLOscillatingWall extends Wall {
	
	private static final int SPEED = 8;
	
	public int period, position;
	
	public RLOscillatingWall(Rectangle2D.Float frame, int period, boolean direction) {
		super(frame);
		
		this.period = period;
		position = 0;
		
		//set velocity to initial speed
		if(direction)
			velocity.x = SPEED;
		else
			velocity.x = -SPEED;
	}
	
	public void update(Shrooms game){
		position += SPEED;
		
		//flip velocity when period has elapsed
		if(position >= period){
			velocity.x *= -1;
			position = 0;
		}
	}
}
