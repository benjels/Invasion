package gamelogic.entities;

import gamelogic.CardinalDirection;
import imagehelper.GCImageH;

import java.awt.Image;
import java.awt.Point;

public class RenderPylonAttacker extends RenderEntity {
	/**
	 *
	 */
	private static final long serialVersionUID = -4190362462073725574L;
	private Point offset = new Point();
	private CardinalDirection dir;
	//textual desc
	private static final String INV_IMAGE_TEXTUAL_DESC = "some money";

	private static final String GAME_IMAGE_NAME = "joelychangethislol";

	public RenderPylonAttacker(CardinalDirection directionFacing) {
		super(directionFacing, INV_IMAGE_TEXTUAL_DESC, GAME_IMAGE_NAME);
		this.dir = directionFacing;
		switch (dir) {
		case NORTH:
			offset.x = 40-GCImageH.width/2;
			offset.y = 0 - GCImageH.height/2;
			break;
		case SOUTH:
			offset.x = 40- GCImageH.width/2;
			offset.y = 0- GCImageH.height/2;
			break;
		case EAST:
			offset.x = 40 - GCImageH.width/2;
			offset.y = -GCImageH.height/2;
			break;
		case WEST:
			offset.x = 40;
			offset.y = -GCImageH.height;
			break;
		}
	}

	@Override
	public Image getImg() {
		throw new RuntimeException();
	}

	@Override
	public String toString() {
		switch (dir) {
		case NORTH:
			return "robotN";
		case SOUTH:
			return "robotS";
		case EAST:
			return "robotE";
		case WEST:
			return "robotW";
		}
		return "";
	}

	@Override
	public Point getOffset() {
		return offset;
	}
}
