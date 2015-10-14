package gamelogic.entities;

import gamelogic.CardinalDirection;
import imagehelper.GCImageH;

import java.awt.Image;
import java.awt.Point;

public class RenderSmallCarrier extends RenderCarryable {
	/**
	 *
	 */
	private static final long serialVersionUID = 7657009300299614446L;
	private Point offset = new Point();
	// textual desc
	private static final String INV_IMAGE_TEXTUAL_DESC = "A small sized bag.";
	// image file names:
	private static final String INV_IMAGE_FILE_NAME = "smallCarrier";
	private static final String GAME_IMAGE_NAME = "smallCarrier";

	public RenderSmallCarrier(CardinalDirection directionFacing) {
		super(directionFacing, INV_IMAGE_TEXTUAL_DESC, INV_IMAGE_FILE_NAME,
				GAME_IMAGE_NAME);
		offset.x = 0;
		offset.y = 0;
	}

	@Override
	public Image getImg() {
		throw new RuntimeException("cant do this atm");
	}

	@Override
	public String getName() {
		return "smallC";
	}

	@Override
	public Point getOffset() {
		return offset;
	}
}
