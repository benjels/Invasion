package gamelogic.entities;

import gamelogic.CardinalDirection;

import java.awt.Image;
import java.awt.Point;

public class RenderGun extends  RenderEntity{
	/**
	 *
	 */
	private static final long serialVersionUID = 8375627365775521507L;
	private Point offset = new Point();
	private static final String GAME_IMAGE_NAME = "gun";



	public RenderGun(CardinalDirection directionFacing, String textualDesc) {
		super(directionFacing,GAME_IMAGE_NAME);
		offset.x = 0;
		offset.y = 0;
	}
	@Override
	public Point getOffset(){
		return offset;
	}

	@Override
	public String getName(){
		return "gun";
	}

}
