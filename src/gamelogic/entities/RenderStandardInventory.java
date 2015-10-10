package gamelogic.entities;

import gamelogic.CardinalDirection;

import java.awt.Image;
import java.awt.Point;

public class RenderStandardInventory extends RenderEntity {
	private Point offset = new Point();

	public RenderStandardInventory(CardinalDirection directionFacing) {
		super(directionFacing, "I shouldn't drop this.");
		offset.x = 0;
		offset.y = 0;
	}

	@Override
	public Image getImg() {
		throw new RuntimeException("this is not supported just yet tbh");
	}

	@Override
	public Point getOffset() {
		return offset;
	}

}
