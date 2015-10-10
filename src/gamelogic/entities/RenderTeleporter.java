package gamelogic.entities;

import gamelogic.CardinalDirection;

import java.awt.Image;
import java.awt.Point;

public class RenderTeleporter extends RenderEntity {
	private Point offset = new Point();

	public RenderTeleporter(CardinalDirection directionFaced) {
		super(directionFaced, "This will take me somewhere else.");
		offset.x = 0;
		offset.y = 0;
	}

	@Override
	public Image getImg() {
		throw new RuntimeException("this not supported atm");
	}

	@Override
	public Point getOffset() {
		return offset;
	}

}
