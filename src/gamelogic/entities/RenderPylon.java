package gamelogic.entities;

import gamelogic.CardinalDirection;

import java.awt.Image;
import java.awt.Point;

public class RenderPylon extends RenderEntity {
	
	private Point offset = new Point();

	public RenderPylon(CardinalDirection directionFacing, String textualDesc) {
		super(directionFacing, textualDesc);
		offset.x = 8;
		offset.y = -100;
	}

	@Override
	public Image getImg() {
		throw new RuntimeException("this is not supported at the moment");
	}

	@Override
	public String toString() {
		return "pylon";
	}

	@Override
	public Point getOffset() {
		return offset;
	}

}
