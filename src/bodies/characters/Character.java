package bodies.characters;

import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

import bodies.walls.Wall;
import shrooms.Body;
import shrooms.Shrooms;

public abstract class Character extends Body{
	
	private static final float FRICTION_COEFFICIENT = 0.3f;
	private static final int GRAVITY = 2;
	
	public boolean grounded, frictionless;
	
	public int maxHealth, health;
	
	boolean collided_up, collided_down, collided_left, collided_right;
	boolean friction_dx_set, friction_dy_set, floor_friction_set;
	boolean grounded_set;
	
	public float floorFriction;
	
	public Character(Rectangle2D.Float frame, Rectangle2D.Float bounds, String spriteKey){
		super(frame, bounds, spriteKey);
		
		frictionless = grounded = false;
		
		floor_friction_set = friction_dx_set = friction_dy_set = false;
		collided_up = collided_down = collided_left = collided_right = false;
		grounded_set = false;
		
		floorFriction = 0;
		
		maxHealth = health = 100;
	}

	public void update(Shrooms game) {
		//reset friction and grounded, if not set
		if(!friction_dx_set)
			friction.x = 0;
		if(!friction_dy_set)
			friction.y = 0;
		if(!floor_friction_set)
			floorFriction = 0;
		if(!grounded_set)
			grounded = false;
		
		//apply gravity
		velocity.y += GRAVITY;
		
		//apply floor friction
		if(!frictionless){
			velocity.x += floorFriction;
			
			//stop when velocity gets near 0
			if(Math.abs(velocity.x) < 1)
				velocity.x = 0;
		}
		
		//check if squished
		if((collided_left && collided_right) || (collided_up && collided_down))
			alive = false;
		
		//check if out of health
		if(health <= 0)
			alive = false;
		
		//check if fallen 
		if(bounds.y >= 2000)
			alive = false;
		
		//reset flags
		floor_friction_set = friction_dx_set = friction_dy_set = false;
		collided_up = collided_down = collided_left = collided_right = false;
		grounded_set = false;
	}
	
	public void move(){
		super.move();
		
		//apply friction
		shift(friction);
	}
	
	public void moveAwayFromWall(Body b){
		Direction collisionDirection = collisionDirection(b);
		
		//decide witch way to push character
		if(collisionDirection == Direction.RIGHT){
			//push away from wall
			shift(new Point2D.Float(b.bounds.x - bounds.x - bounds.width, 0));
			collided_right = true;
			
			//move with wall
			friction.x = b.velocity.x;
			friction_dx_set = true;
			
			//stop in x direction
			velocity.x = 0;
		}else if(collisionDirection == Direction.LEFT){
			//push away from wall
			shift(new Point2D.Float(b.bounds.x + b.bounds.width - bounds.x, 0));
			collided_left = true;
			
			//move with wall
			friction.x = b.velocity.x;
			friction_dx_set = true;
			
			//stop in x direction
			velocity.x = 0;
		}else if(collisionDirection == Direction.DOWN){
			//push away from wall
			shift(new Point2D.Float(0, b.bounds.y - bounds.y - bounds.height));
			collided_down = true;
			
			//set grounded flag
			grounded = grounded_set = true;
			
			//move with wall
			friction.x = b.velocity.x;
			friction_dx_set = true;
			
			//floor friction
			if(!frictionless){
				floorFriction = - velocity.x*FRICTION_COEFFICIENT;
				floor_friction_set = true;
			}
			
			//move with wall
			friction.y = b.velocity.y;
			friction_dy_set = true;
			
			//stop in y direction
			velocity.y = 0;
		}else if(collisionDirection == Direction.UP){
			//push away from wall
			shift(new Point2D.Float(0, b.bounds.y + b.bounds.height - bounds.y));
			collided_up = true;
			
			//move with wall
			friction.y = b.velocity.y;
			friction_dy_set = true;
			
			//stop in y direction
			velocity.y = 0;
		}
	}

	public void collisionWith(Body b, Shrooms game) {
		//push away from walls
		if(b instanceof Wall){
			moveAwayFromWall(b);
			
			//check again for collisions with walls since character has been moved
			for(Body b1 : game.bodies){
				if(b1 instanceof Wall && bounds.intersects(b1.bounds)){
					moveAwayFromWall(b1);
					break;
				}
			}
		}
	}
}
