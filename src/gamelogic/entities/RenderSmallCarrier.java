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
	private static final String GAME_IMAGE_NAME = "smallCarrier";

	public RenderSmallCarrier(CardinalDirection directionFacing) {
		super(directionFacing, GAME_IMAGE_NAME);
		offset.x = 0;
		offset.y = 0;
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
