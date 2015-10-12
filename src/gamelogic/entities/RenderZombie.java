package gamelogic.entities;

import gamelogic.CardinalDirection;

import java.awt.Image;
import java.awt.Point;

public class RenderZombie extends RenderEntity {
	/**
	 *
	 */
	private static final long serialVersionUID = -4190362462073725574L;
	private Point offset = new Point();
	private CardinalDirection dir;
	//textual desc
	private static final String INV_IMAGE_TEXTUAL_DESC = "some money";

	private static final String GAME_IMAGE_NAME = "joelychangethislol";

	public RenderZombie(CardinalDirection directionFacing) {
		super(directionFacing, INV_IMAGE_TEXTUAL_DESC, GAME_IMAGE_NAME);
		offset.x = 0;
		offset.y = 0;
		this.dir = directionFacing;
	}

	@Override
	public Image getImg() {
		throw new RuntimeException();
	}

	@Override
	public String toString() {
		/*switch (dir) {
		case NORTH:
			return "robotN";
		case SOUTH:
			return "robotS";
		case EAST:
			return "robotE";
		case WEST:
			return "robotW";
		}*/
		return "";
	}

	@Override
	public Point getOffset() {
		return offset;
	}
}
