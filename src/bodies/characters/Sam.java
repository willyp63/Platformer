package bodies.characters;

import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import bodies.enemies.Enemy;
import bodies.items.Item;
import bodies.projectiles.Projectile;
import shrooms.Body;
import shrooms.Shrooms;

public class Sam extends Character{
	
	//look diection
	public enum LookDirection{UP, FORWARD, DOWN};
	
	//animation constants
	private static final String SPRITE_KEY = "sam";
	private static final int NUM_FRAMES = 5;
	private static final int STANDING_FRAME = 0, RUNNING_FRAME = 1, JUMPING_FRAME = 4;
	private static final int DEFAULT_ROW = 0, THROWING_ROW = 1;
	private static final int NUM_RUN_FRAMES = 3;
	private static final int RUN_ANIMATION_DELAY = 3;
	
	//bounds and frame
	private static final Rectangle2D.Float FRAME = new Rectangle2D.Float(0, 0, 48, 48);
	private static final Rectangle2D.Float BOUNDS = new Rectangle2D.Float(4, 8, 40, 40);
	
	//movement constants
	private static final int THROW_SPEED = 20;
	private static final int SPEED = 3;
	private static final int MAX_SPEED = 12;
	private static final int JUMP = 25;
	
	//ivars
	public Projectile projectile;
	public boolean direction, throwing, running;
	public int runCycle;
	public LookDirection look;
	
	
	
	public Sam(Point2D.Float p){
		super((Rectangle2D.Float)FRAME.clone(), (Rectangle2D.Float)BOUNDS.clone(), SPRITE_KEY);
		shift(p);
		
		projectile = null;
		direction = true;
		throwing = running = false;
		runCycle = 0;
		look = LookDirection.FORWARD;
	}

	public void update(Shrooms game) {
		super.update(game);
		
		//check if sam is running and not throwing
		if(running){
			frictionless = true;
			
			//update run frame
			runCycle++;
			if(runCycle >= NUM_RUN_FRAMES*RUN_ANIMATION_DELAY)
				runCycle = 0;
			
			//set velocity to run speed
			if(direction){
				velocity.x += SPEED;
				
				//limit velocity
				if(velocity.x > MAX_SPEED)
					velocity.x = MAX_SPEED;
			}else{
				velocity.x -= SPEED;
				
				//limit velocity
				if(velocity.x < -MAX_SPEED)
					velocity.x = -MAX_SPEED;
			}
		}else{
			frictionless = false;
		}
		
		//update sprite frame
		if(!grounded){
			spriteFrame = JUMPING_FRAME;
		}else if(running){
			//frame plus offset for animation
			spriteFrame = RUNNING_FRAME + runCycle/RUN_ANIMATION_DELAY;
		}else{
			spriteFrame = STANDING_FRAME;
		}
		
		if(throwing){
			spriteRow = THROWING_ROW;
		}else{
			spriteRow = DEFAULT_ROW;
		}
		
		//offset by NUM_FRAMES if facing backwards
		if(!direction){
			spriteFrame += NUM_FRAMES;
		}
	}
	
	public void jump(){
		//check if grounded and not throwing
		if(grounded){
			grounded = false;
			
			//boost velocity
			velocity.y -= JUMP;
		}
	}
	
	public void beginThrow(Projectile projectile){
		//check if grounded and not throwing
		if(!throwing){
			//wield projectile
			throwing = true;
			this.projectile = projectile;
		}
	}
	
	public void throwItem(Shrooms game){
		if(throwing){
			//set projectiles location based on direction
			if(direction){
				projectile.shift(new Point2D.Float(frame.x, frame.y));
			}else{
				projectile.shift(new Point2D.Float(frame.x + frame.width/2, frame.y));
			}
			
			//init projectiles velocity based on look and direction
			if(look == LookDirection.FORWARD){
				if(direction){
					projectile.velocity.y += friction.y + velocity.y;
					projectile.velocity.x += friction.x + velocity.x + THROW_SPEED;
				}else{
					projectile.velocity.y += friction.y + velocity.y;
					projectile.velocity.x += friction.x + velocity.x - THROW_SPEED;
				}
			}else if(look == LookDirection.UP){
				projectile.velocity.y += friction.y + velocity.y - THROW_SPEED;
				projectile.velocity.x += friction.x + velocity.x;
			}else if(look == LookDirection.DOWN){
				projectile.velocity.y += friction.y + velocity.y + THROW_SPEED;
				projectile.velocity.x += friction.x + velocity.x;
			}
			
			//add projectile to game and stop throwing
			game.newBodies.add(projectile);
			projectile = null;
			throwing = false;
		}
	}
	
	public void runRight(){
		direction = true;
		running = true;
	}
	
	public void runLeft(){
		direction = false;
		running = true;
	}
	
	public void stop(){
		running = false;
	}

	public void collisionWith(Body b, Shrooms game) {
		super.collisionWith(b, game);
		
		if(b instanceof Enemy){
			//kill Sam
			health -= 5;
		}else if(b instanceof Item){
			Item item = (Item)b;
			
			//remove item
			item.alive = false;
			
			//add item to inventory
			game.storeItem(item);
		}
	}
	
	public void draw(Graphics2D g){
		super.draw(g);
		
		//check for projectile
		if(projectile != null){
			//drawX based on direction
			int drawX;
			if(direction)
				drawX = 0;
			else
				drawX = (int)frame.width/2;
			
			//draw projectile in hand
			BufferedImage spriteSheet = spriteSheets.get(projectile.spriteKey);
			BufferedImage sprite = spriteSheet.getSubimage(0, 0, (int)projectile.frame.width, (int)projectile.frame.height);
			g.drawImage(sprite, (int)frame.x + drawX, (int)frame.y, null);
		}
	}
}
