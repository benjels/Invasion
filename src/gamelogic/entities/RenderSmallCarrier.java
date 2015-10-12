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
	//textual desc
	private static final String INV_IMAGE_TEXTUAL_DESC = "A small sized bag.";
	//image file names:
	private static final String INV_IMAGE_FILE_NAME = "sbag";
	private static final String GAME_IMAGE_NAME = "joelychangethislol";


	public RenderSmallCarrier(CardinalDirection directionFacing) {
		super(directionFacing, INV_IMAGE_TEXTUAL_DESC, INV_IMAGE_FILE_NAME, GAME_IMAGE_NAME);
		offset.x = GCImageH.smallC.getWidth(null) / 2;
		;
		offset.y = -GCImageH.smallC.getHeight(null) / 4;
		;
	}

	@Override
	public Image getImg() {
		throw new RuntimeException("cant do this atm");
	}

	@Override
	public String toString() {
		return "smallC";
	}

	@Override
	public Point getOffset() {
		return offset;
	}
}
