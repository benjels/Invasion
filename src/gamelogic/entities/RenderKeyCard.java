package gamelogic.entities;

import gamelogic.CardinalDirection;
import imagehelper.GCImageH;
import imagehelper.Imagehelper;

import java.awt.Image;
import java.awt.Point;

public class RenderKeyCard extends RenderCarryable{
	/**
	 *
	 */
	private static final long serialVersionUID = -2404309219798888125L;
	private Point offset = new Point();
	private static final String GAME_IMAGE_NAME = "keycard";




	public RenderKeyCard(CardinalDirection directionFacing) {
		super(directionFacing,GAME_IMAGE_NAME);
		offset.x = 0;
		offset.y = 0;
	}

	@Override
	public String getName(){
		return "key";
	}

	@Override
	public Point getOffset() {
		return offset;
	}
}
