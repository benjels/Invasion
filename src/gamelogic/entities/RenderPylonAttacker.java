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
	// textual desc
	private static final String INV_IMAGE_TEXTUAL_DESC = "some money";

	private static final String GAME_IMAGE_NAME = "joelychangethislol";

	public RenderPylonAttacker(CardinalDirection directionFacing) {
		super(directionFacing, INV_IMAGE_TEXTUAL_DESC, GAME_IMAGE_NAME);
		this.dir = directionFacing;
		updateOffset();

	}

	public void updateOffset() {
		switch (dir) {
		case NORTH:
			offset.x = 40 - GCImageH.width / 2;
			offset.y = 0 - GCImageH.height / 2;
			break;
		case SOUTH:
			offset.x = 40 - GCImageH.width / 2;
			offset.y = 0 - GCImageH.height / 2;
			break;
		case EAST:
			offset.x = 40 - GCImageH.width / 2;
			offset.y = -GCImageH.height / 2;
			break;
		case WEST:
			offset.x = 40 - GCImageH.width / 2;
			offset.y = -GCImageH.height / 2;
			break;
		}
	}

	@Override
	public Image getImg() {
		throw new RuntimeException();
	}

	@Override
	public String getName() {
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

	public void changeDir(CardinalDirection roomDir) {
		switch (roomDir) {
		case SOUTH:
			south();
			break;
		case EAST:
			east();
			break;
		case WEST:
			west();
			break;
		}
		updateOffset();

	}

	private void south() {
		switch (dir) {
		case NORTH:
			dir = CardinalDirection.SOUTH;
			break;
		case SOUTH:
			dir = CardinalDirection.NORTH;
			break;
		case EAST:
			dir = CardinalDirection.WEST;
			break;
		case WEST:
			dir = CardinalDirection.EAST;
			break;
		}
	}

	private void east() {
		switch (dir) {
		case NORTH:
			dir = CardinalDirection.EAST;
			break;
		case SOUTH:
			dir = CardinalDirection.WEST;
			break;
		case WEST:
			dir = CardinalDirection.NORTH;
			break;
		case EAST:
			dir = CardinalDirection.SOUTH;
			break;
		}
	}

	private void west() {
		switch (dir) {
		case NORTH:
			dir = CardinalDirection.WEST;
			break;
		case SOUTH:
			dir = CardinalDirection.EAST;
			break;
		case WEST:
			dir = CardinalDirection.SOUTH;
			break;
		case EAST:
			dir = CardinalDirection.NORTH;
			break;
		}
	}
}
