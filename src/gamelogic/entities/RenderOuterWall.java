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
	//textual desc
	private static final String INV_IMAGE_TEXTUAL_DESC = "Some money.";
	//image file names:
	private static final String GAME_IMAGE_NAME = "coin";

	RenderOuterWall(CardinalDirection directionFaced) {
		super(directionFaced, INV_IMAGE_TEXTUAL_DESC, GAME_IMAGE_NAME);
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
