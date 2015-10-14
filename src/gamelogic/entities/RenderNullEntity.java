package gamelogic.entities;

import gamelogic.CardinalDirection;

import java.awt.Image;
import java.awt.Point;

public class RenderNullEntity extends RenderEntity {
	/**
	 *
	 */
	private static final long serialVersionUID = -7779631880041938902L;
	private Point offset = new Point();
	private static final String GAME_IMAGE_NAME = "nullentity";


	public RenderNullEntity(CardinalDirection directionFacing){
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
		return "";
	}

}
