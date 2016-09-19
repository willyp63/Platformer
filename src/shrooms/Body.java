package shrooms;

import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import javax.imageio.ImageIO;
public abstract class Body {
	
	public enum Direction{LEFT, UP, RIGHT, DOWN};
	
	public boolean alive;
	
	public boolean tile;
	public int spriteFrame, spriteRow;
	public Rectangle2D.Float bounds, frame;
	public Point2D.Float velocity, friction;
	
	public static HashMap<String, BufferedImage> spriteSheets =  new HashMap<String, BufferedImage>();
	public String spriteKey;
	
	public Body(Rectangle2D.Float frame, Rectangle2D.Float bounds, String spriteKey){
		addImage(spriteKey);
		
		this.frame = frame;
		this.bounds = bounds;
		this.spriteKey = spriteKey;
		velocity = new Point2D.Float(0, 0);
		friction = new Point2D.Float(0, 0);
		spriteRow = spriteFrame = 0;
		tile = false;
		alive = true;
	}
	
	public void addImage(String spriteKey){
		//check if the image is already loaded
		if(!spriteSheets.keySet().contains(spriteKey)){
			//load image and add to spriteSheets
			try {
				BufferedImage spriteSheet = ImageIO.read(new File("images/" + spriteKey + ".png"));
				spriteSheets.put(spriteKey, spriteSheet);
			} catch (IOException e) {
				System.out.println("failed to load image");
			}
		}
	}
	
	public void move(){
		shift(velocity);
	}
	
	public void shift(Point2D.Float p){
		//move frame and bounds
		frame.x += p.x;
		frame.y += p.y;
		
		bounds.x += p.x;
		bounds.y += p.y;
	}
	
	public Direction collisionDirection(Body b){
		//time to backtrack out of collision in x or y direction
		float xtime, ytime;
		
		//velocity relative to the other body
		float dx = velocity.x + friction.x - b.velocity.x;
		float dy = velocity.y + friction.y - b.velocity.y;
		
		//find xtime
		if(dx > 0){
			xtime = (bounds.x + bounds.width - b.bounds.x)/dx;
		}else if(dx < 0){
			xtime = (b.bounds.x + b.bounds.width - bounds.x)/-dx;
		}else{
			xtime = Float.MAX_VALUE;
		}
		
		//find ytime
		if(dy > 0){
			ytime = (bounds.y + bounds.height - b.bounds.y)/dy;
		}else if(dy < 0){
			ytime = (b.bounds.y + b.bounds.height - bounds.y)/-dy;
		}else{
			ytime = Float.MAX_VALUE;
		}
		
		//decide witch way to push character
		if(xtime < ytime){
			if(dx > 0){
				return Direction.RIGHT;
			}else{
				return Direction.LEFT;
			}
		}else{
			if(dy > 0){
				return Direction.DOWN;
			}else{
				return Direction.UP;
			}
		}
	}
	
	public void draw(Graphics2D g){
		BufferedImage spriteSheet = spriteSheets.get(spriteKey);
		if(spriteSheet ==  null)
			return;
		
		if(tile){
			//tile the sprite in the first frame over the body's frame
			int imageWidth = spriteSheet.getWidth();
			int imageHeight = spriteSheet.getHeight();
			for(int i = 0; i < frame.width; i += imageWidth){
				for(int j = 0; j < frame.height; j += imageHeight){
					g.drawImage(spriteSheet, (int)(frame.x + i), (int)(frame.y + j), null);
				}
			}
		}else{
			//draw the frame specified by spriteFrame
			BufferedImage sprite = spriteSheet.getSubimage((int)frame.width*spriteFrame, (int)frame.height*spriteRow, (int)frame.width, (int)frame.height);
			g.drawImage(sprite, (int)frame.x, (int)frame.y, null);
		}
	}
	
	public abstract void update(Shrooms game);
	public abstract void collisionWith(Body b, Shrooms game);
}
