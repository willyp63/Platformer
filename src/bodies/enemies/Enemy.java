package bodies.enemies;

import bodies.characters.Character;
import bodies.projectiles.Projectile;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;

import shrooms.Body;
import shrooms.Shrooms;

public abstract class Enemy extends Character{
	
	private static final int HEALTH_BAR_WIDTH = 48, HEALTH_BAR_HEIGHT = 12, HEALTH_BAR_Y_OFFSET = 24;
	
	public Rectangle2D.Float healthBarRect;

	public Enemy(Rectangle2D.Float frame, Rectangle2D.Float bounds, String spriteKey) {
		super(frame, bounds, spriteKey);
		
		healthBarRect = new Rectangle2D.Float(0, 0, HEALTH_BAR_WIDTH, HEALTH_BAR_HEIGHT);
	}
	
	public void collisionWith(Body b, Shrooms game) {
		super.collisionWith(b, game);
		
		if(b instanceof Projectile){
			health -= 25;
			b.alive = false;
		}
	}
	
	public void draw(Graphics2D g){
		super.draw(g);
		
		//move healthBarRect
		healthBarRect.x = frame.x + (frame.width - HEALTH_BAR_WIDTH)/2;
		healthBarRect.y = frame.y - HEALTH_BAR_Y_OFFSET;
		
		//draw health bar
		g.setColor(Color.red);
		g.fill(healthBarRect);
		
		healthBarRect.width *= health / (float)maxHealth;
		g.setColor(Color.green);
		g.fill(healthBarRect);
		
		//reset width
		healthBarRect.width = HEALTH_BAR_WIDTH;
	}
}
