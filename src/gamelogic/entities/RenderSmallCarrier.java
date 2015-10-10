package gamelogic.entities;

import gamelogic.CardinalDirection;
import imagehelper.GCImageH;

import java.awt.Image;
import java.awt.Point;

public class RenderSmallCarrier extends RenderEntity {
	private Point offset = new Point();

	public RenderSmallCarrier(CardinalDirection directionFacing) {
		super(directionFacing, "I can put a couple of things in here.");
		offset.x = GCImageH.smallC.getWidth(null) / 2;
		;
		offset.y = -GCImageH.smallC.getHeight(null) / 4;
		;
	}

	@Override
	public Image getImg() {
		throw new RuntimeException("cant do this atm");
	}

	@Override
	public String toString() {
		return "smallC";
	}

	@Override
	public Point getOffset() {
		return offset;
	}
}
