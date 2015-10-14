package gamelogic.renderentities;

import gamelogic.CardinalDirection;

import java.awt.Image;
import java.awt.Point;

public class RenderPylon extends RenderEntity {

	/**
	 *
	 */
	private static final long serialVersionUID = 7103042911521918018L;
	private Point offset = new Point();
	//image file names:
	private static final String GAME_IMAGE_NAME = "pylon";

	public RenderPylon(CardinalDirection directionFacing) {
		super(directionFacing,GAME_IMAGE_NAME);
		offset.x = 13;
		offset.y = -83;
	}

	@Override
	public String getName() {
		return "pylon";
	}

	@Override
	public Point getOffset() {
		return offset;
	}

}
