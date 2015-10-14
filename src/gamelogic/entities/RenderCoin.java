package gamelogic.entities;

import gamelogic.CardinalDirection;
import imagehelper.GCImageH;

import java.awt.Image;
import java.awt.Point;

public class RenderCoin extends RenderEntity{
	/**
	 *
	 */
	private static final long serialVersionUID = -4054947792720836188L;
	private Point offset = new Point();
	private static final String GAME_IMAGE_NAME = "coin";

	public RenderCoin(CardinalDirection directionFacing) {
		super(directionFacing,GAME_IMAGE_NAME);
		offset.x = 0;
		offset.y = 0;
	}
	@Override
	public String getName(){
		return "coin";
	}

	@Override
	public Point getOffset(){
		return offset;
	}


}
