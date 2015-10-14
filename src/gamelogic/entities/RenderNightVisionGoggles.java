package gamelogic.entities;

import gamelogic.CardinalDirection;

import java.awt.Image;
import java.awt.Point;

public class RenderNightVisionGoggles extends RenderCarryable {
	/**
	 *
	 */
	private static final long serialVersionUID = 6749289290022645417L;
	private Point offset = new Point();
	private static final String GAME_IMAGE_NAME = "nvg";


	public RenderNightVisionGoggles(CardinalDirection directionFacing) {
		super(directionFacing, GAME_IMAGE_NAME);
		offset.x = 0;
		offset.y = 0;
	}

	@Override
	public Point getOffset(){
		return offset;
	}


	@Override
	public String getName(){
		return "nightV";
	}
}
