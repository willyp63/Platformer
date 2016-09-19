package bodies.walls;

import java.awt.geom.Rectangle2D;

import shrooms.Shrooms;

public class UDOscillatingWall extends Wall {
	
	private static final int SPEED = 4;
	
	public int period, position;
	
	public UDOscillatingWall(Rectangle2D.Float frame, int period, boolean direction) {
		super(frame);
		
		this.period = period;
		position = 0;
		
		//set velocity to initial speed
		if(direction)
			velocity.y = SPEED;
		else
			velocity.y = -SPEED;
	}
	
	public void update(Shrooms game){
		position += SPEED;
		
		//flip velocity when period has elapsed
		if(position >= period){
			velocity.y *= -1;
			position = 0;
		}
	}
}
