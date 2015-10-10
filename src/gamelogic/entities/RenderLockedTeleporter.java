package gamelogic.entities;

import gamelogic.CardinalDirection;

import java.awt.Image;
import java.awt.Point;

public class RenderLockedTeleporter extends RenderEntity {
	private Point offset = new Point();

	public RenderLockedTeleporter(CardinalDirection directionFaced) {
		super(directionFaced, "This will take me somewhere else if I have the key.");
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
