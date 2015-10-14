package gamelogic.entities;

import gamelogic.CardinalDirection;

import java.awt.Image;
import java.awt.Point;

public class RenderTreasure extends RenderEntity {

	private static final String GAME_IMAGE_NAME = "treasure";



	public RenderTreasure(CardinalDirection directionFacing) {
		super(directionFacing,GAME_IMAGE_NAME);

	}

	@Override
	public Point getOffset() {
		return new Point(0, 0);
	}

	@Override
	public String getName(){
		return "treasure";
	}

}
