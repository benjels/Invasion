package gamelogic.entities;

import gamelogic.CardinalDirection;

import java.awt.Image;
import java.awt.Point;

public class RenderZombie extends RenderEntity {
	private Point offset = new Point();
	public RenderZombie(CardinalDirection directionFacing) {
		super(directionFacing, "This is not a friend.");
		offset.x = 0;
		offset.y = 0;
	}

	@Override
	public Image getImg() {
		throw new RuntimeException();
	}

	@Override
	public String toString() {
		return "Zombie";
	}

	@Override
	public Point getOffset() {
		return offset;
	}
}
