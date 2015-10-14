package gamelogic.entities;

import gamelogic.CardinalDirection;

import java.awt.Image;
import java.awt.Point;

public class RenderStandardInventory extends RenderEntity {
	/**
	 *
	 */
	private static final long serialVersionUID = 5007705979278006330L;
	private Point offset = new Point();
	//image file names:
	private static final String GAME_IMAGE_NAME = "standardInventory";


	public RenderStandardInventory(CardinalDirection directionFacing) {
		super(directionFacing,GAME_IMAGE_NAME);
		offset.x = 0;
		offset.y = 0;
	}

	@Override
	public Point getOffset() {
		return offset;
	}

	@Override
	public String getName(){
		return "";
	}


}
