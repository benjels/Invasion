package gamelogic;

import imagehelper.GCImageH;

import java.awt.Image;
import java.awt.Point;

import gamelogic.entities.RenderEntity;

public class RenderZombieAttacker extends RenderEntity {

	// textual desc
	private static final String INV_IMAGE_TEXTUAL_DESC = "an unfriendly zombie.";
	// image file names:
	private static final String INV_IMAGE_FILE_NAME = "NA";
	private static final String GAME_IMAGE_NAME = "joely fill this in tbh";
	private Point offset = new Point();

	private CardinalDirection dir;

	public RenderZombieAttacker(CardinalDirection directionFacing) {
		super(directionFacing, INV_IMAGE_TEXTUAL_DESC, GAME_IMAGE_NAME);
		this.dir = directionFacing;
		offset.x = 0;
		offset.y = -(int) (1.5 * GCImageH.height);
	}

	@Override
	public Image getImg() {
		throw new RuntimeException("no ooo");// TODO: all these ened to be
												// deleted
	}

	@Override
	public Point getOffset() {
		// TODO Auto-generated method stub
		// throw new RuntimeException("no ooo");//TODO: all these ened to be
		// deleted i think joely using the map for these now?
		// return null;
		return offset;
	}

	@Override
	public String getName() {
		switch (this.getFacingCardinalDirection()) {
		case NORTH:
			return "burningN";
		case SOUTH:
			return "burningS";
		case EAST:
			return "burningW";
		case WEST:
			return "burningW";
		}
		return "";
	}
}
