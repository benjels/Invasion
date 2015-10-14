package gamelogic.entities;

import gamelogic.CardinalDirection;

import java.awt.Image;
import java.awt.Point;

public class RenderTreasure extends RenderEntity {

	//textual desc
	private static final String INV_IMAGE_TEXTUAL_DESC = "normal teleporter";
	private static final String GAME_IMAGE_NAME = "treasure";



	public RenderTreasure(CardinalDirection directionFacing) {
		super(directionFacing, INV_IMAGE_TEXTUAL_DESC, GAME_IMAGE_NAME);

	}

	@Override
	public Image getImg() {
		// TODO Auto-generated method stub
		return null;
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
