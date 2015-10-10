package gamelogic.entities;

import gamelogic.CardinalDirection;
import imagehelper.GCImageH;

import java.awt.Image;
import java.awt.Point;

public class RenderOuterWall extends RenderEntity {
	private CardinalDirection dir;
	private Point offset = new Point();

	RenderOuterWall(CardinalDirection directionFaced) {
		super(directionFaced, "I can't move through this wall.");
		this.dir = directionFaced;
		switch (dir) {
		case NORTH:
			offset.x = (GCImageH.width / 2) - 7;
			offset.y = -(GCImageH.WallNS.getHeight(null) - GCImageH.height);
			break;
		case SOUTH:
			offset.x = -7;
			offset.y = -(GCImageH.WallNS.getHeight(null) - GCImageH.height / 2);
			break;
		case WEST:
			offset.x = (GCImageH.width / 2) - 7;
			offset.y = -(GCImageH.WallEW.getHeight(null) - GCImageH.height / 2);
			break;
		case EAST:
			offset.x = 0;
			offset.y = -(GCImageH.WallEW.getHeight(null) - GCImageH.height);
			break;
		}
	}

	@Override
	public String toString() {
		switch (dir) {
		case NORTH:
			return "WallNS";
		case SOUTH:
			return "WallNS";
		case EAST:
			return "WallEW";
		case WEST:
			return "WallEW";
		}
		return "";
	}

	@Override
	public Image getImg() {
		throw new RuntimeException("cannot do this yet");
	}

	@Override
	public Point getOffset() {
		return offset;
	}

}
