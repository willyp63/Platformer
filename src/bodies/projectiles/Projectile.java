package bodies.projectiles;

import java.awt.geom.Rectangle2D;

import shrooms.Body;
import shrooms.Shrooms;

public class Projectile extends Body {

	public Projectile(Rectangle2D.Float frame, Rectangle2D.Float bounds, String spriteKey) {
		super(frame, bounds, spriteKey);
	}

	public void update(Shrooms game) {

	}

	public void collisionWith(Body b, Shrooms game) {

	}
}
