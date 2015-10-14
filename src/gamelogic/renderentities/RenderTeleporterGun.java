package gamelogic.renderentities;

import gamelogic.CardinalDirection;

import java.awt.Image;
import java.awt.Point;

public class RenderTeleporterGun extends RenderEntity {
	/**
	 *
	 */
	private static final long serialVersionUID = -1878039200861357116L;
	private Point offset = new Point();
	private static final String GAME_IMAGE_NAME = "teleportGun";


	public RenderTeleporterGun(CardinalDirection directionFacing) {
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
		return "teleGun";
	}
}
