package gamelogic.entities;

import gamelogic.CardinalDirection;

import java.awt.Image;
import java.awt.Point;

public class RenderTeleporterGun extends RenderCarryable {
	private Point offset = new Point();
	//textual desc
	private static final String INV_IMAGE_TEXTUAL_DESC = "make portals";
	//image file name
	private static final String INV_IMAGE_FILE_NAME = "maxchangethislol";
	private static final String GAME_IMAGE_NAME = "joelychangethislol";
	
	
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
}
