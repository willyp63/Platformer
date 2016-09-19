package bodies.walls;

import java.awt.geom.Rectangle2D;

import shrooms.Body;
import shrooms.Shrooms;

public class Wall extends Body{
	
	private static final String SPRITE_KEY = "wall";
	
	public Wall(Rectangle2D.Float frame){
		super(frame, (Rectangle2D.Float)frame.clone(), SPRITE_KEY);
		tile = true;
	}
	
	public void update(Shrooms game) {
		
	}
	
	public void collisionWith(Body b, Shrooms game) {
		
	}
}
