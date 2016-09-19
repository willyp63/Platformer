package bodies.items;

import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import bodies.projectiles.Projectile;
import bodies.projectiles.Stick;

public class StickItem extends Item {
	
	//frame
	private static final String SPRITE_KEY = "stick";
	private static final Rectangle2D.Float FRAME = new Rectangle2D.Float(0, 0, 24, 24);

	public StickItem(Point2D.Float p, int count){
		super((Rectangle2D.Float)FRAME.clone(), (Rectangle2D.Float)FRAME.clone(), SPRITE_KEY, count);
		shift(p);
		
		velocity.x = 10;
		velocity.y = -10;
	}

	public Projectile makeProjectile() {
		return new Stick();
	}
}