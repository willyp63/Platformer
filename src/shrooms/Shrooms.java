package shrooms;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import bodies.characters.Sam;
import bodies.characters.Sam.LookDirection;
import bodies.enemies.Shroom;
import bodies.items.AppleItem;
import bodies.items.Item;
import bodies.items.StickItem;
import bodies.walls.RLOscillatingWall;
import bodies.walls.UDOscillatingWall;
import bodies.walls.Wall;
import engine.MyGame;

public class Shrooms extends MyGame{

	public static void main(String[] args) {
		new Shrooms().gameLoop();
	}
	
	//health bar rendering constant
	private static final Rectangle2D.Float HEALTH_BAR_RECT = new Rectangle2D.Float(15, 15, 240, 48);//with respect to top right corner
	private static final Font HEALTH_BAR_FONT =  new Font("TimesRoman", Font.BOLD, 24);
	
	//inventory rendering constants
	private static final Rectangle2D.Float INVENTORY_RECT = new Rectangle2D.Float(17, 47, 96, 96);//with respect to bottom left corner
	private static final Rectangle2D.Float INVENTORY_BORDER_RECT = new Rectangle2D.Float(5, 35, 120, 120);//with respect to bottom left corner
	private static final Font INVENTORY_COUNT_FONT =  new Font("TimesRoman", Font.BOLD, 48);
	
	//game vars
	public Sam sam;
	public ArrayList<Body> bodies;
	public ArrayList<Body> newBodies;
	
	//inventory
	public int selectedItem;
	public ArrayList<Item> inventory;
	
	//rendering
	private Rectangle2D.Float inventoryRect, inventoryBorderRect, healthBarRect;
	
	public Shrooms(){
		//setup game
		super("Shrooms", 900, 600);
		setRefreshRate(30);
		
		//create body arrays
		bodies = new ArrayList<Body>();
		newBodies = new ArrayList<Body>();
		
		selectedItem = -1;
		inventory =  new ArrayList<Item>();
		
		inventoryRect =  new Rectangle2D.Float(INVENTORY_RECT.x, this.height - INVENTORY_RECT.y - INVENTORY_RECT.height, INVENTORY_RECT.width, INVENTORY_RECT.height);
		inventoryBorderRect = new Rectangle2D.Float(INVENTORY_BORDER_RECT.x, this.height - INVENTORY_BORDER_RECT.y - INVENTORY_BORDER_RECT.height, INVENTORY_BORDER_RECT.width, INVENTORY_BORDER_RECT.height);
		healthBarRect = (Rectangle2D.Float) HEALTH_BAR_RECT.clone();
	}
	
	
	public void setup() {
		//clear arrays
		sam = null;
		newBodies.clear();
		bodies.clear();
		
		inventory.clear();
		selectedItem = -1;
		
		//create sam
		sam =  new Sam(new Point2D.Float(0, 0));
		bodies.add(sam);
		
		//stairs
		bodies.add(new Wall(new Rectangle2D.Float(-40, 80, 120, 40)));
		bodies.add(new Wall(new Rectangle2D.Float(80, 40, 120, 40)));
		bodies.add(new Wall(new Rectangle2D.Float(200, 0, 120, 40)));
		bodies.add(new Wall(new Rectangle2D.Float(320, -40, 120, 40)));
		bodies.add(new Wall(new Rectangle2D.Float(440, -80, 120, 40)));
		//stairs
		bodies.add(new Wall(new Rectangle2D.Float(560, -40, 120, 40)));
		bodies.add(new Wall(new Rectangle2D.Float(680, 0, 120, 40)));
		bodies.add(new Wall(new Rectangle2D.Float(800, 40, 120, 40)));
		bodies.add(new Wall(new Rectangle2D.Float(920, 80, 120, 40)));
		
		
		//moving block
		bodies.add(new RLOscillatingWall(new Rectangle2D.Float(-160, 200, 240, 80), 1080, true));
		
		//elevator
		bodies.add(new UDOscillatingWall(new Rectangle2D.Float(-320, 160, 240, 40), 480, false));
		
		//shroom
		bodies.add(new Shroom(new Point2D.Float(240, -60)));
		
		bodies.add(new AppleItem(new Point2D.Float(40, -40), 4));
		bodies.add(new StickItem(new Point2D.Float(80, -80), 6));
	}

	public void update() {
		//add new bodies
		for(Body b : newBodies){
			bodies.add(b);
		}
		
		newBodies.clear();
		
		//update bodies
		for(Body b : bodies){
			b.update(this);
		}
		
		//move bodies
		for(Body b : bodies){
			b.move();
		}
		
		//check for collisions between all bodies
		for(int i = 0; i < bodies.size(); i++){
			for(int j = i + 1; j < bodies.size(); j++){
				Body b = bodies.get(i);
				Body b1 = bodies.get(j);
				
				//check for intersection in bounds
				if(b.bounds.intersects(b1.bounds)){
					b.collisionWith(b1, this);
					b1.collisionWith(b, this);
				}
			}
		}
		
		//remove dead characters
		for(int i = 0; i < bodies.size(); i++){
			Body b = bodies.get(i);
			if(!b.alive){
				//check if Sam is dead
				if(b instanceof Sam){
					reset();
					break;
				}
				
				bodies.remove(i--);
			}
		}
	}

	public void draw(Graphics2D g) {
		//create copy g
		Graphics2D g1 = (Graphics2D) g.create();
		
		//clear image
		g1.setColor(Color.white);
		g1.fillRect(0, 0, width, height);
		
		//move g to align with Sam
		if(sam != null){
			int trans_x = (int)(width/2 - sam.frame.width/2 - sam.frame.x);
			int trans_y = (int)(height/2 - sam.frame.height/2 - sam.frame.y);
			g1.translate(trans_x, trans_y);
		}
		
		//draw bodies
		for(int i = 0; i < bodies.size(); i++){
			bodies.get(i).draw(g1);
		}
		
		//draw inventory
		if(selectedItem >= 0){
			Item item = inventory.get(selectedItem);
			
			BufferedImage icon = Body.spriteSheets.get(item.spriteKey + "_icon");
			g.drawImage(icon, (int)inventoryRect.x, (int)inventoryRect.y, null);
			
			g.setColor(Color.gray);
			g.setFont(INVENTORY_COUNT_FONT);
			g.drawString(Integer.toString(item.count), (int)inventoryRect.x, (int)(inventoryRect.y + inventoryRect.height));
		}
		
		g.setStroke(new BasicStroke(4));
		g.setColor(Color.black);
		g.draw(inventoryBorderRect);
		
		//draw health bar
		if(sam != null){
			g.setColor(Color.red);
			g.fill(healthBarRect);
			
			healthBarRect.width *= sam.health / (float)sam.maxHealth;
			g.setColor(Color.green);
			g.fill(healthBarRect);
			
			//reset width
			healthBarRect.width = HEALTH_BAR_RECT.width;
			
			//draw health text
			g.setColor(Color.gray);
			g.setFont(HEALTH_BAR_FONT);
			g.drawString(Integer.toString(sam.health), (int)(healthBarRect.x + healthBarRect.width/2), (int)(healthBarRect.y + healthBarRect.height/2));
			
			//draw border
			g.setColor(Color.black);
			g.draw(healthBarRect);
		}
		
		//dispose g1
		g1.dispose();
	}
	
	public void storeItem(Item item){
		for(int i = 0; i < inventory.size(); i++){
			//check if item is already in inventory
			if(inventory.get(i).spriteKey.equals(item.spriteKey)){
				//add to count
				inventory.get(i).count += item.count;
				
				return;
			}
		}
		
		//add item to inventory
		inventory.add(item);
		
		//select item if inventory was empty
		if(selectedItem < 0)
			selectedItem = 0;
	}

	public void keyPressed(KeyEvent e) {
		switch(e.getKeyCode()){
			case KeyEvent.VK_LEFT:
				//run left
				sam.runLeft();
				break;
			
			case KeyEvent.VK_RIGHT:
				//run right
				sam.runRight();
				break;
				
			case KeyEvent.VK_UP:
				//look up
				sam.look = LookDirection.UP;
				break;
				
			case KeyEvent.VK_DOWN:
				//look down
				sam.look = LookDirection.DOWN;
				break;
			
			case KeyEvent.VK_Z:
				//jump
				sam.jump();
				break;
				
			case KeyEvent.VK_X:
				//begin throw
				if(selectedItem >= 0 && sam.projectile == null){
					Item item = inventory.get(selectedItem);
					
					sam.beginThrow(item.makeProjectile());
					
					//remove item from inventory
					item.count--;
					
					//check if there are no more of that item
					if(item.count <= 0){
						inventory.remove(selectedItem);
						
						if(inventory.size() > 0){
							selectedItem--;
							
							if(selectedItem < 0)
								selectedItem = inventory.size() - 1;
						}else{
							selectedItem = -1;
						}
					}
				}
				break;
				
			case KeyEvent.VK_A:
				//select next item above
				if(inventory.size() > 0){
					selectedItem++;
					
					if(selectedItem >= inventory.size())
						selectedItem = 0;
				}
				break;
				
			case KeyEvent.VK_S:
				//select next item below
				if(inventory.size() > 0){
					selectedItem--;
					
					if(selectedItem < 0)
						selectedItem = inventory.size() - 1;
				}
				break;
		}
	}

	public void keyReleased(KeyEvent e) {
		switch(e.getKeyCode()){
			case KeyEvent.VK_LEFT:
				//if running left, stop
				if(!sam.direction)
					sam.stop();
				break;
			
			case KeyEvent.VK_RIGHT:
				//if running right, stop
				if(sam.direction)
					sam.stop();
				break;
				
			case KeyEvent.VK_UP:
				//stop looking up
				if(sam.look == LookDirection.UP)
					sam.look = LookDirection.FORWARD;
				break;
				
			case KeyEvent.VK_DOWN:
				//stop looking up
				if(sam.look == LookDirection.DOWN)
					sam.look = LookDirection.FORWARD;
				break;
				
			case KeyEvent.VK_X:
				//throw
				sam.throwItem(this);
				break;
		}
	}
}
