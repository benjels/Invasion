package gamelogic.entities;

import gamelogic.CardinalDirection;

import java.awt.Image;
import java.awt.Point;

public class RenderMediumCarrier extends RenderEntity {
	private Point offset = new Point();
	public RenderMediumCarrier(CardinalDirection directionFacing) {
		super(directionFacing, "I can store a few things in here.");
		offset.x = 0;
		offset.y = 0;
	}

	@Override
	public Image getImg() {
		throw new RuntimeException("this is not supported atm tbhj");
	}
	@Override 
	public Point getOffset(){
		return offset;
	}
}
