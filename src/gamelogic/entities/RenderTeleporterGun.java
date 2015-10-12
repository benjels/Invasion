package gamelogic.entities;

import gamelogic.CardinalDirection;

import java.awt.Image;
import java.awt.Point;

public class RenderTeleporterGun extends RenderCarryable {
	/**
	 *
	 */
	private static final long serialVersionUID = -1878039200861357116L;
	private Point offset = new Point();
	//textual desc
	private static final String INV_IMAGE_TEXTUAL_DESC = "make portals";
	//image file name
	private static final String INV_IMAGE_FILE_NAME = "teleportGun";
	private static final String GAME_IMAGE_NAME = "teleportGun";


	public RenderTeleporterGun(CardinalDirection directionFacing) {
		super(directionFacing, INV_IMAGE_TEXTUAL_DESC, INV_IMAGE_FILE_NAME, GAME_IMAGE_NAME);
		offset.x = 0;
		offset.y = 0;
	}

	@Override
	public Image getImg() {
		throw new RuntimeException("this not supported atm");
	}
	@Override
	public Point getOffset(){
		return offset;
	}


	@Override
	public String toString(){
		return "teleGun";
	}
}
