package bodies.items;

import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D.Float;
import bodies.projectiles.Projectile;
import bodies.walls.Wall;
import shrooms.Body;
import shrooms.Shrooms;

public abstract class Item extends Body {
	
	private static final float FRICTION_COEFFICIENT = 0.2f;
	private static final float ENERGY_CONSERVED = .4f;
	private static final int GRAVITY = 2;
	
	public int count;

	public Item(Float frame, Float bounds, String spriteKey, int count) {
		super(frame, bounds, spriteKey);
		this.count = count;
		
		//add projectile and icon images
		addImage(spriteKey + "_projectile");
		addImage(spriteKey + "_icon");
	}

	public void update(Shrooms game) {
		//apply gravity
		velocity.y += GRAVITY;
		
		if(Math.abs(velocity.x) < 1)
			velocity.x = 0;
		if(Math.abs(velocity.y) < 1)
			velocity.y = 0;
	}
	
	public abstract Projectile makeProjectile();

	public void collisionWith(Body b, Shrooms game) {
		if(b instanceof Wall){
			Direction collisionDirection = collisionDirection(b);
			
			//decide witch way to push character
			if(collisionDirection == Direction.RIGHT){
				//push away from wall
				shift(new Point2D.Float(b.bounds.x - bounds.x - bounds.width, 0));
				
				//bounce off wall
				velocity.x *= -ENERGY_CONSERVED;
			}else if(collisionDirection == Direction.LEFT){
				//push away from wall
				shift(new Point2D.Float(b.bounds.x + b.bounds.width - bounds.x, 0));
				
				//bounce off wall
				velocity.x *= -ENERGY_CONSERVED;
			}else if(collisionDirection == Direction.DOWN){
				//push away from wall
				shift(new Point2D.Float(0, b.bounds.y - bounds.y - bounds.height));
				
				//bounce off wall
				velocity.y *= -ENERGY_CONSERVED;
				
				//apply floor friction
				velocity.x *= 1 - FRICTION_COEFFICIENT;
			}else if(collisionDirection == Direction.UP){
				//push away from wall
				shift(new Point2D.Float(0, b.bounds.y + b.bounds.height - bounds.y));
				
				//bounce off wall
				velocity.y *= -ENERGY_CONSERVED;
			}
		}
	}

}
