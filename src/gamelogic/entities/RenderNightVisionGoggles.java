package gamelogic.entities;

import gamelogic.CardinalDirection;

import java.awt.Image;
import java.awt.Point;

public class RenderNightVisionGoggles extends RenderEntity {
	private Point offset = new Point();

	public RenderNightVisionGoggles(CardinalDirection directionFacing) {
		super(directionFacing, "I can use these to see further in the dark rooms.");
		offset.x = 0;
		offset.y = 0;
	}

	@Override
	public Image getImg() {
		throw new RuntimeException("not supported yet coming soon");
	}
	@Override 
	public Point getOffset(){
		return offset;
	}
}
