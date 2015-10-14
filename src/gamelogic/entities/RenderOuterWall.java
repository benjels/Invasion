package gamelogic.entities;

import gamelogic.CardinalDirection;
import imagehelper.GCImageH;

import java.awt.Image;
import java.awt.Point;

public class RenderOuterWall extends RenderEntity {
	/**
	 *
	 */
	private static final long serialVersionUID = 2284332744122392754L;
	private CardinalDirection dir;
	private Point offset = new Point();
	// image file names:
	private static final String GAME_IMAGE_NAME = "outerwall";

	RenderOuterWall(CardinalDirection directionFaced) {
		super(directionFaced, GAME_IMAGE_NAME);
		this.dir = directionFaced;
		updateOffset();
	}

	public void updateOffset() {
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
	public String getName() {
		switch (getDir()) {
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
	public Point getOffset() {
		return offset;
	}

	public CardinalDirection getDir() {
		return dir;
	}

	public void setDir(CardinalDirection dir) {
		this.dir = dir;
		updateOffset();

	}

}
