package perf.cfg.dl4j.tetris.util;

import java.awt.Rectangle;

import perf.cfg.dl4j.tetris.common.BoundsGenerator;

public class FixedBoundsGenerator implements BoundsGenerator{
	
	private Rectangle bounds;
	
	public FixedBoundsGenerator(int x, int y, int width, int height) {
		bounds = new Rectangle(x, y, width, height);
	}
	
	public FixedBoundsGenerator(Rectangle bounds) {
		this(bounds.x, bounds.y, bounds.width, bounds.height);
	}


	@Override
	public Rectangle getBounds() {
		return new Rectangle(bounds);
	}

}
